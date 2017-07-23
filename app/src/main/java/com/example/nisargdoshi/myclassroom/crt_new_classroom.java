package com.example.nisargdoshi.myclassroom;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;
public class crt_new_classroom extends AppCompatActivity {
    Button btn_new_class;
    final Context context = this;
    private Firebase mref;
    private FirebaseAuth mAuth;
    private Firebase getname;
    String f_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_crt_new_classroom);
        btn_new_class=(Button)findViewById(R.id.crt_new_class_btn);
        mref=new Firebase("https://virtual-class-476be.firebaseio.com/Classrooms");


        btn_new_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.crt_new_class_popup, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText classname = (EditText) promptsView.findViewById(R.id.editText3);
                final EditText classsection = (EditText) promptsView.findViewById(R.id.editText4);
                final EditText classsubject = (EditText) promptsView.findViewById(R.id.editText5);
                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Create class")
                        .setPositiveButton("Create",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        if(classname.getText().toString().equals(""))
                                        {
                                            Toast.makeText(getBaseContext(),"please enter class name",Toast.LENGTH_LONG).show();
                                        }
                                        else if(classsection.getText().toString().equals(""))
                                        {
                                            Toast.makeText(getBaseContext(),"please enter section of class",Toast.LENGTH_LONG).show();
                                            //
                                        }
                                        else if(classsubject.getText().toString().equals(""))
                                        {
                                            Toast.makeText(getBaseContext(),"please enter subject for class",Toast.LENGTH_LONG).show();
                                        }
                                        else if(classname.getText().toString().length()>0 && classsection.getText().toString().length()>0&&classsubject.toString().toString().length()>0)
                                        {



                                            Random rand = new Random();
                                            int value = rand.nextInt(999999);

                                            Firebase c=mref.child(Integer.toString(value));
                                            c.child("Name").setValue(classname.getText().toString());
                                            c.child("Subjet").setValue(classsubject.getText().toString());
                                            c.child("section").setValue(classsection.getText().toString());
                                            c.child("Code").setValue(Integer.toString(value));
                                            c.child("Total students").setValue("0");
                                            Firebase Tutor_child=c.child("TutorsDetail");
                                            Tutor_child.child("Email").setValue(mAuth.getCurrentUser().getEmail());
                                            Tutor_child.child("ID").setValue(mAuth.getCurrentUser().getUid());



                                            Toast.makeText(getBaseContext(),classname.getText().toString()+classsection.getText().toString()+classsubject.getText().toString(),Toast.LENGTH_LONG).show();
                                            Intent i=new Intent(getBaseContext(),virtual_classroom.class);
                                            i.putExtra("classname",classname.getText().toString());
                                            i.putExtra("classsection",classsection.getText().toString());
                                            i.putExtra("classsubject",classsubject.getText().toString());
                                            startActivity(i);
                                        }
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
    }
}