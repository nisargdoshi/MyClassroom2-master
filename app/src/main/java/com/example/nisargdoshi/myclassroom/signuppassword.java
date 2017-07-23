package com.example.nisargdoshi.myclassroom;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signuppassword extends AppCompatActivity implements View.OnClickListener {
    public Button btn_pwd;
    public EditText et_pwd;
    public TextView err_msg_pwd;
    String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
    public  String first_name,last_name,email;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private ProgressDialog mprog;
    private DatabaseReference mdatabase;
    private Firebase mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppassword);
         Firebase.setAndroidContext(this);
        btn_pwd=(Button)findViewById(R.id.btn_password_signup);
        et_pwd=(EditText)findViewById(R.id.et_pwd_singup);
        err_msg_pwd=(TextView)findViewById(R.id.tv_pwd_signup_err_msg);
        btn_pwd.setOnClickListener(this);
        first_name=getIntent().getStringExtra("fname");
        last_name=getIntent().getStringExtra("lname");
        email=getIntent().getStringExtra("email");
        Toast.makeText(getBaseContext(),first_name+last_name+email,Toast.LENGTH_LONG).show();
        mAuth=FirebaseAuth.getInstance();
        mprog=new ProgressDialog(this);

    //    mdatabase= FirebaseDatabase.getInstance().getReference("https://virtual-class-476be.firebaseio.com/").child("Users");
    mref=new Firebase("https://virtual-class-476be.firebaseio.com/Users");
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_password_signup)
        {
            if(!et_pwd.getText().toString().matches(PASSWORD_PATTERN))
            {
                err_msg_pwd.setVisibility(View.VISIBLE);
            }
           else if(!checkConnection())
            {
                AlertDialog alertDialog = new AlertDialog.Builder(signuppassword.this).create();
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
            else {
                //Intent signup_intent = new Intent(getBaseContext(), Login.class);
                //startActivity(signup_intent);
                //Toast.makeText(getBaseContext(), "You are successfully signup", Toast.LENGTH_LONG).show();
                err_msg_pwd.setVisibility(View.INVISIBLE);
                //this.finish();
                startRegister();

            }
        }
    }
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;

    }
    public  void startRegister()
    {
        final String fname=first_name.trim();
        String lname=last_name.trim();
        String emai=email.trim();
        String pwd=et_pwd.getText().toString().trim();
        if(!TextUtils.isEmpty(fname)&&!TextUtils.isEmpty(lname)&& !TextUtils.isEmpty(emai)&&!TextUtils.isEmpty(pwd))
        {
            mprog.setMessage("Siging up...");
            mprog.show();
            mAuth.createUserWithEmailAndPassword(emai,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        String user_id=mAuth.getCurrentUser().getUid();
                        //DatabaseReference current_user_db= mdatabase.child(user_id);
                        //current_user_db.child("fname").setValue(first_name);
                        //current_user_db.child("lname").setValue(last_name);
                        Firebase c=mref.child(user_id);
                        c.child("First Name").setValue(first_name);
                        c.child("Last Name").setValue(last_name);

                        mprog.dismiss();
                        Toast.makeText(getBaseContext(),"successfull signup",Toast.LENGTH_LONG);
                        Intent main=new Intent(signuppassword.this,Home.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(main);

                    }
                }
            });
        }
    }
}
