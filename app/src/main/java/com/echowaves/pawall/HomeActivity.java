package com.echowaves.pawall;

import android.os.Bundle;

import com.echowaves.pawall.core.PAWActivity;


public class HomeActivity extends PAWActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
    }

}
