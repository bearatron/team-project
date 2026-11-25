package use_case.post_review;

import entities.Category;
import entities.FoodTruck;
import entities.Review;
import entities.User;
import entities.UserType;
import java.awt.Image;
import java.io.File;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import use_case.image_storage.ImageStorage;
import use_case.image_storage.ImageStorageException;

/**
 * The interactor for the Post a Review use case, which also attaches any
 * images included in the same request.
 */
public class PostReviewInteractor implements PostReviewInputBoundary {

    /** The longest review text allowed, in characters. */
    public static final int MAX_REVIEW_TEXT_LENGTH = 1000;

    /** The most images one review can have. */
    public static final int MAX_IMAGES_PER_REVIEW = 5;

    /** The largest image file allowed, in bytes (5 MB). */
    public static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;

    /** The file extensions accepted for images (JPEG and PNG). */
    public static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    private final PostReviewDataAccessInterface dataAccess;
    private final ImageStorage imageStorage;
    private final PostReviewOutputBoundary presenter;
    private final Clock clock;

    /**
     * Creates a new PostReviewInteractor that timestamps reviews with the
     * system clock.
     *
     * @param dataAccess the data access object
     * @param imageStorage where attached images are stored
     * @param presenter the output boundary
     */
    public PostReviewInteractor(PostReviewDataAccessInterface dataAccess,
                                ImageStorage imageStorage,
                                PostReviewOutputBoundary presenter) {
        this(dataAccess, imageStorage, presenter, Clock.systemDefaultZone());
    }

    /**
     * Creates a new PostReviewInteractor that timestamps reviews with the
     * given clock, so tests can control the time.
     *
     * @param dataAccess the data access object
     * @param imageStorage where attached images are stored
     * @param presenter the output boundary
     * @param clock the clock used for the review's timestamp
     */
    public PostReviewInteractor(PostReviewDataAccessInterface dataAccess,
                                ImageStorage imageStorage,
                                PostReviewOutputBoundary presenter, Clock clock) {
        this.dataAccess = dataAccess;
        this.imageStorage = imageStorage;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(PostReviewInputData inputData) {
        // 1. Check the user is a reviewer and the truck exists
        final User user = dataAccess.getUser(inputData.getUsername());
        if (user == null || user.getUserType() != UserType.REVIEWER) {
            presenter.prepareFailView("You must be logged in as a reviewer to post a review.");
            return;
        }
        final FoodTruck truck = dataAccess.getTruck(inputData.getTruckName());
        if (truck == null) {
            presenter.prepareFailView("That food truck does not exist.");
            return;
        }

        // 2. Validate the ratings and text
        final String ratingsError = checkRatings(inputData.getRatings());
        if (ratingsError != null) {
            presenter.prepareFailView(ratingsError);
            return;
        }
        final String text = inputData.getText();
        if (text != null && text.length() > MAX_REVIEW_TEXT_LENGTH) {
            presenter.prepareFailView("Review text cannot be longer than "
                    + MAX_REVIEW_TEXT_LENGTH + " characters.");
            return;
        }

        // 3. A reviewer can only review each truck once
        for (Review existing : truck.getReviews()) {
            if (existing.isAuthoredBy(user)) {
                presenter.prepareFailView("You have already reviewed this truck.");
                return;
            }
        }

        // 4. Store the valid images; skipped ones are reported but don't stop the review
        final List<String> skippedImages = new ArrayList<>();
        final List<Image> images = storeImages(inputData.getImageFiles(), skippedImages);

        // 5. Save the review and report success
        final LocalDateTime now = LocalDateTime.now(clock);
        final Review review = Review.fromInApp(user, inputData.getRatings(), text, images, now);
        truck.addReview(review);
        dataAccess.save(truck);

        presenter.prepareSuccessView(new PostReviewOutputData(truck.getName(),
                review.getAuthorName(), review.getRatings(), text, images.size(),
                skippedImages, now));
    }

    /**
     * Checks that every category has a score in the allowed range.
     *
     * @param ratings the scores the user gave
     * @return a message describing the problem, or null if the ratings are valid
     */
    private String checkRatings(Map<Category, Integer> ratings) {
        for (Category category : Category.values()) {
            final Integer score = ratings == null ? null : ratings.get(category);
            if (score == null) {
                return "Please rate every category.";
            }
            if (score < Review.MIN_SCORE || score > Review.MAX_SCORE) {
                return "Each rating must be between " + Review.MIN_SCORE + " and "
                        + Review.MAX_SCORE + ".";
            }
        }
        return null;
    }

    /**
     * Validates and stores each image file. A file that is the wrong type, too
     * large, over the image limit, or cannot be stored is skipped, and a
     * message saying why is added to {@code skippedImages}.
     *
     * @param imageFiles the attached files, or null for none
     * @param skippedImages receives one message per skipped file
     * @return the stored images
     */
    private List<Image> storeImages(List<File> imageFiles, List<String> skippedImages) {
        final List<Image> images = new ArrayList<>();
        if (imageFiles == null) {
            return images;
        }
        for (File file : imageFiles) {
            final String problem = checkImage(file, images.size());
            if (problem != null) {
                skippedImages.add(problem);
                continue;
            }
            try {
                images.add(imageStorage.save(file));
            } catch (ImageStorageException exception) {
                skippedImages.add(file.getName() + " could not be saved.");
            }
        }
        return images;
    }

    /**
     * Checks one image file against the type, size and count limits.
     *
     * @param file the file to check
     * @param imagesSoFar how many images have already been accepted
     * @return a message describing the problem, or null if the file is acceptable
     */
    private String checkImage(File file, int imagesSoFar) {
        if (file == null) {
            return "An attached image was missing.";
        }
        if (imagesSoFar >= MAX_IMAGES_PER_REVIEW) {
            return file.getName() + " was not added: a review can have at most "
                    + MAX_IMAGES_PER_REVIEW + " images.";
        }
        if (!ALLOWED_IMAGE_EXTENSIONS.contains(extensionOf(file))) {
            return file.getName() + " is not a JPEG or PNG image.";
        }
        if (!file.isFile()) {
            return file.getName() + " could not be read.";
        }
        if (file.length() > MAX_IMAGE_SIZE) {
            return file.getName() + " is larger than 5 MB.";
        }
        return null;
    }

    /**
     * Returns a file's extension in lower case.
     *
     * @param file the file
     * @return the extension without the dot, or an empty string if there is none
     */
    private String extensionOf(File file) {
        final String name = file.getName();
        final int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
