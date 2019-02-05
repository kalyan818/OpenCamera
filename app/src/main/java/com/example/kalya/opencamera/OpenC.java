package com.example.kalya.opencamera;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class OpenC extends Service implements SensorEventListener{
PowerManager.WakeLock wakeLock;
    boolean open,open1;
    private SensorManager sensorManager;
    Sensor sensor;
    ActivityManager manager;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;
    private static final int CAMERA_REQUEST = 9999;
    float a;
    int count = 0;
    Button button;


    @Override
    public void onCreate() {

        super.onCreate();


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        System.out.println("servcice created");

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor , SensorManager.SENSOR_DELAY_NORMAL);

        Log.e("Google", "Service Created");
    }
    /**
     * Called when sensor values have changed.
     * <p>See {@link SensorManager SensorManager}
     * for details on possible sensor types.
     * <p>See also {@link SensorEvent SensorEvent}.
     * <p/>
     * <p><b>NOTE:</b> The application doesn't own the
     * {@link SensorEvent event}
     * object passed as a parameter and therefore cannot hold on to it.
     * The object may be part of an internal pool and may be reused by
     * the framework.
     *
     * @param event the {@link SensorEvent SensorEvent}.
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Boolean screenOn;
        KeyguardManager myKm = (KeyguardManager) this.getSystemService(Context.KEYGUARD_SERVICE);
        if (myKm.inKeyguardRestrictedInputMode()) {
            screenOn = false;
        } else {
            screenOn = true;
        }
        if (screenOn == true /*&& appopened == false*/) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float z = event.values[2];

                System.out.print(z);
                if (z <= -3) {
                    open1 = true;
                    if (open1 == true) {
                        if (count < 1) {
                            count = 1;
                        } else {
                            count++;
                        }
                    }
                } else {
                    open1 = false;
                    count = 0;
                }
            }
            if (count == 1) {
                //Toast.makeText(this,"launching camera...",Toast.LENGTH_LONG).show();
                Intent o = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Intent i = new Intent(getApplicationContext(),Main2Activity.class);
                startActivity(o);//null pointer check in case package name was not found
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        System.out.println("sensor changing onAccuracyChanged");
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}