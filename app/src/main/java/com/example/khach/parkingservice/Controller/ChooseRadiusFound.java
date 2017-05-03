package com.example.khach.parkingservice.Controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.khach.parkingservice.R;

/**
 * Created by khach on 01/05/2017.
 */

public class ChooseRadiusFound extends ActionBarActivity{
    String[] listKM;
    Spinner spinnerKM;
    Button btnOK;
    Button btnCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_accountcode);
        spinnerKM = (Spinner) findViewById(R.id.spnSoKm);
        btnOK = (Button) findViewById(R.id.btnOK);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        listKM = new String[]{"1","2","3","All"};
        ArrayAdapter<String> adapterRadius = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, listKM);
        spinnerKM.setAdapter(adapterRadius);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                      String radius = spinnerKM.getSelectedItem().toString();
                    if(radius ==null){
                        Toast.makeText(ChooseRadiusFound.this, "Lỗi Kiểm tra lại bán kính tìm kiếm ", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }catch (Exception e){
                    Toast.makeText(ChooseRadiusFound.this, "Lỗi Kiểm tra lại bán kính tìm kiếm ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


}
