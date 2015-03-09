package com.echowaves.pawall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import com.echowaves.pawall.core.PAWApplication;
import com.echowaves.pawall.core.PAWTabFragment;
import com.echowaves.pawall.model.GAlert;
import com.echowaves.pawall.model.GBookmark;
import com.echowaves.pawall.model.GPost;
import com.echowaves.pawall.model.PAWModelCallback;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;


/**
 *
 */
public class AlertsTabFragment extends PAWTabFragment {

    private View view;

    private List<ParseObject> myAlerts;
    private ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_alerts_tab, container, false);
        listView = (ListView) view.findViewById(R.id.alerts_list);

        loadAlerts();
        return view;
    }

    private void loadAlerts() {
        GAlert.findMyAlerts(
                PAWApplication.getInstance().getUUID(),
                new PAWModelCallback() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void succeeded(Object results) {
                        myAlerts = (List<ParseObject>) results;

                        AlertsAdapter defaultAdapter = new AlertsAdapter(getActivity(), myAlerts);
                        listView.setAdapter(defaultAdapter);

//                        Utility.setListViewHeightBasedOnChildren(listView);
                    }

                    @Override
                    public void failed(com.parse.ParseException e) {
                        AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(getActivity());
                        alertDialogConfirmWaveDeletion.setTitle("Error");

                        // set dialog message
                        alertDialogConfirmWaveDeletion
                                .setMessage("Unable to retrieve alerts. Try again.")
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
    private static class ViewHolder1 {
        protected TextView alertBody;
        protected TextView chatMessageBody;
        protected TextView originalPostBody;
        protected TextView updatedAt;
    }
    private static class ViewHolder2 {
        protected TextView createdAt;
        protected Switch activeSwitch;
        protected EditText postBody;
    }


    public class AlertsAdapter extends BaseAdapter {
        private List<ParseObject> mListItems;
        private LayoutInflater mLayoutInflater;

        public AlertsAdapter(Context context, List<ParseObject> arrayList) {

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

            // create a ViewHolder references
            ViewHolder1 holder1;
            ViewHolder2 holder2;
            final ParseObject alert = myAlerts.get(position);
//            try {
//                alert.fetchIfNeeded();
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
            final String alertBody = alert.getString(GAlert.ALERT_BODY);
            final ParseObject parentConversation = alert.getParseObject(GAlert.PARENT_CONVERSATION);
            SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm a");
            Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/AmericanTypewriter.ttc");

            if (parentConversation != null) {
                view = mLayoutInflater.inflate(R.layout.row_alerts_tab_1, null);
            } else
            if (parentConversation == null) {
                view = mLayoutInflater.inflate(R.layout.row_alerts_tab_2, null);
            }

            //check to see if the reused view is null or not, if is not null then reuse it
            if (view == null) {

                if (parentConversation != null) { // only the alerts that have paren conversation can be replied to
                    holder1 = new ViewHolder1();

                    holder1.updatedAt.setText(df.format(alert.getUpdatedAt()));
                    holder1.alertBody.setText(alertBody);
                    holder1.chatMessageBody.setText(alert.getString(GAlert.MESSAGE_BODY));
                    holder1.chatMessageBody.setTypeface(font);
                    holder1.originalPostBody.setText(alert.getString(GAlert.POST_BODY));
                    holder1.originalPostBody.setTypeface(font);
                    view.setTag(holder1);
                } else
                if (parentConversation == null){ // this alert is created by me, so there is no conversation involved and it can't replied to
                    holder2 = new ViewHolder2();

                    holder2.createdAt.setText(df.format(alert.getCreatedAt()));
                    holder2.postBody.setText(alert.getString(GAlert.POST_BODY));
                    holder2.postBody.setTypeface(font);

                    ParseObject post = alert.getParseObject(GAlert.PARENT_POST);
                    try {
                        post.fetchIfNeeded();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    holder2.activeSwitch.setChecked(post.getBoolean(GPost.ACTIVE));
                    view.setTag(holder2);
                }

                // the setTag is used to store the data within this view
            }


            //this method must return the view corresponding to the data at the specified position.
            return view;

        }

    }


}
