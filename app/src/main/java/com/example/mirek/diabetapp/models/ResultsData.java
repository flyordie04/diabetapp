package com.example.mirek.diabetapp.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mirek.diabetapp.StatisticsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ResultsData {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DataPoint[] results;


    public DataPoint[] Results(long timeFrom, long timeTo){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        final FirebaseCallback firebaseCallback = new FirebaseCallback() {
            @Override
            public void onCallback(DataPoint[] value) {
                results = value;
            }
        };
            readData(firebaseCallback, timeFrom, timeTo);

        if(results != null) {
            return results;
        } else return null;
    }


    public void readData(final FirebaseCallback firebaseCallback, final long timeFrom, final long timeTo) {
        mDatabaseReference.child("users").child(user.getUid()).child("diabetes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DataPoint> dpArrayList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    String data = mDataSnapshot.getKey();

                    try{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                        Date xValue = simpleDateFormat.parse(data);
                        long time = xValue.getTime();

                        if(time>= timeFrom && time <= timeTo){

                        try {
                            double value = Double.parseDouble(mDataSnapshot.getValue(String.class));
                            Log.e("value",String.valueOf(value));
                            dpArrayList.add(new DataPoint(xValue,value));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        }



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DataPoint[] dp = dpArrayList.toArray(new DataPoint[dpArrayList.size()]);
                if(dp.length > 0){
                    firebaseCallback.onCallback(dp);
                } else {
                    firebaseCallback.onCallback(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
        );
    }




    }