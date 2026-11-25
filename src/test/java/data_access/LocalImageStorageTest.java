package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.image_storage.ImageStorageException;

class LocalImageStorageTest {

    @TempDir
    Path tempDir;

    private List<Path> storedFiles(Path folder) throws IOException {
        if (!Files.exists(folder)) {
            return List.of();
        }
        try (Stream<Path> files = Files.list(folder)) {
            return files.toList();
        }
    }

    @Test
    void storesAnImageUnderAUniqueName() throws IOException, ImageStorageException {
        final File png = tempDir.resolve("taco.png").toFile();
        ImageIO.write(new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB), "png", png);
        final Path folder = tempDir.resolve("stored");
        final LocalImageStorage storage = new LocalImageStorage(folder);

        final Image first = storage.save(png);
        storage.save(png);

        assertEquals(3, first.getWidth(null));
        assertEquals(2, first.getHeight(null));
        final List<Path> stored = storedFiles(folder);
        assertEquals(2, stored.size());
        assertTrue(stored.stream().allMatch(p -> p.toString().endsWith(".png")));
    }

    @Test
    void rejectsAFileThatIsNotAnImage() throws IOException {
        final File fake = Files.writeString(tempDir.resolve("fake.png"), "not an image").toFile();
        final Path folder = tempDir.resolve("stored");

        assertThrows(ImageStorageException.class, () -> new LocalImageStorage(folder).save(fake));
        assertTrue(storedFiles(folder).isEmpty());
    }

    @Test
    void rejectsAMissingFile() {
        final File missing = tempDir.resolve("missing.png").toFile();

        assertThrows(ImageStorageException.class,
                () -> new LocalImageStorage(tempDir.resolve("stored")).save(missing));
    }
}
