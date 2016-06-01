package com.xc.dashboard;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private Sensor mAccelerometer;
    public AppView appView;

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
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //called when location is updated
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        try{
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 2, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 2, mLocationListener);
        }
        catch(SecurityException e){
            Toast t = Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG);
            t.show();
            Log.d("Location", e.toString());
        }

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
            Log.d("Sensor", "onSensorChanged");
            appView.accelX = event.values[0];
            appView.accelY = event.values[1];
            appView.accelZ = event.values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
