package com.example.nik7.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    static ArrayList<String> desList;
    static ArrayList<String> titleList;
    ListView listView;
    static ArrayAdapter<String> adapter;
    int size;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.neww:
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                return true;

            default:
                return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = this.getSharedPreferences("com.example.nik7.notesapp", Context.MODE_PRIVATE);

        desList = new ArrayList<String>();
        titleList = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.list);

        getLists();

        if (titleList.size() == 0){
            titleList.add("Click on the 3 dots to make a new note");
            desList.add(null);
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, titleList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                    intent.putExtra("type", 0);
                    intent.putExtra("ind", position);
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position > 0) {
                    showDelete(position);
                }

                return false;
            }
        });

    }

    public void getLists(){
        try {
            SharedPreferences sp = this.getSharedPreferences("com.example.nik7.notesapp", Context.MODE_PRIVATE);
            desList = (ArrayList<String>) ObjectSerializer.deserialize(sp.getString("des", ObjectSerializer.serialize(new ArrayList<String>())));
            titleList = (ArrayList<String>) ObjectSerializer.deserialize(sp.getString("title", ObjectSerializer.serialize(new ArrayList<String>())));
        }

        catch (Exception e){
            e.printStackTrace();
        }
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

    public void showDelete(final int position){
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Do you want to delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        titleList.remove(position);
                        desList.remove(position);
                        adapter.notifyDataSetChanged();
                        updateSP();
                        Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

}



