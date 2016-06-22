package com.qstartlabs.commons.geo;

public class StreetviewImageParameters {

    private Double latitude;
    private Double longitude;
    private String location;
    private String pano;
    private Double heading;
    private Integer pitch;
    private Integer fov = 40;
    private Integer width = 1024;
    private Integer height = 768;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPano() {
        return pano;
    }

    public void setPano(String pano) {
        this.pano = pano;
    }

    public Double getHeading() {
        return heading;
    }

    public void setHeading(Double heading) {
        this.heading = heading;
    }

    public Integer getPitch() {
        return pitch;
    }

    public void setPitch(Integer pitch) {
        this.pitch = pitch;
    }

    public Integer getFov() {
        return fov;
    }

    public void setFov(Integer fov) {
        this.fov = fov;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
