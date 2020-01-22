package com.example.myapplication;

import android.app.Activity;
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
        super(context, R.layout.list_order, OrderList);
        this.context = context;
        this.OrderList = OrderList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewOrder = inflater.inflate(R.layout.list_order, null, true);
        TextView textViewOrderId = (TextView) listViewOrder.findViewById(R.id.order_id);
        TextView textTableNum = (TextView) listViewOrder.findViewById(R.id.tvTableNum);
        //TextView textViewOrderTime = (TextView) listViewOrder.findViewById(R.id.tvOrderTime);
        TextView textViewTotalPrice = (TextView) listViewOrder.findViewById(R.id.tvTotalPrice);

        Map<String, Object> order = OrderList.get(position);
        textViewOrderId.setText("Order Number: "+order.get("OID"));
        //textViewOrderTime.setText("Ordered at" + ((Timestamp)order.get("ordered_at")).);
        textTableNum.setText("Table Number: "+order.get("table"));
        textViewTotalPrice.setText("Total: "+order.get("total"));

        return listViewOrder;
    }
}
