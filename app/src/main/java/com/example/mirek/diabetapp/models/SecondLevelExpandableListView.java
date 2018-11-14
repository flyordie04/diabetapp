package com.example.mirek.diabetapp.models;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by Mirek on 14.11.2018.
 */

public class SecondLevelExpandableListView extends ExpandableListView {

    public SecondLevelExpandableListView(Context context){
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
