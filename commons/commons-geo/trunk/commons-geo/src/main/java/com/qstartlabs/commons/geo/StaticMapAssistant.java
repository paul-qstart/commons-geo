package com.qstartlabs.commons.geo;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class StaticMapAssistant {

    private static final String CLIENT_ID = "gme-qstartlabsllc";

    private static final Logger logger = Logger.getLogger(GoogleGeoCoder.class);

    public static String getStaticMapURL(StaticMapParameters parameters) {
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
            if (!StringUtils.isBlank(parameters.getMarkers())) {
                if(!StringUtils.isBlank(parameters.getIconUrl())) {
                    stringBuilder.append("&markers=icon:"+parameters.getIconUrl()+"|"+parameters.getMarkers());
                } else {
                    stringBuilder.append("&markers="+parameters.getMarkers());
                }
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
            return RequestSigner.signRequest(stringBuilder.toString());
        } catch (Exception e) {
            logger.debug("Error signing request: "+e.getMessage(), e);
        }
        return null;
    }
}
