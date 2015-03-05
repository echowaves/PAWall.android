package com.echowaves.pawall.model;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by dmitry on 3/4/15.
 *
 */
public class GMessage extends BaseDataModel {
    final static String CLASS_NAME = "GMessages";
    final static String PARENT_CONVERSATION = "parentConversation";
    final static String REPLIED_BY = "repliedBy";//uuid
    final static String BODY = "body";
    final static String LOCATION = "location";

    public static void createMessage(
            ParseObject parentConversation,
            String repliedBy,
            String body,
            ParseGeoPoint location
    ) {
        ParseObject gMessage = new ParseObject(CLASS_NAME);
        gMessage.put(PARENT_CONVERSATION, parentConversation);
        gMessage.put(REPLIED_BY, repliedBy);
        gMessage.put(BODY, body);
        gMessage.put(LOCATION, location);
        try {
            gMessage.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parentConversation.increment(GConversation.MESSAGES_COUNT);
        parentConversation.saveEventually();
    }


    public static void createFirstMessage(
            ParseObject parentConversation,
            ParseObject parentPost,
            ParseGeoPoint location
    ) {
        if (parentConversation.getInt(GConversation.MESSAGES_COUNT) == 0) {
            GMessage.createMessage(
                    parentConversation,
                    parentPost.getString(GPost.POSTED_BY),
                    parentPost.getString(GPost.BODY),
                    location);
        }
    }


}
