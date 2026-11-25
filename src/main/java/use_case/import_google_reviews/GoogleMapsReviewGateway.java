package use_case.import_google_reviews;

import java.util.List;

/**
 * Fetches reviews from a Google Maps listing. The real implementation calls
 * the Google Maps API.
 */
public interface GoogleMapsReviewGateway {

    /**
     * Returns the reviews on a Google Maps listing. The Google Maps API
     * returns at most {@value ImportGoogleReviewsInteractor#GOOGLE_IMPORT_MAX}.
     *
     * @param listingId the identifier of the Google Maps listing
     * @return the listing's reviews; empty if it has none
     * @throws ListingNotFoundException if there is no listing with that identifier
     * @throws ReviewImportException if the service fails or is rate-limited
     */
    List<GoogleMapsReviewData> fetchReviews(String listingId)
            throws ListingNotFoundException, ReviewImportException;
}
