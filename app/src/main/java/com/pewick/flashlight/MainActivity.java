package com.pewick.flashlight;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Button toggleLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toggleLight = (Button) this.findViewById(R.id.toggle_light_button);
        this.setOnClickListeners();
    }

    private void setOnClickListeners() {
        this.toggleLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLight();
            }
        });
    }

    private void toggleLight(){
        Intent intent = new Intent(this, FlashlightService.class);
        this.startService(intent);
    }
}
