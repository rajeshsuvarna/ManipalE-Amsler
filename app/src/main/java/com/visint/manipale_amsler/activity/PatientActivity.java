package com.visint.manipale_amsler.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.visint.manipale_amsler.R;
import com.visint.manipale_amsler.helper.SQLiteHandler;
import com.visint.manipale_amsler.helper.SessionManager;

import java.util.HashMap;

/**
 * Created by MAHE on 5/23/2016.
 */
public class PatientActivity extends Activity {
 /*   private TextView txtName;
    private TextView w;
    private TextView txtEmail;
    private Button btnLogout; */

    CardView jcardpamsler,jcardpappoint,jcardpchat,jcardphistory,jcardplogout;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

     /*   txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        w = (TextView) findViewById(R.id.w);
        btnLogout = (Button) findViewById(R.id.btnLogout); */

        jcardpamsler=(CardView)findViewById(R.id.card_view_pamsler);
        jcardpappoint=(CardView)findViewById(R.id.card_view_pappoint);
        jcardpchat=(CardView)findViewById(R.id.card_view_pchat);
        jcardphistory=(CardView)findViewById(R.id.card_view_phistory);
        jcardplogout=(CardView)findViewById(R.id.card_view_plogout);
        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();
        String did = user.get("lid");
        String name = user.get("email");
        String type = user.get("utype");
        //   String did = user.get("lid");

    /*    // Displaying the user details on the screen
        txtName.setText(did);
        txtEmail.setText(name);
       // w.setText(type);  */


        // Amsler Cardview click event
        jcardpamsler.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent amslertest= new Intent(PatientActivity.this,AmslerTest.class);
                startActivity(amslertest);
            }
        });


        // Chat with doctor Cardview click event
        jcardpchat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent amslertest= new Intent(PatientActivity.this,ChatActivity.class);
                startActivity(amslertest);
            }
        });



        // Book an appointment with doctor Cardview click event
        jcardpappoint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent patdocappoint= new Intent(PatientActivity.this,AppointmentActivity.class);
                startActivity(patdocappoint);
            }
        });
   // Logout Cardview click event
        jcardplogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // Appointment Cardview click event
        jcardpappoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent appoint= new Intent(PatientActivity.this,AppointmentActivity.class);
                startActivity(appoint);
            }
        });


        // Medical History Cardview click event
        jcardphistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent medhistory= new Intent(PatientActivity.this,MedicalHistory.class);
                startActivity(medhistory);
            }
        });


    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(PatientActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}