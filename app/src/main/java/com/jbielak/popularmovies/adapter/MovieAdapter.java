package com.jbielak.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jbielak.popularmovies.DetailActivity;
import com.jbielak.popularmovies.R;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.utilities.PathProvider;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Justyna on 2018-02-28.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public static final String EXTRA_MOVIE = "EXTRA_MOVIE";

    private List<Movie> movies;
    private Context context;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
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
                .load(PathProvider.getPosterUrl(movie.getPosterPath()).toString())
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
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.image_view_poster);
        }

    }
}
