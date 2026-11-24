package entities;

/**
 * Location entity.
 */
public class Location {
    private static final double MAX_LATITUDE = 90.0;
    private static final double MAX_LONGITUDE = 180.0;

    private final double latitude;
    private final double longitude;

    /**
     * Creates a new Location with the given latitude and longitude.
     *
     * @param latitude The latitude of the FoodTruck, within [-90, 90]
     * @param longitude The longitude of the FoodTruck, within [-180, 180]
     * @throws IllegalArgumentException if either value is out of range
     */
    public Location(double latitude, double longitude) {
        if (!(latitude >= -MAX_LATITUDE && latitude <= MAX_LATITUDE)) {
            throw new IllegalArgumentException(
                    "Latitude must be within [-90, 90]: " + latitude);
        }
        if (!(longitude >= -MAX_LONGITUDE && longitude <= MAX_LONGITUDE)) {
            throw new IllegalArgumentException(
                    "Longitude must be within [-180, 180]: " + longitude);
        }
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }
}
