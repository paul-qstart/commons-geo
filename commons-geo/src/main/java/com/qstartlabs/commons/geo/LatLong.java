package com.qstartlabs.commons.geo;

public class LatLong implements java.io.Serializable {
    final static long serialVersionUID = 201003291332L;

    /**
     * Keep only 16 bits of fraction, good for not quite meter accuracy.
     */
    public final static int PRECISION = 16;

    private final int latitude;
    private final int longitude;

    /**
     * Normalize a latitude or longitude to 16 bits of fraction. Assumes
     * argument is in appropriate range.
     */
    public static double normalize(double ll) {
        return Math.scalb((int) (Math.round(Math.scalb(ll, PRECISION))), -PRECISION);
    }

    public LatLong(double latitude, double longitude) {
        this.latitude = (int) (Math.round(Math.scalb(latitude, PRECISION)));
        this.longitude = (int) (Math.round(Math.scalb(longitude, PRECISION)));
    }

    public double getLatitude() {
        return Math.scalb(latitude, -PRECISION);
    }

    public double getLongitude() {
        return Math.scalb(longitude, -PRECISION);
    }

    /**
     * Return a string appropriate for a geolocation lookup.
     */
    public String toGeoString() {
        return (getLatitude() + "," + getLongitude());
    }

    @Override
    public String toString() {
        return "(" + getLatitude() + ", " + getLongitude() + ")";
    }

    @Override
    public int hashCode() {
        return (latitude ^ longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LatLong)) {
            return false;
        }
        LatLong that = (LatLong) o;
        return ((latitude == that.latitude) && (longitude == that.longitude));
    }
}
