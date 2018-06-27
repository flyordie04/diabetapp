package com.example.mirek.diabetapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static java.security.AccessController.getContext;

public class DiabeteStatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner mSpinner;
    private GraphView mGraphView;


    private ArrayList<Date> dates = new ArrayList<>();
    private ArrayList<Date> hours = new ArrayList<>();
    private ArrayList<Integer> values = new ArrayList<>();


    LineGraphSeries<DataPoint> series;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabete_statistics);

        mSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.diabete_length, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(user.getUid()).child("diabetes");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getKey();

                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy",
                                Locale.ENGLISH);
                        Date parsedDate = sdf.parse(data);
                        dates.add(parsedDate);
                        int size = dates.size();
                        Log.d("qwertyu", Integer.toString(size) );
                        Log.d("qwertyu", dates.toString());
                    } catch (java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    SimpleDateFormat df3 = new SimpleDateFormat("hh:mm:ss a MMM dd, yyyy");
                    for(DataSnapshot contact : snapshot.getChildren()){
                        String hour = contact.getKey();
                        String value = contact.getValue(String.class);
                        try {
                            Date hourDate = df3.parse(hour+ " " + data);
                            hours.add(hourDate);
                            values.add(Integer.parseInt(value));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }

                graphDrawer(hours,values);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void graphDrawer(ArrayList<Date> dates, ArrayList<Integer> values){
        Log.d("graph drawer","działą");
        series = new LineGraphSeries<>(dateAdd(dates, values));
        DateFormat sdf = new SimpleDateFormat("dd/MM");
        mGraphView = findViewById(R.id.diabeteGraph);
        mGraphView.addSeries(series);

        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setMinY(0);
        mGraphView.getViewport().setMaxY(140);

        mGraphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext(), sdf));

        mGraphView.getViewport().setMinX(series.getLowestValueX());
        mGraphView.getViewport().setMaxX(series.getHighestValueX());
        mGraphView.getViewport().setXAxisBoundsManual(true);
    }
    private DataPoint[] dateAdd(ArrayList<Date> dates, ArrayList<Integer> values){
        Date[] datesString = dates.toArray(new Date[dates.size()]);
        Integer[] valuesString = values.toArray(new Integer[values.size()]);
        return getDataPoint(datesString, valuesString);

    }

    private DataPoint[] getDataPoint(Date[] datesString, Integer[] valuesString) {

        ArrayList<DataPoint> dataArray = new ArrayList<>();

        for(int i=0; i<dates.size(); i++){
            dataArray.add(new DataPoint(datesString[i],valuesString[i]));
        }
        DataPoint[] dataPoints = dataArray.toArray(new DataPoint[dataArray.size()]);
        return dataPoints;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
