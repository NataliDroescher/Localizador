package com.example.localizador.receiver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.example.localizador.sqlite.DBHelper;
import com.example.localizador.sqlite.Localizador;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import androidx.lifecycle.LifecycleService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GPSService extends Service {

    public static final String TAG = "GPService";
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationClient;
    Location lastLoc = null;
    private NotificationChannel canalNotificacao;
    DBHelper db = new DBHelper(this);
    FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ponto 1");
        if (ActivityCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        Log.d(TAG, "onCreate: ponto 2");


        NotificationChannel channel2 = new NotificationChannel(
                "UniR",
                "Channel UniRitter",
                NotificationManager.IMPORTANCE_LOW
        );
        channel2.setDescription("This is channel 2");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel2);


        Notification notification =
                new Notification.Builder(this, "UniR")
                        .setContentTitle("Titulo")
                        .setContentText("Texto")
                        .build();

// Notification ID cannot be 0.
        //if (intent.getBooleanExtra("boot", false)) {
        Log.d(TAG, "onStartCommand: dando o start");
        startForeground(1234, notification);
        //}

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // LocationRequest currentLocationRequest = LocationRequest.create();
        //currentLocationRequest.setInterval(5000)
        //      .setMaxWaitTime(10000)
        //    .setSmallestDisplacement(0)
        //  .setFastestInterval(3000)
        //.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)

            @Override
            public void onLocationChanged(@NonNull Location location) {

                float distancia = 0;
                Location loc = location;


                Log.d(TAG, "ATENCAO: consegui testar o login " + usuarioAtual.getEmail());

                //if (usuarioAtual.getEmail() != "admin@local.com"){
                  Timestamp data = Timestamp.now();
                // DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm:ss");
                String aux = usuarioAtual.getEmail();
                loc.getLongitude();
                loc.getLatitude();

                Dados.dados.add(loc.toString());
                Log.d(TAG, "onLocationResult: vou gravar");
                Dados.gravar(data, aux, loc, distancia, "0", "0");
                Log.d(TAG, "onLocationResult: gravei");
                if (lastLoc != null) {
                    distancia = loc.distanceTo(lastLoc);
                }
                lastLoc = loc;
                //}


                Toast.makeText(GPSService.this, "Salvo com sucesso NO sqlite", Toast.LENGTH_LONG).show();


                Log.w(TAG, "onLocationResult: " + distancia + "m acc:" + loc.getAccuracy());
                Toast.makeText(GPSService.this, distancia + "m", Toast.LENGTH_SHORT).show();


            }
        });
        Log.w(TAG, "onStartCommand ");




        if (intent == null) {
            return START_NOT_STICKY;
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
