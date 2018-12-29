package com.example.nik7.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.nik7.notesapp.MainActivity.adapter;
import static com.example.nik7.notesapp.MainActivity.desList;
import static com.example.nik7.notesapp.MainActivity.titleList;

public class NoteActivity extends AppCompatActivity {
    EditText title;
    EditText des;
    int type;
    int ind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        title = (EditText) findViewById(R.id.title);
        des = (EditText) findViewById(R.id.des);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);

        if (type == 0){
            ind = intent.getIntExtra("ind", -1);
            title.setText(titleList.get(ind));
            des.setText(desList.get(ind));
        }

    }


    public void addNote(String titleString, String desString){

        desList.add(desString);
        titleList.add(titleString);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        updateSP();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    public void updateNote(String titleString, String desString){
        titleList.set(ind, titleString);
        desList.set(ind, desString);
        updateSP();
        Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        finish();
    }


    public void updateSP(){
        try {
            SharedPreferences sp = this.getSharedPreferences("com.example.nik7.notesapp", Context.MODE_PRIVATE);
            sp.edit().putString("des", ObjectSerializer.serialize(desList)).apply();
            sp.edit().putString("title", ObjectSerializer.serialize(titleList)).apply();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        final String titleString = title.getText().toString();
        final String desString = des.getText().toString();

        if (titleString.equals("") || desString.equals("")){
            Toast.makeText(this, "Both fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type == 1){
            new AlertDialog.Builder(this)
                    .setTitle("Save Changes?")
                    .setMessage("Do you want to save this note")
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            addNote(titleString, desString);
                        }
                    })
                    .setNegativeButton("Don't save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
        }

        else{

            if (titleString.equals(titleList.get(ind)) && desString.equals(desList.get(ind))){
                //No changes made
                finish();
            }

            else {

                new AlertDialog.Builder(this)
                        .setTitle("Save Changes?")
                        .setMessage("Do you want to save the changes you have made")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                updateNote(titleString, desString);

                            }
                        })
                        .setNegativeButton("Don't save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        }
    }
}
