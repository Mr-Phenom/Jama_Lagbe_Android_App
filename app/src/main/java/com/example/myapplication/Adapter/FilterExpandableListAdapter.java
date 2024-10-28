package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;

public class FilterExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataGroup;
    private HashMap<String, List<String>> listDataChild;

    public FilterExpandableListAdapter(Context context, List<String> listDataGroup, HashMap<String, List<String>> listDataChild) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        this.listDataChild = listDataChild;
    }

    @Override
    public int getGroupCount() {
        return listDataGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView textViewGroup = convertView.findViewById(R.id.listGroupTitle);
        textViewGroup.setText(groupTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item_child, null);
        }

        CheckBox checkBox = convertView.findViewById(R.id.checkBoxFilter);
        EditText editText = convertView.findViewById(R.id.editTextFilter);

        // Hide both views initially
        checkBox.setVisibility(View.GONE);
        editText.setVisibility(View.GONE);

        // Show the correct widget based on the group type
        if (getGroup(groupPosition).equals("Price Range")) {
            // Show EditText for "Price Range"
            editText.setVisibility(View.VISIBLE);
            editText.setHint(childText);  // Set the hint to something like "Enter min price"
        } else {
            // Show CheckBox for other filter groups (Status, Type, etc.)
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setText(childText);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Toast.makeText(context, childText + " is " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
