package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.lang.collections.LRUCache;
import com.qstartlabs.commons.lang.location.Country;
import com.qstartlabs.commons.lang.location.USState;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoogleGeoCoder implements Geocoder {

//	private final static String BASEURL = "https://maps.googleapis.com/maps/api/geocode/json?sensor=false";
    private final static String BASEURL = "https://maps.googleapis.com/maps/api/geocode/json?sensor=false&client=gme-qstartlabsllc";
    private final static String KEYURL = BASEURL;

    private final static String PARAM_ENCODING = "UTF-8";
    private final static String CONTENT_TYPE = "Content-Type";
    private final static String CHARSET = "charset";
    private final static String DEFAULT_ENCODING = "ISO-8859-1";

    private final static String STATUS = "status";
    private final static String RESULTS = "results";
    private final static String FORMATTED_ADDRESS = "formatted_address";
    private final static String ADDRESS_COMPONENTS = "address_components";
    private final static String COUNTRY = "country";
    private final static String ADMINISTRATIVE_AREA = "administrative_area_level_1";
    private final static String ADMINISTRATIVE_AREA2 = "administrative_area_level_2";
    private final static String LOCALITY = "locality";
    private final static String POSTAL_CODE = "postal_code";
    private final static String ROUTE = "route";
    private final static String STREET_NUMBER = "street_number";
    private final static String TYPES = "types";
    private final static String LONG_NAME = "long_name";
    private final static String SHORT_NAME = "short_name";
    private final static String GEOMETRY = "geometry";
    private final static String LOCATION = "location";
    private final static String LONGITUDE = "lng";
    private final static String LATITUDE = "lat";
    private final static long MIN_DELAY = 50L;
    private final static long MAX_DELAY = 1000L;
    private final static long INCREMENT_DELAY = 50L;
    private final static long DECREMENT_DELAY = 10L;
    private static long delay;
    private static long lastUsedTime;
    private static final Logger logger = Logger.getLogger(GoogleGeoCoder.class);

    private final static USState[] countryLikeStates = { USState.AS, USState.GU, USState.MP, USState.PR, USState.UM, USState.VI };

    /**
     * The cache sizes.
     */
    private final static int GEO_CACHE_SIZE = 1024;

    /**
     * A cache of address strings to geocoder results.
     */
    private final static LRUCache<String, GeocoderResult> addressCache = new LRUCache<String, GeocoderResult>(GEO_CACHE_SIZE);

    /**
     * A cache of latitude/longitude to geocoder results.
     */
    private final static LRUCache<LatLong, GeocoderResult> latLongCache = new LRUCache<LatLong, GeocoderResult>(GEO_CACHE_SIZE);

    /**
     * Check the cache for a geolocation. If found, re-add at the head of the
     * list.
     */
    private static GeocoderResult getForwardCache(String address) {
        GeocoderResult result = null;
        synchronized (addressCache) {
            result = addressCache.remove(address);
            if (result != null) {
                addressCache.put(address, result);
            }
        }
        return result;
    }

    /**
     * Check the cache for a reverse geolocation. If found, re-add at the head
     * of the list.
     */
    private static GeocoderResult getReverseCache(LatLong latLong) {
        GeocoderResult result = null;
        synchronized (latLongCache) {
            result = latLongCache.remove(latLong);
            if (result != null) {
                latLongCache.put(latLong, result);
            }
        }
        return result;
    }

    /**
     * Cache a forward result if it's good or will always be bad.
     */
    private static void cacheForward(String address, GeocoderResult result) {
        GeocoderStatus status = result.getStatusCode();
        if (status != GeocoderStatus.UNKNOWN_ERROR && status != GeocoderStatus.OVER_QUERY_LIMIT) {
            synchronized (addressCache) {
                addressCache.put(address, result);
            }
        }
    }

    /**
     * Cache a reverse result if it's good or will always be bad.
     */
    private static void cacheReverse(LatLong latLong, GeocoderResult result) {
        GeocoderStatus status = result.getStatusCode();
        if (status != GeocoderStatus.UNKNOWN_ERROR && status != GeocoderStatus.OVER_QUERY_LIMIT) {
            synchronized (latLongCache) {
                latLongCache.put(latLong, result);
            }
        }
    }

    public GeocoderResult getLocation(String location) {
        return getLocation(KEYURL, location, Country.US);
    }

    public GeocoderResult getLocation(String location, Country country) {
        return getLocation(KEYURL, location, country);
    }

    /**
     * This method, and the one that follows, takes each address field
     * separately, and negates the accuracy field in the case where the city,
     * state, zip, or country disagrees.
     * 
     * In order to make sure extraneous info is not confusing Google, three
     * combinations of including/excluding the Street fields are checked (when
     * different) and the best result is used. (The possibility of including
     * neither field is not done, since it was never useful in a sample of 1000
     * assets.)
     */
    public GeocoderResult getLocation(String street1, String street2, String city, String state, String zipcode, Country country) {
        String zip5 = ((zipcode != null && zipcode.length() > 4) ? zipcode.substring(0, 5) : "");
        String stateAbbr = abbreviate(state);
        String[] addresses = assembleAddresses(street1, street2, city, state, zipcode);
        boolean[] streetFlags = addressSort(addresses);
        if (country == null) {
            country = Country.US;
        }

        GeocoderResult[] results = new GeocoderResult[3];
        GeocoderResult bestResult = results[0];
        for (int i = 0; i < results.length; i++) {
            if (streetFlags[i]) {
                results[i] = getLocation(addresses[i], country);
                String resultCity = results[i].getCity();
                boolean cityMatch = compareCity(city, resultCity);
                String resultState = results[i].getState();
                boolean stateMatch = stateAbbr.equalsIgnoreCase(resultState);
                String resultZipcode = results[i].getZipCode();
                boolean zipMatch = zip5.equals(resultZipcode);
                String resultCountry = results[i].getCountry();
                boolean countryMatch = country.toString().equals(resultCountry.toString());
                if (!(cityMatch && stateMatch && zipMatch && countryMatch)) {
                    results[i].negateAccuracy();
                }
                if (i == 0) {
                    bestResult = results[i];
                }
                int bestAccuracy = bestResult.getAccuracy();
                int currentAccuracy = results[i].getAccuracy();
                if (Math.abs(currentAccuracy) > Math.abs(bestAccuracy)) {
                    bestResult = results[i];
                }
                if (bestAccuracy < 0 && i < results.length - 1) {
                    // Going through again, so slow down.
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        // won't happen
                    }
                } else {
                    break; // Good info, we're done;
                }
            }
        }
        return bestResult;
    }

    private String[] assembleAddresses(String street1, String street2, String city, String state, String zipcode) {
        String[] addresses = new String[3];
        addresses[0] = assembleAddress(street1, street2, city, state, zipcode);
        addresses[1] = assembleAddress(street1, null, city, state, zipcode);
        addresses[2] = assembleAddress(null, street2, city, state, zipcode);
        return addresses;
    }

    private String assembleAddress(String street1, String street2, String city, String state, String zipcode) {
        StringBuilder sb = new StringBuilder();
        if (street1 != null && !street1.isEmpty()) {
            sb.append(street1);
        }
        if (street2 != null && !street2.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(street2);
        }
        if (city != null && !city.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(city);
        }
        if (state != null && !state.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(state);
        }
        if (zipcode != null && !zipcode.isEmpty()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(zipcode);
        }
        return sb.toString();
    }

    /**
     * This method fixes states which are written out, changing to the
     * appropriate abbreviation.
     */
    private String abbreviate(String state) {
        String stateAbbr = state;
        if (state != null && state.length() > 2) {
            USState usState = USState.getStateByCode(state);
            if (usState != null) {
                stateAbbr = usState.toString();
            }
        }
        if (stateAbbr == null) {
            stateAbbr = "";
        }
        return stateAbbr;
    }

    /**
     * This method tests the various address strings, and returns a boolean
     * array that indicates which addresses need to be tested, in order to check
     * each distinct string, but avoid checking the same string twice.
     */
    private boolean[] addressSort(String[] addresses) {
        boolean[] flags = new boolean[addresses.length];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = true;
            for (int j = 0; j < i; j++) {
                if (addresses[i] == addresses[j]) {
                    flags[i] = false;
                    break;
                }
            }
        }
        return flags;
    }

    /**
     * This method compares city names, and checks for common variants. It
     * considers citynames the same if the share the first 7 or last 7
     * characters, though few characters are sufficient if one of the names has
     * less than 7 characters. Also, if removing spaces makes the names equal,
     * then they are considered equal.
     * 
     * @param original
     *            - city from database
     * @param result
     *            - city from Google
     * @return
     */
    private boolean compareCity(String original, String result) {
        original = cleanCity(original);
        result = cleanCity(result);
        int originalLength = original.length();
        int resultLength = result.length();
        boolean equality = original.equals(result);
        boolean sameStart = false;
        boolean sameEnd = false;
        boolean noSpace = false;
        if (originalLength > 3 && resultLength > 3 && !equality) {
            int minLength = Math.min(originalLength, resultLength);
            int subStringSize = Math.min(minLength - 1, 7);
            String originalStart = original.substring(0, subStringSize);
            String resultStart = result.substring(0, subStringSize);
            String originalEnd = original.substring(originalLength - subStringSize);
            String resultEnd = result.substring(resultLength - subStringSize);
            sameStart = originalStart.equals(resultStart);
            sameEnd = originalEnd.equals(resultEnd);
            String originalWithoutSpace = original.replaceAll(" ", "");
            String resultWithoutSpace = result.replaceAll(" ", "");
            noSpace = originalWithoutSpace.equals(resultWithoutSpace);
        }
        return (equality || sameStart || sameEnd || noSpace);
    }

    /**
     * This method, a helper for the previous method, removes all commas,
     * hyphens, or periods from city names, and turns all whitespace occurrences
     * into a single space. Leading and following spaces are removed, and the
     * String is made all upper case.
     */
    private String cleanCity(String city) {
        city = city.replaceAll("[,-\\.\\s]", "~");
        city = city.replaceAll("~+", " ");
        city = city.trim();
        city = city.toUpperCase();
        return city;
    }

    public GeocoderResult reverseLookup(double latitude, double longitude) {
        return reverseLookup(KEYURL, latitude, longitude);
    }

    private GeocoderResult getLocation(String key, String location, Country country) {
        if (country == null)
            country = Country.US;
        // Country-biased location.
        String cbLocation = country.name() + ";" + location;
        GeocoderResult result = getForwardCache(cbLocation);
        if (result == null) {
            String encLocation = null;
            try {
                encLocation = URLEncoder.encode(location, PARAM_ENCODING);
            } catch (UnsupportedEncodingException uee) {
                // This can't happen.
            }
            String url = key + "&address=" + encLocation;

            logger.info("*** Requesting GeoCode for  : " + url);
            result = makeRequest(url, location);
            cacheForward(cbLocation, result);
        }
        return result;
    }

    private GeocoderResult reverseLookup(String key, double latitude, double longitude) {
        LatLong latLong = new LatLong(latitude, longitude);
        GeocoderResult result = getReverseCache(latLong);
        if (result == null) {
            String encLatLong = null;
            try {
                encLatLong = URLEncoder.encode(latLong.toGeoString(), PARAM_ENCODING);
            } catch (UnsupportedEncodingException uee) {
                // This can't happen
            }
            String url = key + "&latlng=" + encLatLong;
            result = makeRequest(url, latLong.toString());
            cacheReverse(latLong, result);
        }
        return result;
    }

    private static synchronized GeocoderResult makeRequest(String url, String location) {
        GeocoderResult geoResult = null;
        JSONObject googleResult = null;

        long diffTime = System.currentTimeMillis() - lastUsedTime;
        long sleepTime = 0L;
        if (diffTime < delay) {
            sleepTime = delay - diffTime;
            logger.info("*** GoogleGeoCoder.java :: sleepTime = delay - diffTime = " + sleepTime + " ***");
        }
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ignored) {
            }
            googleResult = makeRequestInternal(url);
            geoResult = decodeJSON(googleResult, location);
            if (!geoResult.getStatusCode().equals(GeocoderStatus.UNKNOWN_ERROR)) {
                if (delay > MIN_DELAY) {
                    delay -= DECREMENT_DELAY;
                    logger.info("*** GoogleGeoCoder.java :: Decreasing delay to " + delay + " ***");
                }
                break;
            }
            if (delay >= MAX_DELAY) {
                break;
            }
            delay += INCREMENT_DELAY;
            sleepTime = delay;
            logger.info("*** GoogleGeoCoder.java :: Increasing delay to " + delay + " ***");
        }

        lastUsedTime = System.currentTimeMillis();
        return geoResult;
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
    private static synchronized GeocoderResult decodeJSON(JSONObject googleResult, String request) {
        double latitude = 37.09024;
        double longitude = -95.712891;
        int accuracy = 0;
        String streetNumber = "";
        String street = "";
        String city = "";
        String state = "";
        String zipCode = "";
        String countryCode = "";
        String correctedAddress = "";
        GeocoderStatus statusCode = GeocoderStatus.OK;

        List<Integer> accuracyFrequency = new ArrayList<Integer>();
        if (googleResult != null) {
            try {
                statusCode = GeocoderStatus.findByValue(googleResult.getString(STATUS));
                /*
                 * The HTTP status may be OK, but this may not if there are
                 * problems with the request.
                 */
                if (statusCode.equals(GeocoderStatus.OK)) {

                    /*
                     * Iterate through the result elements using the one with
                     * the highest accuracy having the necessary data.
                     */
                    int geoAccuracy = 0;
                    
                    JSONArray results = googleResult.getJSONArray(RESULTS);

                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        
                        // Get the geocoder corrected address
                        if (result.has(FORMATTED_ADDRESS))
                        	correctedAddress = result.getString(FORMATTED_ADDRESS);

                        JSONArray addressComponents = result.getJSONArray(ADDRESS_COMPONENTS);                        
	                        
                        for (int j = 0; j < addressComponents.length(); j++) {
                        	JSONObject addressComponent = addressComponents.getJSONObject(j);
	                        
	                        /*
	                         * Get the accuracy of this address components block and
	                         * make sure it has some address breakdown.
	                         */
	                        if (!addressComponent.has(TYPES))
	                            continue;
	                        
	                        JSONArray types = addressComponent.getJSONArray(TYPES);
	                        
	                        for (int k = 0; k < types.length(); k++) {
	                        	String type = types.getString(k);
	                        	
	                        	if (type.equals(POSTAL_CODE)) {
	                        		zipCode = addressComponent.getString(SHORT_NAME);
	                        	}
	                        	else if (type.equals(COUNTRY)) {
	                        		if (GeocoderResult.COUNTRY > geoAccuracy) geoAccuracy = GeocoderResult.COUNTRY;
	                        		countryCode = addressComponent.getString(SHORT_NAME);
	                        	}
	                        	else if (type.equals(ADMINISTRATIVE_AREA)) {
	                        		if (GeocoderResult.REGION > geoAccuracy) geoAccuracy = GeocoderResult.REGION;
	                        		state = addressComponent.getString(SHORT_NAME);
	                        	}
	                        	else if (type.equals(ADMINISTRATIVE_AREA2)) {
	                        		if (GeocoderResult.SUB_REGION > geoAccuracy) geoAccuracy = GeocoderResult.SUB_REGION;
	                        	}
	                        	else if (type.equals(LOCALITY)) {
	                        		if (GeocoderResult.TOWN > geoAccuracy) geoAccuracy = GeocoderResult.TOWN;
	                        		city = addressComponent.getString(SHORT_NAME);
	                        	}
	                        	else if (type.equals(ROUTE)) {
	                        		if (GeocoderResult.STREET > geoAccuracy) geoAccuracy = GeocoderResult.STREET;
                                    street = addressComponent.getString(SHORT_NAME);
	                        	}
	                        	else if (type.equals(STREET_NUMBER)) {
	                        		if (GeocoderResult.ADDRESS > geoAccuracy) geoAccuracy = GeocoderResult.ADDRESS;
                                    streetNumber = addressComponent.getString(SHORT_NAME);
	                        	}
	                        }
	                    }
                        
                        /*
                         * If this accuracy is better than the current lat/long
                         * data use it.
                         */
                        if (geoAccuracy > accuracy) {
                        	accuracy = geoAccuracy;
                        	if (result.has(GEOMETRY)) {
                        		JSONObject geometry = result.getJSONObject(GEOMETRY);
	                        	if (geometry.has(LOCATION)) {
	                        		JSONObject location = geometry.getJSONObject(LOCATION);
	                        		if (location.has(LATITUDE) && location.has(LONGITUDE)) {
	                        			latitude = location.getDouble(LATITUDE);
	                        			longitude = location.getDouble(LONGITUDE);
	                        		}
	                        	}   
                        	}
                        }
                        
                        accuracyFrequency.add(geoAccuracy);
                    }

                    /*
                     * Try to make sure state is a code, not a name.
                     */
                    state = convertStateCode(countryCode, state);

                    /*
                     * Special handling of US territories that may come back as
                     * countries.
                     */
                    for (USState cls : countryLikeStates) {
                        if (cls.name().equals(countryCode)) {
                            countryCode = Country.US.name();
                            state = cls.name();
                            break;
                        }
                    }

                } else {
                    // TODO Replace with real logging.
                    System.err.println("GoogleGeoCoder geocode request returned status " + statusCode + " for query '" + request + "'");
                }
            } catch (JSONException jsone) {
                // TODO Replace with real logging.
                System.err.println("GoogleGeoCoder error parsing/traversing JSON response for query '" + request + "' : " + jsone);
            }
        }

        // Greg B. -> Because of bug : 2771 - Error message - New trip w/no
        // city/state/zip
        // We will return GeocoderResult.UNKNOWN_LOCATION if there are multiple
        // locations with same 'accuracy' as there is no way to distinguish
        // between them
        int frequency = Collections.frequency(accuracyFrequency, accuracy);
        if (frequency > 1) {
            accuracy = GeocoderResult.UNKNOWN_LOCATION;
        }
        logger.info("Accuracy Frequency :" + frequency);
        return new GeocoderResult(latitude, longitude, accuracy, streetNumber, street, city, state, zipCode, countryCode, statusCode, correctedAddress);
    }

    /**
     * Google variously returns state name or code. What happened to not doing
     * evil? Try to always resolve to state code. Only handling US now.
     */
    private static synchronized String convertStateCode(String countryCode, String state) {
        if ("US".equals(countryCode)) {
            USState usState = null;
            try {
                usState = USState.valueOf(state);
            } catch (Exception ignored) {
            }
            if (usState == null) {
                usState = USState.getStateByCode(state);
            }
            if (usState != null) {
                state = usState.name();
            }
        }
        return state;
    }

}
