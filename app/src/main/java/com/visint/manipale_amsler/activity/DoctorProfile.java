package com.visint.manipale_amsler.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.visint.manipale_amsler.R;
import com.visint.manipale_amsler.app.AppConfig;
import com.visint.manipale_amsler.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Joyel on 6/8/2016.
 */
public class DoctorProfile extends Activity {
    String tag_json_obj = "json_obj_req";
    String doc_id="1";
    TextView jfname,jlname,jdob,jmobile,jemail,jaddress,jgender,jqualification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);

        jfname=(TextView)findViewById(R.id.docfname);
        jlname=(TextView)findViewById(R.id.doclname);
        jdob=(TextView)findViewById(R.id.docdob);
        jmobile=(TextView)findViewById(R.id.docmobile);
        jemail=(TextView)findViewById(R.id.docemail);
        jaddress=(TextView)findViewById(R.id.docaddress);
        jgender=(TextView)findViewById(R.id.docgender);
        jqualification=(TextView)findViewById(R.id.docqualification);

        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.DOCTOR_PROFILE,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(DoctorProfile.this, response.toString(),Toast.LENGTH_LONG).show();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(!error)
                            {

                                // Fetching data for Doctor profile
                                JSONObject doctor = jsonObject.getJSONObject("doctor");
                                String f_name = doctor.getString("f_name");
                                jfname.setText(f_name);
                                String l_name = doctor.getString("l_name");
                                jlname.setText(l_name);
                                String dob = doctor.getString("dob");
                                jdob.setText(dob);
                                String phone = doctor.getString("phone");
                                jmobile.setText(phone);
                                String gender = doctor.getString("gender");
                                jgender.setText(gender);
                                String email = doctor.getString("email");
                                jemail.setText(email);
                                String qualification = doctor.getString("qualification");
                                jqualification.setText(qualification);
                                String address = doctor.getString("address");
                                jaddress.setText(address);

                            }
                            else
                            {
                                String errorMsg = jsonObject.getString("error_msg");
                                Toast.makeText(getApplicationContext(),errorMsg, Toast.LENGTH_LONG).show();
                            }


                        }

                        catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error:" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),"Unexcepted error, Please check your 3G/ WIFI connection availability or try after some time", Toast.LENGTH_LONG).show();
                // hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("doc_id",doc_id);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);




    }

    // Back pressed code to go to doctor dashboard
    @Override
    public void onBackPressed()
    {
        Intent docbackpress=new Intent(DoctorProfile.this,DoctorActivity.class);
        startActivity(docbackpress);
        finish();
    }
}
