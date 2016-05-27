package com.visint.manipale_amsler.ImageEffects;

/**
 * Created by Darshan on 29-03-2016.
 */
import android.graphics.Bitmap;

import java.util.Arrays;

class Filters {
    float xscale;
    float yscale;
    float xshift;
    float yshift;
    int[] s = new int[4];
    int[] s1 = new int[4];
    int[] s2 = new int[4];
    int[] s3 = new int[4];
    int[] s4 = new int[4];
    private String TAG = "Filters";
    long getRadXStart = 0;
    long getRadXEnd = 0;
    long startSample = 0;
    long endSample = 0;
    float xr;
    float yr;

    public Filters() {
        //Log.e(TAG, "***********inside filter constructor");
    }

    public Bitmap barrel(Bitmap input, float k) {
        //Log.e(TAG, "***********INSIDE BARREL METHOD ");

        float centerX = input.getWidth() / 2; //center of distortion
        float centerY = input.getHeight() / 2;

        int width = input.getWidth(); //image bounds
        int height = input.getHeight();

        Bitmap dst = Bitmap.createBitmap(width, height, input.getConfig()); //output pic
        // Log.e(TAG, "***********dst bitmap created ");
        xshift = calc_shift(0, centerX - 1, centerX, k);

        float newcenterX = width - centerX;
        float xshift_2 = calc_shift(0, newcenterX - 1, newcenterX, k);

        yshift = calc_shift(0, centerY - 1, centerY, k);

        float newcenterY = height - centerY;
        float yshift_2 = calc_shift(0, newcenterY - 1, newcenterY, k);

        xscale = (width - xshift - xshift_2) / width;
        //  Log.e(TAG, "***********xscale ="+xscale);
        yscale = (height - yshift - yshift_2) / height;
        //  Log.e(TAG, "***********yscale ="+yscale);
        //  Log.e(TAG, "***********filter.barrel() about to loop through bm");


        int origPixel;
        long startLoop = System.currentTimeMillis();
        for (int j = 0; j < dst.getHeight(); j++) {
            for (int i = 0; i < dst.getWidth(); i++) {
                origPixel = input.getPixel(i, j);
                getRadXStart = System.currentTimeMillis();
                getRadialXY(j, i, centerX, centerY, k);
                getRadXEnd = System.currentTimeMillis();

                sampleImage(input, xr, yr);

                int color = ((s[1] & 0x0ff) << 16) | ((s[2] & 0x0ff) << 8) | (s[3] & 0x0ff);
                //            System.out.print(i+" "+j+" \\");

                dst.setPixel(i, j,
                        (sqr(i - centerX) + sqr(j - centerY) <= 150 * 150)
                                ? color
                                : origPixel);
            }
        }

        return dst;
    }

    float sqr(float x) {
        return x * x;
    }
    float cube(float x) {
        return x * x * x;
    }

    void sampleImage(Bitmap arr, float idx0, float idx1) {
        startSample = System.currentTimeMillis();
        Arrays.fill(s, 0);
        if (idx0 < 0 || idx1 < 0 || idx0 > (arr.getHeight() - 1) || idx1 > (arr.getWidth() - 1)) {
            return;
        }

        float idx0_fl = (float) Math.floor(idx0);
        float idx0_cl = (float) Math.ceil(idx0);
        float idx1_fl = (float) Math.floor(idx1);
        float idx1_cl = (float) Math.ceil(idx1);

        getARGB(s1, arr, (int) idx0_fl, (int) idx1_fl);
        getARGB(s2, arr, (int) idx0_fl, (int) idx1_cl);
        getARGB(s3, arr, (int) idx0_cl, (int) idx1_cl);
        getARGB(s4, arr, (int) idx0_cl, (int) idx1_fl);

        float x = idx0 - idx0_fl;
        float y = idx1 - idx1_fl;

        //reordered for less multiplications
        for(int i = 0; i < 4; i++) {
            s[i] = (int) (s1[i] + x*(s4[i]-s1[i]) + y *(s2[i]-s1[i] + x*(s1[i]-s2[i]+s3[i]-s4[i])));
        }

        endSample = System.currentTimeMillis();
    }

    void getARGB(int[] scalar, Bitmap buf, int x, int y) {
        int rgb = buf.getPixel(y, x); // Returns by default ARGB.
        scalar[0] = (rgb >>> 24) & 0xFF;
        scalar[1] = (rgb >>> 16) & 0xFF;
        scalar[2] = (rgb >>> 8) & 0xFF;
        scalar[3] = rgb & 0xFF;
    }

    void getRadialXY(float x, float y, float cx, float cy, float k) {
        x = (x * xscale + xshift);
        y = (y * yscale + yshift);
        float f = k * (sqr(x - cx) + sqr(y - cy));
        xr = x + ((x - cx) * f);
        yr = y + ((y - cy) * f);
    }
    float thresh = 1;

    float calc_shift(float x1, float x2, float cx, float k) {

        float x3 = x1 + (x2 - x1) * 0.5f;
        float res1 = x1 + cube(x1 - cx) * k ;

        if (-thresh < res1 && res1 < thresh) {
            return x1;
        }

        float res3 = x3 + cube(x3 - cx) * k;
        return (res3 < 0)
                ? calc_shift(x3, x2, cx, k)
                : calc_shift(x1, x3, cx, k);
    }
}// end of filters class