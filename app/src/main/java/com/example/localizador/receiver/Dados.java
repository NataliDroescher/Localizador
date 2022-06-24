package com.example.localizador.receiver;

import static android.content.ContentValues.TAG;

import android.location.Location;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Dados {

    public static List<String> dados = new ArrayList<>();

    private static TextView time = null;

    private static TextView provider = null;



    public static void gravar(Timestamp date, String aux, Location loc, float distancia, String tempoParado, String tempoAndando) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(loc != null) {
            Log.i("Teste", loc.getLatitude() + " " + loc.getLongitude());

            Log.i("Teste Conse", String.valueOf(loc.getBearing()));
          Log.i("Teste Distacia", String.valueOf(loc.getAccuracy()));
            Log.i("Teste Distacia", String.valueOf(distancia));
            Log.i("Teste login", String.valueOf(aux));
            HashMap<String, Object> local = new HashMap<>();
            local.put("Timestamp", date);
            local.put("String", aux + "");
            local.put("geopont", loc.getLatitude() + " " + loc.getLongitude());
            local.put("string",distancia + "");
            local.put("string", tempoAndando);
            local.put("string", tempoParado);



            db.collection("localizador")
                    .add(local)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });

        } else{
            Log.i("Alerta Banco FireBase", "não está vindo o loc com a localização");
        }
    }
    public static void selecionar(){

    }
}
