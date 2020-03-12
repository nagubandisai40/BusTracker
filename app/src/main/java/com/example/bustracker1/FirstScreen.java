package com.example.bustracker1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirstScreen extends AppCompatActivity {

    Button bt1,bt2,bt3;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_screen);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        final Searchfragment searchfragment=new Searchfragment();
        final contactfragment contactfragment=new contactfragment();
        final BusesNearMe busesNearMe=new BusesNearMe();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.get_nearby_buses:getSupportFragmentManager().beginTransaction().replace(R.id.container,busesNearMe).commit();
                    return  true;
                    case R.id.search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,searchfragment).commit();
                        return  true;
                    case R.id.contact:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,contactfragment).commit();
                        return  true;
                }
                return false;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.get_nearby_buses);


//        bt1=(Button) findViewById(R.id.btn1);
//        bt2=(Button) findViewById(R.id.btn2);
//        bt3=(Button) findViewById(R.id.btn3);
//
//        bt1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(FirstScreen.this,MapsActivity.class));
//            }
//        });
//        bt2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(FirstScreen.this,SearchScreen.class));
//                Toast.makeText(FirstScreen.this, "Button 2 is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        bt3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(FirstScreen.this,Contact.class));
//                Toast.makeText(FirstScreen.this, "Button 3 is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
