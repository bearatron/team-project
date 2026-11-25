package use_case.post_review;

/**
 * Input boundary for the Post a Review use case.
 */
public interface PostReviewInputBoundary {

    /**
     * Executes the Post a Review use case.
     *
     * @param inputData the input data
     */
    void execute(PostReviewInputData inputData);
}
