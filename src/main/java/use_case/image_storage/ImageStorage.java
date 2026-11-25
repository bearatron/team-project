package use_case.image_storage;

import java.awt.Image;
import java.io.File;

/**
 * Stores uploaded image files. Used by any use case that saves images, such as
 * attaching images to a review.
 */
public interface ImageStorage {

    /**
     * Stores an image file that the calling use case has already validated.
     * The file is stored exactly once under a unique name. If storing fails,
     * nothing is left behind.
     *
     * @param imageFile the image file to store
     * @return the stored image
     * @throws ImageStorageException if the file cannot be read or stored
     */
    Image save(File imageFile) throws ImageStorageException;
}
