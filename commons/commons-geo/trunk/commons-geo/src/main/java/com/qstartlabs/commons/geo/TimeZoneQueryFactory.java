package com.qstartlabs.commons.geo;

public class TimeZoneQueryFactory {
    public static TimeZoneQuery getInstance() {
        return new GoogleTimeZoneQuery();
    }
}
