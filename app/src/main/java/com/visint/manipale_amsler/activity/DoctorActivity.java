package com.visint.manipale_amsler.activity;

import android.content.Intent;
import android.os.Bundle;

import com.visint.manipale_amsler.R;
import com.visint.manipale_amsler.helper.SQLiteHandler;
import com.visint.manipale_amsler.helper.SessionManager;

import java.util.HashMap;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DoctorActivity extends Activity {

 /*   private TextView txtName;
    private TextView w;
    private TextView txtEmail;
    private Button btnLogout;
*/

    CardView jcarddpatients,jcarddappoint,jcarddhistory,jcarddprofile,jcarddlogout;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

    /*    txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        w = (TextView) findViewById(R.id.w);
        btnLogout = (Button) findViewById(R.id.btnLogout);*/

        jcarddpatients=(CardView)findViewById(R.id.card_view_dpatient);
        jcarddappoint=(CardView)findViewById(R.id.card_view_dappoint);
        jcarddhistory=(CardView)findViewById(R.id.card_view_dhistory);
        jcarddprofile=(CardView)findViewById(R.id.card_view_dprofile);
        jcarddlogout=(CardView)findViewById(R.id.card_view_dlogout);
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

        // Displaying the user details on the screen
      /*  txtName.setText(did);
        txtEmail.setText(name);*/
      //  w.setText(type);



        // Logout cardview click event
        jcarddlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

    }

    // Medical History Cardview click event



    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(DoctorActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
