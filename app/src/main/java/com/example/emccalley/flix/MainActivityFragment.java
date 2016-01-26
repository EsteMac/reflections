package com.example.emccalley.flix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
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
}
