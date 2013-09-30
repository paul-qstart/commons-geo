package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.lang.location.Country;


/**
 * Interface to represent a Geocoder service. A geocoder takes a location string
 * and returns the lat / long of the specified location.
 * 
 * @author Eric Rosenberg 
 */
public interface Geocoder {

    GeocoderResult getLocation(String location);

    GeocoderResult getLocation(String location, Country country);

    GeocoderResult getLocation(String address1, String address2, String city, String state, String zipcode, Country country);

    GeocoderResult reverseLookup(double latitude, double longitude);

}
