package com.qstartlabs.commons.geo;


/**
 * Class to hold the result of a call to geo-code an object. It contains a
 * latitude, a longitude, and an 'accuracy' to indicate how effectively the
 * geo-coder was able to encode the location.
 * 
 * @author Eric Rosenberg
 */
public class GeocoderResult implements java.io.Serializable {
    final static long serialVersionUID = 201003291336L;

    public static final int UNKNOWN_LOCATION = 0;
    public static final int COUNTRY = 1;
    public static final int REGION = 2;
    public static final int SUB_REGION = 3;
    public static final int TOWN = 4;
    public static final int POST = 5;
    public static final int STREET = 6;
    public static final int INTERSECTION = 7;
    public static final int ADDRESS = 8;
    public static final int PREMISE = 9;

    private double latitude;
    private double longitude;
    private int accuracy;
    private String streetNumber;
    private String street;
    private String city;
    private String state;
    private String county;
    private String zipCode;
    private String country;
    private GeocoderStatus statusCode;
    
    private String correctedAddress;

    /**
     * Create a Geocoder result object to represent the result of a geocoding
     * operation.
     * 
     * @param latitude
     * @param longitude
     * @param accuracy
     *            Should be one of the constants defined in this file
     */
    public GeocoderResult(double latitude, double longitude, int accuracy, String streetNumber, String street, String city, String state, String zipCode,
            String country) {
        this(latitude, longitude, accuracy, streetNumber, street, city, state, zipCode, country, GeocoderStatus.OK);
    }

    public GeocoderResult(double latitude, double longitude, int accuracy, String streetNumber, String street, String city, String state, String zipCode,
            String country, GeocoderStatus statusCode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.streetNumber = streetNumber;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.statusCode = statusCode;
    }

    public GeocoderResult(double latitude, double longitude, int accuracy, String streetNumber, String street, String city, String state, String zipCode,
            String country, GeocoderStatus statusCode, String correctedAddress) {
    	this(latitude, longitude, accuracy, streetNumber, street, city, state, zipCode, country, statusCode);
    	this.correctedAddress = correctedAddress;
    }

    public GeocoderResult(double latitude, double longitude, int accuracy, String streetNumber, String street, String city, String state, String county, String zipCode,
                          String country, GeocoderStatus statusCode, String correctedAddress) {
        this(latitude, longitude, accuracy, streetNumber, street, city, state, zipCode, country, statusCode);
        this.correctedAddress = correctedAddress;
        this.county = county;
    }
	public String getCorrectedAddress() {
		return correctedAddress;
	}

	public void setCorrectedAddress(String correctedAddress) {
		this.correctedAddress = correctedAddress;
	}

	public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getStreetNumber() {
        return this.streetNumber;
    }

    public String getStreet() {
        return this.street;
    }

    public String getCity() {
        return this.city;
    }

    public String getState() {
        return this.state;
    }

    public String getZipCode() {
        return this.zipCode;
    }

    public String getCountry() {
        return this.country;
    }

    public GeocoderStatus getStatusCode() {
        return statusCode;
    }
    
    public void setStatusCode(GeocoderStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getCounty() { return this.county; }

    /**
     * Returns a constant indicating how accurately the location was geo-coded.
     * Bigger numbers generally means the address resolution was more accurate.
     * 
     * @return
     */
    public int getAccuracy() {
        return this.accuracy;
    }

    /**
     * In the case that the GeocoderResults seem inaccurate, we can negate this
     * number as a flag. If this number is already negative (i.e., the flag has
     * already been set) we do nothing.
     */
    public void negateAccuracy() {
        if (accuracy > 0) {
            accuracy = -accuracy;
        }
    }

    @Override
    public String toString() {
        return "GeocoderResult(status=" + statusCode + ")[latitude->" + latitude + ", longitude->" + longitude
                + ", accuracy->" + accuracy + ", city->" + city + ", state->" + state + ", zipCode->" + zipCode
                + ", country->" + country;
    }
    
}
