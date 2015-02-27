package com.echowaves.pawall;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.echowaves.pawall.core.PAWActivity;


public class CreatePostActivity extends PAWActivity {

    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        backButton = (ImageButton) findViewById(R.id.createPost_backButton);
        //Listening to button event
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

    }

}
