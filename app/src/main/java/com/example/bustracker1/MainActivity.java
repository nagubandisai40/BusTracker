package com.example.bustracker1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button usrbtn,admnbtn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usrbtn=(Button)findViewById(R.id.user);
        admnbtn=(Button)findViewById(R.id.admin);
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCES",MODE_PRIVATE);

        admnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid=sharedPreferences.getString("UID","");
                Toast.makeText(MainActivity.this, "The fetched uid is "+uid, Toast.LENGTH_SHORT).show();
                if(uid.isEmpty())
                {
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(MainActivity.this,NavigateAfterLogin.class));
                    finish();
                }

            }
        });
        usrbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,FirstScreen.class));
//                Toast.makeText(MainActivity.this, "User Button clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
