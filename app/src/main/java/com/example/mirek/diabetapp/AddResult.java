package com.example.mirek.diabetapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.support.v7.widget.Toolbar;

import com.example.mirek.diabetapp.models.DrawerHeader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddResult extends AppCompatActivity {

    DateFormat formatDate = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateTime = Calendar.getInstance();
    private TextView textDate;
    private TextView textTime;
    private TextView textResult;

    Button btnDate;
    Button btnTime;
    Button btnResult;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    SharedPreferences sp;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        textDate = findViewById(R.id.txtDatePicker);
        textTime = findViewById(R.id.txtTimePicker);
        textResult = findViewById(R.id.txtResultPicker);

        btnDate = findViewById(R.id.btnDatePicker);
        btnTime = findViewById(R.id.btnTimePicker);
        btnResult = findViewById(R.id.btnResultPicker);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
            }
        });
        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateResult();
            }
        });

        updateTextLabel();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        //MENU
        Toolbar toolbar = findViewById(R.id.addResultToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_name);
        configureNavigationDrawer();

        sp = getSharedPreferences("login",MODE_PRIVATE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();

        switch (itemId){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configureNavigationDrawer(){
        drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.settings) {
                    Intent i = new Intent(AddResult.this, Settings.class);
                    startActivity(i);
                }
                else if(id == R.id.home){
                    Intent i = new Intent(AddResult.this, AddResult.class);
                    startActivity(i);
                }
                else if(id == R.id.log){
                    sp.edit().putBoolean("logged",false).apply();
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(AddResult.this, LoginActivity.class);
                    startActivity(i);
                }
                else if(id == R.id.addInsulin){
                    Intent i = new Intent(AddResult.this, AddInsulinActivity.class);
                    startActivity(i);
                }
                else if(id == R.id.addActivity){
                    Intent i = new Intent(AddResult.this, PhysicalActivity.class);
                    startActivity(i);
                }
                else if(id == R.id.table){
                    Intent i = new Intent(AddResult.this, TableCarboActivity.class);
                    startActivity(i);
                }
                else if(id == R.id.statistics){
                    Intent i = new Intent(AddResult.this, StatisticsActivity.class);
                    startActivity(i);
                }

                return false;
            }
        });
    }

    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
    }
    private void updateResult(){
        final Dialog d = new Dialog(AddResult.this);
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(300);
        np.setMinValue(0);
        np.setValue(Integer.parseInt(textResult.getText().toString()));
        np.setWrapSelectorWheel(false);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textResult.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
            }
        });
        d.show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dateTime.set(Calendar.YEAR, i);
            dateTime.set(Calendar.MONTH, i1);
            dateTime.set(Calendar.DAY_OF_MONTH, i2);
            updateTextLabel();
        }
    };

    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            dateTime.set(Calendar.HOUR_OF_DAY, i);
            dateTime.set(Calendar.MINUTE, i1);
            updateTextLabel();

        }
    };

    private void updateTextLabel(){

        textDate.setText(formatDate.format(dateTime.getTime()));
        textTime.setText(formatTime.format(dateTime.getTime()));
    }



    public void addResult(View v){
        final String stringNumber=textResult.getText().toString();
        final int number = Integer.parseInt(textResult.getText().toString());
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");

        final Dialog confirmation = new Dialog (AddResult.this);
        confirmation.setContentView(R.layout.dialog_confirmation);
        Button confirm = confirmation.findViewById(R.id.confirm);
        Button back = confirmation.findViewById(R.id.back);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user != null) {
                    String email = ""+user.getUid();
                    mDatabaseReference.child("users").child(email).child("diabetes").child(simpleDateFormat.format(dateTime.getTime())).setValue(stringNumber);
                    if(number<70 || number >140) {
                        Intent i = new Intent(AddResult.this, SendSmsActivity.class);
                        Log.e("wynik cukru", stringNumber);
                        i.putExtra("STRING_I_NEED", stringNumber);
                        i.putExtra("DATA_BADANIA", simpleDateFormat.format(dateTime.getTime()));
                        startActivity(i);
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(AddResult.this).create();
                        alertDialog.setMessage("Wartość dodana - poziom cukru w normie");
                        alertDialog.setTitle("Poziom cukru dodany");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();
                    }
                }
                confirmation.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmation.dismiss();
            }
        });
        confirmation.show();


    }

    public void bluetoothMode(View v){
        Intent i = new Intent(AddResult.this, BluetoothActivity.class);
        startActivity(i);
    }

}
