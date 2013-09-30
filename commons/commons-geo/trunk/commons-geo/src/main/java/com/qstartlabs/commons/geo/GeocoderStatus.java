package com.qstartlabs.commons.geo;

public enum GeocoderStatus {
    OK ("OK"),
    ZERO_RESULTS ("ZERO_RESULTS"),
    OVER_QUERY_LIMIT ("OVER_QUERY_LIMIT"),
    REQUEST_DENIED ("REQUEST_DENIED"),
    INVALID_REQUEST ("INVALID_REQUEST"),
    UNKNOWN_ERROR ("UNKNOWN_ERROR");
    
    private String value;
    
    private GeocoderStatus (String value) {
    	this.value = value;
    }
    
    public String getValue() {
    	return this.value;
    }
    
    public static GeocoderStatus findByValue(String value) {
    	for (GeocoderStatus status : GeocoderStatus.values()) {
    		if (value.equals(status.getValue())) {
    			return status;
    		}
    	}
    	return null;
    }
}
