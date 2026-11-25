package interface_adapter.import_google_reviews;

/**
 * The state for the Import Google Maps Reviews view.
 */
public class ImportGoogleReviewsState {
    private String listingId = "";
    private String errorMessage;
    private String successMessage;

    public String getListingId() {
        return listingId;
    }

    public void setListingId(String listingId) {
        this.listingId = listingId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
