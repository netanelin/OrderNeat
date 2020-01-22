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

public class EmployeesList extends  ArrayAdapter<Map<String, Object>> {
    private Activity context;
    private List<Map<String, Object>> employees_list;

    public EmployeesList(Activity context, List<Map<String, Object>> employees_list){
        super(context, R.layout.employee_list_layout, employees_list);
        this.context = context;
        this.employees_list = employees_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.employee_list_layout, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textView_name);
        TextView textViewPhone = (TextView) listViewItem.findViewById(R.id.textView_phone);
        ImageView imageViewUser = (ImageView) listViewItem.findViewById(R.id.imageView5);

        Map<String, Object> employee = employees_list.get(position);

        String fullName = employee.get("first_name").toString()+ " " + employee.get("last_name").toString();

        //--------
        System.out.println("employee full name: " + fullName);
        //--------

        if(fullName != null)
            textViewName.setText(fullName);
        //--------
        System.out.println("employee phone number: " + employee.get("phone_num").toString());
        //-------
        textViewPhone.setText(employee.get("phone_num").toString());

        return listViewItem;
    }
}
