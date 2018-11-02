package com.example.mirek.diabetapp.models;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by Mirek on 01.11.2018.
 */

public interface FirebaseCallbackPhysical {
    void onCallback(DataPoint[] value, ArrayList<String> type);
}
