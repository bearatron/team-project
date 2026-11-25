package use_case.import_google_reviews;

import java.time.LocalDateTime;

/**
 * One review as returned by Google Maps.
 */
public class GoogleMapsReviewData {
    private final String authorDisplayName;
    private final int rating;
    private final String text;
    private final LocalDateTime timePosted;

    /**
     * Creates the data for one Google Maps review.
     *
     * @param authorDisplayName the name shown on the review
     * @param rating the star rating, from 1 to 5
     * @param text the review text, or null if there is none
     * @param timePosted when the review was posted on Google Maps
     */
    public GoogleMapsReviewData(String authorDisplayName, int rating, String text,
                                LocalDateTime timePosted) {
        this.authorDisplayName = authorDisplayName;
        this.rating = rating;
        this.text = text;
        this.timePosted = timePosted;
    }

    public String getAuthorDisplayName() {
        return authorDisplayName;
    }

    public int getRating() {
        return rating;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }
}
