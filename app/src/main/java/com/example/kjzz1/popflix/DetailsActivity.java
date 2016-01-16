package com.example.kjzz1.popflix;


import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_view);

        //The easiest way to get a transparent back button was with a Toolbar

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        String key = getString(R.string.key);

        //get all the info from the intent
        String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("releaseDate");
        String overview = getIntent().getStringExtra("plotSummary");
        String rating = getIntent().getStringExtra("averageRating");
        String id = getIntent().getStringExtra("movieID");
        String url = "https://api.themoviedb.org/3/movie/" + id + "/images?"+key;
        String backdrop = null;
        try {
            //retrieve image from AsyncTask. I KNOW this is not the best way to do this, please help!
            backdrop = new DetailProcessJSON().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //don't want title in DetailsActivity
        setTitle(null);

        ImageView posterView = (ImageView) findViewById(R.id.grid_item_image);
        ImageView backdropView = (ImageView) findViewById(R.id.backdrop_view);

        TextView titleView = (TextView) findViewById(R.id.title_view);
        titleView.setText(title);

        TextView releaseDate = (TextView) findViewById(R.id.releaseDate);
        releaseDate.setText(getString(R.string.released) + date);

        TextView plotSummary = (TextView) findViewById(R.id.plotSummary);
        plotSummary.setText(overview);

        TextView averageRating = (TextView) findViewById(R.id.averageRating);
        averageRating.setText(getString(R.string.userRating) + rating + getString(R.string.outOfTen));

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

    }
}