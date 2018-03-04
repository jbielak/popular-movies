package com.jbielak.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.jbielak.popularmovies.adapter.MovieAdapter;
import com.jbielak.popularmovies.model.Movie;
import com.jbielak.popularmovies.utilities.DateUtils;
import com.jbielak.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

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

    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().hasExtra(MovieAdapter.EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(MovieAdapter.EXTRA_MOVIE);

            mTitleTextView.setText(mMovie.getTitle());
            Picasso.with(this)
                    .load(NetworkUtils.buildPosterUrl(mMovie.getPosterPath()).toString())
                    .into(mPosterImageView);
            mReleaseDateTextView.setText(
                    DateUtils.getFriendlyDateString(mMovie.getReleaseDate()));
            mVoteAverageTextView.setText(getString(R.string.vote_average, mMovie.getVoteAverage())
                    .replace(',', '.'));
            mOverviewTextView.setText(mMovie.getOverview());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mian, menu);
        return true;
    }
}
