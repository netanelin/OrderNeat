package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    }

    @Override
    protected void onStart() {
        super.onStart();

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
                            //if the product is available
                           // if((boolean)productSnapshot.get("available") == true)
                                //add the product to the list as a Map<String, Object>
                                productsList.add(productSnapshot.getData());
                        Log.d(TAG, "Current available products: " + productsList);
                        //creating new adapter and giving him this activity as context and the current list of products
                        ProductsList adapter = new ProductsList(menu.this, productsList);

                        //giving the adapter to the listView
                        listViewProducts.setAdapter(adapter);
                    }
                });

        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item Map object
                Map<String, Object> selectedItem = (Map<String, Object>) parent.getItemAtPosition(position);


            }
        });
    }
}
