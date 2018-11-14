package com.example.mirek.diabetapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.mirek.diabetapp.R;

import java.util.List;

/**
 * Created by Mirek on 14.11.2018.
 */

public class SecondLevelAdapter extends BaseExpandableListAdapter {

    private Context context;
    List<String[]> data;
    String[] headers;

    public SecondLevelAdapter(Context context, String[] headers, List<String[]> data){
        this.context = context;
        this.headers = headers;
        this.data = data;
    }

    @Override
    public int getGroupCount() {
        return headers.length;
    }

    @Override
    public int getChildrenCount(int i) {
        String[] children = data.get(i);
        return children.length;
    }

    @Override
    public Object getGroup(int i) {
        return headers[i];
    }

    @Override
    public Object getChild(int i, int i1) {
        String[] childData;
        childData = data.get(i);
        return childData[i1];
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_group,null);
        TextView text = view.findViewById(R.id.lblListGroup);
        String groupText = getGroup(i).toString();
        text.setText(groupText);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item,null);

        TextView textView = view.findViewById(R.id.lblListItem);

        String[] childArray = data.get(i);
        String text = childArray[i1];
        textView.setText(text);

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
