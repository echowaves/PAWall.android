package com.echowaves.pawall.model;

import com.echowaves.pawall.core.PAWApplication;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 *
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
                if(e == null) {
                    if(list.size() > 0) {
                        gBookmark[0] = list.get(0);
//                        gBookmark.put(SEARCH_TEXT, list.get(0).getString(SEARCH_TEXT));
//                        gBookmark.put(CREATED_BY, list.get(0).getString(CREATED_BY));
                    }
                    else {
                        gBookmark[0].put(SEARCH_TEXT, myBookmark.toLowerCase());
                        gBookmark[0].put(CREATED_BY, PAWApplication.getInstance().getUUID());
                    }
                    gBookmark[0].put(LOCATION, PAWApplication.getInstance().getCurrentLocation());
                    gBookmark[0].saveEventually();
                }
            }
        });
    }

}
