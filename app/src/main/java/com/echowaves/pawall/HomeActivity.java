package com.echowaves.pawall;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.echowaves.pawall.core.PAWActivity;
import com.echowaves.pawall.core.PAWApplication;


public class HomeActivity extends PAWActivity {

    private ImageButton searchButton;
    private ImageButton createPostButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();

        searchButton = (ImageButton) findViewById(R.id.home_searchButton);
        //Listening to button event
//        createPostButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                Intent createWave = new Intent(ApplicationContextProvider.getContext(), SignUpActivity.class);
//                startActivity(createWave);
//            }
//        });


        createPostButton = (ImageButton) findViewById(R.id.home_createPostButton);
        //Listening to button event
        createPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent createPostIntent = new Intent(PAWApplication.getInstance(), CreatePostActivity.class);
                startActivity(createPostIntent);
            }
        });


    }

}
