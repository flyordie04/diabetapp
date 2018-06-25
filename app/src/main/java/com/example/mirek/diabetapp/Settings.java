package com.example.mirek.diabetapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mirek.diabetapp.models.UserInformation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText etPhoneNumber;

    private TextView textBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        textBack = findViewById(R.id.txtBack);

        textBack.setPaintFlags(textBack.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                String phoneNumber = dataSnapshot.child(user.getUid()).child("settings").getValue(UserInformation.class).getPhone_number();
                etPhoneNumber.setText(phoneNumber, TextView.BufferType.EDITABLE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
                Toast.makeText(Settings.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    public void saveSettings(View v){
        String number = ""+etPhoneNumber.getText().toString();
        if(user != null) {
            String email = ""+user.getUid();
            mDatabaseReference.child(email).child("settings").child("phone_number").setValue(number);
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

    public void back(View v){
        Intent i = new Intent(Settings.this, AddResult.class);
        startActivity(i);
    }

}
