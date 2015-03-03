package com.echowaves.pawall.model;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by dmitry on 3/2/15.
 */
public class GPost extends BaseDataModel {
    private static String CLASS_NAME = "GPosts";
    private static String POSTED_BY = "postedBy";//uuid
    private static String BODY = "body";
    private static String WORDS = "words";
    private static String HASH_TAGS = "hashtags";
    private static String LOCATION = "location";
    private static String ACTIVE = "active";
    private static String REPLIES = "replies";

    public static void createPost(
            String body,
            ParseGeoPoint location,
            String postedBy,
            final PAWModelCallback callback) {
        ParseObject postObject = new ParseObject(CLASS_NAME);

        postObject.put(BODY, body);
        postObject.put(LOCATION, location);
        postObject.put(ACTIVE, true);
        postObject.put(POSTED_BY, postedBy);
        postObject.put(REPLIES, 0);


        postObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
//                    GAlert.createAlertForMyPost(postObject)
//                    GAlert.createAlertsForMatchingBookmarks(postObject, location:location)
                }
                callback.done(e);
            }
        });

    }

}
