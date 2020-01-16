package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Timestamp;

public class menu extends AppCompatActivity implements NumpickDialog.NumpickDialogListener {
    private FirebaseAuth user_auth ;
    private static final String TAG = "Menu";
    private ListView listViewProducts;
    private TextView textViewPrice;
    private Button buttonProccedToCheckout;

    private List<Map<String, Object>> productsList;
    private FirebaseFirestore db;
    private Map<String, Object> order;
    private List<Map<String, Object>> ordered_items;
    private int currentClickedProduct;
    private double current_price;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        listViewProducts = (ListView) findViewById(R.id.listViewProducts);
        textViewPrice = findViewById(R.id.tv_price);
        buttonProccedToCheckout = findViewById(R.id.btn_procceed_to_checkout);
        productsList = new ArrayList<>();
        ordered_items = new ArrayList<>();
        current_price = 0;
        textViewPrice.setText(current_price + " nis");
        user_auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        order = new HashMap<>();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        order.put("OID", timestamp.getTime());// using timestamp as an unique order id
        order.put("UID", user_auth.getCurrentUser().getUid());
        order.put("ordered_at", timestamp);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            order.put("table", extras.getInt("table_number"));



        //
        buttonProccedToCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.put("total", current_price);
                order.put("items", ordered_items);
                Intent i = new Intent(menu.this,checkout.class);
                i.putExtra("order", (Serializable) order);
                startActivity(i);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        //show all available products in listView. (updates in realtime)
        db.collection("Products")
                .whereEqualTo("available", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen to products list failed.", e);
                            return;
                        }
                        //empty products list before getting the new one from firestore
                        productsList.clear();
                        //for each document in the collection
                        for (QueryDocumentSnapshot productSnapshot : queryDocumentSnapshots)
                                //add the product to the list as a Map<String, Object>
                                productsList.add(productSnapshot.getData());
                        Log.d(TAG, "Current available products: " + productsList);
                        //creating new adapter and giving him this activity as context and the current list of products
                        ProductsList adapter = new ProductsList(menu.this, productsList);

                        //giving the adapter to the listView
                        listViewProducts.setAdapter(adapter);
                    }
                });
        //when clicking on a product - save his position and open a quantity picker dialog
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item Map object
                //Map<String, Object> selectedItem = (Map<String, Object>) parent.getItemAtPosition(position);

                //save the position of the selected item
                currentClickedProduct = position;
                openDialog();
            }
        });
    }

    private void openDialog() {
        NumpickDialog numpickDialog = new NumpickDialog();
        numpickDialog.show(getSupportFragmentManager(), "Quantity picker");

    }

    //this function gets called when an ok button of a dialog is clicked - it gives us the chosen quantity;
    @Override
    public void applyChangesFromDialog(int quantity) {
        //add new selected item at the quantity the user chose to the orders items list;
        Map<String, Object> new_item = new HashMap<>();
        Map<String, Object> chosen_product = productsList.get(currentClickedProduct);
        double item_price_times_quantity = (long)(chosen_product.get("price"))*quantity;
        current_price += item_price_times_quantity;
        textViewPrice.setText(current_price + " nis");
        new_item.put("name", chosen_product.get("name"));
        new_item.put("quantity", quantity);
        new_item.put("price", item_price_times_quantity);
        ordered_items.add(new_item);



    }


}
