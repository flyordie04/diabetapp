package com.example.mirek.diabetapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private EditText etUserPhoneNumber;
    private EditText etName;
    private EditText etSurname;
    private EditText etCity;


    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etWeight = findViewById(R.id.etWeight);
        etUserPhoneNumber = findViewById(R.id.etUserPhoneNumber);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etCity = findViewById(R.id.etCity);



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
                }
                    if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("weight").getValue()!= null) {
                        String weight = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getWeight();
                        etWeight.setText(weight, TextView.BufferType.EDITABLE);
                    }

                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("city").getValue()!= null) {
                    String city = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getCity();
                    etCity.setText(city, TextView.BufferType.EDITABLE);
                }
                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("name").getValue()!= null) {
                    String name = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getName();
                    etName.setText(name, TextView.BufferType.EDITABLE);
                }
                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("surname").getValue()!= null) {
                    String surname = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getSurname();
                    etSurname.setText(surname, TextView.BufferType.EDITABLE);
                }
                if(dataSnapshot.child("users").child(user.getUid()).child("settings").child("userPhoneNumber").getValue()!= null) {
                    String userPhoneNumber = dataSnapshot.child("users").child(user.getUid()).child("settings").getValue(UserInformation.class).getUserPhoneNumber();
                    etUserPhoneNumber.setText(userPhoneNumber, TextView.BufferType.EDITABLE);
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

        sp = getSharedPreferences("login",MODE_PRIVATE);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void saveSettings(View v){
        String number = ""+etPhoneNumber.getText().toString();

        if(user != null && !number.equals("")) {
            String email = ""+user.getUid();
            mDatabaseReference.child("users").child(email).child("settings").child("phone_number").setValue(number);
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Settings.this).create();
            alertDialog.setMessage("Dane zaktualizowane");
            alertDialog.setTitle("Ustawienia");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        } else {
            Toast.makeText(Settings.this, "Wartość nie może być pusta!", Toast.LENGTH_SHORT).show();
        }
    }
    public void saveNewAccount(View v){
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Settings.this).create();
        alertDialog.setMessage("Upewnij się że wpisane dane są prawidłowe");
        alertDialog.setTitle("Zmiana e-mail i hasła");
        EditText updateEmail = findViewById(R.id.updateEmail);
        EditText updatePassword = findViewById(R.id.updatePassword);
        final String email = updateEmail.getText().toString();
        final String password = updatePassword.getText().toString();
        if(user != null && (!email.equals("") || !password.equals(""))) {
            alertDialog.setButton("Zmień", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    if(!email.equals("")){
                        user.updateEmail(email);
                        Toast.makeText(Settings.this, "E-mail został zmieniony.", Toast.LENGTH_SHORT).show();
                    }
                    if(!password.equals("")) {
                        user.updatePassword(password);
                        Toast.makeText(Settings.this, "Hasło zostało zmienione.", Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton2("Wróć", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            Toast.makeText(Settings.this, "Wartości nie mogą być puste!", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAccount(View v){
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Settings.this).create();
        alertDialog.setMessage("Czy chcesz bezpowrotnie usunąć konto?");
        alertDialog.setTitle("Usuwanie konta");
        alertDialog.setButton("Usuń konto", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user.delete();
                alertDialog.dismiss();
                sp.edit().putBoolean("logged",false).apply();
                Intent intent = new Intent(Settings.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setButton2("Wróć", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void saveUserInfo(View v){
        String weight = ""+etWeight.getText().toString();
        String userPhoneNumber = ""+etUserPhoneNumber.getText().toString();
        String name = ""+etName.getText().toString();
        String surname = ""+etSurname.getText().toString();
        String city = ""+etCity.getText().toString();

        if(user != null) {
            String email = ""+user.getUid();
            mDatabaseReference.child("users").child(email).child("settings").child("weight").setValue(weight);
            mDatabaseReference.child("users").child(email).child("settings").child("userPhoneNumber").setValue(userPhoneNumber);
            mDatabaseReference.child("users").child(email).child("settings").child("name").setValue(name);
            mDatabaseReference.child("users").child(email).child("settings").child("surname").setValue(surname);
            mDatabaseReference.child("users").child(email).child("settings").child("city").setValue(city);
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(Settings.this).create();
            alertDialog.setMessage("Dane zaktualizowane");
            alertDialog.setTitle("Ustawienia");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alertDialog.show();
        } else {
            Toast.makeText(Settings.this, "Wartość nie może być pusta!", Toast.LENGTH_SHORT).show();
        }


    }

    public void doctors(View v){
        Intent i = new Intent(Settings.this, DoctorsChooserActivity.class);
        startActivity(i);
    }


}
