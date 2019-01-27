package com.jbielak.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jbielak.popularmovies.R;
import com.jbielak.popularmovies.data.model.Video;
import com.jbielak.popularmovies.data.network.NetworkUtils;

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

        final Video trailer = trailers.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri trailerUri = NetworkUtils.buildVideoUri(trailer.getKey());
                if (trailerUri != null) {
                    playTrailer(trailerUri);
                }
                else {
                    Toast.makeText(context, R.string.trailer_play_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    private void playTrailer(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
