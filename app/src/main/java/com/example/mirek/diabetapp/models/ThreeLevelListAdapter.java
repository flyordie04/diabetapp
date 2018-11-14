package com.example.mirek.diabetapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.mirek.diabetapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Mirek on 14.11.2018.
 */

public class ThreeLevelListAdapter extends BaseExpandableListAdapter{

    String[] parentHeader;
    List<String[]> secondLevel;
    private Context mContext;
    List<LinkedHashMap<String, String[]>> data;

    public ThreeLevelListAdapter(Context context, String[] parentHeader, List<String[]> secondLevel, List<LinkedHashMap<String, String[]>> data){
        this.mContext = context;
        this.parentHeader = parentHeader;
        this.secondLevel = secondLevel;
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return parentHeader.length;
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return i;
    }

    @Override
    public Object getChild(int i, int i1) {
        return i1;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_header, null);
        TextView text = view.findViewById(R.id.lblListHeader);
        text.setText(this.parentHeader[i]);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final SecondLevelExpandableListView secondLevelELV = new SecondLevelExpandableListView(mContext);
        String[] headers = secondLevel.get(i);

        List<String[]> childData = new ArrayList<>();
        HashMap<String, String[]> secondLevelData = data.get(i);

        for(String key : secondLevelData.keySet()){
            childData.add(secondLevelData.get(key));
        }

        secondLevelELV.setAdapter(new SecondLevelAdapter(mContext, headers, childData));
        secondLevelELV.setGroupIndicator(null);

        secondLevelELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener(){
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    secondLevelELV.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        return secondLevelELV;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
