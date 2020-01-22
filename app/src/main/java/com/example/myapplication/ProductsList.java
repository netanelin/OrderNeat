package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;

class ProductsList extends ArrayAdapter<Map<String, Object>> {
    private Activity context;
    private List<Map<String, Object>> products_list;

    public ProductsList(Activity context, List<Map<String, Object>> products_list){
        super(context, R.layout.product_list_layout, products_list);
        this.context = context;
        this.products_list = products_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.product_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewPrice = (TextView) listViewItem.findViewById(R.id.textViewPrice);
        TextView textViewDescription = (TextView) listViewItem.findViewById(R.id.textViewDescription);
        ImageView imageView = (ImageView) listViewItem.findViewById(R.id.imageView);


        Map<String, Object> product = products_list.get(position);

        textViewName.setText((String)product.get("name"));
        textViewDescription.setText((String)product.get("description"));
        textViewPrice.setText(product.get("price").toString() + " nis");

        if ((product.get("name").toString().equals("Sandwich")))
        {
            imageView.setImageResource(R.drawable.sandwich);
        }
        else if ((product.get("name").toString().equals("Salad")))
        {
            imageView.setImageResource(R.drawable.salad);
        }
        else if ((product.get("name").toString().equals("Hamburger")))
        {
            imageView.setImageResource(R.drawable.hamburger);
        }
        else if ((product.get("name").toString().equals("Coca Cola")))
        {
            imageView.setImageResource(R.drawable.coca_cola);
        }
        else if ((product.get("name").toString().equals("Beer")))
        {
            imageView.setImageResource(R.drawable.beer);
        }
        else  if ((product.get("name").toString().equals("Sushi")))
        {
            imageView.setImageResource(R.drawable.sushi);
        }
        else  if ((product.get("name").toString().equals("Lemon")))
        {
            imageView.setImageResource(R.drawable.lemon);
        }
        else {
            imageView.setImageResource(R.drawable.chef);
        }

        return listViewItem;
    }
}
