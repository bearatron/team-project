package entities;

/**
 * Location entity.
 */
public class Location {
    private final double latitude;
    private final double longitude;

    /**
     * Creates a new Location with the given latitude and longitude.
     *
     * @param latitude The latitude of the FoodTruck.
     * @param longitude The longitude of the FoodTruck.
     */
    public Location(double latitude, double longitude) {
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
