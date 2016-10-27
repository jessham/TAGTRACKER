package com.weebly.taggtracker.tagtracker;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Barbara on 27/10/2016.
 */

public class ListCheckboxAdapter extends ArrayAdapter<String> {
    private List<String> myFriends;
    private Activity activity;
    private int selectedPosition = -1;


    public ListCheckboxAdapter(Activity context, int resource, List<String> objects) {
        super(context, resource, objects);

        this.activity = context;
        this.myFriends = objects;
    }



}
