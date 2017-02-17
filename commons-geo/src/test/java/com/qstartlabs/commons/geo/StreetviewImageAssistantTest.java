package com.qstartlabs.commons.geo;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class StreetviewImageAssistantTest {

    @Test
    public void testGetStreetviewImage() throws IOException, URISyntaxException {
        StreetviewImageParameters params = new StreetviewImageParameters();
        params.setPano("6pnQW_lZ9uXmnHXZs84GfA");
        params.setHeading(90D);
        params.setPitch(10);
        params.setHeight(500);
        params.setWidth(500);
        params.setFov(100);

        ByteArrayOutputStream image = StreetviewImageAssistant.getStreetviewImage(params, "jpg");

        assertNotNull(image);
    }

}