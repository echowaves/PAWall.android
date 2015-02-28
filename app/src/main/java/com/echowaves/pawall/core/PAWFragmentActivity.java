package com.echowaves.pawall.core;

import android.support.v4.app.FragmentActivity;

import com.flurry.android.FlurryAgent;

/**
 * Created by dmitry on 2/27/15.
 *
 */
public class PAWFragmentActivity extends FragmentActivity implements PAWConstants {

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
