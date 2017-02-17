package com.qstartlabs.commons.geo;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Documentation on this rest api can be found here: https://developers.google.com/maps/documentation/streetview/intro
 */
public class StreetViewImageMetadataService {

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/streetview/metadata?client=" + StreetViewImageMetadataService.CLIENT_ID;
    private static final String CLIENT_ID = "gme-qstartlabsllc";

    private static final Logger logger = Logger.getLogger(StreetViewImageMetadataService.class);


    public static StreetViewImageMetadata getStreetViewImageMetadata(LatLong latLong) throws JSONException {
        String url = BASE_URL;
        StreetViewImageMetadata result = null;
        if (!ObjectUtils.equals(latLong.getLatitude(), 0D) || !ObjectUtils.equals(latLong.getLongitude(), 0D)) {
            url = url + "&location=" + latLong.getLatitude() + "," + latLong.getLongitude();
        } else {
            result = new StreetViewImageMetadata();
            result.setStatus(StreetViewImageMetadataStatusType.INVALID_REQUEST);
        }

        try {
            JSONObject jsonResponse = HttpAssitant.makeRequestInternal(url);
            result = new StreetViewImageMetadata(jsonResponse);
        } catch (RuntimeException ex) {
            logger.error("An error has occurred calling Google streetview metadata.", ex);
            result = new StreetViewImageMetadata();
            result.setStatus(StreetViewImageMetadataStatusType.UNKNOWN_ERROR);
        }

        return result;
    }


}
