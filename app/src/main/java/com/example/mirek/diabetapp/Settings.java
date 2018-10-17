package com.example.mirek.diabetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mirek.diabetapp.models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Settings extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText etPhoneNumber;
    private EditText etWeight;

    private CheckBox cbDiabete;
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView textTime;

    private Button btnTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etWeight = findViewById(R.id.etWeight);

        textTime = findViewById(R.id.txtTimePicker);
        btnTime = findViewById(R.id.btnTimePicker);

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
            }
        });

        updateTextLabel();
        cbDiabete = findViewById(R.id.cbDiabete);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("phone_number").getValue()!= null) {
                    String phoneNumber = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getPhone_number();
                    etPhoneNumber.setText(phoneNumber, TextView.BufferType.EDITABLE);
                    if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("weight").getValue()!= null) {
                        String weight = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getWeight();
                        etWeight.setText(weight, TextView.BufferType.EDITABLE);
                    }
}
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
                Toast.makeText(Settings.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //MENU
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Ustawienia");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void updateTime(){
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            dateTime.set(Calendar.HOUR_OF_DAY, i);
            dateTime.set(Calendar.MINUTE, i1);
            updateTextLabel();

        }
    };
    private void updateTextLabel(){
        textTime.setText(formatTime.format(dateTime.getTime()));
    }

    public void saveSettings(View v){
        String number = ""+etPhoneNumber.getText().toString();
        String weight = ""+etWeight.getText().toString();
        if(user != null) {
            String email = ""+user.getUid();
            mDatabaseReference.child("users").child(email).child("settings").child("phone_number").setValue(number);
            mDatabaseReference.child("users").child(email).child("settings").child("weight").setValue(weight);
            if(cbDiabete.isChecked()) {
                notification();
            }
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Settings.this).create();
            alertDialog.setMessage("Dane zaktualizowane");
            alertDialog.setTitle("Ustawienia");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }
    }
    private void notification(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(),Notification_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


}
