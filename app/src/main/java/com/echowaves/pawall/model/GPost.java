package com.echowaves.pawall.model;

import android.util.Log;

import com.echowaves.pawall.core.PAWApplication;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dmitry on 3/2/15.
 *
 */
public class GPost extends BaseDataModel {
    final static String CLASS_NAME = "GPosts";
    final static String POSTED_BY = "postedBy";//uuid
    final static String BODY = "body";
    final static String WORDS = "words";
    final static String HASH_TAGS = "hashtags";
    final static String LOCATION = "location";
    final static String ACTIVE = "active";
    final static String REPLIES = "replies";

    public static void createPost(
            final String body,
            final ParseGeoPoint location,
            final String postedBy,
            final PAWModelCallback callback) {
        final ParseObject postObject = new ParseObject(CLASS_NAME);

        postObject.put(BODY, body);
        postObject.put(LOCATION, location);
        postObject.put(ACTIVE, true);
        postObject.put(POSTED_BY, postedBy);
        postObject.put(REPLIES, 0);


        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    GAlert.createAlertForMyPost(postObject);
                    GAlert.createAlertsForMatchingBookmarks(postObject, location);
                    callback.succeeded(postObject);
                } else {
                    callback.failed(e);
                }
            }
        });

    }


    public static void findPostNearMe(
            final ParseGeoPoint location,
            final String searchText,
            final int resultsLimit,
            final PAWModelCallback callback) {


        // Create a query for places
        ParseQuery<ParseObject> query = new ParseQuery<>(CLASS_NAME);
        // Interested in locations near user.
        query.whereNear(LOCATION, location);
        query.whereEqualTo(ACTIVE, true);
        query.whereNotEqualTo(POSTED_BY, PAWApplication.getInstance().getUUID());
        Log.d("GPost", "Searching for string " + searchText);
        if (!searchText.isEmpty()) {
            List<String> hashTags = Arrays.asList(searchText.toLowerCase().replace("#", "").split(" "));
            query.whereContainsAll(HASH_TAGS, hashTags);
        }
        // Limit what could be a lot of points.
        query.setLimit(resultsLimit);
        // Final list of objects
        //                self.postsNearMe =

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    // The find succeeded.
                    // Do something with the found objects
                    callback.succeeded(objects);
                } else {
                    // Log details of the failure
                    callback.failed(e);
                }
            }
        });

    }

}
