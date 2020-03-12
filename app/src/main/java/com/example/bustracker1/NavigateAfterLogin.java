package com.example.bustracker1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

public class NavigateAfterLogin extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate_after_login);
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCES",MODE_PRIVATE);
        String busName=sharedPreferences.getString("BUS_NAME","");
        String driverNo=sharedPreferences.getString("DRIVER_NUMBER","");
        if(busName.isEmpty() || driverNo.isEmpty())
        {
            startActivity(new Intent(NavigateAfterLogin.this,NamePhoneLayout.class));
            Toast.makeText(this, "The details like name and driver no entered must be navigated", Toast.LENGTH_SHORT).show();
        }else{
            finish();
            startActivity(new Intent(NavigateAfterLogin.this,AfterLogin.class));
        }


    }
}
