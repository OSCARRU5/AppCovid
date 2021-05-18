package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>(;
    private ArrayList<Marker> RealTimeMarkers = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        countDownTime();



    }

    private void countDownTime(){

        new CountDownTimer(10000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("seconds remaining:", "" + millisUntilFinished /1000);
                onMapReady(mMap);
            }
            public  void onFinish(){
                Toast.makeText(MapsActivity.this, "puntos actualizados", Toast.LENGTH_SHORT).show();
                onMapReady(mMap);

            }
        }.start();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        mDatabase.child("usuarios").addListenerForSingleValueEvent (new ValueEventListener(){
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                for(Marker marker:tmpRealTimeMarkers){
                    marker.remove();
                }

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    MapsPojo mp = snapshot.getValue (MapsPojo.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(latitud, longitud));
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));


                }

                tmpRealTimeMarkers.clear();
                tmpRealTimeMarkers.addAll(tmpRealTimeMarkers);
                countDownTime();



            }


            @Override
            public void onCancelled(@NonNull DatabaseError dataSnapshot){
            }





        });


    }
}