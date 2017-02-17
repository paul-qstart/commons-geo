package com.qstartlabs.commons.geo;

import org.json.JSONException;
import org.junit.Test;

import static org.junit.Assert.*;

public class StreetViewImageMetadataServiceTest {

    @Test
    public void testGetStreetViewImageMetadataLocatesAPanorama() throws JSONException {
        LatLong parameters = new LatLong(-80D, 40D);

        StreetViewImageMetadata streetViewImageMetadata = StreetViewImageMetadataService.getStreetViewImageMetadata(parameters);

        assertEquals(StreetViewImageMetadataStatusType.OK, streetViewImageMetadata.getStatus());
        assertNotNull(streetViewImageMetadata.getPanoId());
    }

    @Test
    public void testGetStreetViewImageMetadataInvalidLocation() throws JSONException {
        LatLong latLong = new LatLong(40D, -80000D);

        StreetViewImageMetadata streetViewImageMetadata = StreetViewImageMetadataService.getStreetViewImageMetadata(latLong);

        assertEquals(StreetViewImageMetadataStatusType.NOT_FOUND, streetViewImageMetadata.getStatus());
    }

}