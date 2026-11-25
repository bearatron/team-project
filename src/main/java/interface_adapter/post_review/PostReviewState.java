package interface_adapter.post_review;

import java.util.List;

/**
 * The state for the Post a Review view.
 */
public class PostReviewState {
    private String text = "";
    private String errorMessage;
    private String successMessage;
    private List<String> skippedImages = List.of();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public List<String> getSkippedImages() {
        return skippedImages;
    }

    public void setSkippedImages(List<String> skippedImages) {
        this.skippedImages = skippedImages;
    }
}
