package com.jbielak.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.adapter.VideoAdapter;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.Video;
import com.jbielak.popularmovies.network.MoviesService;
import com.jbielak.popularmovies.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String VIDEO_TYPE_TRAILER = "Trailer";

    @BindView(R.id.text_view_title)
    TextView mTitleTextView;

    @BindView(R.id.image_view_poster)
    ImageView mPosterImageView;

    @BindView(R.id.text_view_release_date)
    TextView mReleaseDateTextView;

    @BindView(R.id.text_view_vote_average)
    TextView mVoteAverageTextView;

    @BindView(R.id.text_view_overview)
    TextView mOverviewTextView;

    @BindView(R.id.rl_trailers_section)
    RelativeLayout mTrailersSectionRelativeLayout;

    @BindView(R.id.recycler_view_trailers)
    RecyclerView mTrailersRecyclerView;

    private MoviesService mMoviesService;
    private Movie mMovie;
    private List<Video> mTrailers;
    private VideoAdapter mVideoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        mMoviesService = new MoviesService();
        setupFetchVideosDataCallback();

        setupTrailersRecyclerView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra(MovieAdapter.EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(MovieAdapter.EXTRA_MOVIE);

            mTitleTextView.setText(mMovie.getTitle());
            Picasso.with(this)
                    .load(NetworkUtils.buildPosterUrl(mMovie.getPosterPath()).toString())
                    .into(mPosterImageView);
            String date = DateUtils.getYearString(mMovie.getReleaseDate());
            mReleaseDateTextView.setText(date == null
                    ? getString(R.string.unknown_release_date) : date);
            mVoteAverageTextView.setText(getString(R.string.vote_average, mMovie.getVoteAverage())
                    .replace(',', '.'));
            mOverviewTextView.setText(mMovie.getOverview());
            mMoviesService.getMovieVideos(String.valueOf(mMovie.getId()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mian, menu);
        return true;
    }

    private void setupTrailersRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTrailersRecyclerView.setLayoutManager(linearLayoutManager);

        mTrailersRecyclerView.addItemDecoration(new DividerItemDecoration(
                mTrailersRecyclerView.getContext(),LinearLayoutManager.VERTICAL));

        mVideoAdapter = new VideoAdapter(this);
        mTrailersRecyclerView.setAdapter(mVideoAdapter);
    }

    private void setupFetchVideosDataCallback() {
        mMoviesService.setFetchVideosDataListener(new FetchDataListener<List<Video>>() {
            @Override
            public void onPreExecute() {
                setTrailersViewVisibility(View.GONE);
            }

            @Override
            public void onResponse(List<Video> videos) {
                if (videos != null ) {
                    mTrailers = getVideosByType(videos, VIDEO_TYPE_TRAILER);
                    if(mTrailers == null || mTrailers.isEmpty()) {
                        setTrailersViewVisibility(View.GONE);
                    } else {
                        mVideoAdapter.setTrailers(mTrailers);
                        setTrailersViewVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(DetailActivity.this, R.string.trailers_fetching_error,
                        Toast.LENGTH_SHORT).show();
                setTrailersViewVisibility(View.GONE);
            }
        });
    }

    private List<Video> getVideosByType(List<Video> videos, String type) {
        List<Video> filteredVideos = new ArrayList<>();
        if (videos != null && !videos.isEmpty() && type != null) {
            for (Video video : videos) {
                if (video.getType().equals(type)) {
                    filteredVideos.add(video);
                }
            }
        }
        return filteredVideos;
    }

    private void setTrailersViewVisibility(int visibility) {
        if (mTrailersSectionRelativeLayout != null) {
            mTrailersSectionRelativeLayout.setVisibility(visibility);
        }
    }
}
