package com.example.localizador.gps.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.example.localizador.R;
import com.example.localizador.receiver.GPSService;

public class LandActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent1 = new Intent(this, GPSService.class);
        startForegroundService(intent1);
        Log.w("LandActivity", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land);
    }
}