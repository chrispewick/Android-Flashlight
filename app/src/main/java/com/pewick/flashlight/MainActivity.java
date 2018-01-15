package com.pewick.flashlight;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        toggleLight = (Button) this.findViewById(R.id.toggle_light_button);
        this.setOnClickListeners();
        isLightOn = false;
    }

    private void toggleLight(){
        long startTime = System.currentTimeMillis();
        //If needed: instead of checking for the permission, put the code in a try/catch block,
        // and when catching a permission exception handle it.

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

        long endTime = System.currentTimeMillis() - startTime;
        Log.i("Main","Time: " + endTime);
        //200-225
        //350-430
        //So, trade off in doing it the current way vs the deprecated way?
    }

    private void setOnClickListeners() {
        this.toggleLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLight();
            }
        });
    }

//    private boolean checkPermissions(){
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//            Log.i("Main","camera permission granted");
//            return true;
//        } else{
//            Log.i("Main","camera permission NOT granted");
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.CAMERA},
//                    PERMISSION_REQUEST_CODE);
//            //getResources().getString(R.string.permission_request)
//            return false;
//        }
//    }

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
