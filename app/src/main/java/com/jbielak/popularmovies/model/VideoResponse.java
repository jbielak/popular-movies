package com.jbielak.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Justyna on 2018-03-10.
 */

public class VideoResponse {

    @SerializedName("id")
    private long id;

    @SerializedName("results")
    private List<Video> results;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
