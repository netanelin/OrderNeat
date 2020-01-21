package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orders_panel extends AppCompatActivity {

    private FirebaseAuth user_auth ;
    private static final String TAG = "orders_panel";

    //Contents
    private ListView lvUp;
    private ListView lvDown;
    private FirebaseFirestore db;
    //For the list view
    private Map<String, Object> order;
    private List<Map<String, Object>> wfApprove;
    private List<Map<String, Object>> Approved;

    private int currentClickedOrderWFS;
    private int currentClickedOrderS;

    private Order orderchoosed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_panel);


        lvUp = (ListView) findViewById(R.id.lvWFapproval);
        lvDown = (ListView) findViewById(R.id.lvApproved);


        user_auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        wfApprove = new ArrayList<>();
        Approved = new ArrayList<>();


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
                wfApprove.clear();
                //for each document in the collection
                for (QueryDocumentSnapshot orderSnapshot : queryDocumentSnapshots)
                    //add the product to the list as a Map<String, Object>
                    wfApprove.add(orderSnapshot.getData());
                Log.d(TAG, "Current Waiting Orders: " + wfApprove);
                //creating new adapter and giving him this activity as context and the current list of products
                OrdersList adapter = new OrdersList(orders_panel.this, wfApprove);

                //giving the adapter to the listView
                lvUp.setAdapter(adapter);

            }
        });

        lvUp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClickedOrderWFS = position;
                openDialog1();
            }
        });



        //When served

        db.collection("Orders").whereEqualTo("served",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen to Orders list failed.", e);
                    return;
                }

                //empty products list before getting the new one from firestore
                Approved.clear();
                //for each document in the collection
                for (QueryDocumentSnapshot orderSnapshot : queryDocumentSnapshots)
                    //add the product to the list as a Map<String, Object>
                    Approved.add(orderSnapshot.getData());
                Log.d(TAG, "Current Waiting Orders: " + Approved);
                //creating new adapter and giving him this activity as context and the current list of products
                OrdersList adapter = new OrdersList(orders_panel.this, Approved);

                //giving the adapter to the listView
                lvDown.setAdapter(adapter);

            }
        });

        lvDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentClickedOrderS = position;
                openDialog2();
            }
        });





    }








//**************************************************************************************************
    private void openDialog1() {
        AlertDialog alertDialog = new AlertDialog.Builder(orders_panel.this).create();
        alertDialog.setTitle("Order Confirmation\n");
        alertDialog.setMessage("Are you sure to confirm the order?");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> chosen_product = wfApprove.get(currentClickedOrderWFS);
                        String nameOfOrder = chosen_product.get("OID").toString();
                        db.collection("Orders").document(nameOfOrder).update("served",true);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }





    private void openDialog2() {
        AlertDialog alertDialog = new AlertDialog.Builder(orders_panel.this).create();
        alertDialog.setTitle("Order Details\n");
        Map<String,Object> chosen_product = Approved.get(currentClickedOrderS);
        String str = convertWithIteration(chosen_product);

        alertDialog.setMessage(str);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }
    public String convertWithIteration(Map<String, Object> chosen_product) {
        StringBuilder mapAsString = new StringBuilder();
        List<Map<String, Object>> items = (List<Map<String, Object>>) chosen_product.get("items");
        int i = 0;
        for (Map<String, Object> item : items){
            mapAsString.append("Dish Name: "+items.get(i).get("name"));
            mapAsString.append("\n");
            mapAsString.append("Price: "+items.get(i).get("price")+" ILS");
            mapAsString.append("\n");
            mapAsString.append("Quantity: "+items.get(i).get("quantity"));
            mapAsString.append("\n");
            i++;
            mapAsString.append("\n");
            mapAsString.append("\n");

        }
        mapAsString.append("Total: "+chosen_product.get("total").toString()+" ILS");
        mapAsString.append("\n");
        mapAsString.append("\n");
        if(chosen_product.get("comments").toString().equals(null)){
            mapAsString.append("Comments: Nothing");
        }else {
            mapAsString.append("Comments: "+chosen_product.get("comments").toString());
        }


//        StringBuilder mapAsString = new StringBuilder();
//        for (String key : chosen_product.keySet()) {
//                mapAsString.append(key + ": " + chosen_product.get(key));
//                mapAsString.append("\n");
//        }
//        //mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
       return mapAsString.toString();
    }
}
