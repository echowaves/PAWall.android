package com.echowaves.pawall;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.echowaves.pawall.core.PAWActivity;


public class CreatePostActivity extends PAWActivity {

    private ImageButton backButton;

    private EditText bodyText;
    private ImageButton createPostButton;

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

        createPostButton = (ImageButton) findViewById(R.id.createPost_createPostButton);
        //Listening to button event
        createPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        bodyText = (EditText) findViewById(R.id.createPost_bodyText);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AmericanTypewriter.ttc");
        bodyText.setTypeface(font);

        bodyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17.f);
    }

}
