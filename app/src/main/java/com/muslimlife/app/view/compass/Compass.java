package com.muslimlife.app.view.compass;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class Compass implements SensorEventListener {
    private static final String TAG = Compass.class.getSimpleName();

    public interface CompassListener {
        void onNewAzimuth(float azimuth);
    }

    private CompassListener listener;

    private final SensorManager sensorManager;
    private final Sensor asensor;
    private final Sensor msensor;

    private final float[] aData = new float[3];
    private final float[] mData = new float[3];
    private final float[] R = new float[9];
    private final float[] I = new float[9];

    public Compass(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        asensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start(Context context) {
        sensorManager.registerListener(this, asensor,
                SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, msensor,
                SensorManager.SENSOR_DELAY_GAME);

        PackageManager manager = context.getPackageManager();
        boolean haveAS = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
        boolean haveCS = manager.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS);

        if (!haveAS || !haveCS) {
            sensorManager.unregisterListener(this, asensor);
            sensorManager.unregisterListener(this, msensor);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setAzimuthFix(float fix) {
    }

    public void resetAzimuthFix() {
        setAzimuthFix(0);
    }

    public void setListener(CompassListener l) {
        listener = l;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                aData[0] = alpha * aData[0] + (1 - alpha)
                        * event.values[0];
                aData[1] = alpha * aData[1] + (1 - alpha)
                        * event.values[1];
                aData[2] = alpha * aData[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                mData[0] = alpha * mData[0] + (1 - alpha)
                        * event.values[0];
                mData[1] = alpha * mData[1] + (1 - alpha)
                        * event.values[1];
                mData[2] = alpha * mData[2] + (1 - alpha)
                        * event.values[2];
            }

            boolean success = SensorManager.getRotationMatrix(R, I, aData, mData);
            if (success) {
                float[] orientation = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuth = (float) Math.toDegrees(orientation[0]);
                //azimuth = (azimuth + 360) % 360;

                if (listener != null)
                    listener.onNewAzimuth(azimuth);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}