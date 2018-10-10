package com.jbielak.popularmovies.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Justyna on 2018-02-27.
 */

@Entity(tableName = "movies")
public class Movie implements Parcelable {

    @PrimaryKey
    private long id;

    private String title;

    @SerializedName("release_date")
    private Date releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("vote_average")
    private Double voteAverage;

    private Double popularity;

    private String overview;

    @Ignore
    private List<Video> videos;

    @Ignore
    private List<Review> reviews;

    public Movie() {
    }

    public Movie(long id, String title, Date releaseDate, String posterPath, Double voteAverage,
                 Double popularity, String overview, List<Video> videos, List<Review> reviews) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.overview = overview;
        this.videos = videos;
        this.reviews = reviews;
    }

    public Movie(long id, String title, Date releaseDate, String posterPath, Double voteAverage,
                 Double popularity, String overview) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.popularity = popularity;
        this.overview = overview;
        this.videos = new ArrayList<>();
        this.reviews = new ArrayList<>();
    }


    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        releaseDate = new Date(in.readLong());
        posterPath = in.readString();
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        overview = in.readString();
        videos = new ArrayList<>();
        in.readTypedList(videos, Video.CREATOR);
        reviews = new ArrayList<>();
        in.readTypedList(reviews, Review.CREATOR);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.title);
        parcel.writeLong((this.releaseDate != null) ? this.releaseDate.getTime()
        : 0);
        parcel.writeString(this.posterPath);
        parcel.writeDouble(this.voteAverage);
        parcel.writeDouble(this.popularity);
        parcel.writeString(this.overview);
        parcel.writeTypedList(this.videos);
        parcel.writeTypedList(this.reviews);
    }
}