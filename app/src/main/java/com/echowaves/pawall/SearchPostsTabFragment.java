package com.echowaves.pawall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.echowaves.pawall.core.PAWApplication;
import com.echowaves.pawall.core.PAWTabFragment;
import com.echowaves.pawall.core.Utility;
import com.echowaves.pawall.model.GBookmark;
import com.echowaves.pawall.model.GPost;
import com.echowaves.pawall.model.PAWModelCallback;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 */
public class SearchPostsTabFragment extends PAWTabFragment {

    static SearchView searchView;
    private View view;
    private List<ParseObject> postsNearMe;
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                GPost.findPostNearMe(
                        PAWApplication.getInstance().getCurrentLocation(),
                        query,
                        300,
                        new PAWModelCallback() {
                            @SuppressWarnings("unchecked")
                            @Override
                            public void succeeded(Object results) {
                                postsNearMe = (List<ParseObject>) results;
                                Log.d("SearchPostsTabFragment", postsNearMe.size() + " posts near me found");

                                SearchPostsAdapter defaultAdapter = new SearchPostsAdapter(getActivity(), postsNearMe);
                                listView.setAdapter(defaultAdapter);

                                Utility.setListViewHeightBasedOnChildren(listView);

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
                searchView.setFocusable(true);
                searchView.setIconified(false);
                searchView.requestFocusFromTouch();
                searchView.requestFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        listView = (ListView) view.findViewById(R.id.searchPosts_listView);


        GPost.findPostNearMe(
                PAWApplication.getInstance().getCurrentLocation(),
                "",
                1000,
                new PAWModelCallback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void succeeded(Object results) {
//                                    @SuppressWarnings("unchecked")
                        postsNearMe = (List<ParseObject>) results;
                        Log.d("SearchPostsTabFragment", postsNearMe.size() + " posts near me found");

                        SearchPostsAdapter defaultAdapter = new SearchPostsAdapter(getActivity(), postsNearMe);
                        listView.setAdapter(defaultAdapter);

//                        Utility.setListViewHeightBasedOnChildren(listView);
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


        bookmarkImageButton = (ImageButton) view.findViewById(R.id.searchPosts_bookmarkButton);
        bookmarkImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchView.getQuery().length() <= 0) {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
                    alertDialogConfirmWaveDeletion.setTitle("Warning");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Can't save empty bookmark. Try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                    // show it
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
//                    alertDialogConfirmWaveDeletion.setTitle("Warning");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Your search will be bookmarked. You will be Alerted about new posts matching your bookmark.")
                            .setCancelable(true)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    GBookmark.createBookmark(searchView.getQuery().toString());
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


    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView replies;
        protected TextView postedAt;
        protected TextView distance;
        protected EditText postBody;

    }


    public class SearchPostsAdapter extends BaseAdapter {
        private List<ParseObject> mListItems;
        private LayoutInflater mLayoutInflater;

        public SearchPostsAdapter(Context context, List<ParseObject> arrayList) {

            mListItems = arrayList;

            //get the layout inflater
            mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            //getCount() represents how many items are in the list
            return mListItems.size();
        }

        @Override
        //get the data of an item from a specific position
        //i represents the position of the item in the list
        public Object getItem(int i) {
            return null;
        }

        @Override
        //get the position id of the item from the list
        public long getItemId(int i) {
            return 0;
        }

        @Override

        public View getView(int position, View view, ViewGroup viewGroup) {

            // create a ViewHolder reference
            ViewHolder holder;

            //check to see if the reused view is null or not, if is not null then reuse it
            if (view == null) {
                holder = new ViewHolder();

                view = mLayoutInflater.inflate(R.layout.row_search_posts_tab, null);

                holder.replies = (TextView) view.findViewById(R.id.row_searchPostsTab_replies);
                holder.postedAt = (TextView) view.findViewById(R.id.row_searchPostsTab_postedAt);
                holder.distance = (TextView) view.findViewById(R.id.row_searchPostsTab_distance);
                holder.postBody = (EditText) view.findViewById(R.id.row_searchPostsTab_postBody);
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AmericanTypewriter.ttc");
                holder.postBody.setTypeface(font);


                ParseObject post = postsNearMe.get(position);
                double cost = (1.0 / (post.getInt(GPost.REPLIES) + 1));

                holder.replies.setText("$" + Utility.round(cost, 2) + " to reply");
                holder.postedAt.setText(new SimpleDateFormat("MM-dd-yyyy").format(post.getCreatedAt()));
                double roundedDistance = Utility.round(post.getParseGeoPoint(GPost.LOCATION).distanceInMilesTo(PAWApplication.getInstance().getCurrentLocation()), 2);

                holder.distance.setText(roundedDistance + " miles");
                holder.postBody.setText(post.getString(GPost.BODY));

                // the setTag is used to store the data within this view
                view.setTag(holder);
            }
//            else {
//                // the getTag returns the viewHolder object set as a tag to the view
////                holder = (ViewHolder) view.getTag();
//            }


            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    Log.d("SearchPostsTabFragment", "clicked on a row");

//                    Intent acceptBlendingIntent = new Intent(v.getContext(), AcceptBlendingRequestActivity.class);
//
//                    acceptBlendingIntent.putExtra("FROM_WAVE", waveName);
//                    startActivity(acceptBlendingIntent);

                }
            });


            //this method must return the view corresponding to the data at the specified position.
            return view;

        }

    }


}
