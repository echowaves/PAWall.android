package com.echowaves.pawall;

import android.app.Application;

import com.flurry.android.FlurryAgent;

/**
 * Created by dmitry on 2/25/15.
 *
 */
public class PAWApplication extends Application implements PAWConstants {
    @Override
    public void onCreate() {
        super.onCreate();
        // init Flurry
        FlurryAgent.init(this, FLURRY_API_KEY);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_ID);

    }
}
