package com.example.bustracker1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AfterLogin extends AppCompatActivity {

    long remainingTime;
    SharedPreferences sharedPreferences;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location mlocation;
    private  static  final int REQ_CODE=101;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("buses");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
        getLocationUpdates();

    }

    private void getLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQ_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mlocation=locationResult.getLastLocation();
                String uid=sharedPreferences.getString("UID","");
                if(!uid.isEmpty())
                {
                    HashMap<String,Object> map=new HashMap<>();
                    String latitude= String.valueOf(mlocation.getLatitude());
                    String longitude= String.valueOf(mlocation.getLongitude());
                    map.put("latitude",latitude);
                    map.put("longitude",longitude);
                    databaseReference.child(uid).updateChildren(map);
                }
                Toast.makeText(AfterLogin.this, "The location fetched is "+locationResult.getLastLocation().getLatitude()+"long:"+locationResult.getLastLocation().getLongitude(), Toast.LENGTH_SHORT).show();
            }
        },getMainLooper());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocationUpdates();
                }
                break;
            }
        }
    }

    private void createLocationRequest()
    {
        locationRequest=new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(3000);
        locationRequest.setInterval(6000);
//        locationRequest.setSmallestDisplacement(5);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        finishAffinity();
        if(remainingTime+2000>System.currentTimeMillis())
        {
            System.exit(1);
        }else{
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        remainingTime=System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logouticon) {
            sharedPreferences = getSharedPreferences("SHARED_PREFERENCES", MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
//            String usrid=FirebaseAuth.getInstance().getUid();
            edit.clear();
            edit.commit();
//            Toast.makeText(this, "logout icon is clicked", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
//            Toast.makeText(this, "Signout success", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(AfterLogin.this, MainActivity.class));
        }
        return true;
    }
}
