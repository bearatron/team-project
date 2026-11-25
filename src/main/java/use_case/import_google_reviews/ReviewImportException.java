package use_case.import_google_reviews;

/**
 * Thrown when the Google Maps service fails or is rate-limited.
 */
public class ReviewImportException extends Exception {

    /**
     * Creates an exception with a description of what went wrong.
     *
     * @param message what went wrong
     * @param cause the underlying error, or {@code null} if there is none
     */
    public ReviewImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
