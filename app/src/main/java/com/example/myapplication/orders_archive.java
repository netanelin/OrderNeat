package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class orders_archive extends AppCompatActivity {
//    private FirebaseAuth user_auth ;
    private static final String TAG = "order_archive";

    private ListView listView_approved;
    private FirebaseFirestore db;
//    private Map<String, Object> order;
    private List<Map<String, Object>> Approved;
    //private int currentClickedApprovedOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_archive);
        listView_approved = (ListView) findViewById(R.id.listView_approved);
        db = FirebaseFirestore.getInstance();
        Approved = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        db.collection("Orders").whereEqualTo("served",true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen to Order archive list failed.", e);
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
                OrdersList adapter = new OrdersList(orders_archive.this, Approved);

                //giving the adapter to the listView
                listView_approved.setAdapter(adapter);

            }
        });

//        listView_approved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                currentClickedApprovedOrder = position;
//                openDialog2();
//            }
//        });
    }

//    private void openDialog2() {
//        AlertDialog alertDialog = new AlertDialog.Builder(orders_panel.this).create();
//        alertDialog.setTitle("Order Details\n");
//        alertDialog.setIcon(R.drawable.chef);
//        Map<String,Object> chosen_product = Approved.get(currentClickedWaitingOrder);
//        String str = convertWithIteration(chosen_product);
//
//        alertDialog.setMessage(str);
//        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Back",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        alertDialog.show();
//
//    }
}
