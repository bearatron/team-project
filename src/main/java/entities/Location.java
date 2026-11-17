package entities;

public class Location {
    private double latitude;
    private double longitude;

    public Location(int latitude, int longitude) {
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("latitude must be in the range [-90, 90]");
        }

        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("longitude must be in the range [-180, 180]");
        }

        this.longitude = longitude;
    }
}
