package com.example.mirek.diabetapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.mirek.diabetapp.models.FirebaseCallback;
import com.example.mirek.diabetapp.models.InsulinData;
import com.example.mirek.diabetapp.models.ResultsData;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {

    GraphView mGraphView;
    LineGraphSeries results1;
    PointsGraphSeries results2;
    BarGraphSeries insulin1;
    //PointsGraphSeries insulin2;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    private TextView txtFrom;
    private TextView txtTo;

    DateFormat formatDate = DateFormat.getDateInstance();
    Calendar dateFromDB = Calendar.getInstance();
    Calendar dateToDB = Calendar.getInstance();

    CheckBox diabetesChart;
    CheckBox physicalChart;
    CheckBox insulinChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        txtFrom = findViewById(R.id.timeFrom);
        txtTo = findViewById(R.id.timeTo);

        diabetesChart = findViewById(R.id.diabetesChart);
        physicalChart = findViewById(R.id.physicalChart);
        insulinChart = findViewById(R.id.insulinChart);

        updateTextLabel();

        mGraphView = findViewById(R.id.statisticsGraph);


        //MENU
        Toolbar toolbar = findViewById(R.id.statisticsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Statystyki");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);




        final ResultsData resultsData = new ResultsData();
        resultsData.Results();
        final InsulinData insulinData = new InsulinData();
        insulinData.Insulin();

        results1 = new LineGraphSeries();
        results2 = new PointsGraphSeries();
        insulin1 = new BarGraphSeries();
        //insulin2 = new PointsGraphSeries();




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
        diabetesChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(diabetesChart.isChecked()){
                    resultsData.readData(new FirebaseCallback() {
                        @Override
                        public void onCallback(DataPoint[] value) {
                            results1.resetData(value);
                            results2.resetData(value);
                        }
                    });
                    results1.setColor(Color.RED);
                    mGraphView.addSeries(results1);
                    results2.setColor(Color.RED);
                    mGraphView.addSeries(results2);
                } else {
                    mGraphView.removeSeries(results1);
                    mGraphView.removeSeries(results2);
                }
            }

        });
        insulinChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(insulinChart.isChecked()){
                    insulinData.readData(new FirebaseCallback() {
                        @Override
                        public void onCallback(DataPoint[] value) {
                            insulin1.resetData(value);
                            //insulin2.resetData(value);
                        }
                    }                    );
                    insulin1.setColor(Color.BLUE);
                    insulin1.setDrawValuesOnTop(true);
                    insulin1.setValuesOnTopColor(Color.BLUE);
                    mGraphView.addSeries(insulin1);


                    //insulin2.setColor(Color.BLUE);
                    //mGraphView.addSeries(insulin2);
                } else {
                    mGraphView.removeSeries(insulin1);
                    //mGraphView.removeSeries(insulin2);
                }
            }

        });


        //mGraphView.getViewport().setYAxisBoundsManual(true);
        //mGraphView.getViewport().setMinY(0);
        //mGraphView.getViewport().setMaxY(200);
        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getViewport().setScalable(true);
        mGraphView.getViewport().setScrollable(true);


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
        }
    };

    private void updateFromLabel(){
        txtFrom.setText(formatDate.format(dateFromDB.getTime()));
    }
    private void updateToLabel(){
        txtTo.setText(formatDate.format(dateToDB.getTime()));
    }

    public void timeFrom(View view) {
        updateFrom();
    }
    public void timeTo(View view) {
        updateTo();
    }
    private void updateTextLabel(){

        txtTo.setText(formatDate.format(dateToDB.getTime()));
        txtFrom.setText(formatDate.format(dateFromDB.getTime()));
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
