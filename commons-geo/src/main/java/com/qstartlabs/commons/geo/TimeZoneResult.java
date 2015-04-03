package com.qstartlabs.commons.geo;

public class TimeZoneResult {
    private Double dstOffset;
    private Double rawOffset;
    private TimeZoneStatus status;
    private String timeZoneId;
    private String timeZoneName;

    public TimeZoneResult() {
        // Exposing default constructor
    }

    public TimeZoneResult(Double dstOffset, Double rawOffset, String timeZoneId, String timeZoneName, TimeZoneStatus status) {
        this.dstOffset = dstOffset;
        this.rawOffset = rawOffset;
        this.timeZoneId = timeZoneId;
        this.timeZoneName = timeZoneName;
        this.status = status;
    }

    public Double getDstOffset() {
        return dstOffset;
    }

    public void setDstOffset(Double dstOffset) {
        this.dstOffset = dstOffset;
    }

    public Double getRawOffset() {
        return rawOffset;
    }

    public void setRawOffset(Double rawOffset) {
        this.rawOffset = rawOffset;
    }

    public TimeZoneStatus getStatus() {
        return status;
    }

    public void setStatus(TimeZoneStatus status) {
        this.status = status;
    }

    public String getTimeZoneId() {
        return timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    @Override
    public String toString() {
        return "TimeZoneResult{" +
                "dstOffset=" + dstOffset +
                ", rawOffset=" + rawOffset +
                ", status=" + status +
                ", timeZoneId='" + timeZoneId + '\'' +
                ", timeZoneName='" + timeZoneName + '\'' +
                '}';
    }

}
