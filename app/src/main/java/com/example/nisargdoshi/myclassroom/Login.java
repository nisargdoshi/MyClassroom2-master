package com.example.nisargdoshi.myclassroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class Login extends AppCompatActivity implements View.OnClickListener {
    public Button login, crt_nw_ac;
    public TextView f_pwd;
    public EditText email, pwd;
    FirebaseAuth mAuth;
    private DatabaseReference mdb;
    private Firebase mref;
    private ProgressDialog mprog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.email_et);
        pwd = (EditText) findViewById(R.id.password_et);
        login = (Button) findViewById(R.id.login_btn);
        crt_nw_ac = (Button) findViewById(R.id.crt_new_acc_btn);
        f_pwd = (TextView) findViewById(R.id.forgot_password_tv);
        mprog=new ProgressDialog(this);
        login.setOnClickListener(this);
        crt_nw_ac.setOnClickListener(this);
        f_pwd.setOnClickListener(this);
        Firebase.setAndroidContext(this);

        email.addTextChangedListener(new TextWatcher() {        //to check lenght of edittext inside
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 2) {
                    pwd.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            login.setEnabled(s.length() > 5);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pwd.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    login();
                }
                return false;
            }
        });
        mAuth = FirebaseAuth.getInstance();
        mref = new Firebase("https://virtual-class-476be.firebaseio.com/Users");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_btn)       //onclick of login button
        {
            login();
        } else if (v.getId() == R.id.crt_new_acc_btn)            //on click of signup button
        {
            if (!checkConnection())                  //if no internet connection
            {
                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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

            } else {                                      //create new acccount activity
                Intent signup_intent = new Intent(getBaseContext(), signup.class);
                startActivity(signup_intent);
            }
        } else if (v.getId() == R.id.forgot_password_tv) //if user forgot his/her password
        {
            Intent for_pwd = new Intent(getBaseContext(), Forgot_passsword.class);
            startActivity(for_pwd);
        }
    }

    @Override
    public void onBackPressed() {                           //on click of back pressed  activity finished
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startActivity(startMain);
        finish();
    }

    public void login()                 //login method to validate username,password and internet connection
    {
        if (!checkConnection())                              //if no internet connection
        {
            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
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
            final String username = email.getText().toString();
            String password = pwd.getText().toString();
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
            {
                Toast.makeText(getBaseContext(), "please enter username or password", Toast.LENGTH_LONG).show();
            }
            else if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                mprog.setMessage("Login...");
                mprog.show();
                mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getBaseContext(), "login", Toast.LENGTH_LONG).show();
                            mprog.dismiss();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("user_name", username);
                            editor.commit();


                            Intent Home_int = new Intent(getBaseContext(), Home.class);
                            startActivity(Home_int);
                        } else {
                            AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                            alertDialog.setTitle("Incorrect Username and Password");
                            alertDialog.setMessage("The username and password you entered dosen't appear to belong to an account.\n" +
                                    "Please check username and password and try again.");
                            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            mprog.dismiss();
                            alertDialog.show();
                        }

                    }
                });
            }

        }

    }

    private boolean checkConnection() {     //for checking internet connection
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }
}