package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class orders_panel extends AppCompatActivity {

    private FirebaseAuth user_auth ;
    private static final String TAG = "orders_panel";

    //Contents
    private ListView waiting_list_view;
    private FirebaseFirestore db;
    //For the list view
    private Map<String, Object> order;
    private List<Map<String, Object>> waiting_list;
    private int currentClickedWaitingOrder;
    private Button button_order_archive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_panel);
        waiting_list_view = (ListView) findViewById(R.id.listView_waiting_for_approval);
        user_auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        waiting_list = new ArrayList<>();
        button_order_archive = findViewById(R.id.button_order_archive);

        button_order_archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(orders_panel.this, orders_archive.class);
                startActivity(i);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        //When the dish is not served yet!!
        db.collection("Orders").whereEqualTo("served",false).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen to Orders list failed.", e);
                    return;
                }

                //empty products list before getting the new one from firestore
                waiting_list.clear();
                //for each document in the collection
                for (QueryDocumentSnapshot orderSnapshot : queryDocumentSnapshots)
                    //add the product to the list as a Map<String, Object>
                    waiting_list.add(orderSnapshot.getData());
                Log.d(TAG, "Current Waiting Orders: " + waiting_list);
                //creating new adapter and giving him this activity as context and the current list of products
                OrdersList adapter = new OrdersList(orders_panel.this, waiting_list);

                //giving the adapter to the listView
                waiting_list_view.setAdapter(adapter);

            }
        });

        waiting_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClickedWaitingOrder = position;
                openApprovalDialog();
            }
        });
    }


    private void openApprovalDialog() {
        Map<String, Object> chosen_product = waiting_list.get(currentClickedWaitingOrder);
        String message = "Order description:\n";
        List<Map<String, Object>> itemsList = (List<Map<String, Object>>)chosen_product.get("items");
        for (Map<String, Object> item : itemsList){
            message += "item name: " + item.get("name")+"\n";
            message += "quantity: " +item.get("quantity")+"\n\n";
        }

        AlertDialog alertDialog = new AlertDialog.Builder(orders_panel.this).create();
        alertDialog.setTitle("Serve order\n");
        alertDialog.setMessage("Please confirm order has been served\n" + message);
        alertDialog.setIcon(R.drawable.chef);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> chosen_product = waiting_list.get(currentClickedWaitingOrder);
                        String nameOfOrder = chosen_product.get("OID").toString();
                        db.collection("Orders").document(nameOfOrder).update("served",true);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

//
//    public String convertWithIteration(Map<String, Object> chosen_product) {
//        StringBuilder mapAsString = new StringBuilder();
//        List<Map<String, Object>> items = (List<Map<String, Object>>) chosen_product.get("items");
//        int i = 0;
//        for (Map<String, Object> item : items){
//            mapAsString.append("Dish Name: "+items.get(i).get("name"));
//            mapAsString.append("\n");
//            mapAsString.append("Price: "+items.get(i).get("price")+" ILS");
//            mapAsString.append("\n");
//            mapAsString.append("Quantity: "+items.get(i).get("quantity"));
//            mapAsString.append("\n");
//            i++;
//            mapAsString.append("\n");
//            mapAsString.append("\n");
//
//        }
//        mapAsString.append("Total: "+chosen_product.get("total").toString()+" ILS");
//        mapAsString.append("\n");
//        mapAsString.append("\n");
//        if(chosen_product.get("comments").toString().equals(null)){
//            mapAsString.append("Comments: Nothing");
//        }else {
//            mapAsString.append("Comments: "+chosen_product.get("comments").toString());
//        }
//
//
//       return mapAsString.toString();
//    }
}
