package com.example.nisargdoshi.myclassroom;

import android.content.BroadcastReceiver;
import java.util.Random;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.Toast;



public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ConnectivityReceiver.ConnectivityReceiverListener{
    public ListView lv;
    final Context context = this;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthlistner;
    private GoogleApiClient mGoogleApiClient;
    private Firebase mref;
    Integer nos=0;
    String nostudent;
    String abc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        final com.github.clans.fab.FloatingActionButton fa1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.subFloatingMenu1);
        final FloatingActionMenu fm=(FloatingActionMenu) findViewById(R.id.FloatingActionMenu1);
        fab.setColorNormalResId(R.color.colorPrimaryDark);
        fa1.setColorNormalResId(R.color.colorPrimaryDark);
        fab.setColorPressedResId(R.color.colorPrimary);
        fa1.setColorPressedResId(R.color.colorPrimary);

        mref=new Firebase("https://virtual-class-476be.firebaseio.com/Classrooms");
        fa1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Home.this,crt_new_classroom.class);
                startActivity(i);
                fm.close(true);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.join_classroom_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView.findViewById(R.id.editText6);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Join classroom")

                        .setPositiveButton("Join",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        //  if(userInput.getText().toString().length()<6)
                                        //{
                                        //  Toast.makeText(getBaseContext(),"Invalid Code retry again",Toast.LENGTH_LONG).show();
                                        //dialog.dismiss();
                                        //}
                                        //else
                                        // {
                                        final Firebase c=mref.child(userInput.getText().toString());
                                        c.child("Total students").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                nostudent =dataSnapshot.getValue(String.class);
                                                Firebase studetns=c.child("Students");
                                                Firebase s=studetns.child("S"+nostudent);
                                                s.child("Name").setValue("");
                                                s.child("ID").setValue(Integer.toString(nos));
                                                nos=Integer.parseInt(nostudent);
                                                nos++;
                                                c.child("Total students").setValue(nos);
                                                Toast.makeText(getBaseContext(),nostudent,Toast.LENGTH_SHORT).show();

                                            }

                                            @Override
                                            public void onCancelled(FirebaseError firebaseError) {

                                            }
                                        });



                                        //       nos=Integer.parseInt(abc);
                                        //     //Toast.makeText(getBaseContext(),a,Toast.LENGTH_SHORT).show();
                                        //   nos++;
                                        //
                                        //Firebase s=studetns.child("S"+nostudent);
                                        //s.child("Name").setValue("");
                                        //s.child("ID").setValue(Integer.toString(nos));
                                        //nos=nos+1;
                                        //c.child("Total students").setValue(Integer.toString(nos));

                                        Intent i=new Intent(Home.this,virtual_classroom.class);
                                        startActivity(i);
                                    }

//                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                fm.close(true);


            }
        });



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        checkConnection();
        myclasses weather_data[] = new myclasses[]
                {
                        new myclasses(R.drawable.logo, "Cloudy","agvv"),
                        new myclasses(R.drawable.logo, "Showers","gf"),
                        new myclasses(R.drawable.logo, "Snow","gff"),
                        new myclasses(R.drawable.logo, "Storm","ffgd"),
                        new myclasses(R.drawable.logo, "Sunny","gegeg")
                };
        myclassesadepter adapter = new myclassesadepter(this,
                R.layout.listclasses_listview, weather_data);
        lv=(ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);

        mAuth= FirebaseAuth.getInstance();
        mAuthlistner=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null)
                {
                    Intent login_intent =new Intent(Home.this,Login.class);
                    login_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(login_intent);

                }
            }
        };


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthlistner);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }
    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            color=Color.WHITE;
            message="You are connected with intenet ";

        }
        else {
            color=Color.RED;
            message="please make sure your internet connection";
        }
        final Snackbar snackbar = Snackbar
                .make(findViewById(R.id.fab),message, Snackbar.LENGTH_INDEFINITE);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }
    /* private boolean isNetworkAvailable() {
         ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
         NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
         return activeNetworkInfo != null;
     }*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_myclasses) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        else if (id == R.id.nav_logout) {

            mAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            //      updateUI(null);
                        }
                    });

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.commit();

            Toast.makeText(getBaseContext(),"logout successfull",Toast.LENGTH_LONG).show();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}