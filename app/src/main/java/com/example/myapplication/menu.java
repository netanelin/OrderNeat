package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class menu extends AppCompatActivity {

    private ListView listViewProducts;
    private List<Map<String, Object>> productsList;
    private FirebaseFirestore db;
    private static final String TAG = "Menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        listViewProducts = (ListView) findViewById(R.id.listViewProducts);
        productsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

//        Map<String, Object> m1 = new HashMap<>();
//        m1.put("name", "Humburger");
//        m1.put("description", "Atomic Hum");
//        m1.put("price", 32);
//
//        Map<String, Object> m2 = new HashMap<>();
//        m1.put("name", "Sandwich");
//        m1.put("description", "Good vibes Sandwich");
//        m1.put("price", 25);
//
//        Map<String, Object> m3 = new HashMap<>();
//        m1.put("name", "Salad");
//        m1.put("description", "Refreshing israely Salad");
//        m1.put("price", 19);
//
//        Vector<Map> productsList = new Vector<>();
//        productsList.add(m1);
//        productsList.add(m2);
//        productsList.add(m3);
//
//        Map<String, Object> productsList[] = new Map[]{m1, m2, m3};
//
//        ListView listview = findViewById(R.id.lv_products_list);
//        ArrayAdapter<Map<String, Object>> adapter = new ArrayAdapter<Map<String, Object>>(this, android.R.layout.simple_list_item_1, productsList);
//        listview.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ///--------debug--------
        Toast.makeText(menu.this,"debugging: inside menu::onStart", Toast.LENGTH_LONG);
        ///--------debug--------

        db.collection("Products")
                //.whereEqualTo("available", true)//
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        ///--------debug--------
                        Toast.makeText(menu.this,"debugging: inside menu::onEvent", Toast.LENGTH_LONG);
                        ///--------debug--------

                        if (e != null) {
                            Log.w(TAG, "Listen to products list failed.", e);

                            ///--------debug--------
                            Toast.makeText(menu.this,"error: Listen to products list failed.", Toast.LENGTH_LONG);
                            ///--------debug--------

                            return;
                        }
                        productsList.clear();
                        for (QueryDocumentSnapshot productSnapshot : queryDocumentSnapshots)
                            if((boolean)productSnapshot.get("available") == true)
                                productsList.add(productSnapshot.getData());
                        Log.d(TAG, "Current available products: " + productsList);
                        //creating new adapter
                        ProductsList adapter = new ProductsList(menu.this, productsList);

                        ///--------debug--------
                        System.out.println(productsList);
                        Toast.makeText(menu.this,"we have got products list from firestore", Toast.LENGTH_LONG);
                        ///--------debug--------

                        //giving the adapter to the listView
                        listViewProducts.setAdapter(adapter);
                    }
                });
    }
}
