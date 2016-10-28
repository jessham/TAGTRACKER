package com.weebly.taggtracker.tagtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Barbara on 27/10/2016.
 */

public class ListCheckboxAdapter extends ArrayAdapter<String> {
    ArrayList<String> dados;

        public ListCheckboxAdapter(Context context, ArrayList<String> dados) {
            super(context, android.R.layout.simple_list_item_multiple_choice, dados);
            this.dados = dados;
        }

        @Override
        //the method that returns the actual view used as a row within the ListView at a particular position
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String item = dados.get(position);

            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview, parent, false);
            }

            // Lookup view for data population
            TextView txtRotulo = (TextView) convertView.findViewById(R.id.rotulo);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check);


            // Populate the data into the template view using the data object
            txtRotulo.setText(item);
            checkBox.setFocusable(false);

            // Return the completed view to render on screen
            return convertView;
        }


}
