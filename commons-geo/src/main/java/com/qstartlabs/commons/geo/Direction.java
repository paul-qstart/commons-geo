package com.qstartlabs.commons.geo;

public enum Direction {
    N("North", 0),
    NE("North East", 45),
    E("East", 90),
    SE("South East", 135),
    S("South", 180),
    SW("South West", 225),
    W("West", 270),
    NW("North West", 315);

    private final String description;
    private final double heading;

    Direction(String description, double heading) {
        this.description = description;
        this.heading = heading;
    }

    public String getDescription() {
        return description;
    }

    public double getHeading() {
        return this.heading;
    }

    public static Direction fromName(String name) {
        if (name != null) {
            for (Direction d : Direction.values()) {
                if (d.name().equals(name)) {
                    return d;
                }
            }
        }

        return null;
    }

}