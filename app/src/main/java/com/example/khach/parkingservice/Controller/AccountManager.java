package com.example.khach.parkingservice.Controller;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.khach.parkingservice.R;

import java.util.ArrayList;

/**
 * Created by khach on 13/04/2017.
 */

public class AccountManager extends ActionBarActivity
{
    Spinner spinnerSex;
    Spinner spinnerYealOlds;
    private String[] arraySpinnerName;
    private ArrayList<String> arraySpinnerYealOlds;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quanlytaikhoan);
        this.arraySpinnerName = new String[] {"Nam","Nữ"
        };
        this.arraySpinnerYealOlds = new ArrayList<>();
        for(int i=15;i<81;i++){
             arraySpinnerYealOlds.add(String.valueOf(2016-i));
        }
        spinnerSex  = (Spinner) findViewById(R.id.spinnerSex);
        spinnerYealOlds  = (Spinner) findViewById(R.id.spinnerOlds);
        ArrayAdapter<String> adapterSex = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, arraySpinnerName);
        spinnerSex.setAdapter(adapterSex);
        ArrayAdapter<String> adapterYealOlds = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked,arraySpinnerYealOlds);
        spinnerYealOlds.setAdapter(adapterYealOlds);

    }
}
