package use_case.post_review;

/**
 * Output boundary for the Post a Review use case.
 */
public interface PostReviewOutputBoundary {

    /**
     * Prepares the success view.
     *
     * @param outputData the posted review's details
     */
    void prepareSuccessView(PostReviewOutputData outputData);

    /**
     * Prepares the failure view.
     *
     * @param errorMessage why the review could not be posted
     */
    void prepareFailView(String errorMessage);
}
