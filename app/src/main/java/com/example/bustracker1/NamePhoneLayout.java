package com.example.bustracker1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NamePhoneLayout extends AppCompatActivity {

    Button btn;
    EditText busName;
    EditText driverNum;
    EditText driverName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_phone_layout);
        btn=findViewById(R.id.submit_name);
        busName=findViewById(R.id.busName);
        driverNum=findViewById(R.id.driverNum);
        sharedPreferences=getSharedPreferences("SHARED_PREFERENCES",MODE_PRIVATE);
        driverName=findViewById(R.id.driverName);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(busName.getText().toString().isEmpty() || driverNum.getText().toString().isEmpty()||driverName.getText().toString().isEmpty())
                {
                    Toast.makeText(NamePhoneLayout.this, "Please enter all the details", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("BUS_NAME",busName.getText().toString());
                editor.putString("DRIVER_NUMBER",driverNum.getText().toString());
                editor.putString("DRIVER_NAME",driverName.getText().toString());
                editor.commit();
                finish();
                startActivity(new Intent(NamePhoneLayout.this,AfterLogin.class));
            }
        });

    }
}
