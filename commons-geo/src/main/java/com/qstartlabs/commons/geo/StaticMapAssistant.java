package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.geo.staticmap.StaticMapMarkerList;
import com.qstartlabs.commons.geo.staticmap.StaticMapParameters;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class StaticMapAssistant {

    private static final String CLIENT_ID = "gme-qstartlabsllc";

    private static final Logger logger = Logger.getLogger(StaticMapAssistant.class);
    private static final int MAX_REQUEST_LENGTH = 2048;

    public static String getStaticMapURL(StaticMapParameters parameters) {
        if (!validateParameters(parameters)) {
            return null;
        }

        try {
            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?client=" + CLIENT_ID);
            if (!StringUtils.isBlank(parameters.getCenter())) {
                stringBuilder.append("&center="+parameters.getCenter());
            }
            if (!StringUtils.isBlank(parameters.getZoom())) {
                stringBuilder.append("&zoom="+parameters.getZoom());
            }
            if (!StringUtils.isBlank(parameters.getSize())) {
                stringBuilder.append("&size="+parameters.getSize());
            }
            if (!StringUtils.isBlank(parameters.getScale())) {
                stringBuilder.append("&scale="+parameters.getScale());
            }
            if (!StringUtils.isBlank(parameters.getFormat())) {
                stringBuilder.append("&format="+parameters.getFormat());
            }
            if (!StringUtils.isBlank(parameters.getMaptype())) {
                stringBuilder.append("&maptype="+parameters.getMaptype());
            }
            if (!StringUtils.isBlank(parameters.getLanguage())) {
                stringBuilder.append("&language="+parameters.getLanguage());
            }
            if (!StringUtils.isBlank(parameters.getRegion())) {
                stringBuilder.append("&region="+parameters.getRegion());
            }
            if (!StringUtils.isBlank(parameters.getPath())) {
                stringBuilder.append("&path="+parameters.getPath());
            }
            if (!StringUtils.isBlank(parameters.getVisible())) {
                stringBuilder.append("&visible="+parameters.getVisible());
            }
            if (!StringUtils.isBlank(parameters.getStyle())) {
                stringBuilder.append("&style="+parameters.getStyle());
            }
            for (StaticMapMarkerList staticMapMarkerList : parameters.getMarkerLists()) {
                stringBuilder.append(staticMapMarkerList.getMarkersParameterString());
            }
            String signedRequest = RequestSigner.signRequest(stringBuilder.toString());
            if (signedRequest.length() <= MAX_REQUEST_LENGTH) {
                return signedRequest;
            }
            else {
                logger.debug("getStaticMapURL() -- Request greater than max allowed length of "+MAX_REQUEST_LENGTH+" characters.");
            }
        } catch (Exception e) {
            logger.debug("getStaticMapURL() -- Error signing request: "+e.getMessage(), e);
        }
        return null;
    }

    private static boolean validateParameters(StaticMapParameters parameters) {
        boolean parametersAreValid = true;

        if (StringUtils.isBlank(parameters.getCenter()) && parameters.getMarkerLists().size() == 0) {
            logger.debug("getStaticMapURL() -- Center or Markers must be specified.");
            parametersAreValid = false;
        }
        else if (parameters.getMarkerLists().size() == 0) {
            if (StringUtils.isBlank(parameters.getCenter())) {
                logger.debug("getStaticMapURL() -- Center must be specified.");
                parametersAreValid = false;
            }
            if (StringUtils.isBlank(parameters.getZoom())) {
                logger.debug("getStaticMapURL() -- Zoom must be specified.");
                parametersAreValid = false;
            }
        }
        if (StringUtils.isBlank(parameters.getSize())) {
            logger.debug("getStaticMapURL() -- Size must be specified.");
            parametersAreValid = false;
        }

        return parametersAreValid;
    }
}
