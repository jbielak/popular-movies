package com.jbielak.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.adapter.ReviewAdapter;
import com.jbielak.popularmovies.adapter.VideoAdapter;
import com.jbielak.popularmovies.database.MovieDao;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.model.Review;
import com.jbielak.popularmovies.model.Video;
import com.jbielak.popularmovies.network.MoviesService;
import com.jbielak.popularmovies.network.NetworkUtils;
import com.jbielak.popularmovies.utilities.AppExecutors;
import com.jbielak.popularmovies.utilities.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class DetailActivity extends DaggerAppCompatActivity {

    private static final String VIDEO_TYPE_TRAILER = "Trailer";
    private static final String SELECTED_MOVIE_KEY = "selected_movie";
    private static final String SCROLL_POSITION_KEY = "scroll_position";

    @BindView(R.id.nested_scroll_view_movie_details)
    NestedScrollView mMovieDetailsNestedScrollView;

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

    @BindView(R.id.rl_reviews_section)
    RelativeLayout mReviewsSectionRelativeLayout;

    @BindView(R.id.recycler_view_reviews)
    RecyclerView mReviewsRecyclerView;

    @BindView(R.id.button_add_to_favorites)
    ImageButton mAddToFavoritesButton;

    @BindView(R.id.button_share_trailer)
    ImageButton mShareTrailerButton;

    @Inject
    MovieDao mMovieDao;

    @Inject
    AppExecutors mAppExecutors;

    @Inject
    MoviesService mMoviesService;

    private Movie mMovie;
    private List<Video> mTrailers;
    private VideoAdapter mVideoAdapter;
    private List<Review> mReviews;
    private ReviewAdapter mReviewAdapter;
    private int[] mScrollViewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(SELECTED_MOVIE_KEY);
            mScrollViewPosition = savedInstanceState.getIntArray(SCROLL_POSITION_KEY);

        } else {
            if (getIntent().hasExtra(MovieAdapter.EXTRA_MOVIE)) {
                mMovie = getIntent().getParcelableExtra(MovieAdapter.EXTRA_MOVIE);
            }
        }

        setupFetchVideosDataListener();
        setupTrailersRecyclerView();

        setupFetchReviewsDataListener();
        setupReviewsRecyclerView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupMovieView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(SELECTED_MOVIE_KEY, mMovie);
        outState.putIntArray(SCROLL_POSITION_KEY,
                new int[] {mMovieDetailsNestedScrollView.getScrollX(),
                mMovieDetailsNestedScrollView.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setupMovieView() {
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
        mMoviesService.getMovieReviews(String.valueOf(mMovie.getId()));
        setupFavoritesButton();
        setupShareButton();
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

    private void setupReviewsRecyclerView() {
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewsRecyclerView.setLayoutManager(linearLayoutManager);

        mReviewsRecyclerView.addItemDecoration(new DividerItemDecoration(
                mReviewsRecyclerView.getContext(),LinearLayoutManager.VERTICAL));

        mReviewAdapter = new ReviewAdapter(this);
        mReviewsRecyclerView.setAdapter(mReviewAdapter);
    }

    private void setupFetchVideosDataListener() {
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
                        scrollToSavedPosition();
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

    private void setupFetchReviewsDataListener() {
        mMoviesService.setFetchReviewsDataListener(new FetchDataListener<List<Review>>() {
            @Override
            public void onPreExecute() {
                setReviewsViewVisibility(View.GONE);
            }

            @Override
            public void onResponse(List<Review> reviews) {
                if (reviews != null ) {
                    mReviews = reviews;
                    if(mReviews.isEmpty()) {
                        setReviewsViewVisibility(View.GONE);
                    } else {
                        mReviewAdapter.setReviews(mReviews);
                        setReviewsViewVisibility(View.VISIBLE);
                        scrollToSavedPosition();
                    }
                }
            }

            @Override
            public void onError() {
                Toast.makeText(DetailActivity.this, R.string.reviews_fetching_error,
                        Toast.LENGTH_SHORT).show();
                setReviewsViewVisibility(View.GONE);
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

    private void setReviewsViewVisibility(int visibility) {
        if (mReviewsSectionRelativeLayout != null) {
            mReviewsSectionRelativeLayout.setVisibility(visibility);
        }
    }

    private void setupFavoritesButton() {
        LiveData<Movie> movie = mMovieDao.getMovie(mMovie.getId());
        movie.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie changedMovie) {
                if (changedMovie != null) {
                    setRemoveFromFavoritesButton();
                } else {
                    setAddToFavoritesButton();
                }
            }
        });
    }

    private void setAddToFavoritesButton() {
        mAddToFavoritesButton.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_star_white_48dp));
        mAddToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mMovieDao.insertMovie(mMovie);
                    }
                });
            }
        });
    }

    private void setRemoveFromFavoritesButton() {
        mAddToFavoritesButton.setImageDrawable(
                getResources().getDrawable(R.drawable.ic_delete_white_48dp));
        mAddToFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAppExecutors.diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        mMovieDao.deleteMovie(mMovie);
                    }
                });
            }
        });
    }

    private void setupShareButton() {
        mShareTrailerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Video trailerToShare = mTrailers.get(0);
                if (trailerToShare != null) {
                    Uri trailerUri = NetworkUtils.buildVideoUri(trailerToShare.getKey());
                    shareTrailer(trailerUri.toString());
                }
            }
        });
    }

    private void shareTrailer(String trailerUrl) {
        String mimeType = "text/plain";
        String title = "Share trailer";

        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(trailerUrl)
                .startChooser();
    }

    private void scrollToSavedPosition() {
        if (mScrollViewPosition != null) {
            mMovieDetailsNestedScrollView.post(new Runnable() {
                public void run() {
                    mMovieDetailsNestedScrollView.scrollTo(mScrollViewPosition[0], mScrollViewPosition[1]);
                }
            });
        }
    }
}
