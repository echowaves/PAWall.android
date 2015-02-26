package com.echowaves.pawall.core;

import android.app.Application;

import com.flurry.android.FlurryAgent;
import com.parse.Parse;

/**
 * Created by dmitry on 2/25/15.
 *
 */
public class PAWApplication extends Application implements PAWConstants {
    private static PAWApplication singleton;

    public PAWApplication getInstance(){
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        // init Flurry
        FlurryAgent.init(this, FLURRY_API_KEY);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_ID);

    }
}