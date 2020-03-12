package com.example.bustracker1;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link contactfragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class contactfragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView myList;
    ArrayList<BusModel> list_items;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;

    public contactfragment() {
        // Required empty public constructor
    }

    public static contactfragment newInstance(String param1, String param2) {
        contactfragment fragment = new contactfragment();
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
        list_items=new ArrayList<>();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getActivity(), "onDataChange MEthod called", Toast.LENGTH_SHORT).show();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    BusModel bus = new BusModel();
                    bus.setName(data.child("name").getValue().toString());
                    bus.setLatitude(data.child("latitude").getValue().toString());
                    bus.setLongitude(data.child("longitude").getValue().toString());
                    bus.setPhoneNum(data.child("phoneNum").getValue().toString());
                    bus.setDriverName(data.child("driverName").getValue().toString());
                    list_items.add(bus);
                }
                MyAdapter adapter = new MyAdapter(getActivity(), R.layout.contact_list_item, list_items);
                myList.setAdapter(adapter);
                progressDialog.cancel();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "onCancelled Method Called", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_contactfragment, container, false);
                myList=view.findViewById(R.id.mListView);
        return view;
    }
}
