package com.xc.dashboard;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by Aaron on 2016-05-23.
 */
public class AppThread extends Thread {
    boolean threadLoaded;
    AppView appView;
    private AppState appState;
    public enum AppState{LOADING, CAMERA, RECORD};

    public AppThread(AppView appView){
        this.appView = appView;
        threadLoaded = false;
        appState = AppState.LOADING;
    }

    public void run(){
        SurfaceHolder sh = appView.getHolder();
        Canvas c;
        while (!Thread.interrupted()){
            switch (appState){
                case LOADING:

                    break;
                case CAMERA:

                    break;
                case RECORD:

                    break;
            }
        }
    }

    public void setAppState(AppState appState){
        this.appState = appState;
    }
    public AppState getAppState(){
        return appState;
    }
}
