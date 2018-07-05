package com.example.mirek.diabetapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class PhysicalStatisticsActivity extends AppCompatActivity{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

    GraphView mGraphView;
    LineGraphSeries series;

    private TextView txtFrom;
    private TextView txtTo;

    private Button btnFrom;
    private Button btnTo;

    DateFormat formatDate = DateFormat.getDateInstance();
    Calendar dateFrom = Calendar.getInstance();
    Calendar dateTo = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_statistics);

        txtFrom = findViewById(R.id.txtFrom);
        txtTo = findViewById(R.id.txtTo);

        btnFrom = findViewById(R.id.btnTimeFrom);
        btnTo = findViewById(R.id.btnTimeTo);

        btnFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFrom();
            }
        });

        btnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTo();
            }
        });


        mGraphView = findViewById(R.id.physicalGraph);
        series = new LineGraphSeries();
        mGraphView.addSeries(series);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(user.getUid()).child("physical_activity");


        mGraphView.getViewport().setScrollable(true);


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

    private void updateFrom(){
        new DatePickerDialog(this, d, dateFrom.get(Calendar.YEAR), dateFrom.get(Calendar.MONTH), dateFrom.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTo(){
        new DatePickerDialog(this, d1, dateTo.get(Calendar.YEAR), dateTo.get(Calendar.MONTH), dateTo.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dateFrom.set(Calendar.YEAR, i);
            dateFrom.set(Calendar.MONTH, i1);
            dateFrom.set(Calendar.DAY_OF_MONTH, i2);

            updateFromLabel();

        }
    };

    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dateTo.set(Calendar.YEAR, i);
            dateTo.set(Calendar.MONTH, i1);
            dateTo.set(Calendar.DAY_OF_MONTH, i2);
            updateToLabel();

            ValueEventListener valueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long timeFrom = dateFrom.getTime().getTime(), timeTo = dateTo.getTime().getTime();
                    ArrayList<DataPoint> dpArrayList = new ArrayList<DataPoint>();

                    for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                        String data = mDataSnapshot.getKey();

                        try{
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM dd yyyy hh:mm");
                            Date xValue = simpleDateFormat.parse(data);
                            long time = xValue.getTime();

                            if(time>= timeFrom && time <= timeTo){

                            for(DataSnapshot dataSnapshot1 : mDataSnapshot.getChildren()) {
                                try {
                                    double value = dataSnapshot1.getValue(double.class);
                                    dpArrayList.add(new DataPoint(xValue,value));
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            }


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    DataPoint[] dp = dpArrayList.toArray(new DataPoint[dpArrayList.size()]);
                    series.resetData(dp);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    };

    private void updateFromLabel(){

        txtFrom.setText(formatDate.format(dateFrom.getTime()));
    }
    private void updateToLabel(){

        txtTo.setText(formatDate.format(dateTo.getTime()));
    }
}
