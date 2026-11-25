package use_case.import_google_reviews;

/**
 * The input data for the Import Google Maps Reviews use case.
 */
public class ImportGoogleReviewsInputData {
    private final String username;
    private final String listingId;

    /**
     * Creates the input data for importing Google Maps reviews.
     *
     * @param username the logged-in owner
     * @param listingId the identifier of the truck's Google Maps listing
     */
    public ImportGoogleReviewsInputData(String username, String listingId) {
        this.username = username;
        this.listingId = listingId;
    }

    public String getUsername() {
        return username;
    }

    public String getListingId() {
        return listingId;
    }
}
