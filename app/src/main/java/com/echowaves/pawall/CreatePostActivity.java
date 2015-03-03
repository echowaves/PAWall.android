package com.echowaves.pawall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.echowaves.pawall.core.PAWActivity;
import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;


public class CreatePostActivity extends PAWActivity {

    protected ParseGeoPoint currentLocation;
    private ImageButton backButton;
    private TextView locationLable;
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

        bodyText = (EditText) findViewById(R.id.createPost_bodyText);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AmericanTypewriter.ttc");
        bodyText.setTypeface(font);
        bodyText.setEnabled(false);

        locationLable = (TextView) findViewById(R.id.createPost_locationLable);

        ParseGeoPoint.getCurrentLocationInBackground(10000, new LocationCallback() {
            @Override
            public void done(ParseGeoPoint geoPoint, ParseException e) {
                if (e == null) {
                    // We were able to get the location
                    currentLocation = geoPoint;
                    locationLable.setText("Current Location Detected");
                    bodyText.setEnabled(true);
                } else {
                    // handle your error
                    locationLable.setText("Unable to detect current location. Make sure to enable GPS.");
                    locationLable.setBackgroundColor(Color.RED);
                    bodyText.setEnabled(false);
                }
            }
        });

        createPostButton = (ImageButton) findViewById(R.id.createPost_createPostButton);
        //Listening to button event
        createPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String text = bodyText.getText().toString();
                if (text.length() < 10) {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(CreatePostActivity.this);
                    alertDialogConfirmWaveDeletion.setTitle("Warning");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Your post can't be empty. Try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                    // show it
                    alertDialog.show();
                } else if (!text.contains("#")) {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(CreatePostActivity.this);
                    alertDialogConfirmWaveDeletion.setTitle("Warning");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("You post can not be saved without any #hash_tags. You post will not be searchable unless it has #hash_tags. Add some #has_tags and try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                    // show it
                    alertDialog.show();
                } else {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(CreatePostActivity.this);
//                    alertDialogConfirmWaveDeletion.setTitle("Warning");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("You Post will be saved now.")
                            .setCancelable(true)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                    // show it
                    alertDialog.show();
                }
            }
        });

    }

}
