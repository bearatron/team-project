package interface_adapter.post_review;

import entities.Category;
import java.io.File;
import java.util.List;
import java.util.Map;
import use_case.post_review.PostReviewInputBoundary;
import use_case.post_review.PostReviewInputData;

/**
 * The controller for the Post a Review use case.
 */
public class PostReviewController {
    private final PostReviewInputBoundary interactor;

    /**
     * Creates a new PostReviewController.
     *
     * @param interactor the Post a Review interactor
     */
    public PostReviewController(PostReviewInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Runs the Post a Review use case with what the user entered.
     *
     * @param username the logged-in user
     * @param truckName the name of the truck being reviewed
     * @param ratings the score the user chose for each category
     * @param text the optional review text
     * @param imageFiles the image files the user attached, or null for none
     */
    public void execute(String username, String truckName, Map<Category, Integer> ratings,
                        String text, List<File> imageFiles) {
        final PostReviewInputData inputData =
                new PostReviewInputData(username, truckName, ratings, text, imageFiles);
        interactor.execute(inputData);
    }
}
