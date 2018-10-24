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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Mirek on 22.10.2018.
 */

public class PhysicalData {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DataPoint[] results;


    public DataPoint[] Physical(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        final FirebaseCallback firebaseCallback = new FirebaseCallback() {
            @Override
            public void onCallback(DataPoint[] value) {
                results = value;
            }
        };
        Log.e("1","1");
        readData(firebaseCallback);

        Log.e("3","3");
        if(results != null) {
            Log.e("results",results.toString());
            return results;
        }
        return results;
    }


    public void readData(final FirebaseCallback firebaseCallback) {
        mDatabaseReference.child("users").child(user.getUid()).child("physical_activity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //long timeFrom = dateFrom.getTime(), timeTo = dateTo.getTime();
                ArrayList<DataPoint> dpArrayList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    String data = mDataSnapshot.getKey();
                    Log.e("data",data);

                    try{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                        Date xValue = simpleDateFormat.parse(data);
                        //long time = xValue.getTime();

                        //if(time>= timeFrom && time <= timeTo){

                        try {
                            double value = Double.parseDouble(mDataSnapshot.getValue(String.class));
                            Log.e("value",String.valueOf(value));
                            dpArrayList.add(new DataPoint(xValue,value));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        //}



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DataPoint[] dp = dpArrayList.toArray(new DataPoint[dpArrayList.size()]);
                firebaseCallback.onCallback(dp);
                //series.resetData(dp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
        );
    }
}
