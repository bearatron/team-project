package use_case.post_review;

import entities.Category;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The output data for the Post a Review use case.
 */
public class PostReviewOutputData {
    private final String truckName;
    private final String authorName;
    private final Map<Category, Integer> ratings;
    private final String text;
    private final int imagesAttached;
    private final List<String> skippedImages;
    private final LocalDateTime timePosted;

    /**
     * Creates the output data for a posted review.
     *
     * @param truckName the name of the reviewed truck
     * @param authorName the reviewer's username
     * @param ratings the score given to each category
     * @param text the review text, or null if there is none
     * @param imagesAttached how many images were attached to the review
     * @param skippedImages one message per image that was not attached,
     *     saying which file and why
     * @param timePosted when the review was posted
     */
    public PostReviewOutputData(String truckName, String authorName,
                                Map<Category, Integer> ratings, String text,
                                int imagesAttached, List<String> skippedImages,
                                LocalDateTime timePosted) {
        this.truckName = truckName;
        this.authorName = authorName;
        this.ratings = ratings;
        this.text = text;
        this.imagesAttached = imagesAttached;
        this.skippedImages = skippedImages;
        this.timePosted = timePosted;
    }

    public String getTruckName() {
        return truckName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public Map<Category, Integer> getRatings() {
        return ratings;
    }

    public String getText() {
        return text;
    }

    public int getImagesAttached() {
        return imagesAttached;
    }

    public List<String> getSkippedImages() {
        return skippedImages;
    }

    public LocalDateTime getTimePosted() {
        return timePosted;
    }
}
