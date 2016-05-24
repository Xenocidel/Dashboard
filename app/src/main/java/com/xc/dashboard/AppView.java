package com.xc.dashboard;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by Aaron on 2016-05-23.
 */
public class AppView extends SurfaceView implements SurfaceHolder.Callback {
    public AppView(final Context context) {
        super(context) ;
        this.context = context;
        getHolder (). addCallback(this);
        setFocusable(true);
        loaded = false;
        jerkCounter = 0;
        jerkNotify = -1;
        /*jerkDetectionEnable = (Switch)findViewById(R.id.switch1);
        jerkDetectionEnable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ((MainActivity)context).loadSensor();
                } else {
                    ((MainActivity)context).unloadSensor();
                }
            }
        });*/
        p = new Paint();
    }

    Context context;
    Switch jerkDetectionEnable;
    boolean loaded;
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
            accelString = "!";
            Log.i("jerk", ""+accelZ);
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
        switch(appThread.getAppState()){
            case LOADING:

                break;
            case CAMERA:
                canvas.drawColor(Color.BLACK);
                p.setTextSize(getHeight()/8);
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
        }

    }

    @Override
    public void surfaceCreated ( SurfaceHolder holder ) {
        // Launch animator thread
        if (!loaded) {
            appThread = new AppThread(this);
            appThread.start();
            loaded = true;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
