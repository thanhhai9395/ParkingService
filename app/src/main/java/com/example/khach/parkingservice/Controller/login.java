package com.example.khach.parkingservice.Controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khach.parkingservice.Entities.Account;
import com.example.khach.parkingservice.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by khach on 17/04/2017.
 */

public class login extends Activity {
    EditText edtAccount;
    EditText edtPass;
    TextView txtRegis;
    TextView txtChangePass;
    Button btnLogin;
    private DatabaseReference myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        myData = FirebaseDatabase.getInstance().getReference();
        edtAccount = (EditText) findViewById(R.id.ediAcountText);
        edtPass = (EditText) findViewById(R.id.ediPassText);
        txtRegis = (TextView) findViewById(R.id.txtRegister);
        txtChangePass = (TextView) findViewById(R.id.txtforgotpass);
        btnLogin = (Button) findViewById(R.id.btnLogIn);
        View.OnClickListener handle = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == txtRegis) {
                    Intent intentMain = new Intent(login.this,
                            CreateAccount.class);
                    login.this.startActivity(intentMain);
                }
            }
        };
        findViewById(R.id.txtRegister).setOnClickListener(handle);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String accountName = edtAccount.getText().toString();
                String pass = edtPass.getText().toString();
                if ( accountName== "" || edtPass.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "Nhập đầy đủ thông tin tài khoản", Toast.LENGTH_LONG).show();
                } else {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    db.child("Account").child(accountName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //String a = dataSnapshot.getValue().toString();
                            try {
                                Account account = dataSnapshot.getValue(Account.class);
                                if (account.getPassWord().equals(MD5.encryptMD5(edtPass.getText().toString()))) {
                                    Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_LONG).show();
                                    Intent aM = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(aM);
                                }else
                                {
                                    Toast.makeText(getApplicationContext(), "Kiểm tra lại tài khoản", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

    }
}
