package com.example.emccalley.flix;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

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

        // Dummy data
        ArrayList<String> dummyArray = new ArrayList<String>();
        dummyArray.add("First Movie");
        dummyArray.add("Second Movie");
        dummyArray.add("Third Movie");
        dummyArray.add("Fourth Movie");
        dummyArray.add("Fifth Movie");
        dummyArray.add("Sixth Movie");
        dummyArray.add("Seventh Movie");
        dummyArray.add("Eighth Movie");
        dummyArray.add("Ninth Movie");
        dummyArray.add("Tenth Movie");

        // The ArrayAdapter will take data from a source and
        // use it to populate the ListView it's attached to.
        mMovieAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_movie, // The name of the layout ID.
                R.id.list_item_movie_textview, // The ID of the textview to populate.
                dummyArray);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_movies);
        listView.setAdapter(mMovieAdapter);

        return rootView;
    }
}
