package data_access;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import use_case.image_storage.ImageStorage;
import use_case.image_storage.ImageStorageException;

/**
 * Stores images as files in a local folder, each under a unique name.
 */
public class LocalImageStorage implements ImageStorage {
    private final Path folder;

    /**
     * Creates image storage that saves into the given folder, creating it if
     * needed.
     *
     * @param folder the folder to store images in
     */
    public LocalImageStorage(Path folder) {
        this.folder = folder;
    }

    @Override
    public Image save(File imageFile) throws ImageStorageException {
        final BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException exception) {
            throw new ImageStorageException(imageFile.getName() + " could not be read",
                    exception);
        }
        if (image == null) {
            throw new ImageStorageException(imageFile.getName() + " is not a readable image",
                    null);
        }

        final Path target = folder.resolve(UUID.randomUUID() + extensionOf(imageFile));
        try {
            Files.createDirectories(folder);
            Files.copy(imageFile.toPath(), target);
        } catch (IOException exception) {
            deleteQuietly(target);
            throw new ImageStorageException(imageFile.getName() + " could not be stored",
                    exception);
        }
        return image;
    }

    /**
     * Returns a file's extension, including the dot, in lower case.
     *
     * @param file the file
     * @return the extension such as ".png", or an empty string if there is none
     */
    private static String extensionOf(File file) {
        final String name = file.getName();
        final int dot = name.lastIndexOf('.');
        if (dot < 0) {
            return "";
        }
        return name.substring(dot).toLowerCase(Locale.ROOT);
    }

    /**
     * Deletes a partly written file, ignoring any error, so a failed save
     * leaves nothing behind.
     *
     * @param path the file to delete
     */
    private static void deleteQuietly(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ignored) {
            // Nothing more can be done; the original error is reported instead
        }
    }
}
