package com.qstartlabs.commons.geo;

import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.referencing.FactoryException;

import java.awt.geom.Point2D;

public class LatLongUtils {

    /*
    This system is used by google maps and google earth.
    Some other reference systems are popular in North America, such as NAD83.
    However, NAD83 and WGS84 do not disagree by more than a meter within North America.
    If the methods in this class produce unexpected results check your data and see what coordinate / projection system and units your lat long are in.
    Transform the units to WGS84 if necessary.
    */
    public static final String COORDINATE_REFERENCE_SYSTEM_WGS84 = "EPSG:4326";

    public static LatLong computeOffsetPointInMeters(LatLong latLong, double offset, Direction direction) throws FactoryException {

        GeodeticCalculator calculator = new GeodeticCalculator(CRS.decode(COORDINATE_REFERENCE_SYSTEM_WGS84));

        calculator.setStartingGeographicPoint(latLong.getLongitude(), latLong.getLatitude());
        //geotools uses a heading system that goes from -180 to 180, not from 0 to 360...
        double heading = direction.getHeading();
        if (heading > 180) {
            heading = -1 * (360 - heading);
        }
        calculator.setDirection(heading, offset);
        Point2D destinationGeographicPoint = calculator.getDestinationGeographicPoint();

        return new LatLong(destinationGeographicPoint.getY(), destinationGeographicPoint.getX());
    }

    public static Double computeHeading(LatLong origin, LatLong destinationPoint) throws FactoryException {
        GeodeticCalculator calculator = new GeodeticCalculator(CRS.decode(COORDINATE_REFERENCE_SYSTEM_WGS84));

        calculator.setStartingGeographicPoint(origin.getLongitude(), origin.getLatitude());
        calculator.setDestinationGeographicPoint(destinationPoint.getLongitude(), destinationPoint.getLatitude());

        //geotools uses a heading system that goes from -180 to 180, not from 0 to 360...
        double azimuth = calculator.getAzimuth();
        double heading = azimuth;
        if (azimuth < 0) {
            heading = 360 + azimuth;
        }

        return heading;
    }

}