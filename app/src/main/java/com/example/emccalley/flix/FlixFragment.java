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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A fragment containing main movie grid view
 */
public class FlixFragment extends Fragment {

    // This adapter will populate views with data captured from TMDB
    private CustomAdapter mMovieAdapter;

    // This list will contain all the data from the TMDB API
    private ArrayList<HashMap<String, Object>> movieList;

    // These are the names of the JSON objects from TMDB that need to be extracted
    final String TMDB_RESULTS = "results";
    final String TMDB_ORIGINAL_TITLE = "original_title";
    final String TMDB_POSTER_PATH = "poster_path";
    final String TMDB_OVERVIEW = "overview";
    final String TMDB_VOTE_AVERAGE = "vote_average";
    final String TMDB_POPULARITY = "popularity";
    final String TMDB_RELEASE_DATE = "release_date";

    public FlixFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.flixfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_popularity) {
            sortByPopularity(movieList, TMDB_POPULARITY);
            mMovieAdapter.setGridData(movieList);
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sort_by_rating) {
            sortByPopularity(movieList, TMDB_VOTE_AVERAGE);
            mMovieAdapter.setGridData(movieList);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Sorts movieList from highest to lowest parameter
     * @param alist is the master list used to populate grid.
     * @param movieKey is the HashMap key for the values we are sorting by.
     */
    public void sortByPopularity(ArrayList<HashMap<String, Object>> alist, final String movieKey) {
        Collections.sort(alist, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> first, HashMap<String, Object> second) {
                Double firstPopularity = Double.parseDouble(first.get(movieKey).toString());
                Double secondPopularity = Double.parseDouble(second.get(movieKey).toString());
                return -1 * firstPopularity.compareTo(secondPopularity);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // The CustomAdapter will take data from a source and
        // use it to populate the GridView it's attached to
        mMovieAdapter = new CustomAdapter(getActivity(), new ArrayList<HashMap<String, Object>>());

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView and attach adapter to it
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mMovieAdapter);

        // Intent to see movie details
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                // Convert image poster to byte array in order to pass via intent
                Bitmap bitmap = mMovieAdapter.bitList.get(position);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();

                // Intent to see movie details
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle extras = new Bundle();
                extras.putByteArray("EXTRA_THUMB", b);
                extras.putString("EXTRA_TITLE", getMovieString(position, TMDB_ORIGINAL_TITLE));
                extras.putString("EXTRA_SCORE", getMovieString(position, TMDB_VOTE_AVERAGE));
                extras.putString("EXTRA_POPULARITY", getMovieString(position, TMDB_POPULARITY));
                extras.putString("EXTRA_DATE", getMovieString(position, TMDB_RELEASE_DATE));
                extras.putString("EXTRA_OVERVIEW", getMovieString(position, TMDB_OVERVIEW));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        return rootView;
    }


    // Helper method to pull movie text info from mMovieAdapter
    public String getMovieString(int position, String str) {
        return mMovieAdapter.entireList.get(position).get(str).toString();
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
            bmp = (Bitmap) list.get(i).get(TMDB_POSTER_PATH);
            bitList.add(bmp);
        }
        return bitList;
    }

    public class CustomAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<HashMap<String, Object>> entireList;
        private ArrayList<Bitmap> bitList;

        public CustomAdapter(Context context, ArrayList<HashMap<String, Object>> entireList) {
            this.mContext = context;
            this.entireList = entireList;
            this.bitList = getBitmapList(entireList);
        }

        /**
         * Updates grid data and refresh grid items.
         * @param entireList is all the data from the TMDB API used to populate the UI.
         */
        public void setGridData(ArrayList<HashMap<String, Object>> entireList) {
            this.entireList = entireList;
            this.bitList = getBitmapList(entireList);
            notifyDataSetChanged();
        }

        public int getCount() {
            return entireList.size();
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


    public class FetchMoviesTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>> {

        private final String LOG_TAG = "Estevan";
        private final String apiKey = "13b190834687e176fea30f493119cb2d";
        private final String APIKEY_PARAM = "api_key";


        /**
         * Take the String representing movie data in JSON Format and
         * pull out the data we need to construct the strings and images needed for the wireframes.
         */
        protected ArrayList<HashMap<String, Object>> getMovieDataFromJson (String moviesJsonStr) throws JSONException {

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
                String popularity = r.getString(TMDB_POPULARITY);
                String releaseDate = r.getString(TMDB_RELEASE_DATE);

                // tmp hashmap for single movie
                HashMap<String, Object> movie = new HashMap<String, Object>();

                // adding each child node to HashMap key => value
                movie.put(TMDB_ORIGINAL_TITLE, originalTitle);
                movie.put(TMDB_POSTER_PATH, getBitmapFromURL(posterPath));
                movie.put(TMDB_OVERVIEW, overview);
                movie.put(TMDB_VOTE_AVERAGE, voteAverage);
                movie.put(TMDB_POPULARITY, popularity);
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
                return BitmapFactory.decodeStream(input);
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
                movieList = result;
                mMovieAdapter.setGridData(movieList);
            }
        }
    }
}
