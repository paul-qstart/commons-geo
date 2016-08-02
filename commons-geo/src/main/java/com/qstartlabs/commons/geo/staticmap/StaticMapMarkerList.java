package com.qstartlabs.commons.geo.staticmap;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class StaticMapMarkerList {

    private List<StaticMapMarker> markers = new ArrayList<StaticMapMarker>();
    private String iconUrl;
    private String style;

    public List<StaticMapMarker> getMarkers() {
        return markers;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getMarkersParameterString() {
        StringBuilder markerString = new StringBuilder("&markers=");

        String delimiter = "";
        if (!StringUtils.isBlank(style)) {
            markerString.append(delimiter).append(style);
            delimiter = "|";
        }
        if (!StringUtils.isBlank(iconUrl)) {
            markerString.append(delimiter).append("icon:"+iconUrl);
            delimiter = "|";
        }
        for (StaticMapMarker marker : markers) {
            markerString.append(delimiter).append(marker.getCoordinates());
            delimiter = "|";
        }
        return markerString.toString();
    }
}
