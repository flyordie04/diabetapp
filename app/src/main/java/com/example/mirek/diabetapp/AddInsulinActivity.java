package com.example.mirek.diabetapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddInsulinActivity extends AppCompatActivity {

    DateFormat formatDate = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    private TextView textDate;
    private TextView textTime;
    private TextView textResult;

    private Button btnDate;
    private Button btnTime;
    private Button btnResult;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_insulin);

        textDate = findViewById(R.id.txtDatePicker);
        textTime = findViewById(R.id.txtTimePicker);
        textResult = findViewById(R.id.txtInsulinPicker);

        btnDate = findViewById(R.id.btnDatePicker);
        btnTime = findViewById(R.id.btnTimePicker);
        btnResult = findViewById(R.id.btnInsulinPicker);

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
                updateInsulin();
            }
        });


        updateTextLabel();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        //MENU
        Toolbar toolbar = findViewById(R.id.addInsulinToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dodaj insulinę");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public void updateInsulin(){
        final Dialog d = new Dialog(AddInsulinActivity.this);
        d.setContentView(R.layout.dialog);
        Button b1 = d.findViewById(R.id.button1);
        Button b2 = d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(60);
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
    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTime(){
        new TimePickerDialog(this, t, dateTime.get(Calendar.HOUR_OF_DAY), dateTime.get(Calendar.MINUTE), true).show();
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


    public void addInsulin(View v){
        //final String date = ""+formatDate.format(dateTime.getTime());
        //final String time = ""+formatTime.format(dateTime.getTime());
        final String stringNumber = ""+textResult.getText();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");

        final Dialog confirmation = new Dialog (AddInsulinActivity.this);
        confirmation.setContentView(R.layout.dialog_confirmation);
        Button confirm = confirmation.findViewById(R.id.confirm);
        Button back = confirmation.findViewById(R.id.back);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user != null) {
                String email = ""+user.getUid();
                //mDatabaseReference.child("users").child(email).child("insulin").child(date).child(time).setValue(stringNumber);
                mDatabaseReference.child("users").child(email).child("insulin").child(simpleDateFormat.format(dateTime.getTime())).setValue(stringNumber);
                AlertDialog alertDialog = new AlertDialog.Builder(AddInsulinActivity.this).create();
                alertDialog.setMessage("Powodzenie, przyjęta insulina została dodana");
                alertDialog.setTitle("Przyjęta insulina");
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
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

    public void back(View v){
        Intent i = new Intent(AddInsulinActivity.this, AddResult.class);
        startActivity(i);
    }
}
