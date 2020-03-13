package com.example.bustracker1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<BusModel> {

    private Context mcontext;
    private int mresource;
    TextView busName;
    TextView driverName;
    ImageButton call_btn;
    String bus_name;
    String driver_name;
    String phoneNum;

    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BusModel> objects) {
        super(context, resource, objects);
        mcontext = context;
        mresource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        bus_name = getItem(position).getName();
        driver_name = getItem(position).getDriverName();
        phoneNum = getItem(position).getPhoneNum();

        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
        convertView = layoutInflater.inflate(mresource, parent, false);
        busName = convertView.findViewById(R.id.bus_name);
        driverName = convertView.findViewById(R.id.driver_name);
        call_btn = convertView.findViewById(R.id.call_icon);

        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Call button is clicked" + getItem(position).getPhoneNum(), Toast.LENGTH_SHORT).show();
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + getItem(position).getPhoneNum()));
//                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions();
//                    return;
//                }
                getContext().startActivity(callIntent);
            }
        });

        busName.setText(bus_name);
        driverName.setText(driver_name);

//        TextView txt=(TextView)convertView.findViewById(R.id.bus_name);
//        ImageButton button= (ImageButton)convertView.findViewById(R.id.phone_btn);
//        ImageButton msg_btn=(ImageButton)convertView.findViewById(R.id.msg_btn);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mcontext, "Making a call please wait...", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        msg_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mcontext, "Message Button is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        txt.setText(name);
        return convertView;
    }
}
