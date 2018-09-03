package com.example.mirek.diabetapp;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

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

public class DiabeteStatisticsActivity extends AppCompatActivity{

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");

    GraphView mGraphView;
    LineGraphSeries series;

    private TextView txtFromDB;
    private TextView txtToDB;

    private Button btnFromDB;
    private Button btnToDB;

    DateFormat formatDate = DateFormat.getDateInstance();
    Calendar dateFromDB = Calendar.getInstance();
    Calendar dateToDB = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diabete_statistics);

        txtFromDB = findViewById(R.id.txtFromDB);
        txtToDB = findViewById(R.id.txtToDB);

        btnFromDB = findViewById(R.id.btnTimeFromDB);
        btnToDB = findViewById(R.id.btnTimeToDB);

        btnFromDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFrom();
            }
        });

        btnToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTo();
            }
        });


        mGraphView = findViewById(R.id.diabetesGraph);
        series = new LineGraphSeries();
        mGraphView.addSeries(series);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(user.getUid()).child("diabetes");


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
        new DatePickerDialog(this, d, dateFromDB.get(Calendar.YEAR), dateFromDB.get(Calendar.MONTH), dateFromDB.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateTo(){
        new DatePickerDialog(this, d1, dateToDB.get(Calendar.YEAR), dateToDB.get(Calendar.MONTH), dateToDB.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dateFromDB.set(Calendar.YEAR, i);
            dateFromDB.set(Calendar.MONTH, i1);
            dateFromDB.set(Calendar.DAY_OF_MONTH, i2);

            updateFromLabel();

        }
    };

    DatePickerDialog.OnDateSetListener d1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            dateToDB.set(Calendar.YEAR, i);
            dateToDB.set(Calendar.MONTH, i1);
            dateToDB.set(Calendar.DAY_OF_MONTH, i2);
            updateToLabel();

            ValueEventListener valueEventListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long timeFrom = dateFromDB.getTime().getTime(), timeTo = dateToDB.getTime().getTime();
                    ArrayList<DataPoint> dpArrayList = new ArrayList<DataPoint>();

                    for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                        String data = mDataSnapshot.getKey();
                        Log.e("data",data);

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
                    series.resetData(dp);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    };

    private void updateFromLabel(){

        txtFromDB.setText(formatDate.format(dateFromDB.getTime()));
    }
    private void updateToLabel(){

        txtToDB.setText(formatDate.format(dateToDB.getTime()));
    }
}
