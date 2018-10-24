package com.example.mirek.diabetapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mirek.diabetapp.models.Calories;
import com.example.mirek.diabetapp.models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhysicalActivity extends AppCompatActivity {

    DateFormat formatDate = DateFormat.getDateInstance();
    Calendar dateTime = Calendar.getInstance();

    private TextView textDate;

    private EditText etTimeInterval;

    private Button btnDate;

    private Spinner spinner;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical);


        textDate = findViewById(R.id.txtDatePicker);

        etTimeInterval = findViewById(R.id.etTimeInterval);
        etTimeInterval.addTextChangedListener(new CheckTime());

        spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this,R.array.physical_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnDate = findViewById(R.id.btnDatePicker);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDate();
            }
        });


        updateTextLabel();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        //MENU
        Toolbar toolbar = findViewById(R.id.physicalToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Dodaj aktywność fizyczną");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("weight").getValue()== "") {
                    settings();

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
                Toast.makeText(PhysicalActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void updateDate(){
        new DatePickerDialog(this, d, dateTime.get(Calendar.YEAR), dateTime.get(Calendar.MONTH), dateTime.get(Calendar.DAY_OF_MONTH)).show();
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


    private void updateTextLabel(){

        textDate.setText(formatDate.format(dateTime.getTime()));
    }


    public void addPhysical(View v){

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String timeInterval = ""+etTimeInterval.getText().toString();
                String physicalActivity = ""+spinner.getSelectedItem().toString();
                String weight = dataSnapshot.child("users").child(user.getUid()).child("settings").child("weight").getValue().toString();
                Calories calories1 = new Calories();
                if(!weight.equals("")) {
                    double sum = calories1.activityCalories(Integer.parseInt(timeInterval), Integer.parseInt(weight), physicalActivity);
                    Log.e("sum", String.valueOf(sum));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                    if(user != null) {
                        String email = ""+user.getUid();
                        mDatabaseReference.child("users").child(email).child("physical_activity").child(simpleDateFormat.format(dateTime.getTime())).child(physicalActivity).setValue(sum);
                        AlertDialog alertDialog = new AlertDialog.Builder(PhysicalActivity.this).create();
                        alertDialog.setMessage("Brawo! Podczas tej aktywności straciłeś " + sum +" kalorii!");
                        alertDialog.setTitle("Aktywność fizyczna");
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialog.show();
                    }
                    else {
                        settings();
                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });

    }


void settings(){
    AlertDialog dialog = new AlertDialog.Builder(PhysicalActivity.this).create();
    dialog.setMessage("Dodaj swoją wagę w ustawieniach aby wynik był dokładniejszy.");
    dialog.setTitle("Aktywność fizyczna");
    dialog.setButton("Przejdź do ustawień", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            Intent ii = new Intent(PhysicalActivity.this, Settings.class);
            startActivity(ii);
        }
    });
    dialog.show();
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
