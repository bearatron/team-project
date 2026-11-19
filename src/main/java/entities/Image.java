package entities;

/**
 * Returns an image related to a given FoodTruck.
 */
public class Image {
    private final String url;
    private final String caption;

    /**
     * Creates a new image related to a given FoodTruck with a url and caption.
     * @param url The url of the image.
     * @param caption The caption of the image.
     */
    public Image(String url, String caption) {
        this.url = url;
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }
    public String getCaption() {
        return caption;
    }
}
