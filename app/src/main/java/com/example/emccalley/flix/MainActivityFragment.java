package com.example.emccalley.flix;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mMovieAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(new ImageAdapter(getContext()));

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
                R.drawable.fightclub5, R.drawable.fightclub6,
        };
    }
}
