package com.example.khach.parkingservice.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.khach.parkingservice.Entities.VehicleType;
import com.example.khach.parkingservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by khach on 27/04/2017.
 */

public class DangKyXe extends ActionBarActivity {
    private DatabaseReference myData;
    private Spinner spnVehicle;
    private ArrayList<String> listVehicleName = new ArrayList<String>();
    private ArrayList<VehicleType> listVehicle = new ArrayList<VehicleType>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvehicle);
        spnVehicle = (Spinner) findViewById(R.id.spnVehicleType) ;
        myData = FirebaseDatabase.getInstance().getReference();
        final ArrayAdapter<String> adapterVehicle = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listVehicleName);
        spnVehicle.setAdapter(adapterVehicle);
        myData.child("VehicleType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> data = dataSnapshot.getChildren();
                for(DataSnapshot child : data){
                    VehicleType vehicleType = child.getValue(VehicleType.class);
                    listVehicleName.add(vehicleType.getVehicleName());
                    listVehicle.add(vehicleType);
                }
                adapterVehicle.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
