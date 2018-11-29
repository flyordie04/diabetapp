package com.example.mirek.diabetapp.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;

import java.lang.reflect.Array;
import java.text.ParseException;

/**
 * Created by Mirek on 28.11.2018.
 */

public class Doctors {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String[] result;

    public String[] Doctors(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        final String[] result = null;



        mDatabaseReference.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                int i = 0;

                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                        i++;
                }
                Log.e("i",String.valueOf(i));
                final String[] doctors = new String[i];
                int k = 0;
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    if(!String.valueOf(mDataSnapshot.getValue()).equals("")){
                        doctors[k] = String.valueOf(mDataSnapshot.getValue());
                        Log.e("i",doctors[k]);
                        k++;
                    }
                }
                Log.e("k",String.valueOf(k));
                for(int j = 0; j<k; j++){
                    Log.e("j",String.valueOf(j));
                    result[j] = doctors[j];
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });


        return result;
    }
}
