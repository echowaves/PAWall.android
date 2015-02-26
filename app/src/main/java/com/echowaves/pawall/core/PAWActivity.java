package com.echowaves.pawall.core;

import android.app.Activity;

import com.flurry.android.FlurryAgent;

/**
 * Created by dmitry on 2/25/15.
 *
 */
abstract public class PAWActivity extends Activity implements PAWConstants {
    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, FLURRY_API_KEY);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

}
