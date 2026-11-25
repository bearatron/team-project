package use_case.import_google_reviews;

/**
 * Input boundary for the Import Google Maps Reviews use case.
 */
public interface ImportGoogleReviewsInputBoundary {

    /**
     * Executes the Import Google Maps Reviews use case.
     *
     * @param inputData the input data
     */
    void execute(ImportGoogleReviewsInputData inputData);
}
