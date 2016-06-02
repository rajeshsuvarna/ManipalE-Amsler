package com.visint.manipale_amsler.activity;

/**
 * Created by Darshan on 17-02-2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class CanvasView extends View {

    ArrayList<Integer> zoneList, scoreList;

    int defect1 = 1, defect2 = 2, defect3 = 3, defect4 = 4, score = 0;

    int finalScore;

    // 1 is enabled 0 is disabled
    public int mode;

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    public Path mPath;
    Context context;
    public Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    public CanvasView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10f);
        // mPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));
        // mPaint.setAlpha(60);

        scoreList = new ArrayList<Integer>();
    }

    // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        height = h;
        width = w;
        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawBitmap(mBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));
    }

    // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw

        canvas.drawPath(mPath, mPaint);


        // canvas.drawText("hi",mX,mY,mPaint);
    }

    // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }
    public void invalid()
    {
        invalidate();
    }

    public Bitmap getBitmap() {
        //this.measure(100, 100);
        //this.layout(0, 0, 100, 100);
        this.setDrawingCacheEnabled(true);
        this.buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(this.getDrawingCache());
        this.setDrawingCacheEnabled(false);
        return bmp;
    }

    // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);

                zoneList = new ArrayList<Integer>();
                zoneList.add(getZoneLocation((int) x, (int) y));
                // zoneInfo.setText("Current Score is " + score);

                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);

                zoneList.add(getZoneLocation((int) x, (int) y));

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                upTouch();

                int sum = 0;
                for (int i : zoneList) {
                    sum += i;
                }

                int avg = Math.round((float) sum / zoneList.size());

                score = (defect1 + defect2 + defect3 + defect4)
                        * avg
                        * ((defect1 * avg) + (defect2 * avg) + (defect3 * avg) + (defect4 * avg))
                        + 1;

                scoreList.add(score);

                int scoreSum = 0;
                for (int i : scoreList) {
                    scoreSum += i;
                }

                //zoneInfo.setText("Current Score is " + scoreSum);

                invalidate();
                break;
        }
        return true;
    }


    public Rect getCoordinateRect() {
        return new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
    }

    int getZoneLocation(int x, int y) {

        Rect rectangle = new Rect();

        rectangle = getCoordinateRect();

        // Zone 1 Rectangle

        Rect outerMostRect = new Rect(rectangle);

        int xOffset = rectangle.width() / 10;
        int yOffset = rectangle.height() / 10;


        // Zone 2 Rectangle

        Rect zone2Rectangle = new Rect(outerMostRect.left + xOffset,
                outerMostRect.top + yOffset, outerMostRect.right - xOffset,
                outerMostRect.bottom - yOffset);


        // Zone 3 Rectangle

        Rect zone3Rectangle = new Rect(zone2Rectangle.left + xOffset,
                zone2Rectangle.top + yOffset, zone2Rectangle.right
                - xOffset, zone2Rectangle.bottom - yOffset);

        // Zone 4 Rectangle

        Rect zone4Rectangle = new Rect(zone3Rectangle.left + xOffset,
                zone3Rectangle.top + yOffset, zone3Rectangle.right
                - xOffset, zone3Rectangle.bottom - yOffset);

        // Zone 5 Rectangle
        Rect zone5Rectangle = new Rect(zone4Rectangle.left + xOffset,
                zone4Rectangle.top + yOffset, zone4Rectangle.right
                - xOffset, zone4Rectangle.bottom - yOffset);

        // Check from inside out for point existence
        if (zone5Rectangle.contains(x, y)) {
            return 5;
        } else if (zone4Rectangle.contains(x, y)) {
            return 4;
        } else if (zone3Rectangle.contains(x, y)) {
            return 3;
        } else if (zone2Rectangle.contains(x, y)) {
            return 2;
        } else if (outerMostRect.contains(x, y)) {
            return 1;
        }
        return -1;
    }


    public void calc() {
        finalScore = 0;

        for (int i : scoreList) {
            finalScore += i;
        }


    }
}