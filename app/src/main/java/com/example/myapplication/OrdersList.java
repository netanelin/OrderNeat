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

class OrdersList extends ArrayAdapter<Map<String, Object>> {
    private Activity context;
    private List<Map<String, Object>> OrderList;

    public OrdersList(Activity context, List<Map<String, Object>> OrderList){
        super(context, R.layout.list_layout, OrderList);
        this.context = context;
        this.OrderList = OrderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewOrder = inflater.inflate(R.layout.list_order, null, true);
        TextView textViewOrderTime = (TextView) listViewOrder.findViewById(R.id.tvOrderTime);
        TextView textTableNum = (TextView) listViewOrder.findViewById(R.id.tvTableNum);
        TextView textViewTotalPrice = (TextView) listViewOrder.findViewById(R.id.tvTotalPrice);



        Map<String, Object> order = OrderList.get(position);

        textViewOrderTime.setText("Order ID: "+ (order.get("AID")));
        textTableNum.setText("Table Number: "+order.get("table_num"));
        textViewTotalPrice.setText("Total Price: "+order.get("total_price"));

        return listViewOrder;
    }
}