package com.qstartlabs.commons.geo;

import org.junit.Test;
import org.opengis.referencing.FactoryException;

import static org.junit.Assert.assertEquals;

public class LatLongUtilsTest {

    public static final double DELTA_10_METERS_AT_EQUATOR = .0001D;
    public static final double DELTA_20_METERS_AT_EQUATOR = .0002D;

    @Test
    public void testComputeOffsetPointInMetersN() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.N);

        assertEquals(point.getLongitude(), latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(39.980295D, latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);

    }

    @Test
    public void testComputeOffsetPointInMeters50N() throws FactoryException {
        LatLong point = new LatLong(39.979385D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 50, Direction.N);

        assertEquals(point.getLongitude(), latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(39.979843, latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersNE() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.NE);

        assertEquals(39.9801, latLong.getLatitude(), DELTA_20_METERS_AT_EQUATOR);
        assertEquals(-82.98348, latLong.getLongitude(), DELTA_20_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersE() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 50, Direction.E);

        assertEquals(point.getLatitude(), latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(-82.983535, latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersS() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.S);

        assertEquals(point.getLongitude(), latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(39.978485, latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersSW() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.SW);

        assertEquals(-82.984939, latLong.getLongitude(), DELTA_20_METERS_AT_EQUATOR);
        assertEquals(39.9787445, latLong.getLatitude(), DELTA_20_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersW() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.W);

        assertEquals(point.getLatitude(), latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(-82.985318D, latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeOffsetPointInMetersNW() throws FactoryException {
        LatLong point = new LatLong(39.979381D, -82.984118D);

        LatLong latLong = LatLongUtils.computeOffsetPointInMeters(point, 100, Direction.NW);

        assertEquals(39.980026, latLong.getLatitude(), DELTA_10_METERS_AT_EQUATOR);
        assertEquals(-82.984939, latLong.getLongitude(), DELTA_10_METERS_AT_EQUATOR);
    }

    @Test
    public void testComputeHeadingN() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.N);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(0, heading, 1);
    }

    @Test
    public void testComputeHeadingNE() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.NE);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.NE.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingE() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.E);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.E.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingSE() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.SE);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.SE.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingS() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.S);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.S.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingSW() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.SW);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.SW.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingW() throws FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.W);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.W.getHeading(), heading, 1);
    }

    @Test
    public void testComputeHeadingNW() throws  FactoryException {
        LatLong origin = new LatLong(-82.984118D, 39.979381D);
        LatLong destination = LatLongUtils.computeOffsetPointInMeters(origin, 100, Direction.NW);

        Double heading = LatLongUtils.computeHeading(origin, destination);

        assertEquals(Direction.NW.getHeading(), heading, 1);
    }

}
