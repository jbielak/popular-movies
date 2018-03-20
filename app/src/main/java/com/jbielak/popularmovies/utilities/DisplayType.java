package com.jbielak.popularmovies.utilities;

/**
 * Created by Justyna on 2018-02-26.
 */

public enum DisplayType {
    POPULAR ("popular"),
    RATING ("top_rated"),
    FAVORITES ("favorites");

    private final String value;

    DisplayType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DisplayType getDisplayTypeByValue(String value) {
        for (int i = 0; i < DisplayType.values().length; i++) {
            if (value.equals(DisplayType.values()[i].value))
                return DisplayType.values()[i];
        }
        return null;
    }
}
