package com.qstartlabs.commons.geo;

/**
 * Copy of source code purchased from zipcodedownload.com
 */
public class DistanceAssistant {

  private int EARTH_RADIUS_MILES = 3963;

  public double distance(LatLong pos1, LatLong pos2) {
	  return distance(pos1.getLatitude(), pos1.getLongitude(), pos2.getLatitude(), pos2.getLongitude());
  }
  
  public double distance(double dblLat1, double dblLong1, double dblLat2, double dblLong2) {

      //convert degrees to radians
      dblLat1 = dblLat1 * Math.PI / 180;
      dblLong1 = dblLong1 * Math.PI / 180;
      dblLat2 = dblLat2 * Math.PI / 180;
      dblLong2 = dblLong2 * Math.PI / 180;

      double dist = 0;

      if (dblLat1 != dblLat2 || dblLong1 != dblLong2) 
      {
          //the two points are not the same
          dist = 
              Math.sin(dblLat1) * Math.sin(dblLat2)
              + Math.cos(dblLat1) * Math.cos(dblLat2)
              * Math.cos(dblLong2 - dblLong1);

          dist = 
              EARTH_RADIUS_MILES
              * (-1 * Math.atan(dist / Math.sqrt(1 - dist * dist)) + Math.PI / 2);
      }
      return dist;
  }
}