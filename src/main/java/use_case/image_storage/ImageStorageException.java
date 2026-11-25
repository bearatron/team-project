package use_case.image_storage;

/**
 * Thrown when an image cannot be read or stored.
 */
public class ImageStorageException extends Exception {

    /**
     * Creates an exception with a description of what went wrong.
     *
     * @param message what went wrong
     * @param cause the underlying error, or {@code null} if there is none
     */
    public ImageStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
