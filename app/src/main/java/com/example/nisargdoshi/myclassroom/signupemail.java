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

public class signupemail extends AppCompatActivity implements View.OnClickListener {
    public Button email_btn;
    public EditText email_et;
    public TextView err_msg_email;
    public  String first_name,last_name;
    String emailPattern = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupemail);
        email_btn=(Button)findViewById(R.id.btn_email_signup);
        email_et=(EditText)findViewById(R.id.et_email_signup);
        err_msg_email=(TextView) findViewById(R.id.tv_email_signup_err_msg);
        email_btn.setOnClickListener(this);
        first_name=getIntent().getStringExtra("fname");
        last_name=getIntent().getStringExtra("lname");

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_email_signup) {

            if(!email_et.getText().toString().matches(emailPattern))
            {
                err_msg_email.setVisibility(View.VISIBLE);
            }
            else if(!checkConnection())
            {
                AlertDialog alertDialog = new AlertDialog.Builder(signupemail.this).create();
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
                Intent signup_email = new Intent(getBaseContext(), signuppassword.class);
                signup_email.putExtra("fname",first_name);
                signup_email.putExtra("lname",last_name);
                signup_email.putExtra("email",email_et.getText().toString());
                startActivity(signup_email);
                err_msg_email.setVisibility(View.INVISIBLE);

            }
        }
        }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;

    }
}
