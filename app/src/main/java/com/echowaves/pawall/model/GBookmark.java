package com.echowaves.pawall.model;

import android.util.Log;

import com.echowaves.pawall.core.PAWApplication;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by dmitry on 3/4/15.
 */
public class GBookmark extends BaseDataModel {
    final public static String CLASS_NAME = "GBookmarks";
    final public static String LOCATION = "location";
    final public static String SEARCH_TEXT = "searchText";
    final public static String HASH_TAGS = "hashtags";
    final public static String CREATED_BY = "createdBy";//uuid


    public static void createBookmark(final String myBookmark) {
        final ParseObject[] gBookmark = {new ParseObject(CLASS_NAME)};
        ParseQuery query = new ParseQuery(CLASS_NAME);
        query.whereEqualTo(SEARCH_TEXT, myBookmark.toLowerCase());
        query.whereEqualTo(CREATED_BY, PAWApplication.getInstance().getUUID());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        gBookmark[0] = list.get(0);
                    } else {
                        gBookmark[0].put(SEARCH_TEXT, myBookmark.toLowerCase());
                        gBookmark[0].put(CREATED_BY, PAWApplication.getInstance().getUUID());
                    }
                    gBookmark[0].put(LOCATION, PAWApplication.getInstance().getCurrentLocation());
                    gBookmark[0].saveEventually();
                }
            }
        });
    }


    public static void findMyBookmarks(
            final String createdBy,
            final PAWModelCallback callback) {

        ParseQuery<ParseObject> query = new ParseQuery<>(CLASS_NAME);

        query.whereEqualTo(CREATED_BY, createdBy);
        query.orderByDescending(CREATED_AT);

        // Limit what could be a lot of points.

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    Log.d("GBookmark", "Successfully retrieved " + parseObjects.size() + " bookmarks");
                    callback.succeeded(parseObjects);
                } else {
                    Log.e("GBookmark", e.toString());
                    callback.failed(e);
                }
            }
        });
    }

}
