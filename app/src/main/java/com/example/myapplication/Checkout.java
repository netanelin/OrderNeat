package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Checkout extends AppCompatActivity {
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbRef = database.getReference("/Orders");
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public class Order{
        public String getName() {
            return name;
        }

        public String getTotal() {
            return total;
        }

        public String getCard_number() {
            return card_number;
        }

        public String getShipment_address() {
            return shipment_address;
        }

        String name, total, card_number, shipment_address;

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setShipment_address(String shipment_address) {
            this.shipment_address = shipment_address;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
    String val ="";
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        /*accepting data from previous activity*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             val = extras.getString("SumUp") +"$";
            //The key argument here must match that used in the other activity
        }

        /*assets*/
        final TextView tv_total_price = findViewById(R.id.tv_total_price);
        tv_total_price.setText(val);
        final TextView tv_card_number = findViewById(R.id.tv_card_number);
        final TextView tv_address = findViewById(R.id.tv_address);
        Button place_order = findViewById(R.id.button_place_order);

        /*place order button action listener*/
        place_order.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               /*gathering order details*/
               order.setCard_number(tv_card_number.getText().toString());
               order.setShipment_address(tv_address.getText().toString());
               order.setTotal(tv_total_price.getText().toString());
               order.setName(mAuth.getCurrentUser().getDisplayName());

               dbRef.child(order.getName())
                       .setValue(order, completionListener);
           }

                DatabaseReference.CompletionListener completionListener = new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError != null)
                            Toast.makeText(Checkout.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        else{
                            Toast.makeText(Checkout.this, "Bon Appétit!", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Checkout.this, FinalPage.class);
                            startActivity(i);
                            finish();
                        }
                    }
                };

       }
        );
    }
}
