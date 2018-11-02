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
import android.widget.Toast;

import com.example.mirek.diabetapp.models.FirebaseCallback;
import com.example.mirek.diabetapp.models.FirebaseCallbackPhysical;
import com.example.mirek.diabetapp.models.InsulinData;
import com.example.mirek.diabetapp.models.PhysicalData;
import com.example.mirek.diabetapp.models.ResultsData;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivity extends AppCompatActivity {

    GraphView mGraphView;
    LineGraphSeries results1;
    PointsGraphSeries results2;
    BarGraphSeries insulin1;
    LineGraphSeries physical1;
    PointsGraphSeries physical2;
    //PointsGraphSeries insulin2;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm dd-MM-YYYY");

    private TextView txtFrom;
    private TextView txtTo;

    DateFormat formatDate = DateFormat.getDateInstance();

    Calendar dateToDB = Calendar.getInstance();
    Calendar dateFromDB = Calendar.getInstance();


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

        diabetesChart.setChecked(true);
        insulinChart.setChecked(true);
        physicalChart.setChecked(true);

        dateFromDB.add(Calendar.DAY_OF_MONTH, -7);

        updateTextLabel();

        mGraphView = findViewById(R.id.statisticsGraph);


        //MENU
        Toolbar toolbar = findViewById(R.id.statisticsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Statystyki");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);





        results1 = new LineGraphSeries();
        results2 = new PointsGraphSeries();
        insulin1 = new BarGraphSeries();
        physical1 = new LineGraphSeries();
        physical2 = new PointsGraphSeries();
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


        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setMinY(0.0);
        mGraphView.getViewport().setMaxY(200.0);
        mGraphView.getViewport().setXAxisBoundsManual(true);
        mGraphView.getGridLabelRenderer().setVerticalAxisTitle("Wartość");
        //mGraphView.getGridLabelRenderer().setNumHorizontalLabels(2);
        mGraphView.getGridLabelRenderer().setHorizontalAxisTitle("Zakres czasowy");
        //mGraphView.getGridLabelRenderer().setPadding(2);
        //mGraphView.getViewport().setMinX(dateFromDB.getTime().getTime());
        //mGraphView.getViewport().setMaxX(dateToDB.getTime().getTime());


        changeDiabete();
        changeInsulin();
        changePhysical();
        drawPhysical();
        drawInsulin();
        drawDiabete();
        updateFrom();


        mGraphView.getViewport().setScalable(true);
        //mGraphView.getViewport().setScrollable(true);


    }

    private void updateGraph(){
        mGraphView.getViewport().setMinX(dateFromDB.getTime().getTime());
        mGraphView.getViewport().setMaxX(dateToDB.getTime().getTime());
        if(!insulinChart.isChecked()){
            mGraphView.removeSeries(insulin1);
        }
        if(!diabetesChart.isChecked()){
            mGraphView.removeSeries(results1);
            mGraphView.removeSeries(results2);
        }
        if(!physicalChart.isChecked()){
            mGraphView.removeSeries(physical1);
            mGraphView.removeSeries(physical2);
        }
    }
    private void changePhysical(){
        physicalChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(physicalChart.isChecked()){
                    updateGraph();
                    drawPhysical();
                } else {
                    mGraphView.removeSeries(physical1);
                    mGraphView.removeSeries(physical2);
                    updateGraph();
                }
            }

        });
    }
    private void drawPhysical(){
        mGraphView.removeSeries(physical1);
        mGraphView.removeSeries(physical2);

        if(physicalChart.isChecked()){
            final PhysicalData physicalData = new PhysicalData();
            physicalData.Physical(dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());
            DataPoint[] sth = new DataPoint[0];

            physicalData.readData(new FirebaseCallbackPhysical() {
                @Override
                public void onCallback(final DataPoint[] value, final ArrayList type) {
                    if (value != null) {
                        physical1.resetData(value);
                        physical2.resetData(value);


                            //physical1.appendData(value[i],false, value.length);
                            //physical2.appendData(value[i],false, value.length);
                            //Log.e("Wartość",String.valueOf(value[i].getY()));
                            //Log.e("długość", String.valueOf(value.length));
                            //Log.e("data",sdf2.format(new Date((long) value[i].getX())));

                            physical2.setOnDataPointTapListener(new OnDataPointTapListener() {
                                @Override
                                public void onTap(Series series, DataPointInterface dataPoint) {
                                    for(int i = 0; i<value.length;i++) {
                                        if(dataPoint.getX()==value[i].getX()) {
                                            final String typeName = (String) type.get(i);
                                            Toast.makeText(StatisticsActivity.this, "Rodzaj aktywności: " + typeName + "\nIlość spalonych kalorii: " + dataPoint.getY() * 10 + "\nData: " + sdf2.format(new Date((long) dataPoint.getX())), Toast.LENGTH_SHORT).show();
                                        }
                                    }}
                            });
                            //physical1.resetData(value);
                            //physical2.resetData(value);
                        //}
                        Log.e("type adsf",type.toString());
                    } else {
                        mGraphView.removeSeries(physical1);
                        mGraphView.removeSeries(physical2);
                        Toast.makeText(StatisticsActivity.this, "Brak aktywności fizycznej dla wskazanego przedziału czasowego.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());

            if(!physical1.isEmpty() && !physical2.isEmpty()){
                physical1.setColor(Color.GREEN);
                mGraphView.addSeries(physical1);
                physical2.setColor(Color.GREEN);
                mGraphView.addSeries(physical2);


            }

        } else {
            updateGraph();
        }
    }
    private void changeInsulin(){

        insulinChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(insulinChart.isChecked()){
                    updateGraph();
                    drawInsulin();
                } else {
                    mGraphView.removeSeries(insulin1);
                    updateGraph();
                }
                }

        });
    }

    private void drawInsulin(){
        mGraphView.removeSeries(insulin1);
        if(insulinChart.isChecked()){
            final InsulinData insulinData = new InsulinData();
            insulinData.Insulin(dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());

            insulinData.readData(new FirebaseCallback() {
                @Override
                public void onCallback(DataPoint[] value) {
                    if(value != null){
                    insulin1.resetData(value);
                    } else {
                        mGraphView.removeSeries(insulin1);
                        Toast.makeText(StatisticsActivity.this, "Brak dawek insuliny dla wskazanego przedziału czasowego.", Toast.LENGTH_SHORT).show();
                    }
                }
            }, dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());

            if(!insulin1.isEmpty()){
                insulin1.setColor(Color.BLUE);
                insulin1.setDrawValuesOnTop(true);
                insulin1.setValuesOnTopColor(Color.BLUE);
                mGraphView.addSeries(insulin1);
                insulin1.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(StatisticsActivity.this, "Dawka insuliny: " + dataPoint.getY() + "\nData: " + sdf2.format(new Date((long) dataPoint.getX())) ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            updateGraph();
        }

    }
    private void changeDiabete(){
        diabetesChart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(diabetesChart.isChecked()) {
                    drawDiabete();
                } else {
                    mGraphView.removeSeries(results1);
                    mGraphView.removeSeries(results2);
                    updateGraph();
                    Log.e("usuwanie","usuwanie");
                }
            }

        });

    }
    private void drawDiabete(){
        mGraphView.removeSeries(results1);
        mGraphView.removeSeries(results2);
        if(diabetesChart.isChecked()){
            final ResultsData resultsData = new ResultsData();
            resultsData.Results(dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());

                    resultsData.readData(new FirebaseCallback() {
                        @Override
                        public void onCallback(DataPoint[] value) {
                            if (value != null) {
                                results1.resetData(value);
                                results2.resetData(value);
                            } else {
                                mGraphView.removeSeries(results1);
                                mGraphView.removeSeries(results2);
                                Toast.makeText(StatisticsActivity.this, "Brak pomiarów cukru dla wskazanego przedziału czasowego.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, dateFromDB.getTime().getTime(), dateToDB.getTime().getTime());

                    if(!results1.isEmpty() && !results2.isEmpty()){
                        results1.setColor(Color.RED);
                        mGraphView.addSeries(results1);
                        results2.setColor(Color.RED);
                        mGraphView.addSeries(results2);
                        results2.setOnDataPointTapListener(new OnDataPointTapListener() {
                            @Override
                            public void onTap(Series series, DataPointInterface dataPoint) {
                                Toast.makeText(StatisticsActivity.this, "Cukier: " + dataPoint.getY() + "\nData: " + sdf2.format(new Date((long) dataPoint.getX())),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                } else {
                    updateGraph();
                }
    }


    private void updateFrom(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, d, dateFromDB.get(Calendar.YEAR), dateFromDB.get(Calendar.MONTH), dateFromDB.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Data początkowa zakresu");
        datePickerDialog.show();
    }

    private void updateTo(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, d1, dateToDB.get(Calendar.YEAR), dateToDB.get(Calendar.MONTH), dateToDB.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Data końcowa zakresu");
        datePickerDialog.show();
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
        updateGraph();
        drawInsulin();
        drawDiabete();
        drawPhysical();
    }
    private void updateToLabel(){
        txtTo.setText(formatDate.format(dateToDB.getTime()));
        updateGraph();
        drawInsulin();
        drawDiabete();
        drawPhysical();
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
