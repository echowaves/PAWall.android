package com.echowaves.pawall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.location.Criteria;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.echowaves.pawall.core.PAWTabFragment;
import com.echowaves.pawall.model.GPost;
import com.echowaves.pawall.model.PAWModelCallback;
import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.List;


/**
 */
public class SearchPostsTabFragment extends PAWTabFragment {

    protected ParseGeoPoint currentLocation;
    private View view;
    private List<ParseObject> postsNearMe;
    private SearchView searchView;
    private ListView listView;
    private ImageButton bookmarkImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SearchPostsTabFragment", "onCreate()");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("SearchPostsTabFragment", "onCreateView()");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_posts_tab, container, false);

        searchView = (SearchView) view.findViewById(R.id.searchPosts_searchView);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        ParseGeoPoint.getCurrentLocationInBackground(10000, criteria, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint geoPoint, ParseException e) {
                if (e == null) {
                    // We were able to get the location
                    currentLocation = geoPoint;

                    GPost.findPostNearMe(
                            currentLocation,
                            "",
                            1000,
                            new PAWModelCallback() {
                                @Override
                                public void succeeded(Object results) {
//                                    @SuppressWarnings("unchecked")
                                    postsNearMe = (List<ParseObject>) results;
                                    Log.d("SearchPostsTabFragment", postsNearMe.size() + " posts near me found");
                                }

                                @Override
                                public void failed(com.parse.ParseException e) {
                                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
                                    alertDialogConfirmWaveDeletion.setTitle("Error");

                                    // set dialog message
                                    alertDialogConfirmWaveDeletion
                                            .setMessage("Unable to find posts near you. Try again.")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                                    // show it
                                    alertDialog.show();

                                }
                            }
                    );

                } else {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
                    alertDialogConfirmWaveDeletion.setTitle("Error");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Enable GPS and try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                    // show it
                    alertDialog.show();
                }
            }
        });


        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("SearchPostsTabFragment", "onAttach()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SearchPostsTabFragment", "onResume()");
    }
}
