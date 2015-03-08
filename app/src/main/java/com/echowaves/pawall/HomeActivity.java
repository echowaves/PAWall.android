package com.echowaves.pawall;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(PAWApplication.getInstance().getCurrentLocation() == null) {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(HomeActivity.this);
                    alertDialogConfirmWaveDeletion.setTitle("Error");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Enable GPS, or wait few seconds for location to be detected and try again.")
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
                    Intent navTabBarIntent = new Intent(PAWApplication.getInstance(), PAWallTabBarActivity.class);
                    startActivity(navTabBarIntent);
                }
            }
        });


        createPostButton = (ImageButton) findViewById(R.id.home_createPostButton);
        //Listening to button event
        createPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(PAWApplication.getInstance().getCurrentLocation() == null) {
                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(HomeActivity.this);
                    alertDialogConfirmWaveDeletion.setTitle("Error");

                    // set dialog message
                    alertDialogConfirmWaveDeletion
                            .setMessage("Enable GPS, or wait few seconds for location to be detected and try again.")
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
                    Intent createPostIntent = new Intent(PAWApplication.getInstance(), CreatePostActivity.class);
                    startActivity(createPostIntent);
                }
            }
        });


    }

}
