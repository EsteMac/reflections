package com.example.emccalley.flix;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by emccalley on 12/30/15.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    public static class DetailFragment extends Fragment {

        private Bitmap image;
        private String mTitleStr;
        private String score;
        private String date;
        private String overview;

        public DetailFragment() {
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent. Inspect the intent for movie title.
            Intent intent = getActivity().getIntent();
            Bundle extras = intent.getExtras();
            if (extras != null) {

                // Get movie title
                mTitleStr = extras.getString("EXTRA_TITLE");
                ((TextView) rootView.findViewById(R.id.title_text)).setText(mTitleStr);

                // Get movie thumbnail image
                byte[] b = extras.getByteArray("EXTRA_THUMB");
                image = BitmapFactory.decodeByteArray(b, 0, b.length);
                ImageView thumbView = (ImageView) rootView.findViewById(R.id.movie_thumb);
                thumbView.setImageBitmap(image);

                // Get user score
                score = extras.getString("EXTRA_SCORE");
                ((TextView) rootView.findViewById(R.id.rating_score)).setText(score);

                // Get movie release date
                date = extras.getString("EXTRA_DATE");
                ((TextView) rootView.findViewById(R.id.released_date)).setText(date);

                // Get movie overview
                overview = extras.getString("EXTRA_OVERVIEW");
                ((TextView) rootView.findViewById(R.id.movie_overview)).setText(overview);
            }
            return rootView;
        }
    }
}
