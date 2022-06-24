package com.example.localizador.gps.view;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.localizador.R;
import com.example.localizador.gps.viewmodel.SensorsViewModel;
import com.example.localizador.receiver.GPSBroadcastReceiver;
import com.example.localizador.receiver.GPSService;
import com.example.localizador.sqlite.DBHelper;
import com.example.localizador.sqlite.Localizador;
import com.example.localizador.views.GPSActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String PARAM_INTENT = "pendingIntent";
    private SensorsViewModel viewmodel;
    int valor = 0;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    private List<Localizador> listaLocalizacao = new ArrayList<Localizador>();
    private ArrayAdapter<Localizador> arrayAdapterLocalizador;
    ListView listV_dados;

    BroadcastReceiver br;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        br = new GPSBroadcastReceiver();

        IntentFilter intf = new IntentFilter("com.example.localizador.GPS_START");
        IntentFilter intf1 = new IntentFilter("android.intent.action.BOOT_COMPETED");

        registerReceiver(br, intf);

        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            Boolean backgroundLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION,false);
                            if (fineLocationGranted != null && fineLocationGranted && backgroundLocationGranted) {
                                Log.d("MainActivity", "onCreate: autorizado GPS");

                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Somente localização aproximada autorizada
                            } else {
                                // Nenhuma localização autorizada
                            }
                        }
                );
// Before you perform the actual permission request, check whether your app
// already has the permissions, and whether your app needs to show a permission
// rationale dialog. For more details, see Request permissions.
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
        registerReceiver(br, intf1);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        viewmodel = new ViewModelProvider(this).get(SensorsViewModel.class);
        viewmodel.setContexto(this);

        viewmodel.getNome().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ((TextView) findViewById(R.id.textViewNome)).setText(s);
            }
        });
        /*viewmodel.getSensores().observe(this, new Observer<List<Sensor>>() {
            @Override
            public void onChanged(List<Sensor> sensors) {
                LinearLayout layout = findViewById(R.id.layoutSensores);
                layout.removeAllViews();
                for (Sensor sensor : sensors) {
                    TextView tv = new TextView(getBaseContext());
                    tv.setText(sensor.getName()+ " "+ sensor.getStringType());
                    layout.addView(tv);
                    ;
                }

            }
        });*/
        findViewById(R.id.button2).setOnClickListener(view->{
            Intent intent = new Intent();
            intent.setAction("com.example.localizador.GPS_START");
            getApplicationContext().sendBroadcast(intent);

        });
        findViewById(R.id.btNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this.getApplicationContext(), GPSActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });


        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();

    }


    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentuser == null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }
}