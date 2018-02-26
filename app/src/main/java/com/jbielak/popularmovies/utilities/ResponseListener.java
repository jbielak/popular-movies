package com.jbielak.popularmovies.utilities;

/**
 * Created by Justyna on 2018-02-26.
 */

public interface ResponseListener<T> {
    void onResponse(T response);
    void onError(String message);
}
