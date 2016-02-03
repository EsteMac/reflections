package com.example.emccalley.flix;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A fragment containing main movie grid view
 */
public class MainActivityFragment extends Fragment {

    private ImageAdapter mMovieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new ImageAdapter(getContext());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieAdapter);

        // Intent to see movie details
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Intent to see movie details
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("EXTRA_THUMB", R.drawable.fightclub1);
                extras.putString("EXTRA_TITLE", mTitles[position]);
                extras.putFloat("EXTRA_SCORE", 90.5f);
                extras.putString("EXTRA_DATE", "2015");
                extras.putString("EXTRA_OVERVIEW", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(400, 600));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.fightclub1, R.drawable.fightclub2,
                R.drawable.fightclub3, R.drawable.fightclub4,
                R.drawable.fightclub5, R.drawable.fightclub6,
                R.drawable.fightclub1, R.drawable.fightclub2,
                R.drawable.fightclub3, R.drawable.fightclub4,
                R.drawable.fightclub5, R.drawable.fightclub6,
                R.drawable.fightclub1, R.drawable.fightclub2,
                R.drawable.fightclub3, R.drawable.fightclub4,
                R.drawable.fightclub5, R.drawable.fightclub6,
                R.drawable.fightclub1, R.drawable.fightclub2,
                R.drawable.fightclub3, R.drawable.fightclub4,
                R.drawable.fightclub5, R.drawable.fightclub6
        };
    }

    // references to movie titles
    private String[] mTitles = {
            "Fight Club 1", "Fight Club 2",
            "Fight Club 3", "Fight Club 4",
            "Fight Club 5", "Fight Club 6",
            "Fight Club 7", "Fight Club 8",
            "Fight Club 9", "Fight Club 10",
            "Fight Club 11", "Fight Club 12",
            "Fight Club 13", "Fight Club 14",
            "Fight Club 15", "Fight Club 16",
            "Fight Club 17", "Fight Club 18",
            "Fight Club 19", "Fight Club 20",
            "Fight Club 21", "Fight Club 22",
            "Fight Club 23", "Fight Club 24"
    };

    public class FetchMoviesTask extends AsyncTask<String, Void, String[][]> {

        private final String LOG_TAG = "Estevan";

        /**
         * Take the String representing movie data in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         */
        protected String[][] getMovieDataFromJson (String moviesJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted
            final String TMDB_LIST = "results";
            final String TMDB_TITLE = "original_title";
            final String TMDB_POSTER = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_RATING = "vote_average";
            final String TMDB_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(TMDB_LIST);

            String[][] resultStrs = new String[moviesArray.length()][5];

            for (int i = 0; i < moviesArray.length(); i++) {

                String movieTitle;
                String moviePoster;
                String movieOverview;
                String movieRating;
                String movieDate;

                // Get the JSON object representing the movie
                JSONObject movie = moviesArray.getJSONObject(i);

                // Get movie title
                movieTitle = movie.getJSONObject(TMDB_TITLE).toString();
                resultStrs[i][0] = movieTitle;

                // Get movie poster path
                moviePoster = movie.getJSONObject(TMDB_POSTER).toString();
                resultStrs[i][1] = moviePoster;

                // Get movie overview
                movieOverview = movie.getJSONObject(TMDB_OVERVIEW).toString();
                resultStrs[i][2] = movieOverview;

                // Get movie rating
                movieRating = movie.getJSONObject(TMDB_RATING).toString();
                resultStrs[i][3] = movieRating;

                // Get movie release date
                movieDate = movie.getJSONObject(TMDB_DATE).toString();
                resultStrs[i][4] = movieDate;
            }
            return resultStrs;
        }

        @Override
        protected String[][] doInBackground(String... params) {

            // If there are no movies there is nothing to look up. Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;
            String apiKey = "13b190834687e176fea30f493119cb2d";

            try {
                // Construct the URL for the TheMovieDB query
                // Possible parameters are available at TMDB's API page, at
                // https://www.themoviedb.org/documentation/api
                final String FORECAST_BASE_URL = "https://api.themoviedb.org/3/discover/movie";
                final String APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(APIKEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, url.toString());

                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.v(LOG_TAG, moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attempting
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[][] result) {
            if (result != null) {

            }
        }
    }
}
