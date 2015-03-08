package com.echowaves.pawall;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.echowaves.pawall.core.PAWActivity;
import com.echowaves.pawall.core.PAWApplication;
import com.echowaves.pawall.model.GPost;
import com.echowaves.pawall.model.PAWModelCallback;


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

        bodyText = (EditText) findViewById(R.id.createPost_bodyText);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AmericanTypewriter.ttc");
        bodyText.setTypeface(font);

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

                                    GPost.createPost(
                                            bodyText.getText().toString(),
                                            PAWApplication.getInstance().getCurrentLocation(),
                                            PAWApplication.getInstance().getUUID(),
                                            new PAWModelCallback() {
                                                @Override
                                                public void succeeded(Object result) {
                                                }

                                                @Override
                                                public void failed(com.parse.ParseException e) {
                                                    AlertDialog.Builder alertDialogConfirmWaveDeletion = new AlertDialog.Builder(CreatePostActivity.this);
                                                    alertDialogConfirmWaveDeletion.setTitle("Error");

                                                    // set dialog message
                                                    alertDialogConfirmWaveDeletion
                                                            .setMessage("Unable to post. Try again.")
                                                            .setCancelable(false)
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                }
                                                            });
                                                    // create alert dialog
                                                    AlertDialog alertDialog = alertDialogConfirmWaveDeletion.create();
                                                    // show it
                                                    alertDialog.show();
                                                    finish();
                                                }
                                            }
                                    );
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
