package com.example.mirek.diabetapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PhysicalStatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

    GraphView mGraphView;
    LineGraphSeries series;

    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_statistics);

        mSpinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.length, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mGraphView = findViewById(R.id.physicalGraph);
        series = new LineGraphSeries();
        mGraphView.addSeries(series);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(user.getUid()).child("physical_activity");

        mGraphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX){
                if(isValueX){
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        ValueEventListener valueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    String data = mDataSnapshot.getKey();
                    Log.e("data string", data);

                    try{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                        Date xValue = simpleDateFormat.parse(data);

                        for(DataSnapshot dataSnapshot1 : mDataSnapshot.getChildren()){
                            try {
                                double value = dataSnapshot1.getValue(double.class);
                                dp[index] = new DataPoint(xValue, value);
                                Log.e("datapoint", dp[index].toString());
                                index++;
                            } catch (NumberFormatException ex){
                                ex.printStackTrace();
                            }

                        }


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                series.resetData(dp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
