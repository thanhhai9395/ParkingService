package com.example.khach.parkingservice.Controller;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.khach.parkingservice.Entities.Account;
import com.example.khach.parkingservice.Entities.AccountType;
import com.example.khach.parkingservice.Entities.ParkingLot;
import com.example.khach.parkingservice.Entities.User;
import com.example.khach.parkingservice.Entities.VehicleType;
import com.example.khach.parkingservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by khach on 17/04/2017.
 */

public class CreateAccount extends ActionBarActivity {
    EditText edtAccount;
    EditText edtPass;
    EditText edtConPass;
    EditText edtPhoneNo;
    EditText edtAdrress;
    EditText edtSize;
    Spinner spnAccountType,spnVehicle;
    Button btnSave;
    ImageButton imageLocation;
    LinearLayout linearAccountType;
    private DatabaseReference mData;
    private ArrayList<AccountType> listAccountType = new ArrayList<AccountType>();
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayList<String> listAccount = new ArrayList<String>();
    private ArrayList<VehicleType> listVehicleType = new ArrayList<VehicleType>();
    private ArrayList<String> listVehicleName = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_acount);
        edtAccount = (EditText) findViewById(R.id.edtAccount);
        edtPass = (EditText) findViewById(R.id.edtPass);
        edtConPass = (EditText) findViewById(R.id.edtConfiPass);
        edtPhoneNo = (EditText) findViewById(R.id.edtPhoneNo);
        edtAdrress = (EditText) findViewById(R.id.edtAdress);
        edtSize = (EditText) findViewById(R.id.edtSize);
        spnAccountType = (Spinner) findViewById(R.id.spnAccountType);
        btnSave = (Button) findViewById(R.id.btnSave);
        linearAccountType = (LinearLayout) findViewById(R.id.linerParking);
        mData = FirebaseDatabase.getInstance().getReference();
        spnAccountType = (Spinner) findViewById(R.id.spnAccountType);
        spnVehicle =(Spinner)findViewById(R.id.spnVehicle);
        final ArrayAdapter<String> adapterAccountType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listAccount);
        spnAccountType.setAdapter(adapterAccountType);
        mData.child("Accounttype").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();
                for (DataSnapshot child : snap) {
                    AccountType accountType = child.getValue(AccountType.class);
                    listAccountType.add(accountType);
                    listAccount.add(accountType.getNameAccountCode());
                }
                adapterAccountType.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        final ArrayAdapter<String> adapterVehicle = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listVehicleName);
        spnVehicle.setAdapter(adapterVehicle);
        mData.child("VehicleType").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> snap = dataSnapshot.getChildren();
                for (DataSnapshot child : snap) {
                    VehicleType accountType = child.getValue(VehicleType.class);
                    listVehicleType.add(accountType);
                    listVehicleName.add(accountType.getVehicleName());
                }
                adapterVehicle.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        spnAccountType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (listAccountType.get(position).getAccountTypeCode().toString().equals("Account01")) {
                    linearAccountType.setVisibility(View.VISIBLE);
                    spnVehicle.setVisibility(View.VISIBLE);
                } else {
                    linearAccountType.setVisibility(View.INVISIBLE);
                    spnVehicle.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtAccount.length() < 1) {
                    edtAccount.setError("Tên account quá ngắn");
                    return;
                }
                if (!edtPass.getText().toString().equals(edtConPass.getText().toString())) {
                    edtConPass.setError("mật khẩu không trùng khớp");
                    return;
                }
                if (edtConPass.length() < 4) {
                    edtConPass.setError("Mật khẩu quá ngắn");
                    return;
                }
                if (edtPhoneNo.length() < 9 || edtPhoneNo.length() > 13) {
                    edtPhoneNo.setError("số điện thoại không tồn tại");
                    return;
                }
                try {
                    //SAVE ACCOUNT
                    //Account01 : Parking
                    //Account02 : User normal
                    Account newAccount = new Account(edtAccount.getText().toString(), MD5.encryptMD5(edtPass.getText().toString()),
                            edtPhoneNo.getText().toString(), listAccountType.get(spnAccountType.getSelectedItemPosition()).getAccountTypeCode().toString(), "");
                    if (listAccountType.get(spnAccountType.getSelectedItemPosition()).getAccountTypeCode().equals("Account01")) {
                        ParkingLot parkingLot = new ParkingLot(edtPhoneNo.getText().toString(),edtPhoneNo.getText().toString(),edtAccount.getText().toString(),edtSize.getText().toString(),1,edtAdrress.getText().toString(), 1);
                        if( newAccount.Add(mData)) {
                            if(parkingLot.Add(mData)){
                                Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                            }else
                            {
                                Toast.makeText(getApplicationContext(),"Lỗi kết nối",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        User user = new User(edtPhoneNo.getText().toString(),edtAccount.getText().toString(),0,edtPhoneNo.getText().toString());
                        if( newAccount.Add(mData))
                           if(user.Add(mData)){
                               Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công",Toast.LENGTH_SHORT).show();
                           }else{
                               Toast.makeText(getApplicationContext(),"Lỗi kết nối",Toast.LENGTH_SHORT).show();
                           }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),"Chưa tạo được tài khoản,vui lòng kiêm tra lại.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        imageLocation = (ImageButton) findViewById(R.id.imglocation);
        imageLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    protected void CheckInput() {
        edtAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (edtAccount.length() < 1) {
                    edtAccount.setError("Tên account quá ngắn");
                }
                if (edtAccount.getText().toString().contains(" ")) {
                    edtAccount.setError("Tên tài khoản không chứa ' '");
                }
            }
        });
        edtConPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!edtPass.getText().toString().equals(edtConPass.getText().toString())) {
                    edtConPass.setError("mật khẩu không trùng khớp");
                }
                if (edtConPass.length() < 4) {
                    edtConPass.setError("Mật khẩu quá ngắn");
                }
            }
        });
        edtPhoneNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (edtPhoneNo.length() < 9 || edtPhoneNo.length() > 13) {
                    edtPhoneNo.setError("Số điện thoại không tồn tại");

                }
            }
        });
    }
}
