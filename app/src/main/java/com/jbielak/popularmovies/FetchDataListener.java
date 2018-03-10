package com.jbielak.popularmovies;

public interface FetchDataListener<T> {
    void onPreExecute();
    void onResponse(T data);
    void onError();
}
