package com.echowaves.pawall.model;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

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
            String body,
            final ParseGeoPoint location,
            String postedBy,
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
                }
                callback.done(e);
            }
        });

    }

}
