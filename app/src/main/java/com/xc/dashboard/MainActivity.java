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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AppView appView;
    private Camera mCamera;
    private Camera.Parameters mParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d("1a", "onCreate");

        appView = new AppView(this);
        setContentView(appView);

        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume() {
        super.onResume();
        loadSensor();
    }

    protected void onPause() {
        super.onPause();
        unloadSensor();
    }

    public void loadSensor(){
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        try{
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(appView.getHolder());
            mCamera.startPreview();
            mParams = mCamera.getParameters();
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, "Camera Error! Exiting...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void unloadSensor(){
        mSensorManager.unregisterListener(this);
        mCamera.release();
        mCamera = null;
        Log.i("Dashboard", "Sensors and Camera Unloaded");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
            return;
        }
        appView.accelX = event.values[0];
        appView.accelY = event.values[1];
        appView.accelZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
