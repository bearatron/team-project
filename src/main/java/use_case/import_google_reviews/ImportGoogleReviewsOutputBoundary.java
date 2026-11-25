package use_case.import_google_reviews;

/**
 * Output boundary for the Import Google Maps Reviews use case.
 */
public interface ImportGoogleReviewsOutputBoundary {

    /**
     * Prepares the success view.
     *
     * @param outputData how many reviews were imported and skipped
     */
    void prepareSuccessView(ImportGoogleReviewsOutputData outputData);

    /**
     * Prepares the failure view.
     *
     * @param errorMessage why the reviews could not be imported
     */
    void prepareFailView(String errorMessage);
}
