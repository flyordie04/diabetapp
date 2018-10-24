package com.example.mirek.diabetapp.models;

import com.jjoe64.graphview.series.DataPoint;

/**
 * Created by Mirek on 21.10.2018.
 */

public interface FirebaseCallback {
    void onCallback(DataPoint[] value);
}
