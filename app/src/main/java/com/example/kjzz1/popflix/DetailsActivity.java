package com.example.kjzz1.popflix;


import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton playButton;
    private TextView movieReviewTruncated;
    private TextView movieReview;
    private ImageView backdropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_view);

        //The easiest way to get a transparent back button was with a Toolbar

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        //get all the info from the intent
        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("releaseDate");
        String overview = getIntent().getStringExtra("plotSummary");
        String rating = getIntent().getStringExtra("averageRating");
        String id = getIntent().getStringExtra("movieID");
        String key = getString(R.string.key);

        String backdropURL = "https://api.themoviedb.org/3/movie/" + id + "/images?"+key;
        String backdrop = null;

        String reviewURL = "https://api.themoviedb.org/3/movie/" + id + "/reviews?"+ key;
        String review = null;

        try {
            //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
            backdrop = new DetailProcessJSON().execute(backdropURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            review = new ReviewJSON().execute(reviewURL).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        String [] arr = review.split("\\s+");
        String truncatedReview = "";

        if (arr.length < 50) {
            truncatedReview = review;
        }else for (int i=0; i<50; i++){
            truncatedReview = truncatedReview + " " + arr[i];
        }


        //don't want title in DetailsActivity
        setTitle(null);

        ImageView posterView = (ImageView) findViewById(R.id.grid_item_image);

        backdropView = (ImageView) findViewById(R.id.backdrop_view);
        backdropView.setOnClickListener(this);

        TextView titleView = (TextView) findViewById(R.id.title_view);
        titleView.setText(title);

        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        releaseDate.setText(getString(R.string.released) + date);

        TextView plotSummary = (TextView) findViewById(R.id.plotSummary);
        plotSummary.setText(overview);

        TextView averageRating = (TextView) findViewById(R.id.averageRating);
        averageRating.setText(getString(R.string.userRating) + rating + getString(R.string.outOfTen));

        movieReviewTruncated = (TextView) findViewById(R.id.movieReviewTruncated);
        if (arr.length < 50) {
            movieReviewTruncated.setText(truncatedReview);
        }else for (int i=0; i<50; i++){
            movieReviewTruncated.setText(truncatedReview + "...");
        }

        movieReviewTruncated.setTypeface(null, Typeface.ITALIC);
        movieReviewTruncated.setOnClickListener(this);

        movieReview = (TextView) findViewById(R.id.movieReview);
        movieReview.setText("Read Movie Review");
        movieReview.setOnClickListener(this);

        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        if (truncatedReview == "No reviews found."){
            movieReview.setVisibility(View.GONE);
        }

        Picasso
                .with(this)
                .load(image)
                .resize(300, 900)
                .centerInside()
                .into(posterView);


        Picasso
                .with(this)
                .load(backdrop)
                .fit()
                .centerInside()
                .into(backdropView);

        Picasso
                .with(this)
                .load(R.mipmap.ic_play_circle_outline_white_48dp)
                .fit()
                .centerInside()
                .into(playButton);


        }
    @Override
    public void onClick(View v) {
        if (v == movieReview) {
            String key = getString(R.string.key);
            String id = getIntent().getStringExtra("movieID");
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/reviews?"+ key;
            String review = null;
            try {
                //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
                review = new ReviewJSON().execute(databaseURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
            intent.putExtra("review", review);

            startActivity(intent);
        } else if (v == movieReviewTruncated) {
            String key = getString(R.string.key);
            String id = getIntent().getStringExtra("movieID");
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/reviews?"+ key;
            String review = null;
            try {
                //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
                review = new ReviewJSON().execute(databaseURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(DetailsActivity.this, ReviewActivity.class);
            intent.putExtra("review", review);

            startActivity(intent);
        } else if (v == playButton) {
            String key = getString(R.string.key);
            String id = getIntent().getStringExtra("movieID");
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/videos?"+ key;
            String videoURL = null;
            try {
                //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
                videoURL = new YouTubeJSON().execute(databaseURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
            startActivity(intent);
        } else if (v== backdropView){
            String key = getString(R.string.key);
            String id = getIntent().getStringExtra("movieID");
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/videos?"+ key;
            String videoURL = null;
            try {
                //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
                videoURL = new YouTubeJSON().execute(databaseURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
            startActivity(intent);
        }
    }
}