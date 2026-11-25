package use_case.import_google_reviews;

import entities.Category;
import entities.FoodTruck;
import entities.Review;
import entities.User;
import entities.UserType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * The interactor for the Import Google Maps Reviews use case.
 *
 * <p>Imported reviews only rate {@link Category#OVERALL}; the other
 * categories are left blank.
 */
public class ImportGoogleReviewsInteractor implements ImportGoogleReviewsInputBoundary {

    /** The most reviews one import brings in (the Google Maps API limit). */
    public static final int GOOGLE_IMPORT_MAX = 5;

    private final ImportGoogleReviewsDataAccessInterface dataAccess;
    private final GoogleMapsReviewGateway gateway;
    private final ImportGoogleReviewsOutputBoundary presenter;

    /**
     * Creates a new ImportGoogleReviewsInteractor.
     *
     * @param dataAccess the data access object
     * @param gateway fetches reviews from Google Maps
     * @param presenter the output boundary
     */
    public ImportGoogleReviewsInteractor(ImportGoogleReviewsDataAccessInterface dataAccess,
                                         GoogleMapsReviewGateway gateway,
                                         ImportGoogleReviewsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(ImportGoogleReviewsInputData inputData) {
        // 1. Check the user is an owner with a truck
        final User user = dataAccess.getUser(inputData.getUsername());
        final FoodTruck truck = dataAccess.getTruckByOwner(inputData.getUsername());
        if (user == null || user.getUserType() != UserType.OWNER || truck == null) {
            presenter.prepareFailView("You must be an owner with a registered truck.");
            return;
        }
        if (StringUtils.isBlank(inputData.getListingId())) {
            presenter.prepareFailView("Please enter your Google Maps listing.");
            return;
        }

        // 2. Fetch the reviews; if the service fails, nothing is imported
        final List<GoogleMapsReviewData> fetched;
        try {
            fetched = gateway.fetchReviews(inputData.getListingId());
        } catch (ListingNotFoundException exception) {
            presenter.prepareFailView("That Google Maps listing could not be found.");
            return;
        } catch (ReviewImportException exception) {
            presenter.prepareFailView(
                    "Google Maps is unavailable right now, so no reviews were imported.");
            return;
        }

        // 3. Build the new reviews, skipping duplicates and invalid ones
        final List<Review> newReviews = new ArrayList<>();
        int skipped = 0;
        for (GoogleMapsReviewData data : fetched.subList(0,
                Math.min(fetched.size(), GOOGLE_IMPORT_MAX))) {
            if (data.getTimePosted() == null || isAlreadyImported(truck, newReviews, data)) {
                skipped++;
                continue;
            }
            try {
                newReviews.add(Review.fromGoogleMaps(data.getAuthorDisplayName(),
                        Map.of(Category.OVERALL, data.getRating()), data.getText(), null,
                        data.getTimePosted()));
            } catch (IllegalArgumentException exception) {
                // Google sent a review the entity rejects, such as a blank name
                // or a rating outside 1 to 5
                skipped++;
            }
        }

        // 4. Save them all at once and report how many were imported
        for (Review review : newReviews) {
            truck.addReview(review);
        }
        if (!newReviews.isEmpty()) {
            dataAccess.save(truck);
        }
        presenter.prepareSuccessView(new ImportGoogleReviewsOutputData(truck.getName(),
                newReviews.size(), skipped));
    }

    /**
     * Returns whether a Google Maps review is already on the truck, or already
     * in this batch. Reviews match when they have the same author name and
     * posting time.
     *
     * @param truck the truck being imported to
     * @param batch the reviews built so far in this import
     * @param data the review to check
     * @return true if the review was already imported
     */
    private boolean isAlreadyImported(FoodTruck truck, List<Review> batch,
                                      GoogleMapsReviewData data) {
        final List<Review> existing = new ArrayList<>(truck.getReviews());
        existing.addAll(batch);
        for (Review review : existing) {
            if (review.isImported()
                    && review.getAuthorName().equals(data.getAuthorDisplayName())
                    && review.getTimePosted().equals(data.getTimePosted())) {
                return true;
            }
        }
        return false;
    }
}
