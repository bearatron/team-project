package entities;

/**
 * Returns an image related to a given FoodTruck.
 */
public class Image {
    private final String url;
    private final String caption;

    /**
     * Creates a new image related to a given FoodTruck with a url and caption.
     * @param url The location of the image file, which can not be null.
     * @param caption A short description of the image.
     */
    public Image(String url, String caption) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Image url can not be empty");
        }
        this.url = url;
        this.caption = caption == null ? "" : caption;
    }

    /**
     * Return the location of an image file, through a url.
     *
     * @return the url of an image.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Return a short description of an image.
     *
     * @return the caption of an image.
     */
    public String getCaption() {
        return caption;
    }
}
