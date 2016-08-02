package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.geo.staticmap.StaticMapMarker;
import com.qstartlabs.commons.geo.staticmap.StaticMapMarkerList;
import com.qstartlabs.commons.geo.staticmap.StaticMapParameters;
import org.junit.Test;
import static org.junit.Assert.*;

public class StaticMapAssistantTest {

    @Test
    public void testGetStaticMapURL() {
        StaticMapParameters parameters = new StaticMapParameters();
        parameters.setSize("600x400");

        StaticMapMarkerList markerList = new StaticMapMarkerList();
        markerList.getMarkers().add(new StaticMapMarker(39.967575, -83.005078));
        markerList.getMarkers().add(new StaticMapMarker(38.967575, -84.005078));
        markerList.getMarkers().add(new StaticMapMarker(37.967575, -85.005078));
        parameters.getMarkerLists().add(markerList);

        StaticMapMarkerList markerList2 = new StaticMapMarkerList();
        markerList2.setIconUrl("http://www.domedia.com/d/resources/images/iface/mapmarkers/PermanentBulletins_001.png");
        markerList2.getMarkers().add(new StaticMapMarker(40.967575, -82.005078));
        markerList2.getMarkers().add(new StaticMapMarker(41.967575, -81.005078));
        markerList2.getMarkers().add(new StaticMapMarker(42.967575, -80.005078));
        parameters.getMarkerLists().add(markerList2);

        String staticMapURL = StaticMapAssistant.getStaticMapURL(parameters);
        assertNotNull(staticMapURL);
    }

    @Test
    public void testParameterValidationEmptyParameters() {
        StaticMapParameters parameters = new StaticMapParameters();
        String staticMapURL = StaticMapAssistant.getStaticMapURL(parameters);
        assertNull(staticMapURL);
    }

    @Test
    public void testParameterValidationMissingZoom() {
        StaticMapParameters parameters = new StaticMapParameters();
        parameters.setCenter("39.967575,-83.005078");
        parameters.setSize("600x400");
        String staticMapURL = StaticMapAssistant.getStaticMapURL(parameters);
        assertNull(staticMapURL);
    }

    @Test
    public void testParameterValidationMissingSize() {
        StaticMapParameters parameters = new StaticMapParameters();
        parameters.setCenter("39.967575,-83.005078");
        parameters.setZoom("12");
        String staticMapURL = StaticMapAssistant.getStaticMapURL(parameters);
        assertNull(staticMapURL);
    }

    @Test
    public void testParameterValidationSuccessful() {
        StaticMapParameters parameters = new StaticMapParameters();
        parameters.setCenter("39.967575,-83.005078");
        parameters.setZoom("12");
        parameters.setSize("600x400");
        String staticMapURL = StaticMapAssistant.getStaticMapURL(parameters);
        assertNotNull(staticMapURL);
    }
}
