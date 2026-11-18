package entities;

/** Where a {@link Review} came from. */
public enum ReviewSource {
    /** Posted by a reviewer in this app. */
    APP,
    /** Imported from Google Maps; it has no in-app author. */
    GOOGLE_MAPS
}
