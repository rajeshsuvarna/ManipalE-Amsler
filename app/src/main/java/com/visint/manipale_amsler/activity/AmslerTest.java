package com.visint.manipale_amsler.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.visint.manipale_amsler.ImageEffects.MultiRuntimeProcessorFilter;
import com.visint.manipale_amsler.R;
import com.visint.manipale_amsler.app.AppConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;

/**
 * Created by Darshan on 26-05-2016.
 */
public class AmslerTest extends AppCompatActivity {
    private static final int ZERORADIUS = 5;
    CanvasView cv;
    Button jclear, jrogue;
    RadioGroup jrgroup;
    SeekBar jseek;
    TextView jinstruction;

    private static String A_SCORE = "score";
    private static String IMAGE = "image";
    private static String FILENAME = "filename";
    private static String DATE = "test_date";
    private static String CHIEF_COMPLAINT_ID = "idChief_Complaint";
    private static String CLINIC_TEST_ID = "idClinic_Test";
    private static String PATIENT_ID = "idPatient";

    RadioButton[] rbtn;

    Bitmap bm;

    public String tempPhotoPath, rogueState = "";

    int globalscore, md, prog = 1;

    String mCurrentPhotoPath;

    String fname;

    public static int RADIUS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amsler_test);

        cv = (CanvasView) findViewById(R.id.signature_canvas);
        jclear = (Button) findViewById(R.id.clear);
        jrogue = (Button) findViewById(R.id.btn_action_rogue);
        jrgroup = (RadioGroup) findViewById(R.id.rgroup);
        jseek = (SeekBar) findViewById(R.id.seekBar);

        rbtn = new RadioButton[]{(RadioButton) findViewById(R.id.blur),
                (RadioButton) findViewById(R.id.darkspot),
                (RadioButton) findViewById(R.id.wavy),
                (RadioButton) findViewById(R.id.distort)};

        jinstruction = (TextView) findViewById(R.id.instruction);

        rogueState = jrogue.getText().toString();

        final int ori_alpha = cv.mPaint.getAlpha();

        jclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbtn[1].setChecked(true);
                clearCanvas(v);
                clearDistort();
                //radioController(1, 5);
            }
        });

        cv.setVisibility(View.INVISIBLE);
        jclear.setVisibility(View.INVISIBLE);
        jseek.setVisibility(View.INVISIBLE);
        jrgroup.setVisibility(View.INVISIBLE);
        jinstruction.setText("Click START TEST to begin the test");

        jrogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (rogueState.equals("START TEST")) {
                    cv.setVisibility(View.VISIBLE);
                    jclear.setVisibility(View.VISIBLE);
                    jseek.setVisibility(View.VISIBLE);
                    jrgroup.setVisibility(View.VISIBLE);

                    rbtn[1].setChecked(true);

                    rogueState = "FINISH";
                    jrogue.setText("FINISH");
                    //radioController(1, 5);

                    jinstruction.setText("Choose an Effect and Draw on Grid");
                } else if (rogueState.equals("FINISH")) {

                    cv.setVisibility(View.INVISIBLE);
                    jclear.setVisibility(View.INVISIBLE);
                    jseek.setVisibility(View.INVISIBLE);
                    jrgroup.setVisibility(View.INVISIBLE);

                    Bitmap bm = cv.getBitmap();
                    saveImage(bm);

                    globalscore = (int) (cv.score / 100);

                    String encoded_image_string = getStringImage(bm);

                    uploadImage(String.valueOf(globalscore), encoded_image_string, fname);

                    galleryAddPic();

                    jinstruction.setText("Click START TEST to begin the test");

                    rogueState = "START TEST";
                    jrogue.setText("START TEST");
                    clearCanvas(v);
                    clearDistort();
                }
                // Toast.makeText(AmslerTest.this, String.valueOf(globalscore), Toast.LENGTH_SHORT).show();
            }
        });

        jrgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId) {

                                                   jseek.setVisibility(View.INVISIBLE);

                                                   bm = cv.getBitmap();
                                                   grabNchange(bm);

                                                   switch (checkedId) {
                                                       case R.id.blur:

                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);

                                                           cv.clearCanvas();

                                                           cv.mPaint.setColor(Color.BLACK);
                                                           cv.mPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
                                                           cv.mPaint.setAlpha(100);
                                                           jseek.setVisibility(View.VISIBLE);

                                                           break;

                                                       case R.id.darkspot:

                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);

                                                           cv.clearCanvas();

                                                           cv.mPaint.setColor(Color.BLACK);
                                                           cv.mPaint.setMaskFilter(null);
                                                           cv.mPaint.setAlpha(ori_alpha);
                                                           jseek.setVisibility(View.VISIBLE);

                                                           break;

                                                       case R.id.wavy:

                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);

                                                           cv.clearCanvas();

                                                           cv.mPaint.setMaskFilter(null);
                                                           cv.mPaint.setAlpha(ori_alpha);
                                                           cv.mPaint.setColor(Color.RED);
                                                           jseek.setVisibility(View.VISIBLE);
                                                           break;

                                                       case R.id.distort:

                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);
                                                           jseek.setVisibility(View.VISIBLE);

                                                           new AsyncTask<Void, Void, String>() {
                                                               MultiRuntimeProcessorFilter mFilers = new MultiRuntimeProcessorFilter();

                                                               Bitmap bitmapSP;

                                                               protected void onPreExecute() {
                                                               }

                                                               protected String doInBackground(Void... params) {

                                                                   Bitmap bitmap1 = BitmapFactory.decodeFile(tempPhotoPath);
                                                                   bitmapSP = mFilers.barrel(bitmap1, 0.00005f, RADIUS);
                                                                   return "message";
                                                               }

                                                               protected void onPostExecute(String msg) {

                                                                   Drawable drawable = new BitmapDrawable(getResources(), bitmapSP);
                                                                   cv.setBackground(drawable);

                                                               }
                                                           }.execute();

                                                           break;
                                                   }
                                               }
                                           }
        );

        jseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                             @Override
                                             public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                                 prog = progress;
                                             }

                                             @Override
                                             public void onStartTrackingTouch(SeekBar seekBar) {
                                             }

                                             @Override
                                             public void onStopTrackingTouch(SeekBar seekBar) {
                                                 cv.mPaint.setStrokeWidth(prog);
                                                 RADIUS = prog;
                                             }
                                         }
        );

    }

    // onBackPressed directs you to home screen of patient activity
    @Override
    public void onBackPressed() {

        Intent backpressed = new Intent(AmslerTest.this, PatientActivity.class);
        startActivity(backpressed);

    }


    private void radioController(int toggle, int id) {
        if (toggle == 0) {
            for (int i = 0; i <= 3; i++) {
                rbtn[i].setVisibility(View.INVISIBLE);
            }
            rbtn[id].setVisibility(View.VISIBLE);
        } else if (toggle == 1 & id == 5) {
            for (int i = 0; i <= 3; i++) {
                rbtn[i].setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearDistort() {

        new AsyncTask<Void, Void, String>() {
            MultiRuntimeProcessorFilter mFilers = new MultiRuntimeProcessorFilter();

            Bitmap bitmapSP;

            protected void onPreExecute() {
            }

            protected String doInBackground(Void... params) {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),
                        R.drawable.amslergrid);
                bitmapSP = mFilers.barrel(bitmap1, 0.00005f, ZERORADIUS);
                return "message";
            }

            protected void onPostExecute(String msg) {
                Drawable drawable = new BitmapDrawable(getResources(), bitmapSP);
                cv.setBackground(drawable);
            }
        }.execute();

    }

    public void clearCanvas(View v) {
        cv.clearCanvas();
    }

    void saveImage(Bitmap img) {

        String RootDir = Environment.getExternalStorageDirectory()
                + File.separator + "Visint";

        File myDir = new File(RootDir);

        myDir.mkdirs();

        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n); // replace with testid

        fname = "Test" + n + ".jpg";

        File file = new File(myDir, fname);

        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            img.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCurrentPhotoPath = file.getAbsolutePath();

        //Toast.makeText(AmslerTest.this, "Image saved to 'Visint' folder", Toast.LENGTH_SHORT).show();
    }

    private void galleryAddPic() {
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    public void grabNchange(Bitmap bit) {

        String RootDir = Environment.getExternalStorageDirectory()
                + File.separator + "Visint/temp";

        File myDir = new File(RootDir);
        myDir.mkdirs();

        String fname = "temp.jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bit.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tempPhotoPath = file.getAbsolutePath();

        Bitmap myBitmap = BitmapFactory.decodeFile(tempPhotoPath);
        Drawable drawable = new BitmapDrawable(getResources(), myBitmap);
        cv.setBackground(drawable);

        cv.postInvalidate();

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        //Toast.makeText(AmslerTest.this, encodedImage, Toast.LENGTH_LONG).show();

        return encodedImage;
    }

    private void uploadImage(final String a_score,final String image,final String f_name) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConfig.UPLOAD_TEST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        //Showing toast message of the response
                        Toast.makeText(AmslerTest.this, s, Toast.LENGTH_LONG).show();
                        Toast.makeText(AmslerTest.this, "Test has been Saved", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        //Showing toast
                        Toast.makeText(AmslerTest.this,""+ volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(A_SCORE, a_score);
                params.put(IMAGE, image);
                params.put(FILENAME, f_name);
                params.put(DATE, Calendar.getInstance().getTime().toString());
                params.put(CHIEF_COMPLAINT_ID, "1");
                params.put(CLINIC_TEST_ID, "1");
                params.put(PATIENT_ID, "100");

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}