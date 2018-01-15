package com.pewick.flashlight;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private Button toggleLight;

    private Camera camera; // for API < 23
    private CameraManager cameraManager; // for API >= 23
    private String cameraId;
    private boolean isLightOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.checkPermissions();

        //TODO: check if torch is available, if not, pop-up and error message (alert dialog)

        toggleLight = (Button) this.findViewById(R.id.toggle_light_button);
        this.setOnClickListeners();
        isLightOn = false;
    }

    private boolean checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            Log.i("Main","camera permission granted");
            return true;
        } else{
            //TODO: write a dialog explaining why we need these permissions
            //I can show an alert dialog with an ok button, after the user closes the alert dialog, then request permission.
            //Maybe only request when teh user selects "OK", not any time the window is closed.

            Log.i("Main","camera permission NOT granted");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CODE);
            //getResources().getString(R.string.permission_request)
            return false;
        }

        //TODO: if accessing from widget, create toast, if I cannot create permission pop-up
    }

//    @Override
//    public void setPermissionRequestCode

    private boolean isFlashAvailable(){
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    private void toggleLight(){
        //TODO: check if light is on
        //TODO: API check

        //TODO: need to check permissions in both build versions
        //TODO: instead of checking for the permission, put the code in a try/catch block, and when catching a permission exception handle it
        if(Build.VERSION.SDK_INT >= 23) {
            Log.i("Main", "API >= 23");
            cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try{
                cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, !isLightOn);
                isLightOn = !isLightOn;
            } catch(CameraAccessException e){
                e.printStackTrace();
            }

        } else {
            Log.i("Main", "API < 23");
            if (checkPermissions()) {
                if (camera == null) {
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    Log.i("Main", "camera flash: " + parameters.getFlashMode());
                    if (parameters.getFlashMode().equalsIgnoreCase("off")) {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        camera.setParameters(parameters);
                        camera.startPreview();
                    } else {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        camera.setParameters(parameters);
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }
                } else {
                    camera.release();
                    camera = null;
                }

            }
        }
    }

    private void setOnClickListeners(){
        this.toggleLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLight();
            }
        });
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(camera != null){
//            camera.release();
//            camera = null;
//        }
//        if(cameraManager != null && Build.VERSION.SDK_INT >= 23){
//            try{
//                cameraManager.setTorchMode(cameraId, false);
//            } catch(CameraAccessException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
