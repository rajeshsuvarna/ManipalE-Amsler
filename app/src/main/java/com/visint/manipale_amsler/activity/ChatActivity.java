package com.visint.manipale_amsler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.visint.manipale_amsler.R;

/**
 * Created by Darshan on 28-05-2016.
 */
public class ChatActivity extends Activity {

    private Button jsend;
    private EditText jmsg;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        jmsg = (EditText) findViewById(R.id.txtmessage);
        jsend = (Button) findViewById(R.id.btnsendmessage);


        jsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = jmsg.getText().toString().trim();

                if (!message.equals("")) {
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, message);
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Snackbar.make(coordinatorLayout, "WhatsApp has not been installed !", Snackbar.LENGTH_LONG).show();
                    }
                } else {

                    Snackbar.make(coordinatorLayout, "Please enter a message", Snackbar.LENGTH_SHORT).show();
                }

            }
        });


    }
}
