package com.xc.dashboard;

import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public AppView appView;

    private Camera mCamera = null;
    private CameraView mCameraView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    protected void onResume() {
        super.onResume();
        loadSensor();
        loadCamera();
        loadOverlay();
    }

    protected void onPause() {
        super.onPause();
        unloadSensor();
    }

    public void loadSensor(){
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void loadCamera(){
        try{
            mCamera = Camera.open();
        } catch (Exception e) {
            Toast.makeText(this, "Error opening camera!", Toast.LENGTH_LONG).show();
        }
        if(mCamera != null) {
            mCameraView = new CameraView(this, mCamera);
            FrameLayout camera_view = (FrameLayout)findViewById(R.id.camera_view);
            camera_view.addView(mCameraView);
        }
    }

    public void loadOverlay(){
        Log.d("AppView", "Load Overlay");
        appView = new AppView(this);
        FrameLayout appView_ = (FrameLayout) findViewById(R.id.appview);
        if (appView != null) {
            appView_.addView(appView);
        }
    }

    public void unloadSensor(){
        mSensorManager.unregisterListener(this);
        Log.i("Dashboard", "Sensors Unloaded");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
            return;
        }
        if (appView != null) {
            appView.accelX = event.values[0];
            appView.accelY = event.values[1];
            appView.accelZ = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void toggleMetric(View button){
        appView.metric = ((ToggleButton)button).isChecked();
    }
}
