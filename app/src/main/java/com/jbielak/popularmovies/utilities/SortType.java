package com.jbielak.popularmovies.utilities;

/**
 * Created by Justyna on 2018-02-26.
 */

public enum SortType {
    POPULAR ("popular"),
    RATING ("top_rated");

    private final String value;

    SortType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
