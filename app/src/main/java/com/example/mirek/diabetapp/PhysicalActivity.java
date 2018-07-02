package com.example.mirek.diabetapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

public class PhysicalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DateFormat formatDate = DateFormat.getDateInstance();
    DateFormat formatTime = DateFormat.getTimeInstance();
    Calendar dateTime = Calendar.getInstance();

    private TextView textDate;
    private TextView textTime;
    private TextView textBack;
    private TextView txtChosen;

    private EditText etTimeInterval;

    private Button btnDate;
    private Button btnTime;

    private Spinner spinner;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);


        textDate = findViewById(R.id.txtDatePicker);
        textTime = findViewById(R.id.txtTimePicker);
        textBack = findViewById(R.id.txtBack);
        txtChosen = findViewById(R.id.txtChosen);

        etTimeInterval = findViewById(R.id.etTimeInterval);
        etTimeInterval.addTextChangedListener(new CheckTime());

        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.physical_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnDate = findViewById(R.id.btnDatePicker);
        btnTime = findViewById(R.id.btnTimePicker);

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

        updateTextLabel();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String physicalActivity = adapterView.getItemAtPosition(i).toString();
        txtChosen.setText(physicalActivity);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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


    public void addPhysical(View v){
        String timeInterval = ""+etTimeInterval.getText().toString();
        String physicalActivity = ""+txtChosen.getText();
        String date = ""+formatDate.format(dateTime.getTime());
        String time = ""+formatTime.format(dateTime.getTime());
        double calories = calories(Integer.parseInt(timeInterval));
        if(user != null) {
            String email = ""+user.getUid();
            mDatabaseReference.child(email).child("physical_activity").child(date).child(time).child(physicalActivity).setValue(calories);
            AlertDialog alertDialog = new AlertDialog.Builder(PhysicalActivity.this).create();
            alertDialog.setMessage("Powodzenie, aktywność została dodana");
            alertDialog.setTitle("Aktywność fizyczna");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        }

    }

    public void back(View v){
        Intent i = new Intent(PhysicalActivity.this, AddResult.class);
        startActivity(i);
    }

    private double calories(double time){

        String physicalActivity = ""+txtChosen.getText();
        double i = 0;
        if(physicalActivity.equals("Aerobik")){
            i = time * 7.3;
        }
        else if(physicalActivity.equals("Bieganie")){
            i = time * 7.0;
        }
        else if(physicalActivity.equals("Pływanie")){
            i = time * 8.5;
        }
        else if(physicalActivity.equals("Chodzenie")){
            i = time * 3.5;
        }
        else if(physicalActivity.equals("Jazda na rolkach")){
            i = time * 8.5;
        }
        else if(physicalActivity.equals("Jazda na rowerze")){
            i = time * 7.5;
        }
        else if(physicalActivity.equals("Koszykówka")){
            i = time * 6.5;
        }
        else if(physicalActivity.equals("Piłka nożna")){
            i = time * 8;
        }
        else if(physicalActivity.equals("Siatkówka")){
            i = time * 6;
        }
        else if(physicalActivity.equals("Pływanie")){
            i = time * 8.5;
        }

        return i;

    }

    class CheckTime implements TextWatcher{
        public void afterTextChanged(Editable s) {
            try {
                if(Integer.parseInt(s.toString()) > 1440)
                    s.replace(0, s.length(), "1440");
            }
            catch(NumberFormatException nfe){}
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not used, details on text just before it changed
            // used to track in detail changes made to text, e.g. implement an undo
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not used, details on text at the point change made
        }

    }
}
