package com.example.mirek.diabetapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.*;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class BluetoothActivity extends AppCompatActivity {


    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final int DISCOVER_DURATION = 300;
    private static final int REQUEST_BLU = 1;

    private TextView textBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        textBack = findViewById(R.id.txtBack);

        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if(bluetoothAdapter == null)
        {
            Toast.makeText(this,"Na tym urządzeniu nie ma Bluetooth", Toast.LENGTH_LONG).show();
        } else {
            enableBluetooth();
        }

    }

    public void enableBluetooth(){
        Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVER_DURATION);
        startActivityForResult(discoveryIntent, REQUEST_BLU);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            File file = new File(Environment.getExternalStorageDirectory(),"md5sum.txt");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> appsList = packageManager.queryIntentActivities(intent,0);

            if(appsList.size() > 0){
                String packageName = null;
                String className = null;
                boolean found = false;

                for(ResolveInfo info : appsList){
                    packageName = info.activityInfo.packageName;
                    if(packageName.equals("com.android.bluetooth")){
                        className = info.activityInfo.name;
                        found = true;
                        break;
                    }
                }
                if(!found){
                    Toast.makeText(this,"Bluetooth nie zostało znalezione.", Toast.LENGTH_LONG).show();
                } else {
                    intent.setClassName(packageName, className);
                    startActivity(intent);
                }
            }
        } else {
            Toast.makeText(this,"Bluetooth zatrzymane", Toast.LENGTH_LONG).show();

        }
    }

    public void back(View v){
        Intent i = new Intent(BluetoothActivity.this, AddResult.class);
        startActivity(i);
    }


}
