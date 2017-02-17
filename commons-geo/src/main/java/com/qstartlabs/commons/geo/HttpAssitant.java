package com.qstartlabs.commons.geo;

import com.qstartlabs.commons.geo.RequestSigner;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.net.ssl.HttpsURLConnection;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class HttpAssitant {
    private final static String DEFAULT_ENCODING = "ISO-8859-1";
    private static final Logger logger = Logger.getLogger(HttpAssitant.class);

    public static JSONObject makeRequestInternal(String unsignedUrl) {
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
}
