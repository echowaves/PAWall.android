package com.echowaves.pawall.model;

import android.util.Log;

import com.echowaves.pawall.core.PAWApplication;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Created by dmitry on 3/4/15.
 */
public class GAlert extends BaseDataModel {
    final public static String CLASS_NAME = "GAlerts";
    final public static String PARENT_POST = "parentPost";
    final public static String PARENT_CONVERSATION = "parentConversation"; // only the alert that has paren conversation can be replied to
    final public static String TARGET = "target"; //uuid
    final public static String ALERT_BODY = "alertBody";
    final public static String POST_BODY = "postBody";
    final public static String MESSAGE_BODY = "messageBody";

    public static void createOrUpdateAlert(
//        parentPost: PFObject,
            final ParseObject parentConversation,
            final String target,
            final String alertBody,
            final String chatReply) {
        final ParseObject[] alert = {new ParseObject(CLASS_NAME)};
        ParseQuery<ParseObject> query = ParseQuery.getQuery(CLASS_NAME);
//            query.whereKey(GALERT.PARENT_POST, equalTo: parentPost)
        query.whereEqualTo(PARENT_CONVERSATION, parentConversation);
        query.whereEqualTo(TARGET, target);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // The find succeeded.
                    // Do something with the found objects

                    Log.d("GAlert", "Successfully retrieved " + objects.size() + " alerts");
                    if (objects.size() > 0) {
                        // update the found alert here
                        alert[0] = objects.get(0);
                        alert[0].put(ALERT_BODY, alertBody);
                        alert[0].put(MESSAGE_BODY, chatReply);
                        alert[0].saveEventually();
                    } else {

                        ParseObject parentPost = parentConversation.getParseObject(GConversation.PARENT_POST);
                        try {
                            parentPost.fetchIfNeeded();
                        } catch (ParseException e1) {
                            Log.d("GAlert", e1.toString());
                        }
                        // create new alert here
                        alert[0].put(PARENT_POST, parentPost);
                        alert[0].put(PARENT_CONVERSATION, parentConversation);
                        alert[0].put(TARGET, target);
                        alert[0].put(ALERT_BODY, alertBody);
                        alert[0].put(POST_BODY, parentPost.getString(GPost.BODY));
                        alert[0].put(MESSAGE_BODY, chatReply);
                        alert[0].saveEventually();
                    }
                } else {
                    // Log details of the failure
                    Log.d("GAlert", e.toString());
                    //                let alertMessage = UIAlertController(title: "Error", message: "Error retreiving alerts, try agin.", preferredStyle: UIAlertControllerStyle.Alert)
                    //                let ok = UIAlertAction(title: "OK", style: .Default, handler: { (action) -> Void in})
                    //                alertMessage.addAction(ok)
                    //                self.presentViewController(alertMessage, animated: true, completion: nil)

                }
            }
        });
    }


    public static void createAlertForMyPost(ParseObject myPost) {
        ParseObject alert = new ParseObject(CLASS_NAME);
        alert.put(PARENT_POST, myPost);
        alert.put(TARGET, PAWApplication.getInstance().getUUID());
        alert.put(ALERT_BODY, "Post created by me:");
        alert.put(POST_BODY, myPost.getString(GPost.BODY));
        alert.put(MESSAGE_BODY, "");
        alert.saveEventually();
    }

    public static void createAlertsForMatchingBookmarks(final ParseObject myPost, final ParseGeoPoint location) {

        try {
            myPost.fetch(); // to make sure the hashtags are parsed and updated on the server side
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> hashTags = myPost.getList(GPost.HASH_TAGS);
        Log.d("GAlert", "there are " + hashTags.size() + " hashTags in new post");

        for (final String hashTag : hashTags) {
            // here alert all the phones that have bookmarks matching my new post
            // first find all bookmarks that match new post, repeat for ever hash tag in original post
            ParseObject gBookmark = new ParseObject(GBookmark.CLASS_NAME);
            ParseQuery<ParseObject> query = new ParseQuery<>(GBookmark.CLASS_NAME);
            query.whereNear(GBookmark.LOCATION, location);
            query.whereContainsAll(GBookmark.HASH_TAGS, Arrays.asList(hashTag));
            query.whereNotEqualTo(GBookmark.CREATED_BY, PAWApplication.getInstance().getUUID());// exclude my device bookmarks

            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        // The find succeeded.
                        Log.d("GAlert", "Successfully retrieved " + objects.size() + " bookmarks for hashTag: " + hashTag);
                        //                                    finally create an alert here for each bookmark
                        for (ParseObject bookmark : objects) {
                            //first create a convo, then create a corresponding alert
                            try {
                                ParseObject newConversation = GConversation.createConversation(myPost,
                                        location,
                                        bookmark.getString(GBookmark.CREATED_BY));

                                // create alert for post owner
                                ParseObject alert = new ParseObject(CLASS_NAME);
                                alert.put(PARENT_POST, myPost);
                                alert.put(PARENT_CONVERSATION, newConversation);
                                alert.put(TARGET, bookmark.getString(GBookmark.CREATED_BY));
                                alert.put(ALERT_BODY, "New Post matching my bookmark:");
                                alert.put(POST_BODY, myPost.getString(GPost.BODY));
                                alert.put(MESSAGE_BODY, "");
                                alert.saveEventually();
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }//for

                    } //if e
                }
            });
        }
    }

}
