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

public class Employees_management extends AppCompatActivity {
    private FirebaseAuth user_auth ;
    private static final String TAG = "employee_management";
    private ListView listView_waiting_for_approval;
    private ListView listView_approved;
    private List<Map<String, Object>> waitingList;
    private List<Map<String, Object>> approvedList;

    private FirebaseFirestore db;
    private Map<String, Object> order;
    private int current_clicked_waiting;
    private int current_clicked_approved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);
        user_auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        listView_waiting_for_approval = findViewById(R.id.listView_waiting_for_approval);
        listView_approved = findViewById(R.id.listView_approved);
        waitingList = new ArrayList<>();
        approvedList = new ArrayList<>();
        current_clicked_waiting = 0;
        current_clicked_approved = 0;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //waiting list realtime update
        db.collection("Users")
                .whereEqualTo("role", "employee")
                .whereEqualTo("approved", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen to waiting list failed.", e);
                            return;
                        }
                        //empty products list before getting the new one from firestore
                        waitingList.clear();
                        //for each document in the collection
                        for (QueryDocumentSnapshot waitingEmployeeSnapshot : queryDocumentSnapshots) {
                            //add the product to the list as a Map<String, Object>
                            Map<String, Object> employee = waitingEmployeeSnapshot.getData();
                            employee.put("UID",waitingEmployeeSnapshot.getId() );
                            waitingList.add(employee);
                        }
                        Log.d(TAG, "current employees waiting for approval: " + waitingList);
                        //creating new adapter and giving him this activity as context and the current list of products
                        EmployeesList adapter = new EmployeesList(Employees_management.this, waitingList);

                        //giving the adapter to the listView
                        listView_waiting_for_approval.setAdapter(adapter);
                    }
                });
        listView_waiting_for_approval.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                current_clicked_waiting = position;
                openApprovingDialog();
            }
        });

        //approved list realtime update
        db.collection("Users")
                .whereEqualTo("role", "employee")
                .whereEqualTo("approved", true)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                        @Nullable FirebaseFirestoreException e) {

                        if (e != null) {
                            Log.w(TAG, "Listen to aprroved list failed.", e);
                            return;
                        }
                        //empty products list before getting the new one from firestore
                        approvedList.clear();
                        //for each document in the collection
                        for (QueryDocumentSnapshot approvedEmployeeSnapshot : queryDocumentSnapshots) {
                            //add the approved employee to the list as a Map<String, Object>
                            Map<String, Object> employee = approvedEmployeeSnapshot.getData();
                            employee.put("UID",approvedEmployeeSnapshot.getId() );
                            approvedList.add(employee);
                        }
                        Log.d(TAG, "current approved employees: " + approvedList);
                        //creating new adapter and giving him this activity as context and the current list of products
                        EmployeesList adapter = new EmployeesList(Employees_management.this, approvedList);

                        //giving the adapter to the listView
                        listView_approved.setAdapter(adapter);
                    }
                });


    }

    private void openApprovingDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Employees_management.this).create();
        alertDialog.setTitle("New Employee Approval\n");
        alertDialog.setMessage("Are you sure you want to approve new employee?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> chosen_waiting_employee = waitingList.get(current_clicked_waiting);
                        String employeeUID = chosen_waiting_employee.get("UID").toString();
                        db.collection("Users").document(employeeUID).update("approved",true);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which) {
            }
            });
        alertDialog.show();
    }
}
