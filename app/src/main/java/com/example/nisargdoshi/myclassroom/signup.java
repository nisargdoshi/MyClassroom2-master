package com.example.nisargdoshi.myclassroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class signup extends AppCompatActivity implements View.OnClickListener {
    private String first_name,last_name;
    public Button signup_btn;
    private SignInButton google_btn;
    private int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    public ProgressDialog mprog1;
    private Firebase mref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mprog1=new ProgressDialog(this);
        mAuth=FirebaseAuth.getInstance();
        mref=new Firebase("https://virtual-class-476be.firebaseio.com/Users");
        mAuthlistner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                {

                    Intent join_intent=new Intent(signup.this,Home.class);
                    startActivity(join_intent);
                }
            }
        };
        signup_btn = (Button) findViewById(R.id.btn_signup);
        google_btn=(SignInButton)findViewById(R.id.googlebtn);
        signup_btn.setOnClickListener(this);
        google_btn.setOnClickListener(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(getBaseContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getBaseContext(),"Connection fail",Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthlistner);
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        mprog1.setMessage("Signin..");
        mprog1.show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mprog1.setMessage("Signin..");
        mprog1.show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                first_name=account.getGivenName();
                last_name=account.getFamilyName();
                firebaseAuthWithGoogle(account);
                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("user_name", account.getEmail());
                editor.commit();


            } else {
                Toast.makeText(getApplication(),"Can't connect! try again..",Toast.LENGTH_LONG).show();                // Google Sign In failed, update UI appropriately
                // ...
            }



        }
        mprog1.dismiss();

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            //                    Intent join_intent=new Intent(signup.this,Home.class);
                            //                    startActivity(join_intent);
                            String user_id=mAuth.getCurrentUser().getUid();
                            //DatabaseReference current_user_db= mdatabase.child(user_id);
                            //current_user_db.child("fname").setValue(first_name);
                            //current_user_db.child("lname").setValue(last_name);
                            Firebase c=mref.child(user_id);
                            c.child("First Name").setValue(first_name);
                            c.child("Last Name").setValue(last_name);
                            Toast.makeText(signup.this, first_name+last_name,
                                    Toast.LENGTH_SHORT).show();

                        }
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // .
                        //..

                    }

                });

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btn_signup)
        {
            Intent join_intent=new Intent(getBaseContext(),signupname.class);
            startActivity(join_intent);

        }
        else if(v.getId()==R.id.googlebtn)
        {   signIn();


        }

    }


}