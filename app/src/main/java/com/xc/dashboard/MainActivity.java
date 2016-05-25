package com.xc.dashboard;

import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
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
    private CameraManager mCameraManager;
    private Camera mCamera;

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
        //mCameraManager = (CameraManager)getSystemService(CAMERA_SERVICE);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
        int numCams = 0;
        String[] tmp;
        try{
            //tmp = mCameraManager.getCameraIdList();
            //numCams = tmp.length;
            if (numCams > 0){
                //open camera
            }
        }
        catch (Exception e){
            Toast.makeText(MainActivity.this, "Camera Error!", Toast.LENGTH_SHORT).show();
            numCams = -1;
        }
        finally{
            if (numCams<=0){
                finish();
            }
        }
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        Log.i("Dashboard", "Sensors Unloaded");
    }

    public void loadSensor(){
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
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
        appView.accelX = event.values[0];
        appView.accelY = event.values[1];
        appView.accelZ = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
