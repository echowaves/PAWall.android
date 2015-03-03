package com.echowaves.pawall.model;

import android.content.SharedPreferences;

import com.echowaves.pawall.core.PAWApplication;
import com.echowaves.pawall.core.PAWConstants;
import com.echowaves.pawall.core.SecurePreferences;

/**
 * Created by dmitry on 3/2/15.
 */
public abstract class BaseDataModel implements PAWConstants {
    static final private SharedPreferences prefs = new SecurePreferences(PAWApplication.getInstance());

    public static void storeCredentials(String uuid) {
        prefs.edit().putString(UUID_KEY, uuid).commit();
    }

    public static String getStoredCredential() {
        return prefs.getString(UUID_KEY, "");
    }

}
