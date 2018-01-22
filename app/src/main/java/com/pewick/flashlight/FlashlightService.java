package com.pewick.flashlight;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Chris on 1/15/2018.
 */

public class FlashlightService extends IntentService {
    private static final String TAG = "FlashlightService";

    private static Camera camera;

    private static boolean isLightOn = false;

    public FlashlightService(){
        super("FlashlightService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i(TAG, "onHandleIntent");

        if(Build.VERSION.SDK_INT >= 23) {
            Log.i(TAG, "API >= 23");
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try{
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, !isLightOn);
                if(!isLightOn){
                    new Thread(new VibrateRunnable()).start();
                }
                isLightOn = !isLightOn;
            } catch(CameraAccessException e){
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "API < 23");
            if (camera == null) {
                camera = Camera.open();
                Camera.Parameters parameters = camera.getParameters();
                Log.i(TAG, "camera flash: " + parameters.getFlashMode());
                if (parameters.getFlashMode().equalsIgnoreCase("off")) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(parameters);
                    new Thread(new VibrateRunnable()).start();
                    isLightOn = true;
                } else {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(parameters);
                    camera.stopPreview();
                    camera.release();
                    camera = null;
                    isLightOn = false;
                }
            } else {
                camera.release();
                camera = null;
                isLightOn = false;
            }
        }

        Log.i(TAG, "isLightOn: "+isLightOn);
        FlashlightAppWidgetProvider.updateWidget(this, AppWidgetManager.getInstance(this), isLightOn);
    }

    /**
     * This Runnable is necessary to prevent the widget icon change from stalling until the
     * vibration has completed.
     */
    private class VibrateRunnable implements Runnable{
        @Override
        public void run() {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(150);
            try {
                Thread.sleep(150);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            v.vibrate(150);
        }
    }
}