package  com.example.myapplication;



import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import com.example.myapplication.MapsActivity;
import com.example.myapplication.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static androidx.core.content.ContextCompat.startActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    private FusedLocationProviderClient mFusedLocationClient;
    DatabaseReference mDatabase;
    private Button mBtnMaps;

    @Override

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);



        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        subirLatLongFirebase();
        mBtnMaps = findViewById(R.id.btnMaps);


    }

    private void subirLatLongFirebase(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission (this, Manifest.permission.ACCESS_COARSE_LOCATION)
                !=PackageManager.PERMISSION_GRANTED){


            ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);



            return;

        }

        mFusedLocationClient.getLastLocation()

                .addOnSuccessListener (this, new OnSuccessListener<Location>(){
                    @Override
        public void onSuccess (Location location) {
        if (location != null){
            Log.e("latitud", +location.getLatitude() + "longitud" + location.getLongitude());

            Map<String, Object> latlang = new HashMap<>();
            latlang.put("latitud", location.getLatitude());
            latlang.put("longiud",location.getLongitude());
            mDatabase.child("usuarios").push().setValue(latlang);
        }



                }

    });


}


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnMaps : Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(intent);
            break;
        }

    }
}










