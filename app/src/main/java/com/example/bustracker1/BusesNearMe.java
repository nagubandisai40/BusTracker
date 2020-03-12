package com.example.bustracker1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.os.Looper.getMainLooper;


public class BusesNearMe extends Fragment implements OnMapReadyCallback{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "BUSESNEARME.java";

    private GoogleMap mMap;
    private DatabaseReference databaseReference;

    ArrayList<BusModel> busList;
    ProgressDialog progressDialog;

    ArrayList<Location> arrayList;
    ArrayList<Float> finalList;

    private String mParam1;
    private String mParam2;
    boolean isAvailable;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location mlocation;
    private  static  final int REQ_CODE=101;

    public BusesNearMe() {
        // Required empty public constructor
    }

    public static BusesNearMe newInstance(String param1, String param2) {
        BusesNearMe fragment = new BusesNearMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while Loading");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("buses");
        busList = new ArrayList<>();
        progressDialog.show();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(getActivity());
        arrayList=new ArrayList<>();
        finalList=new ArrayList<>();

        getLocationUpdates();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    BusModel bus=new BusModel();
                    bus.setName(data.child("name").getValue().toString());
                    bus.setLatitude(data.child("latitude").getValue().toString());
                    bus.setLongitude(data.child("longitude").getValue().toString());
                    bus.setPhoneNum(data.child("phoneNum").getValue().toString());

                    busList.add(bus);
                }

                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(BusesNearMe.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "onCancelled Method called", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_buses_near_me, container, false);
        return rootView;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng mypos=new LatLng(mlocation.getLatitude(),mlocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(mypos).title("My Location"));
        mMap.setMinZoomPreference(10.0f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mypos));
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.bus);

        for(BusModel bus:busList){
            Location location=new Location("");
            location.setLatitude(Double.parseDouble(bus.getLatitude()));
            location.setLongitude(Double.parseDouble(bus.getLongitude()));
            finalList.add(mlocation.distanceTo(location));
//            Toast.makeText(getActivity(),Float.toString(mlocation.distanceTo(location)),Toast.LENGTH_SHORT).show();
            if(mlocation.distanceTo(location)<3000)
            {
                isAvailable=true;
                LatLng mm=new LatLng(location.getLatitude(),location.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(mm).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//                googleMap.addMarker(new MarkerOptions()
//                        .icon(BitmapDescriptorFactory.fromBitmap(circle))
//                        .flat(true).anchor(0.25f, 0.25f)
//                        .visible(true)
//                        .position(mm));
//                Toast.makeText(getActivity(), "Marker is adding", Toast.LENGTH_SHORT).show();
            }
        }
        if(!isAvailable){
            Toast.makeText(getActivity(), "No buses are available near You", Toast.LENGTH_SHORT).show();
        }

        progressDialog.cancel();
    }


    private void getLocationUpdates(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQ_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                mlocation=task.getResult();
            }
        });
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
}

