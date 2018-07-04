package com.example.mirek.diabetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
    }
    public void diabeteStatistics(View v){
        Intent i = new Intent(StatisticsActivity.this, DiabeteStatisticsActivity.class);
        startActivity(i);
    }
    public void physicalStatistics(View v){
        Intent i = new Intent(StatisticsActivity.this, PhysicalStatisticsActivity.class);
        startActivity(i);
    }
}
