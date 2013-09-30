package com.qstartlabs.commons.geo;

import org.junit.Test;
import static org.junit.Assert.*;

import com.qstartlabs.commons.lang.location.Country;

public class GeocodeV3Test {
	
	@Test
	public void testGetLocationByLocation() {
		Geocoder geocoder = new GoogleGeoCoder();
		GeocoderResult geocoderResult = geocoder.getLocation("274 Marconi Blvd Suite 300, Columbus OH 43215");
		assertNotNull(geocoderResult);
		assertTrue(geocoderResult.getStatusCode() == GeocoderStatus.OK);
	}
	
	@Test
	public void testGetLocationByLocationAndCountry() {
		Geocoder geocoder = new GoogleGeoCoder();
		GeocoderResult geocoderResult = geocoder.getLocation("1 Capitol Square, Columbus, OH ‎43215", Country.US);
		assertNotNull(geocoderResult);
		assertTrue(geocoderResult.getStatusCode() == GeocoderStatus.OK);	
	}
	
	@Test
	public void testGetLocationByFullAddress() {
		Geocoder geocoder = new GoogleGeoCoder();
		GeocoderResult geocoderResult = geocoder.getLocation("1600 Pennsylvania Ave NW", "", "Washington, D.C.", "DC", "20500", Country.US);
		assertNotNull(geocoderResult);
		assertTrue(geocoderResult.getStatusCode() == GeocoderStatus.OK);	
	}
	
	@Test
	public void testReverseLookup() {
		Geocoder geocoder = new GoogleGeoCoder();
		GeocoderResult geocoderResult = geocoder.reverseLookup(39.9675235, -83.0051184);
		assertNotNull(geocoderResult);
		assertEquals("43215", geocoderResult.getZipCode());
		
	}

}
