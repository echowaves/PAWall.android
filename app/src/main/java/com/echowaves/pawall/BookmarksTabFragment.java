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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.echowaves.pawall.core.PAWApplication;
import com.echowaves.pawall.core.PAWTabFragment;
import com.echowaves.pawall.model.GBookmark;
import com.echowaves.pawall.model.PAWModelCallback;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 */
public class BookmarksTabFragment extends PAWTabFragment {

    private View view;

    private List<ParseObject> myBookmarks;
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bookmars_tab, container, false);
        listView = (ListView) view.findViewById(R.id.bookmarks_list);

        loadBookmarks();

        return view;
    }

    private void loadBookmarks() {
        GBookmark.findMyBookmarks(
                PAWApplication.getInstance().getUUID(),
                new PAWModelCallback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void succeeded(Object results) {
//                                    @SuppressWarnings("unchecked")
                        myBookmarks = (List<ParseObject>) results;

                        BookmarksAdapter defaultAdapter = new BookmarksAdapter(getActivity(), myBookmarks);
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
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView bookmark;
        protected TextView postedAt;
        protected ImageButton deleteButton;
    }


    public class BookmarksAdapter extends BaseAdapter {
        private List<ParseObject> mListItems;
        private LayoutInflater mLayoutInflater;

        public BookmarksAdapter(Context context, List<ParseObject> arrayList) {

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

        public View getView(final int position, View view, ViewGroup viewGroup) {

            // create a ViewHolder reference
            ViewHolder holder;
            final ParseObject bookmark = myBookmarks.get(position);

            //check to see if the reused view is null or not, if is not null then reuse it
            if (view == null) {
                holder = new ViewHolder();

                view = mLayoutInflater.inflate(R.layout.row_bookmarks_tab, null);

                holder.bookmark = (TextView) view.findViewById(R.id.row_bookmarksTab_body);
                holder.postedAt = (TextView) view.findViewById(R.id.row_bookmarksTab_postedAt);
                holder.deleteButton = (ImageButton) view.findViewById(R.id.row_bookmarksTab_deleteButton);
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AmericanTypewriter.ttc");
                holder.bookmark.setTypeface(font);



                holder.bookmark.setText(bookmark.getString(GBookmark.SEARCH_TEXT));
                holder.postedAt.setText(new SimpleDateFormat("MM-dd-yyyy").format(bookmark.getCreatedAt()));

                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
                        alertDialogConfirmWaveDeletion.setTitle("Warning");

                        // set dialog message
                        alertDialogConfirmWaveDeletion
                                .setMessage("Sure want to delete bookmark?")
                                .setCancelable(true)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        bookmark.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                loadBookmarks();
                                            }
                                        });

                                    }
                                });
                        // create alert dialog
                        AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                        // show it
                        alertDialog.show();
                    }
                });


                // the setTag is used to store the data within this view
                view.setTag(holder);
            }
// else {
//                // the getTag returns the viewHolder object set as a tag to the view
////                holder = (ViewHolder) view.getTag();
//            }


            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    Log.d("BookmarksTabFragment", "clicked on a row");



                    TabHost host = (TabHost) getActivity().findViewById(R.id.nav_tabhost);
                    host.setCurrentTab(0);
                    SearchPostsTabFragment.searchView.setQuery(bookmark.getString(GBookmark.SEARCH_TEXT), true);
                    SearchPostsTabFragment.searchView.setFocusable(true);
                    SearchPostsTabFragment.searchView.setIconified(false);
                    SearchPostsTabFragment.searchView.requestFocusFromTouch();

                }
            });


            //this method must return the view corresponding to the data at the specified position.
            return view;

        }

    }


}
