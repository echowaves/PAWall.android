package com.echowaves.pawall.model;

import android.util.Log;

import com.echowaves.pawall.core.PAWApplication;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dmitry on 3/4/15.
 *
 */
public class GConversation extends BaseDataModel {
    final static String CLASS_NAME = "GConversations";
    final static String PARENT_POST = "parentPost";
    final static String PARTICIPANTS = "participants"; //array of uuid's
    final static String CHARGES_APPLIED = "chargesApplied";
    final static String MESSAGES_COUNT = "messagesCount";
    final static String LOCATION = "location";

    public static ParseObject createConversation(final ParseObject myPost,
                                                 final ParseGeoPoint myLocation,
                                                 final String replier) throws ParseException {
        ParseObject gConversation = new ParseObject(CLASS_NAME);
        gConversation.put(PARENT_POST, myPost);
        gConversation.put(PARTICIPANTS, Arrays.asList(myPost.getString(GPost.POSTED_BY), replier));
        gConversation.put(LOCATION, myLocation);
        gConversation.put(MESSAGES_COUNT, 0);
        gConversation.save();
        return gConversation;
    }

    public static ParseObject findOrCreateMyConversation(
    final ParseObject postObject
    ) throws ParseException {
        ParseQuery<ParseObject> convQuery = new ParseQuery<>(CLASS_NAME);
        convQuery.whereEqualTo(PARENT_POST, postObject);
        convQuery.whereContainsAll(PARTICIPANTS, Arrays.asList(PAWApplication.getInstance().getUUID()));

        List<ParseObject> objects = convQuery.find();
        // if no conversation is yet created, create one and also create a first message from the post
        if(objects.size() == 0) {
            ParseObject conversation = GConversation.createConversation(
                    postObject,
                    postObject.getParseGeoPoint(GPost.LOCATION),
                    PAWApplication.getInstance().getUUID());
            GMessage.createFirstMessage(conversation, postObject, postObject.getParseGeoPoint(GPost.LOCATION));
            Log.d("GConversation", "found conversation and returning");
            return conversation;
        } else {
            final ParseObject conversation = objects.get(0);
            GMessage.createFirstMessage(conversation, postObject, postObject.getParseGeoPoint(GPost.LOCATION));
            Log.d("GConversation" , "findOrCreateMyConversation + creating conversation and returning");
            return conversation;
        }
    }


    public static String otherParticipantInMyConversation(ParseObject conversation) {
        if (conversation.getList(PARTICIPANTS).get(0) == PAWApplication.getInstance().getUUID()) {
            return (String)conversation.getList(PARTICIPANTS).get(1);
        } else {
            return (String)conversation.getList(PARTICIPANTS).get(0);
        }
    }


}
