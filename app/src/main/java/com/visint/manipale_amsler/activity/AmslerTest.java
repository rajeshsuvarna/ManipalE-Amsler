package com.visint.manipale_amsler.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.visint.manipale_amsler.ImageEffects.MultiRuntimeProcessorFilter;
import com.visint.manipale_amsler.R;

import java.io.File;
import java.io.FileOutputStream;
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

    RadioButton[] rbtn;

    public String tempPhotoPath;

    int globalscore;

    String mCurrentPhotoPath;

    public static int RADIUS = 100;

    int prog = 1;

    public String rogueState = "";

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

                    galleryAddPic();

                    globalscore = (int) (cv.score / 100);

                    jinstruction.setText("Click START TEST to begin the test");

                    rogueState = "START TEST";
                    jrogue.setText("START TEST");
                }


                Toast.makeText(AmslerTest.this, String.valueOf(globalscore), Toast.LENGTH_SHORT).show();
            }
        });


        jrgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                               @Override
                                               public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                   Bitmap bm;
                                                   jseek.setVisibility(View.INVISIBLE);
                                                   switch (checkedId) {
                                                       case R.id.blur:
                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);
                                                           cv.mPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
                                                           cv.mPaint.setAlpha(100);
                                                           jseek.setVisibility(View.VISIBLE);
                                                           //radioController(0, 0);
                                                           break;

                                                       case R.id.darkspot:
                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);
                                                           cv.mPaint.setMaskFilter(null);
                                                           cv.mPaint.setAlpha(ori_alpha);
                                                           jseek.setVisibility(View.VISIBLE);
                                                           //radioController(0, 1);
                                                           break;

                                                       case R.id.wavy:
                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);
                                                           //radioController(0, 2);
                                                           break;

                                                       case R.id.distort:
                                                           bm = cv.getBitmap();
                                                           grabNchange(bm);
                                                           jseek.setVisibility(View.VISIBLE);
                                                           //radioController(0, 3);
                                                           new AsyncTask<Void, Void, String>() {
                                                               MultiRuntimeProcessorFilter mFilers = new MultiRuntimeProcessorFilter();

                                                               Bitmap bitmapSP;

                                                               protected void onPreExecute() {
                                                               }

                                                               protected String doInBackground(Void... params) {
                                                                   Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.amslergrid);
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

        jseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()

                                         {
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

        String fname = "Test" + n + ".jpg";

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

        Toast.makeText(AmslerTest.this, "Image saved to 'Visint' folder", Toast.LENGTH_SHORT).show();
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

    }

}
