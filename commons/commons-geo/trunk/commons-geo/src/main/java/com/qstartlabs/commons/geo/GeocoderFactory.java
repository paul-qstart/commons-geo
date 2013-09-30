package com.qstartlabs.commons.geo;

public class GeocoderFactory {

  public static Geocoder getInstance() {
    return new GoogleGeoCoder();
  }

}
