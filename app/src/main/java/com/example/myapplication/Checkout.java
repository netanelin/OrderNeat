package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Checkout extends AppCompatActivity {
    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbRef = database.getReference("/Users");
    public class User{
        public String   phone, email, password, id, fullName, key;

        public User(){ }

        public User(String phone,String password,String email , String id, String fullName){
            this.fullName = fullName;
            this.id = id;
            this.phone = phone;
            this.password = password;
            this.email = email;
            key = null;

        }
    }
    public class Order{
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
    User user;
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
        TextView tv_total_price = findViewById(R.id.tv_total_price);
        tv_total_price.setText(val);
        final TextView tv_card_number = findViewById(R.id.tv_card_number);
        final TextView tv_address = findViewById(R.id.tv_address);
        Button place_order = findViewById(R.id.button_place_order);

        /*place order button action listener*/
        place_order.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                order.setCard_number(tv_card_number.getText().toString());
                order.setShipment_address(tv_address.getText().toString());
                //order.set

           }
       }
        );
    }
}
