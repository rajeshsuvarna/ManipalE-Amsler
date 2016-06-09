package com.visint.manipale_amsler.activity;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.visint.manipale_amsler.R;
import com.visint.manipale_amsler.app.AppConfig;
import com.visint.manipale_amsler.app.AppController;
import com.visint.manipale_amsler.helper.SQLiteHandler;
import com.visint.manipale_amsler.helper.SessionManager;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;


    private Button btnLogin;
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputUsername = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn())
        {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this,AmslerTest.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = inputUsername.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                // Check for empty data in the form
                if (!username.isEmpty() && !password.isEmpty())
                {
                    // login user
                    checkLogin(username, password);
                }
                else
                {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),"Please enter credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }



    //Multi language menu inflater

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        //  getMenuInflater().inflate(R.menu.lang_setting_menu, menu);
        //Toast.makeText(getApplicationContext(),"Menu inflates",Toast.LENGTH_LONG).show();
        //  return true;
        return super.onCreateOptionsMenu(menu);
    }

    //Multi language Menu options

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.eng:
              //  Toast.makeText(getApplicationContext(),"English selected",Toast.LENGTH_LONG).show();
                String languageToLoad = "en"; // your language
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            case R.id.hin:
               // Toast.makeText(getApplicationContext(),"Hindi selected",Toast.LENGTH_LONG).show();
                languageToLoad = "hi"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            case R.id.kann:
              //  Toast.makeText(getApplicationContext(),"Kannada selected",Toast.LENGTH_LONG).show();
                languageToLoad = "kan"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            case R.id.telu:
              //  Toast.makeText(getApplicationContext(),"Telugu selected",Toast.LENGTH_LONG).show();
                languageToLoad = "tel"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            case R.id.tami:
                //Toast.makeText(getApplicationContext(),"Tamil selected",Toast.LENGTH_LONG).show();
                languageToLoad = "tam"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            case R.id.malya:
               // Toast.makeText(getApplicationContext(),"Malyalam selected",Toast.LENGTH_LONG).show();
                languageToLoad = "mal"; // your language
                locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                this.setContentView(R.layout.activity_login);
                this.recreate();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String username, final String password)
    {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,AppConfig.URL_LOGIN, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    // Check for error node in json
                    if (!error)
                    {
                        // user successfully logged in
                        // Create login session
                        session.setLogin(true);

                        // Now store the user in SQLite
                      //  String lid = jObj.getString("idLogin");

                        JSONObject user = jObj.getJSONObject("user");
                        String idLogin = user.getString("idLogin");
                        String username= user.getString("username");
                        String utype = user.getString("type");
                      //  Toast.makeText(LoginActivity.this,"gg "+utype,Toast.LENGTH_SHORT).show();

                       // String created_at = user.getString("created_at");

                        // Inserting row in users table
                        db.addUser(idLogin,username,utype);
                        // Launch main activity based on doctor or patient

                        String d = "D";
                        String p ="P";



                        if(utype.equals(d)) {

                            Intent intent = new Intent(LoginActivity.this, DoctorActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else  if (utype.equals(p)){
                            Intent intent = new Intent(LoginActivity.this, PatientActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }



                    }
                    else
                    {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                    }
                }
                catch (JSONException e)
                {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Unexcepted error, Please check your 3G/ WIFI connection availability or try after some time", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }





}
