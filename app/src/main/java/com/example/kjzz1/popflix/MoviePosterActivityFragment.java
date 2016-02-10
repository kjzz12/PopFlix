package com.example.kjzz1.popflix;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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

    private MenuItem favorites;
    private MenuItem mostPopular;
    private MenuItem highestRated;



    private View view;

    private GridView gridView;
    private MoviePosterAdapter moviePosterAdapter;
    private ArrayList<MovieData> movieData;
    private ArrayList<MovieData> savedMovieData;
    private Integer posterHeight;

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

        //identify menu options
        favorites = menu.getItem(0);
        mostPopular = menu.getItem(1);
        highestRated = menu.getItem(2);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);


        String key = getString(R.string.key);
        String PopUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" + key;

        view = inflater.inflate(R.layout.fragment_movie_poster_activity, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);

        if (savedInstanceState == null) {

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
        } else {

            //if savedstate isn't null, we can reload images without connectivity
            //dynamically size posters
            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels;
            if (getActivity().findViewById(R.id.movie_detail_container) != null) {
                posterHeight = (int) (1.48 * (.25 * dpWidth));
            } else {
                posterHeight = (int) (1.48 * (.5 * dpWidth));
            }

            for (int i = 0; i<savedMovieData.size();i++) {
                savedMovieData.get(i).setPosterHeight(posterHeight);
            }
            moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, savedMovieData);
            gridView.setAdapter(moviePosterAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                    Log.v("Position ID", Integer.toString(position));

                    MovieData movieItem = (MovieData) parent.getItemAtPosition(position);

                    mListener.OnMovieSelected(movieItem);
                }
            });
        }

        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", savedMovieData);
        super.onSaveInstanceState(outState);
    }
    //handling switching between the two sort orders
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String key = getString(R.string.key);
        switch (item.getItemId()) {
            case R.id.popularity:
                movieData = new ArrayList<>();
                moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, movieData);
                gridView.setAdapter(moviePosterAdapter);

                String PopUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" + key;

                new ProcessJSON().execute(PopUrl);

                //highlight proper icon
                mostPopular.setIcon(R.drawable.fullflame);
                highestRated.setIcon(R.drawable.emptystar);
                favorites.setIcon(R.drawable.emptyheart);

                Toast.makeText(getActivity(), "Most Popular Movies", Toast.LENGTH_SHORT).show();

                break;
            case R.id.rating:
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

                mostPopular.setIcon(R.drawable.emptyflame);
                highestRated.setIcon(R.drawable.fullstar);
                favorites.setIcon(R.drawable.emptyheart);

                Toast.makeText(getActivity(), "Highest Rated Movies", Toast.LENGTH_SHORT).show();

                break;

            //handle favorites
            case R.id.favorites:
                movieData = new ArrayList<>();
                moviePosterAdapter = new MoviePosterAdapter(getActivity(), R.layout.movie_item_layout, movieData);
                gridView.setAdapter(moviePosterAdapter);

                ArrayList ids = new ArrayList();

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                Map<String, ?> allEntries = sharedPreferences.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    ids.add(entry.getValue().toString());
                }
                for (int i = 0; i < ids.size(); i++){

                    String movieURL = "https://api.themoviedb.org/3/movie/" + ids.get(i) + "?" + key;

                    new ProcessJSON().execute(movieURL);
                }

                mostPopular.setIcon(R.drawable.emptyflame);
                highestRated.setIcon(R.drawable.emptystar);
                favorites.setIcon(R.drawable.fullheart);

                Toast.makeText(getActivity(), "Your Favorite Movies", Toast.LENGTH_SHORT).show();

                break;
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

                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                    float dpWidth = displayMetrics.widthPixels;
                    if (getActivity().findViewById(R.id.movie_detail_container) != null) {
                        posterHeight = (int) (1.48 * (.25 * dpWidth));
                    } else {
                        posterHeight = (int) (1.48 * (.5 * dpWidth));
                    }

                    if (results != null) {
                        for (int i = 0; i < results.length(); i++) {

                            //get results
                            JSONObject jsonObject = results.getJSONObject(i);
                            item = new MovieData();

                            //extract strings
                            String last = "http://image.tmdb.org/t/p/w342" + jsonObject.getString("poster_path");
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
                            item.setPosterHeight(posterHeight);
                            movieData.add(item);
                            savedMovieData = movieData;
                        }
                    } else {

                        //this handles the query for favorites. The JSON is formatted somewhat differently, requiring a different query
                        item = new MovieData();

                        String last = "http://image.tmdb.org/t/p/w342" + reader.getString("poster_path");
                        String movieTitle = reader.getString("original_title");
                        String releaseDate = reader.getString("release_date");
                        String plotSummary = reader.getString("overview");
                        String userRating = reader.getString("vote_average");
                        String id = reader.getString("id");

                        //add those to MovieData
                        item.setImage(last);
                        item.setMovieTitle(movieTitle);
                        item.setReleaseDate(releaseDate);
                        item.setPlotSummary(plotSummary);
                        item.setUserRating(userRating);
                        item.setId(id);
                        item.setPosterHeight(posterHeight);
                        movieData.add(item);
                        savedMovieData = movieData;
                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

}
