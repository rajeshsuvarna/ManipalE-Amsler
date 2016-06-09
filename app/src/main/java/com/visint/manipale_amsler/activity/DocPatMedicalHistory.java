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
 * Created by Joyel on 6/9/2016.
 */
public class DocPatMedicalHistory  extends Activity {

    String tag_json_obj = "json_obj_req";
    String pat_id= "102";

    TextView jchiefcomplaint,jcomplaintdate,jsymptoms,jlocation,jseverity,jnatureofonset,jduration,
            jfrequency,jexcrebation,jaccompanyingsigns,jsecondaycomplaint,jocularhistory,jmedicalhistory,
            jmedicationhistory,jallergyhistory,jfamilyocularhistory,jfamilymedicalhistory,jmentalstatus;

    TextView jtestdate,jbvaod,jbvdos,jpupils,jeoms,jconforntationfeild,jslitlamplids,jiops,jfundusod,
            jfundusos,jbloodpressure,jpulse,jcholestrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        //Chief complaints data defination
        jchiefcomplaint=(TextView)findViewById(R.id.Chief_Complaint);
        jcomplaintdate=(TextView)findViewById(R.id.Complaint_Date);
        jsymptoms=(TextView)findViewById(R.id.Symptoms);
        jlocation=(TextView)findViewById(R.id.Location);
        jseverity=(TextView)findViewById(R.id.Severity);
        jnatureofonset=(TextView)findViewById(R.id.Nature_Of_Onset);
        jduration=(TextView)findViewById(R.id.Duration);
        jfrequency=(TextView)findViewById(R.id.Frequency);
        jexcrebation=(TextView)findViewById(R.id.Excerabation);
        jaccompanyingsigns=(TextView)findViewById(R.id.Accompanying_Signs);
        jsecondaycomplaint=(TextView)findViewById(R.id.Secondary_Compaint);
        jocularhistory=(TextView)findViewById(R.id.Ocular_History);
        jmedicalhistory=(TextView)findViewById(R.id.Medical_History);
        jmedicationhistory=(TextView)findViewById(R.id.Medication_History);
        jallergyhistory=(TextView)findViewById(R.id.Allergy_History);
        jfamilymedicalhistory=(TextView)findViewById(R.id.FamilyMedicalHistory);
        jmentalstatus=(TextView)findViewById(R.id.MentalStatus);

        //Patient Clinical data defination
        jtestdate=(TextView)findViewById(R.id.TestDate);
        jbvaod=(TextView)findViewById(R.id.BVAOD);
        jbvdos=(TextView)findViewById(R.id.BVDOS);
        jpupils=(TextView)findViewById(R.id.Puplis);
        jeoms=(TextView)findViewById(R.id.EOMs);
        jconforntationfeild=(TextView)findViewById(R.id.ConfrontationField);
        jslitlamplids=(TextView)findViewById(R.id.SlitLampLids);
        jiops=(TextView)findViewById(R.id.IOPs);
        jfundusod=(TextView)findViewById(R.id.FundusOD);
        jfundusos=(TextView)findViewById(R.id.FundusOS);
        jbloodpressure=(TextView)findViewById(R.id.BloodPressure);
        jpulse=(TextView)findViewById(R.id.Pulse);
        jcholestrol=(TextView)findViewById(R.id.Cholestrol);





        StringRequest jsonObjReq = new StringRequest(Request.Method.POST,
                AppConfig.PATIENT_HEALTH_RECORDS,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(DocPatMedicalHistory.this, response.toString(),Toast.LENGTH_LONG).show();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            boolean error = jsonObject.getBoolean("error");
                            if(!error)
                            {

                                // Fetching data for Chief complaints
                                JSONObject chief_complaint = jsonObject.getJSONObject("chief_complaint");
                                String chief_complain = chief_complaint.getString("chief_complaint");
                                jchiefcomplaint.setText(chief_complain);
                                String complaint_date = chief_complaint.getString("complaint_date");
                                jcomplaintdate.setText(complaint_date);
                                String symptoms = chief_complaint.getString("symptoms");
                                jsymptoms.setText(symptoms);
                                String location = chief_complaint.getString("location");
                                jlocation.setText(location);
                                String severity = chief_complaint.getString("severity");
                                jseverity.setText(severity);
                                String nature_of_onset = chief_complaint.getString("nature_of_onset");
                                jnatureofonset.setText(nature_of_onset);
                                String duration = chief_complaint.getString("duration");
                                jduration.setText(duration);
                                String frequency = chief_complaint.getString("frequency");
                                jfrequency.setText(frequency);
                                String excerbation = chief_complaint.getString("excerbation");
                                jexcrebation.setText(excerbation);
                                String accompany_sign = chief_complaint.getString("accompany_sign");
                                jaccompanyingsigns.setText(accompany_sign);
                                String secondary_complaint = chief_complaint.getString("secondary_complaint");
                                jsecondaycomplaint.setText(secondary_complaint);
                                String ocular_history = chief_complaint.getString("ocular_history");
                                jocularhistory.setText(ocular_history);
                                String medical_history = chief_complaint.getString("medical_history");
                                jmedicalhistory.setText(medical_history);
                                String medication_history = chief_complaint.getString("medication_history");
                                jmedicationhistory.setText(medication_history);
                                String allergy_history = chief_complaint.getString("allergy_history");
                                jallergyhistory.setText(allergy_history);
                                String family_ocular_history = chief_complaint.getString("family_ocular_history");
                                jocularhistory.setText(family_ocular_history);
                                String family_medical_history = chief_complaint.getString("family_medical_history");
                                jfamilymedicalhistory.setText(family_medical_history);
                                String mental_status = chief_complaint.getString("mental_status");
                                jmentalstatus.setText(mental_status);





                                // Fetching data for Clinical_Data

                                JSONObject clinical_data = jsonObject.getJSONObject("clinical_data");
                                String test_date = clinical_data.getString("test_date");
                                jtestdate.setText(test_date);
                                String BVA_od = clinical_data.getString("BVA_od");
                                jbvaod.setText(BVA_od);
                                //  String BVA_os = clinical_data.getString("BVA_os");
                                //jbvdos.setText(BVA_os);
                                String pupils = clinical_data.getString("pupils");
                                jpupils.setText(pupils);
                                String EOM = clinical_data.getString("EOM");
                                jeoms.setText(EOM);
                                String confrontation = clinical_data.getString("confrontation");
                                jconforntationfeild.setText(confrontation);
                                String slit_lamp = clinical_data.getString("slit_lamp");
                                jslitlamplids.setText(slit_lamp);
                                String IOP = clinical_data.getString("IOP");
                                jiops.setText(IOP);
                                String fundus_OD = clinical_data.getString("fundus_OD");
                                jfundusod.setText(fundus_OD);
                                String fundus_OS = clinical_data.getString("fundus_OS");
                                jfundusos.setText(fundus_OS);
                                String blood_pressure = clinical_data.getString("blood_pressure");
                                jbloodpressure.setText(blood_pressure);
                                String pulse = clinical_data.getString("pulse");
                                jpulse.setText(pulse);
                                String cholestrol = clinical_data.getString("cholestrol");
                                jcholestrol.setText(cholestrol);


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
                params.put("pat_id",pat_id);

                return params;
            }
        };



        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    // Back pressed code to go to Doctor dashboard
    @Override
    public void onBackPressed()
    {
        Intent medichisback=new Intent(DocPatMedicalHistory.this,DoctorActivity.class);
        startActivity(medichisback);
        finish();
    }
}