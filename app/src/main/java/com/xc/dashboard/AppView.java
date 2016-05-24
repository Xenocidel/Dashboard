package com.xc.dashboard;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Aaron on 2016-05-23.
 */
public class AppView extends SurfaceView implements SurfaceHolder.Callback {
    public AppView(Context context) {
        super(context) ;
        this.context = context;
        getHolder (). addCallback(this);
        setFocusable(true);
        loaded = false;
    }

    Context context;
    boolean loaded;
    AppThread appThread;
    String speedString;

    public void update(){

    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

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
