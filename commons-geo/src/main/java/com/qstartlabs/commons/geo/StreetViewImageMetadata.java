package com.qstartlabs.commons.geo;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class StreetViewImageMetadata {

    private double lat;
    private double lng;
    private String panoId;
    private StreetViewImageMetadataStatusType status;

    private static final Logger logger = Logger.getLogger(StreetViewImageMetadata.class);

    public  StreetViewImageMetadata() {

    }

    public StreetViewImageMetadata(JSONObject jsonResponse) {
        try {
            this.setStatus(StreetViewImageMetadataStatusType.valueOf(jsonResponse.getString("status")));
            if (StreetViewImageMetadataStatusType.OK.equals(this.getStatus())) {
                JSONObject location = jsonResponse.getJSONObject("location");

                this.setLat(location.getDouble("lat"));
                this.setLng(location.getDouble("lng"));
                this.setPanoId(jsonResponse.getString("pano_id"));
            }
        } catch (JSONException e) {
            logger.error("An error has occurred parsing a google street view metadata response", e);
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPanoId() {
        return panoId;
    }

    public void setPanoId(String panoId) {
        this.panoId = panoId;
    }

    public StreetViewImageMetadataStatusType getStatus() {
        return status;
    }

    public void setStatus(StreetViewImageMetadataStatusType status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StreetViewImageMetadata{" +
                "lat=" + lat +
                ", lng=" + lng +
                ", panoId='" + panoId + '\'' +
                ", status=" + status +
                '}';
    }
}
