package interface_adapter.post_review;

import use_case.post_review.PostReviewOutputBoundary;
import use_case.post_review.PostReviewOutputData;

/**
 * The presenter for the Post a Review use case.
 */
public class PostReviewPresenter implements PostReviewOutputBoundary {
    private final PostReviewViewModel viewModel;

    /**
     * Creates a new PostReviewPresenter.
     *
     * @param viewModel the Post a Review view model
     */
    public PostReviewPresenter(PostReviewViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(PostReviewOutputData outputData) {
        final PostReviewState state = viewModel.getState();
        String message = "Your review of " + outputData.getTruckName() + " was posted";
        if (outputData.getImagesAttached() > 0) {
            message += " with " + outputData.getImagesAttached() + " image(s)";
        }
        state.setSuccessMessage(message + ".");
        state.setSkippedImages(outputData.getSkippedImages());
        state.setErrorMessage(null);
        state.setText("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final PostReviewState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setSuccessMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
