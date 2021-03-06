package com.qstartlabs.commons.geo.staticmap;

import java.util.ArrayList;
import java.util.List;

public class StaticMapParameters {

    private String center;
    private String zoom;
    private String size;
    private String scale;
    private String format;
    private String maptype;
    private String language;
    private String region;
    private String path;
    private String visible;
    private String style;
    private List<StaticMapMarkerList> markerLists = new ArrayList<StaticMapMarkerList>();

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMaptype() {
        return maptype;
    }

    public void setMaptype(String maptype) {
        this.maptype = maptype;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public List<StaticMapMarkerList> getMarkerLists() {
        return markerLists;
    }

    public void setMarkerLists(List<StaticMapMarkerList> markerLists) {
        this.markerLists = markerLists;
    }

    public void setMarkers(StaticMapMarkerList markerList) {
        this.markerLists = new ArrayList<StaticMapMarkerList>();
        this.markerLists.add(markerList);
    }
}
