package use_case.post_review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import data_access.InMemoryReviewDataAccessObject;
import entities.Category;
import entities.FoodTruck;
import entities.Location;
import entities.Review;
import entities.User;
import entities.UserType;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.image_storage.ImageStorage;
import use_case.image_storage.ImageStorageException;

class PostReviewInteractorTest {

    private static final Instant TEST_INSTANT = Instant.parse("2026-11-25T12:00:00Z");
    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2026, 11, 25, 12, 0);

    @TempDir
    Path tempDir;

    private FoodTruck truck;
    private InMemoryReviewDataAccessObject dataAccess;
    private FakeImageStorage imageStorage;
    private FakePresenter presenter;
    private PostReviewInteractor interactor;

    @BeforeEach
    void setUp() {
        truck = new FoodTruck("Taco Truck", new Location(43.66, -79.39),
                List.of("Mexican"), List.of());
        dataAccess = new InMemoryReviewDataAccessObject();
        dataAccess.addUser(new User("alice", UserType.REVIEWER));
        dataAccess.addUser(new User("olivia", UserType.OWNER));
        dataAccess.addTruck(truck, "olivia");
        imageStorage = new FakeImageStorage();
        presenter = new FakePresenter();
        interactor = new PostReviewInteractor(dataAccess, imageStorage, presenter,
                Clock.fixed(TEST_INSTANT, ZoneOffset.UTC));
    }

    private static Map<Category, Integer> allRatings(int score) {
        final Map<Category, Integer> ratings = new EnumMap<>(Category.class);
        for (Category category : Category.values()) {
            ratings.put(category, score);
        }
        return ratings;
    }

    private void post(Map<Category, Integer> ratings, String text, List<File> images) {
        interactor.execute(new PostReviewInputData("alice", "Taco Truck", ratings, text, images));
    }

    private File imageFile(String name) throws IOException {
        return Files.write(tempDir.resolve(name), new byte[] {1, 2, 3}).toFile();
    }

    // Main flow

    @Test
    void successSavesTheReviewOnTheTruck() {
        post(allRatings(4), "Great tacos", null);

        assertNull(presenter.error);
        assertEquals(1, truck.getReviews().size());
        final Review review = truck.getReviews().get(0);
        assertEquals("alice", review.getAuthorName());
        assertEquals("Great tacos", review.getText());
        assertEquals(TEST_DATE, review.getTimePosted());
        assertEquals("Taco Truck", presenter.output.getTruckName());
        assertEquals(TEST_DATE, presenter.output.getTimePosted());
        assertEquals(0, presenter.output.getImagesAttached());
        assertTrue(presenter.output.getSkippedImages().isEmpty());
    }

    @Test
    void textIsOptional() {
        post(allRatings(3), null, null);

        assertNull(presenter.error);
        assertNull(truck.getReviews().get(0).getText());
    }

    // Who and what

    @Test
    void failsWhenUserIsNotAReviewer() {
        interactor.execute(new PostReviewInputData("olivia", "Taco Truck", allRatings(5),
                null, null));

        assertEquals("You must be logged in as a reviewer to post a review.", presenter.error);
        assertTrue(truck.getReviews().isEmpty());
    }

    @Test
    void failsWhenUserDoesNotExist() {
        interactor.execute(new PostReviewInputData("nobody", "Taco Truck", allRatings(5),
                null, null));

        assertEquals("You must be logged in as a reviewer to post a review.", presenter.error);
    }

    @Test
    void failsWhenTruckDoesNotExist() {
        interactor.execute(new PostReviewInputData("alice", "Burger Bus", allRatings(5),
                null, null));

        assertEquals("That food truck does not exist.", presenter.error);
    }

    @Test
    void failsWhenAlreadyReviewed() {
        post(allRatings(4), "First", null);
        post(allRatings(2), "Second", null);

        assertEquals("You have already reviewed this truck.", presenter.error);
        assertEquals(1, truck.getReviews().size());
    }

    // Ratings and text

    @Test
    void failsWhenACategoryIsMissing() {
        final Map<Category, Integer> ratings = allRatings(4);
        ratings.remove(Category.VALUE_FOR_MONEY);

        post(ratings, null, null);

        assertEquals("Please rate every category.", presenter.error);
        assertTrue(truck.getReviews().isEmpty());
    }

    @Test
    void failsWhenRatingsAreMissing() {
        post(null, null, null);

        assertEquals("Please rate every category.", presenter.error);
    }

    @Test
    void failsWhenARatingIsOutOfRange() {
        final Map<Category, Integer> ratings = allRatings(4);
        ratings.put(Category.SERVICE, 6);

        post(ratings, null, null);

        assertEquals("Each rating must be between 1 and 5.", presenter.error);
    }

    @Test
    void acceptsTextAtTheMaximumLength() {
        post(allRatings(4), "a".repeat(PostReviewInteractor.MAX_REVIEW_TEXT_LENGTH), null);

        assertNull(presenter.error);
    }

    @Test
    void failsWhenTextIsTooLong() {
        post(allRatings(4), "a".repeat(PostReviewInteractor.MAX_REVIEW_TEXT_LENGTH + 1), null);

        assertEquals("Review text cannot be longer than 1000 characters.", presenter.error);
        assertTrue(truck.getReviews().isEmpty());
    }

    // Images

    @Test
    void attachesValidImages() throws IOException {
        post(allRatings(5), null, List.of(imageFile("taco.png"), imageFile("burrito.JPG")));

        assertNull(presenter.error);
        assertEquals(2, presenter.output.getImagesAttached());
        assertEquals(2, truck.getReviews().get(0).getPhotos().size());
        assertTrue(presenter.output.getSkippedImages().isEmpty());
    }

    @Test
    void skipsUnsupportedFileTypesButKeepsTheRest() throws IOException {
        post(allRatings(5), null, List.of(imageFile("menu.pdf"), imageFile("taco.jpeg")));

        assertEquals(1, presenter.output.getImagesAttached());
        assertEquals(List.of("menu.pdf is not a JPEG or PNG image."),
                presenter.output.getSkippedImages());
    }

    @Test
    void skipsFilesLargerThanTheLimit() throws IOException {
        final File big = tempDir.resolve("huge.png").toFile();
        try (RandomAccessFile file = new RandomAccessFile(big, "rw")) {
            file.setLength(PostReviewInteractor.MAX_IMAGE_SIZE + 1);
        }

        post(allRatings(5), null, List.of(big));

        assertEquals(0, presenter.output.getImagesAttached());
        assertEquals(List.of("huge.png is larger than 5 MB."), presenter.output.getSkippedImages());
        assertEquals(1, truck.getReviews().size());
    }

    @Test
    void skipsImagesOverTheCountLimit() throws IOException {
        final List<File> files = new ArrayList<>();
        for (int i = 0; i <= PostReviewInteractor.MAX_IMAGES_PER_REVIEW; i++) {
            files.add(imageFile("photo" + i + ".png"));
        }

        post(allRatings(5), null, files);

        assertEquals(PostReviewInteractor.MAX_IMAGES_PER_REVIEW,
                presenter.output.getImagesAttached());
        assertEquals(1, presenter.output.getSkippedImages().size());
        assertTrue(presenter.output.getSkippedImages().get(0).startsWith("photo5.png"));
    }

    @Test
    void skipsImagesThatCannotBeStoredAndStillSavesTheReview() throws IOException {
        imageStorage.failFor = "broken.png";

        post(allRatings(5), null, List.of(imageFile("broken.png"), imageFile("taco.png")));

        assertNull(presenter.error);
        assertEquals(1, presenter.output.getImagesAttached());
        assertEquals(List.of("broken.png could not be saved."),
                presenter.output.getSkippedImages());
        assertEquals(1, truck.getReviews().size());
    }

    @Test
    void skipsFilesThatDoNotExist() {
        post(allRatings(5), null, List.of(tempDir.resolve("missing.png").toFile()));

        assertEquals(List.of("missing.png could not be read."),
                presenter.output.getSkippedImages());
    }

    @Test
    void doesNotStoreImagesWhenTheReviewIsRejected() throws IOException {
        final Map<Category, Integer> ratings = allRatings(4);
        ratings.remove(Category.SERVICE);

        post(ratings, null, List.of(imageFile("taco.png")));

        assertEquals(0, imageStorage.saved);
    }

    /** Stores images in memory, and can be told to fail for one file name. */
    private static final class FakeImageStorage implements ImageStorage {
        private String failFor;
        private int saved;

        @Override
        public Image save(File imageFile) throws ImageStorageException {
            if (imageFile.getName().equals(failFor)) {
                throw new ImageStorageException("disk full", null);
            }
            saved++;
            return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        }
    }

    /** Records what the interactor sent to the presenter. */
    private static final class FakePresenter implements PostReviewOutputBoundary {
        private PostReviewOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(PostReviewOutputData outputData) {
            output = outputData;
            error = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }
}
