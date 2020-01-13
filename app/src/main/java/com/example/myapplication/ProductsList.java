package com.example.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

class ProductsList extends ArrayAdapter<Map<String, Object>> {
    private Activity context;
    private List<Map<String, Object>> products_list;

    public ProductsList(Activity context, List<Map<String, Object>> products_list){
        super(context, R.layout.list_layout, products_list);
        this.context = context;
        this.products_list = products_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        //textViewName.setTextColor(Color.BLACK);

        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
        //textViewPrice.setTextColor(Color.BLACK);

        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewDescription);
        //textViewDescription.setTextColor(Color.BLACK);



        Map<String, Object> product = products_list.get(position);

        textViewName.setText((String)product.get("name"));
        textViewDescription.setText((String)product.get("description"));
        textViewPrice.setText(product.get("price").toString() + " nis");

        return listViewItem;
    }
}
