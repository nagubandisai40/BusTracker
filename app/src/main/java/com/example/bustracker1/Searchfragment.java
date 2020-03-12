package com.example.bustracker1;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Searchfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Searchfragment extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    Button search_btn;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
     GoogleMap mMap;
     EditText txt;
     BusModel bus;
     boolean busFind;

    public Searchfragment() {
        // Required empty public constructor
    }

    public static Searchfragment newInstance(String param1, String param2) {
        Searchfragment fragment = new Searchfragment();
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

        databaseReference= FirebaseDatabase.getInstance().getReference().child("buses");
        progressDialog =new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Searching");
        progressDialog.setMessage("Please wait while searching");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_searchfragment, container, false);
        search_btn=(Button) rootView.findViewById(R.id.search_btn);
        txt=rootView.findViewById(R.id.search);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                progressDialog.show();
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String edt_txt=txt.getText().toString().trim();
                        for(DataSnapshot data: dataSnapshot.getChildren())
                        {
                            String fire_txt=data.child("name").getValue().toString();
                            if(edt_txt.equals(fire_txt))
                            {
                                bus=new BusModel();
                                bus.setPhoneNum(data.child("phoneNum").getValue().toString());
                                bus.setLongitude(data.child("longitude").getValue().toString());
                                bus.setLatitude(data.child("latitude").getValue().toString());
                                bus.setName(data.child("name").getValue().toString());
                                busFind=true;
                                SupportMapFragment mapFragment= (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
                                mapFragment.getMapAsync(Searchfragment.this);
                            }
                        }

                        if(!busFind) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Please enter a valid bus Name", Toast.LENGTH_SHORT).show();
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getActivity(), "onCanceled Method called", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        progressDialog.cancel();
        LatLng sydney=new LatLng(Double.parseDouble(bus.getLatitude()),Double.parseDouble(bus.getLongitude()));
        mMap.addMarker(new MarkerOptions().position(sydney).title(bus.getName()));
        mMap.setMinZoomPreference(10f);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
