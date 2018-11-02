package com.example.mirek.diabetapp.models;

import android.text.TextUtils;
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Mirek on 22.10.2018.
 */

public class PhysicalData {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DataPoint[] results;


    public DataPoint[] Physical(long timeFrom, long timeTo){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();


        final FirebaseCallbackPhysical firebaseCallback = new FirebaseCallbackPhysical() {
            @Override
            public void onCallback(DataPoint[] value, ArrayList type) {
                results = value;
            }
        };
            readData(firebaseCallback, timeFrom, timeTo);

        if(results != null) {
            return results;
        } else return null;
    }


    public void readData(final FirebaseCallbackPhysical firebaseCallback, final long timeFrom, final long timeTo) {
        mDatabaseReference.child("users").child(user.getUid()).child("physical_activity").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DataPoint> dpArrayList = new ArrayList<>();
                ArrayList<String> typeList = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    String data = mDataSnapshot.getKey();

                    try{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                        Date xValue = simpleDateFormat.parse(data);
                        long time = xValue.getTime();
                        Log.e("date physical",xValue.toString());
                        if(time>= timeFrom && time <= timeTo){

                        try {
                            for (DataSnapshot dataSnapshot1 : mDataSnapshot.getChildren()){
                                String type = dataSnapshot1.getKey();
                                double value = dataSnapshot1.getValue(Double.class) / 10;
                                Log.e("type",type);
                                dpArrayList.add(new DataPoint(xValue,value));
                                typeList.add(type);
                            }
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                        }



                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DataPoint[] dp = dpArrayList.toArray(new DataPoint[dpArrayList.size()]);
                String type = TextUtils.join(", ",typeList);
                Log.e("typearray",type);
                if(dp.length > 0){
                    firebaseCallback.onCallback(dp, typeList);
                } else {
                    firebaseCallback.onCallback(null,null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }
        );
    }
}
