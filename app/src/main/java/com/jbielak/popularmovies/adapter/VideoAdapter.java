package com.jbielak.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jbielak.popularmovies.R;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.Video;

import java.util.List;

/**
 * Created by Justyna on 2018-03-11.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<Video> trailers;
    private Context context;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.trailerNameTextView.setText(trailers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (null == trailers) return 0;
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView trailerNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            trailerNameTextView = itemView.findViewById(R.id.tv_trailer_name);
        }
    }

    public void setTrailers(List<Video> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }
}
