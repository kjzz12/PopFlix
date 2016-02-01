package com.example.kjzz1.popflix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutionException;


public class DetailFragment extends Fragment implements View.OnClickListener {

    private ImageButton playButton;
    private TextView movieReviewTruncated;
    private TextView movieReview;
    private ImageView backdropView;

    static final String DETAIL_URI = "URI";

    private Uri mUri;

    private MovieData movieData;

    private String image;
    private String title;
    private String date;
    private String overview;
    private String rating;
    private String id;
    private String key;


    public DetailFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments()!= null) {
            movieData = getArguments().getParcelable("movie");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if (movieData == null) {
            movieData = getActivity().getIntent().getParcelableExtra("movie");

            if (movieData != null) {

                image = movieData.getImage();
                title = movieData.getTitle();
                date = movieData.getRelaseDate();
                overview = movieData.getPlotSummary();
                rating = movieData.getUserRating();
                id = movieData.getId();
                key = getString(R.string.key);
            }

            else  {
                ((TextView) view.findViewById(R.id.title_view))
                        .setText("Select a movie");
                return view;
            }
        } else {
//            image = movieData.getImage();
            title = movieData.getTitle();
            date = movieData.getRelaseDate();
            overview = movieData.getPlotSummary();
            rating = movieData.getUserRating();
            id = movieData.getId();
            key = getString(R.string.key);
        }

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


        ImageView posterView = (ImageView) view.findViewById(R.id.grid_item_image);

        backdropView = (ImageView) view.findViewById(R.id.backdrop_view);
        backdropView.setOnClickListener(this);

        TextView titleView = (TextView) view.findViewById(R.id.title_view);
        titleView.setText(title);

        TextView releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        releaseDate.setText(getString(R.string.released) + date);

        TextView plotSummary = (TextView) view.findViewById(R.id.plotSummary);
        plotSummary.setText(overview);

        TextView averageRating = (TextView) view.findViewById(R.id.averageRating);
        averageRating.setText(getString(R.string.userRating) + rating + getString(R.string.outOfTen));

        movieReviewTruncated = (TextView) view.findViewById(R.id.movieReviewTruncated);
        if (arr.length < 50) {
            movieReviewTruncated.setText(truncatedReview);
        }else for (int i=0; i<50; i++){
            movieReviewTruncated.setText(truncatedReview + "...");
        }

        movieReviewTruncated.setTypeface(null, Typeface.ITALIC);
        movieReviewTruncated.setOnClickListener(this);

        movieReview = (TextView) view.findViewById(R.id.movieReview);
        movieReview.setText("Read Movie Review");
        movieReview.setOnClickListener(this);

        playButton = (ImageButton) view.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);

        if (truncatedReview == "No reviews found."){
            movieReview.setVisibility(View.GONE);
        }

        Picasso
                .with(getActivity())
                .load(image)
                .resize(300, 900)
                .centerInside()
                .into(posterView);


        Picasso
                .with(getActivity())
                .load(backdrop)
                .fit()
                .centerInside()
                .into(backdropView);

        Picasso
                .with(getActivity())
                .load(R.mipmap.ic_play_circle_outline_white_48dp)
                .fit()
                .centerInside()
                .into(playButton);

        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onClick(View v) {
        if (v == movieReview) {
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

            Intent intent = new Intent(getActivity(), ReviewActivity.class);
            intent.putExtra("review", review);

            startActivity(intent);
        } else if (v == movieReviewTruncated) {
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

            Intent intent = new Intent(getActivity(), ReviewActivity.class);
            intent.putExtra("review", review);

            startActivity(intent);
        } else if (v == playButton) {
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/videos?"+ key;
            String videoURL = null;
            try {
                videoURL = new YouTubeJSON().execute(databaseURL).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURL));
            startActivity(intent);
        } else if (v== backdropView){
            String databaseURL = "https://api.themoviedb.org/3/movie/" + id + "/videos?"+ key;
            String videoURL = null;
            try {
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
