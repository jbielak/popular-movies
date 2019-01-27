package com.jbielak.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jbielak.popularmovies.DetailActivity;
import com.jbielak.popularmovies.R;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    private List<Movie> movies = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        movies = new ArrayList<>();
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUrl(movie.getPosterPath()).toString())
                .placeholder(R.drawable.ic_crop_original_black_48dp)
                .error(R.drawable.ic_crop_original_black_48dp)
                .into(holder.posterImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, DetailActivity.class);
                detailIntent.putExtra(EXTRA_MOVIE, movie);
                context.startActivity(detailIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == movies) return 0;
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.image_view_poster);
        }
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<Movie> newMovies) {
        if (movies == null || movies.isEmpty()) {
            movies = newMovies;
        } else {
            movies.addAll(newMovies);
        }
    }

    public void clearMovies() {
        movies.clear();
    }

    public ArrayList<Movie> getMoviesArrayList() {
        return (ArrayList<Movie>) movies;
    }
}
