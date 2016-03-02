package com.example.emccalley.flix;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.HashMap;

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

        // The ImageAdapter will take data from a source and
        // use it to populate the GridView it's attached to
        mMovieAdapter = new ImageAdapter(getActivity(), new ArrayList<Bitmap>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView and attach adapter to it
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

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    // Helper method to pull bitmaps from movieList
    public ArrayList<Bitmap> getBitmapList(ArrayList<HashMap<String, Object>> list) {
        ArrayList<Bitmap> bitList = new ArrayList<>();
        Bitmap bmp;

        for (int i=0; i < list.size(); i++) {
            bmp = (Bitmap) list.get(i).get("poster_path");
            bitList.add(bmp);
        }
        return bitList;
    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Bitmap> bitList;

        public ImageAdapter(Context context, ArrayList<Bitmap> bitList) {
            this.mContext = context;
            this.bitList = bitList;
        }

        /**
         * Updates grid data and refresh grid items.
         * @param bitList is a bitmap list used to populate grid.
         */
        public void setGridData(ArrayList<Bitmap> bitList) {
            this.bitList = bitList;
            notifyDataSetChanged();
        }

        public int getCount() {
            return bitList.size();
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
                imageView = new ImageView(this.mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 450));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageBitmap(this.bitList.get(position));
            return imageView;
        }
    }

    // references to dummy movie titles
    // will eventually replace with titles from TMDB API
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

    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        private final String LOG_TAG = "Estevan";
        private final String apiKey = "13b190834687e176fea30f493119cb2d";
        private final String APIKEY_PARAM = "api_key";


        /**
         * Take the String representing movie data in JSON Format and
         * pull out the data we need to construct the strings and images needed for the wireframes.
         */
        protected ArrayList<HashMap<String, Object>> getMovieDataFromJson (String moviesJsonStr) throws JSONException {

            // These are the names of the JSON objects that need to be extracted
            final String TMDB_RESULTS = "results";
            final String TMDB_ORIGINAL_TITLE = "original_title";
            final String TMDB_POSTER_PATH = "poster_path";
            final String TMDB_OVERVIEW = "overview";
            final String TMDB_VOTE_AVERAGE = "vote_average";
            final String TMDB_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(TMDB_RESULTS);

            // Hashmap for GridView
            ArrayList<HashMap<String, Object>> movieList = new ArrayList<>();

            // Loop through all movies
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject r = resultsArray.getJSONObject(i);

                String originalTitle = r.getString(TMDB_ORIGINAL_TITLE);
                String posterPath = r.getString(TMDB_POSTER_PATH);
                String overview = r.getString(TMDB_OVERVIEW);
                String voteAverage = r.getString(TMDB_VOTE_AVERAGE);
                String releaseDate = r.getString(TMDB_RELEASE_DATE);

                // tmp hashmap for single movie
                HashMap<String, Object> movie = new HashMap<String, Object>();

                // adding each child node to HashMap key => value
                movie.put(TMDB_ORIGINAL_TITLE, originalTitle);
                movie.put(TMDB_POSTER_PATH, getBitmapFromURL(posterPath));
                movie.put(TMDB_OVERVIEW, overview);
                movie.put(TMDB_VOTE_AVERAGE, voteAverage);
                movie.put(TMDB_RELEASE_DATE, releaseDate);

                // add movie to movie list
                movieList.add(movie);
            }
            return movieList;
        }

        // Helper method to get movie poster bitmap from cloud
        protected Bitmap getBitmapFromURL(String fileName) {
            final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
            final String POSTER_WIDTH = "w300";
            final String POSTER_PATH = fileName;

            try {
                Uri posterbuiltUri = Uri.parse(POSTER_BASE_URL).buildUpon()
                        .appendPath(POSTER_WIDTH)
                        .appendPath(fileName.replace("/", ""))
                        .build();

                URL url = new URL(posterbuiltUri.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap posterBitmap = BitmapFactory.decodeStream(input);
                return posterBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(Void... params) {

            // If there are no movies there is nothing to look up. Verify size of params.
            // if (params.length == 0) {
            //    Log.e("ServiceHandler", "There aren't any movies");
            // }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            try {
                // Construct the URL for the TheMovieDB query
                // Possible parameters are available at TMDB's API page, at
                // https://www.themoviedb.org/documentation/api
                final String MOVIES_BASE_URL = "https://api.themoviedb.org/3/discover/movie";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
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
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
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
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                }
                moviesJsonStr = buffer.toString();

                Log.v(LOG_TAG, moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attempting
                // to parse it.
                Log.e("ServiceHandler", "Couldn't get any data from the url");
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
        protected void onPostExecute(ArrayList<HashMap<String, Object>> result) {
            if (result != null) {
                ArrayList<Bitmap> bitmapList = getBitmapList(result);
                mMovieAdapter.setGridData(bitmapList);
            }
        }
    }
}
