package com.example.nisargdoshi.myclassroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;

public class virtual_classroom extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    final Context context = this;
    TextView classname_tv,classsuject_tv;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_classroom);
        mAuth = FirebaseAuth.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent from_crt_new_classroom=getIntent();
        String classname=from_crt_new_classroom.getStringExtra("classname");

        String classsection=from_crt_new_classroom.getStringExtra("classsection");
        String classsubject=from_crt_new_classroom.getStringExtra("classsubject");
        TextView tv_classname=(TextView)findViewById(R.id.classroomname_tv);
        tv_classname.setText(classname);


        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        final com.github.clans.fab.FloatingActionButton fa1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.subFloatingMenu5);
        final com.github.clans.fab.FloatingActionButton fa2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.subFloatingMenu2);
        final com.github.clans.fab.FloatingActionButton fa3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.subFloatingMenu3);
        final com.github.clans.fab.FloatingActionButton fa4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.subFloatingMenu4);
        final FloatingActionMenu fm=(FloatingActionMenu) findViewById(R.id.FloatingActionMenu1);
        fab.setColorNormalResId(R.color.colorPrimaryDark);
        fa1.setColorNormalResId(R.color.colorPrimaryDark);
        fa2.setColorNormalResId(R.color.colorPrimaryDark);
        fa3.setColorNormalResId(R.color.colorPrimaryDark);
        fa4.setColorNormalResId(R.color.colorPrimaryDark);

        fab.setColorPressedResId(R.color.colorPrimary);
        fa1.setColorPressedResId(R.color.colorPrimary);
        fa2.setColorPressedResId(R.color.colorPrimary);
        fa3.setColorPressedResId(R.color.colorPrimary);
        fa4.setColorPressedResId(R.color.colorPrimary);
        fa3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.add_aboutclassroom_by_tutor, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                final EditText classsubject = (EditText) promptsView.findViewById(R.id.about_classroom_tv);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Add about the classroom")
                        .setPositiveButton("Add",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        TextView tv_about=(TextView)findViewById(R.id.about);
                                        tv_about.setText(classsubject.getText().toString());
                                        tv_about.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                                        tv_about.setGravity(View.TEXT_ALIGNMENT_GRAVITY);
                                    }
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


            }
        });





        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        classname_tv = (TextView)hView.findViewById(R.id.classname);
        classsuject_tv=(TextView)hView.findViewById(R.id.subject);
        TextView classdevision_tv=(TextView)hView.findViewById(R.id.devision);
        TextView set_email_tv=(TextView)hView.findViewById(R.id.tutor_email_tv);


        classname_tv.setText(classname);
        classsuject_tv.setText(classsubject);
        classdevision_tv.setText(classsection);
        set_email_tv.setText(mAuth.getCurrentUser().getEmail());
        navigationView.setNavigationItemSelectedListener(this);
    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.virtual_classroom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.assignments_menu) {
            // Handle the camera action
        } else if (id == R.id.work_menu) {

        } else if (id == R.id.students) {

        } else if (id == R.id.submited_assignments_menu) {

        } else if (id == R.id.share_jc_menu) {

        } else if (id == R.id.help_menu) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}