package com.xc.dashboard;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

/**
 * Created by Aaron on 2016-05-23.
 */
public class AppView extends SurfaceView implements SurfaceHolder.Callback {

    public AppView(final Context context) {
        this(context, null);
    }

    public AppView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppView(Context context, AttributeSet attrs, int defStyle) {
        super(context) ;
        Log.d("AppView", "AppView constructed");
        this.context = context;
        getHolder (). addCallback(this);
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        setFocusable(true);
        ((MainActivity)context).appView = this;
        loaded = false;
        jerkCounter = 0;
        jerkNotify = -1;
        p = new Paint();
    }



   // public Camera mCamera;
    Context context;
    Switch jerkDetectionEnable;
    boolean loaded;
    boolean metric;
    AppThread appThread;
    Paint p;
    String speedString;
    String accelString;
    float speed;
    float accelX;
    float accelY;
    float accelZ;
    final int accelThreshold = 8;   //high acceleration if sensor reports above accelThreshold
    final int jerkThreshold = 10;
    int jerkCounter; //determines hard jerk if below jerkThreshold
    int jerkNotify; //queue number of frames to display a jerk event

    public void update(){
        if (!loaded){
            appThread = new AppThread(this);
            loaded = true;
        }

        if (Math.abs(accelZ) > accelThreshold && (Math.abs(accelX) > accelThreshold || Math.abs(accelY) > accelThreshold)) {
            accelString = "High Motion Detected!";
            //Log.i("jerk", ""+accelZ);
            jerkCounter++;
        }
        else{
            accelString = "";
            if (jerkCounter < jerkThreshold){
                jerkNotify = 10;
            }
            jerkCounter = -1;
        }
        if (jerkNotify >= 0){
            jerkNotify--;
        }
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        update();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        p.setTextSize(getHeight() / 12);
        p.setTextAlign(Paint.Align.RIGHT);
        p.setAntiAlias(true);
        if (jerkNotify >= 0){
            p.setColor(Color.RED);
            canvas.drawText(accelString, getWidth()-10, getHeight()/8, p);
        }

        //temporary
        speed = 1.0f;

        if (!metric){
            speedString = "Speed: "+ (int)(speed*=2.23693629);
        }
        else{
            speedString = "Speed: "+ (int)(speed*=3.6);
        }
        p.setColor(Color.BLACK);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(speedString, 10, getHeight()/12, p);

            /*switch(appThread.getAppState()){
            case LOADING:

                break;
            case CAMERA:
                //canvas.drawColor(Color.BLACK);
                ToggleButton metric = (ToggleButton)findViewById(R.id.metric);
                if (metric.isChecked()){
                    canvas.drawColor(Color.LTGRAY);
                }
                else{
                    canvas.drawColor(Color.RED);
                }
                p.setTextSize(getHeight() / 12);
                p.setTextAlign(Paint.Align.LEFT);
                p.setAntiAlias(true);
                if (jerkNotify >= 0){
                    p.setColor(Color.RED);
                    canvas.drawText(accelString, 10, getHeight()/8+10, p);
                }
                //jerkDetectionEnable.draw(canvas);
                break;
            case RECORD:
                break;
        }*/

    }

    @Override
    public void surfaceCreated ( SurfaceHolder holder ) {
        // Launch animator thread
        if (!loaded) {
            appThread = new AppThread(this);
            appThread.start();
            //loadCamera(holder);
            loaded = true;
        }
    }

    /*public void loadCamera(SurfaceHolder holder){
        mCamera = Camera.open();

        try {
                mCamera.setPreviewDisplay(holder); // problem is here
                Log.i("Dashboard", "Camera display preview set");
                mCamera.startPreview();
                Log.i("Camera", "Loaded");
            } catch (Exception e) {
                Toast t = Toast.makeText(context, "Camera Error! Exiting...", Toast.LENGTH_SHORT);
                t.show();
                Log.d("Camera", "Error " + e.toString());
            mCamera.release();
            mCamera = null;
                ((MainActivity) context).finish();
            }

    }*/

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       /* if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }*/
    }
}
