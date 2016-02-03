package com.example.kjzz1.popflix;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePosterActivityFragment extends Fragment {

    private View view;

    private GridView gridView;
    private MoviePosterAdapter moviePosterAdapter;
    private ArrayList<MovieData> movieData;

    private DateTime old = new DateTime().minusMonths (6);
    private String OldDate;
    private DateTime now = new DateTime();
    private String NowDate;

    private String RatingUrl;

    OnMovieSelectedListener mListener;

    public MoviePosterActivityFragment() {
    }

    public interface OnMovieSelectedListener {
        public void OnMovieSelected(MovieData item);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMovieSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort, menu);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String key = getString(R.string.key);
        String PopUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" + key;

        view = inflater.inflate(R.layout.fragment_movie_poster_activity, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);

        //Create empty arraylist
        movieData = new ArrayList<>();
        moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, movieData);
        gridView.setAdapter(moviePosterAdapter);

        //React to Movie Poster clicks

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Log.v("Position ID", Integer.toString(position));

                MovieData movieData = (MovieData) parent.getItemAtPosition(position);

                mListener.OnMovieSelected(movieData);
            }
        });

        new ProcessJSON().execute(PopUrl);


        return view;
    }
    //handling switching between the two sort orders
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String key = getString(R.string.key);
        switch (item.getItemId()) {
            case R.id.popularity:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                movieData = new ArrayList<>();
                moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, movieData);
                gridView.setAdapter(moviePosterAdapter);

                String PopUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" + key;

                new ProcessJSON().execute(PopUrl);
                break;
            case R.id.rating:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                movieData = new ArrayList<>();
                moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, movieData);
                gridView.setAdapter(moviePosterAdapter);

                //Used joda date time to sort by only recent films.

                OldDate = Integer.toString(old.getYear())
                        +"-"
                        +Integer.toString(old.getMonthOfYear())
                        +"-"
                        +Integer.toString(old.getDayOfMonth());

                Log.v("OldDate", OldDate);

                NowDate = Integer.toString(now.getYear())
                        +"-"
                        +Integer.toString(now.getMonthOfYear())
                        +"-"
                        +Integer.toString(now.getDayOfMonth());

                Log.v("NowDate", NowDate);
                RatingUrl = "http://api.themoviedb.org/3/discover/movie?primary_release_date.gte="+ OldDate +"&primary_release_date.lte="+ NowDate +"&vote_count.gte=100&sort_by=vote_average.desc&"+key;
                Log.v("URL", RatingUrl);
                new ProcessJSON().execute(RatingUrl);
                break;
            case R.id.favorites:
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Map<String, ?> allEntries = sharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    Log.d("map values", entry.getKey()+ ": " + entry.getValue().toString());
                }
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    //Querying for movie data
    private class ProcessJSON extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... strings){
            String stream;
            String urlString = strings[0];

            //calling external class for querying database
            MovieDBConnect hh = new MovieDBConnect();
            stream = hh.GetHTTPData(urlString);

            //return data from MovieDB
            return stream;
        }
        @Override
        protected void onPostExecute(String stream) {
            if (stream != null) {
                //Send to JSON parser
                ExtractMovieInfo(stream);
                //Send that data to Adapter
                moviePosterAdapter.setGridData(movieData);}
        }

        protected void ExtractMovieInfo(String stream){
            if (stream != null) {
                try {
                    JSONObject reader = new JSONObject(stream);
                    MovieData item;
                    JSONArray results = reader.optJSONArray("results");

                    for(int i=0; i<results.length(); i++){

                        //get results
                        JSONObject jsonObject = results.getJSONObject(i);
                        item = new MovieData();

                        //extract strings
                        String last = "http://image.tmdb.org/t/p/w342"+jsonObject.getString("poster_path");
                        String movieTitle = jsonObject.getString("original_title");
                        String releaseDate = jsonObject.getString("release_date");
                        String plotSummary = jsonObject.getString("overview");
                        String userRating = jsonObject.getString("vote_average");
                        String id = jsonObject.getString("id");

                        //add those to MovieData
                        item.setImage(last);
                        item.setMovieTitle(movieTitle);
                        item.setReleaseDate(releaseDate);
                        item.setPlotSummary(plotSummary);
                        item.setUserRating(userRating);
                        item.setId(id);
                        movieData.add(item);
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
