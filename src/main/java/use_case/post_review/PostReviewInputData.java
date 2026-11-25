package use_case.post_review;

import entities.Category;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * The input data for the Post a Review use case, including any attached
 * image files.
 */
public class PostReviewInputData {
    private final String username;
    private final String truckName;
    private final Map<Category, Integer> ratings;
    private final String text;
    private final List<File> imageFiles;

    /**
     * Creates the input data for posting a review.
     *
     * @param username the logged-in user
     * @param truckName the name of the truck being reviewed
     * @param ratings the score the user gave each category
     * @param text the optional review text
     * @param imageFiles the image files the user attached, or null for none
     */
    public PostReviewInputData(String username, String truckName,
                               Map<Category, Integer> ratings, String text,
                               List<File> imageFiles) {
        this.username = username;
        this.truckName = truckName;
        this.ratings = ratings;
        this.text = text;
        this.imageFiles = imageFiles;
    }

    public String getUsername() {
        return username;
    }

    public String getTruckName() {
        return truckName;
    }

    public Map<Category, Integer> getRatings() {
        return ratings;
    }

    public String getText() {
        return text;
    }

    public List<File> getImageFiles() {
        return imageFiles;
    }
}
