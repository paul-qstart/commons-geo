package com.qstartlabs.commons.geo.staticmap;

public class StaticMapMarker {

    private Double latitude;
    private Double longitude;

    public StaticMapMarker(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCoordinates() {
        return latitude + "," + longitude;
    }
}
