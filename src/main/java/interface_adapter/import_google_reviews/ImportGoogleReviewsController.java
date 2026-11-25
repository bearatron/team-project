package interface_adapter.import_google_reviews;

import use_case.import_google_reviews.ImportGoogleReviewsInputBoundary;
import use_case.import_google_reviews.ImportGoogleReviewsInputData;

/**
 * The controller for the Import Google Maps Reviews use case.
 */
public class ImportGoogleReviewsController {
    private final ImportGoogleReviewsInputBoundary interactor;

    /**
     * Creates a new ImportGoogleReviewsController.
     *
     * @param interactor the Import Google Maps Reviews interactor
     */
    public ImportGoogleReviewsController(ImportGoogleReviewsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Runs the Import Google Maps Reviews use case with what the owner entered.
     *
     * @param username the logged-in owner
     * @param listingId the identifier of the truck's Google Maps listing
     */
    public void execute(String username, String listingId) {
        interactor.execute(new ImportGoogleReviewsInputData(username, listingId));
    }
}
