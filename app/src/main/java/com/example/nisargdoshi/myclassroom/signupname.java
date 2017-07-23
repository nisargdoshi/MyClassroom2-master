package com.example.nisargdoshi.myclassroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class signupname extends AppCompatActivity implements View.OnClickListener {
    public Button name_btn;
    public EditText fname_et,lname_et;
    public TextView err_msg_tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupname);
        name_btn=(Button)findViewById(R.id.btn_name_signup);
        fname_et=(EditText)findViewById(R.id.et_fname_signup_);
        lname_et=(EditText)findViewById(R.id.et_lname_signup);
        err_msg_tv_name=(TextView)findViewById(R.id.tv_name_signup_err_msg);
        name_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_name_signup)
        {
            if(fname_et.getText().toString().equals("")||lname_et.getText().toString().equals(""))
            {
                err_msg_tv_name.setVisibility(View.VISIBLE);
            }
            else if(!checkConnection())
            {
                AlertDialog alertDialog = new AlertDialog.Builder(signupname.this).create();
                alertDialog.setTitle("No internet connection");
                alertDialog.setMessage("no internet");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Go to settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent dialogIntent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(dialogIntent);
                            }
                        });
                alertDialog.show();

            }
            else
            {
                Intent signup_intent = new Intent(getBaseContext(), signupemail.class);
                signup_intent.putExtra("fname",fname_et.getText().toString());
                signup_intent.putExtra("lname",lname_et.getText().toString());
                startActivity(signup_intent);
                err_msg_tv_name.setVisibility(View.INVISIBLE);

            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to cancel account creation? This will discard any information you've entered so far.")
                .setTitle("Cancel Account Creation")
                .setCancelable(false)

                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                              }
                })
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent signup_intent = new Intent(getBaseContext(), Login.class);
                        startActivity(signup_intent);

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;

    }

}
