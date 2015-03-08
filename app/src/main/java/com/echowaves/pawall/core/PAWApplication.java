package com.echowaves.pawall.core;

import android.app.Application;
import android.location.Criteria;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.echowaves.pawall.model.BaseDataModel;
import com.flurry.android.FlurryAgent;
import com.parse.LocationCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

/**
 * Created by dmitry on 2/25/15.
 */
public class PAWApplication extends Application implements PAWConstants {

    private static PAWApplication instance;
    private ParseGeoPoint currentLocation;

    public PAWApplication() {
        instance = this;

    }

    public static PAWApplication getInstance() {
        if (instance == null) {
            instance = new PAWApplication();
        }
        return instance;
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
        getCurrentLocation();
    }


    public String getUUID() {

        String uuid = BaseDataModel.getStoredCredential();

        if (uuid.equals("")) {
            Log.d("PAWApplication", "uuid = blank");
            TelephonyManager mTelephony = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                Log.d("PAWApplication", "uuid = mTelephony.getDeviceId()");
                uuid = mTelephony.getDeviceId();
            } else {
                uuid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.d("PAWApplication", "uuid = Secure.ANDROID_ID");
            }

            BaseDataModel.storeCredentials(uuid);
        }
        return uuid;
    }

    public ParseGeoPoint getCurrentLocation() {
        if (currentLocation == null) {
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);

            ParseGeoPoint.getCurrentLocationInBackground(10000, criteria, new LocationCallback() {
                @Override
                public void done(ParseGeoPoint geoPoint, ParseException e) {
                    if (e == null) {
                        // We were able to get the location
                        currentLocation = geoPoint;
                    } else {
                        // handle your error
                    }
                }
            });

        }
        return currentLocation;
    }

}
