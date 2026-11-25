package use_case.import_google_reviews;

/**
 * Thrown when there is no Google Maps listing with the requested identifier.
 */
public class ListingNotFoundException extends Exception {

    /**
     * Creates an exception for a listing that could not be found.
     *
     * @param listingId the identifier that was not found
     */
    public ListingNotFoundException(String listingId) {
        super("No Google Maps listing found for " + listingId);
    }
}
