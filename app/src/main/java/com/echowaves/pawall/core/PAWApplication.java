package com.echowaves.pawall.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.parse.Parse;

/**
 * Created by dmitry on 2/25/15.
 *
 */
public class PAWApplication extends Application implements PAWConstants {

    private static PAWApplication instance;
    public static Context getInstance() {
        if(instance == null) {
            instance = new PAWApplication();
        }
        return instance;
    }

    public PAWApplication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        singleton = this;

        // init Flurry
        FlurryAgent.init(this, FLURRY_API_KEY);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_ID);


        Log.d("PAWApplication", getUUID());

    }


    public String getUUID() {
        SharedPreferences prefs = new SecurePreferences(this);

        String uuid = prefs.getString(UUID_KEY, "");

        if(uuid.equals("")) {
            Log.d("PAWApplication", "uuid = blank");
            TelephonyManager mTelephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                Log.d("PAWApplication", "uuid = mTelephony.getDeviceId()");
                uuid = mTelephony.getDeviceId();
            } else {
                uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("PAWApplication", "uuid = Secure.ANDROID_ID");
            }

            prefs.edit().putString(UUID_KEY, uuid).commit();
        }
        return uuid;
    }

}
