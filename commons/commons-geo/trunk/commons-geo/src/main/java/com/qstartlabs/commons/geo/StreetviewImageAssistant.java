package com.qstartlabs.commons.geo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StreetviewImageAssistant {

    private static final String CLIENT_ID = "gme-qstartlabsllc";

    private static final Logger logger = Logger.getLogger(GoogleGeoCoder.class);

    public static String getStreetviewImageURL(StreetviewImageParameters parameters) {
        try {
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/streetview?client=" + CLIENT_ID);

            if (StringUtils.isBlank(parameters.getPano()) && StringUtils.isBlank(parameters.getLocation()) && (parameters.getLatitude() == null || parameters.getLongitude() == null)) {
                logger.debug("getStreetviewImageURL() -- Cannot create Streetview URL: Pano, Location, or Latitude/Longitude must be specified.");
            }
            else {
                if (!StringUtils.isBlank(parameters.getPano())) {
                    stringBuilder.append("&pano=" + parameters.getPano());
                }
                else if (parameters.getLatitude() != null && parameters.getLongitude() != null) {
                    stringBuilder.append("&location=" + parameters.getLatitude()+","+parameters.getLongitude());
                }
                else {
                    stringBuilder.append("&location=" + parameters.getLocation());
                }
                if (parameters.getHeading() != null) {
                    stringBuilder.append("&heading=" + parameters.getHeading());
                }
                if (parameters.getPitch() != null) {
                    stringBuilder.append("&pitch=" + parameters.getPitch());
                }
                stringBuilder.append("&fov=" + parameters.getFov());
                stringBuilder.append("&size=" + parameters.getWidth() + "x" + parameters.getHeight());
                return RequestSigner.signRequest(stringBuilder.toString());
            }
        } catch (Exception e) {
            logger.debug("getStreetviewImageURL() -- Error signing request: "+e.getMessage(), e);
        }
        return null;
    }

}
