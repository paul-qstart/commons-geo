package com.qstartlabs.commons.geo;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

class RequestSigner {

    // This is QStart's private, paid for key. DO not distribute. Do not use without talking to Jeff R. Lamb
    private final static String KEY="hNLLlpxjVJfhSsAsrbWsbpBLl50=";

    protected static synchronized String signRequest(String urlString) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, URISyntaxException, MalformedURLException {
        URL url = new URL(urlString);
        //Convert from web safe base 64 to binary:
        String keyString = KEY.replace('-', '+');
        keyString = keyString.replace('_', '/');
        Base64 base64 = new Base64(true); // websafe base64 encoder/decoder
        byte[] key = base64.decode(keyString.getBytes());
        String path = url.getPath();
        String query = url.getQuery();
        String resource = path+'?'+query;
        SecretKeySpec sha1Key = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(sha1Key);
        byte[] sigBytes = mac.doFinal(resource.getBytes());
        String signature = new String(base64.encode(sigBytes));
        return urlString+"&signature="+signature;
    }

}
