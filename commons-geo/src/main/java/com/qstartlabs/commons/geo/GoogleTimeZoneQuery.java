package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.lang.collections.LRUCache;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleTimeZoneQuery implements TimeZoneQuery {

    private final static String BASEURL = "https://maps.googleapis.com/maps/api/timezone/json?client=gme-qstartlabsllc";
    private final static String KEYURL = BASEURL;

    private final static String DEFAULT_ENCODING = "ISO-8859-1";

    private final static String TIME_ZONE_ID = "timeZoneId";
    private final static String TIME_ZONE_NAME = "timeZoneName";
    private final static String DST_OFFSET = "dstOffset";
    private final static String RAW_OFFSET = "rawOffset";

    private final static String STATUS = "status";
    private final static long MIN_DELAY = 50L;
    private final static long MAX_DELAY = 1000L;
    private final static long INCREMENT_DELAY = 50L;
    private final static long DECREMENT_DELAY = 10L;
    private static long delay;
    private static long lastUsedTime;
    private static final Logger logger = Logger.getLogger(GoogleGeoCoder.class);

    /**
     * The cache sizes.
     */
    private final static int GEO_CACHE_SIZE = 1024;

    /**
     * A cache of latitude/longitude to geocoder results.
     */
    private final static LRUCache<LatLong, TimeZoneResult> latLongCache = new LRUCache<LatLong, TimeZoneResult>(GEO_CACHE_SIZE);

    private static TimeZoneResult getCache(LatLong latLong) {
        TimeZoneResult result = null;
        synchronized (latLongCache) {
            result = latLongCache.remove(latLong);
            if (result != null) {
                latLongCache.put(latLong, result);
            }
        }
        return result;
    }

    /**
     * Cache a result if it's good or will always be bad.
     */
    private static void cache(LatLong latLong, TimeZoneResult result) {
        TimeZoneStatus status = result.getStatus();
        if ((status != TimeZoneStatus.UNKNOWN_ERROR) && (status != TimeZoneStatus.OVER_QUERY_LIMIT) && (status != TimeZoneStatus.INVALID_REQUEST)) {
            synchronized (latLongCache) {
                latLongCache.put(latLong, result);
            }
        }
    }

    @Override
    public TimeZoneResult getTimeZone(Double latitude, Double longitude) {
        LatLong latLong = new LatLong(latitude.doubleValue(), longitude.doubleValue());

        TimeZoneResult result = getCache(latLong);
        if (result == null) {
            String url = KEYURL + "&location=" + latLong.toGeoString() + "&timestamp=" + System.currentTimeMillis() / 1000;

            logger.info("*** Requesting TimeZone for  : " + url);
            result = makeRequest(url, latLong);
            cache(latLong, result);
        }
        return result;
    }

    private static synchronized TimeZoneResult makeRequest(String url, LatLong latLong) {
        TimeZoneResult timeZoneResult = null;
        JSONObject googleResult = null;

        long diffTime = System.currentTimeMillis() - lastUsedTime;
        long sleepTime = 0L;
        if (diffTime < delay) {
            sleepTime = delay - diffTime;
            logger.info("*** GoogleTimeZoneQuery.java :: sleepTime = delay - diffTime = " + sleepTime + " ***");
        }
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
            googleResult = makeRequestInternal(url);
            timeZoneResult = decodeJSON(googleResult, latLong);
            if (!timeZoneResult.getStatus().equals(GeocoderStatus.UNKNOWN_ERROR)) {
                if (delay > MIN_DELAY) {
                    delay -= DECREMENT_DELAY;
                    logger.info("*** GoogleTimeZoneQuery.java :: Decreasing delay to " + delay + " ***");
                }
                break;
            }
            if (delay >= MAX_DELAY) {
                break;
            }
            delay += INCREMENT_DELAY;
            sleepTime = delay;
            logger.info("*** GoogleTimeZoneQuery.java :: Increasing delay to " + delay + " ***");
        }

        lastUsedTime = System.currentTimeMillis();
        return timeZoneResult;
    }

    private static synchronized JSONObject makeRequestInternal(String unsignedUrl) {
        JSONObject googleResult = null;
        long t0 = System.nanoTime();

        String url = unsignedUrl;

        try {
            url = RequestSigner.signRequest(unsignedUrl);
            HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
            Reader reader = new InputStreamReader(connection.getInputStream(), DEFAULT_ENCODING);

            JSONTokener jTokener = new JSONTokener(reader);
            googleResult = new JSONObject(jTokener);

        } catch (Exception e) {
            e.printStackTrace();
        }

        long t1 = System.nanoTime() - t0;
        logger.info("GoogleGeoCoder.java::makeRequestInternal() took: " + (t1 / 1000000000.0) + " second(s)");

        return googleResult;
    }

    /**
     * Google sucks big-time. They keep making changes to the data they return.
     * Trying to find the best value for the data you're looking for is like
     * nailing jello to the wall.
     */
    private static synchronized TimeZoneResult decodeJSON(JSONObject googleResult, LatLong request) {
        Double dstOffset = null;
        Double rawOffset = null;
        String timeZoneId = null;
        String timeZoneName = null;
        TimeZoneStatus statusCode = TimeZoneStatus.OK;

        List<Integer> accuracyFrequency = new ArrayList<Integer>();
        if (googleResult != null) {
            try {
                statusCode = TimeZoneStatus.valueOf(googleResult.getString(STATUS));
                /*
                 * The HTTP status may be OK, but this may not if there are
                 * problems with the request.
                 */
                if (statusCode.equals(TimeZoneStatus.OK)) {
                    timeZoneId = googleResult.getString(TIME_ZONE_ID);
                    timeZoneName = googleResult.getString(TIME_ZONE_NAME);
                    dstOffset = googleResult.getDouble(DST_OFFSET);
                    rawOffset = googleResult.getDouble(RAW_OFFSET);

                } else {
                    // TODO Replace with real logging.
                    System.err.println("GoogleTimeZoneQuery request returned status " + statusCode + " for query '" + request + "'");
                }
            } catch (JSONException jsone) {
                // TODO Replace with real logging.
                System.err.println("GoogleTimeZoneQuery error parsing/traversing JSON response for query '" + request + "' : " + jsone);
            }
        }

        return new TimeZoneResult(dstOffset, rawOffset, timeZoneId, timeZoneName, statusCode);
    }

}
