package com.example.mirek.diabetapp.models;

import android.util.Log;
import android.widget.Toast;

import com.example.mirek.diabetapp.PhysicalActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

/**
 * Created by Mirek on 16.10.2018.
 */

public class Calories {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Calories(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.


                final String weight = dataSnapshot.child("users").child(user.getUid()).child("settings").child("weight").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException());
            }
        });
    }


    public double activityCalories(double time, double weight, String physicalActivity){
        double kcal = 0;
        double i;

        if(physicalActivity.equals("Ogrodnictwo")){
            i = (weight * 0.095)/65;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Taniec")){
            i = (weight * 0.07)/77;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Aerobik")){
            i = (weight * 0.172)/65;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Kręgle")){
            i = (weight * 0.059)/70;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Biathlon")){
            i = (weight * 0.124)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Bieg na nartach")){
            i = (weight * 0.095)/65;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Boks")){
            i = (weight * 0.234)/63.2;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Gimnastyka")){
            i = (weight * 0.05)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Hokej")){
            i = (weight * 0.43)/65.1;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Jazda na łyżwach")){
            i = (weight * 0.061)/70;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Jeździectwo")){
            i = (weight * 0.041)/73;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Judo")){
            i = (weight * 0.107)/74.5;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Kajakarstwo")){
            i = (weight * 0.12)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Kolarstwo (25 km/h)")){
            i = (weight * 0.171)/70;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Kolarstwo (13 km/h)")){
            i = (weight * 0.114)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Koszykówka")){
            i = (weight * 0.217)/75;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Bieganie")){
            i = (weight * 0.163)/65;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Bieganie szybkie")){
            i = (weight * 0.59)/70;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Łucznictwo")){
            i = (weight * 0.076)/69;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Jazda na nartach")){
            i = (weight * 0.12)/83;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Pływanie")){
            i = (weight * 0.1)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Piłka nożna")){
            i = (weight * 0.131)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Podnoszenie ciężarów")){
            i = (weight * 0.12)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Rugby")){
            i = (weight * 0.149)/68;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Siatkówka")){
            i = (weight * 0.097)/76;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Tenis ziemny")){
            i = (weight * 0.101)/71;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Tenis stołowy")){
            i = (weight * 0.078)/69;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Wioślarstwo")){
            i = (weight * 0.04)/70;
            kcal = i * weight * time;
        }
        else if(physicalActivity.equals("Zapasy")){
            i = (weight * 0.175)/63;
            kcal = i * weight * time;
        }

        kcal = Math.abs(kcal);
        kcal *= 100;
        kcal = Math.round(kcal);
        kcal /= 100;
        return kcal;
    }
}
