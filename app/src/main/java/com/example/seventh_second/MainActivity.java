package com.example.seventh_second;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;

public class MainActivity extends AppCompatActivity {
    private String newId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addData=(Button)findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.seventh_second.provider/Contacts");
                ContentValues values = new ContentValues();
                values.put("name","张三");
                values.put("phonenumber","13565485263");
                values.put("sex","男");
                Uri newUri=getContentResolver().insert(uri,values);
                newId=newUri.getPathSegments().get(1);
            }
        });
        Button queryData = (Button)findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("content://com.example.seventh_second.provider/Contacts");
                Cursor cursor=getContentResolver().query(uri,null,null,null,null);
                if(cursor!=null){
                    while(cursor.moveToNext()){
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String phonenumber=cursor.getString(cursor.getColumnIndex("phonenumber"));
                        String sex=cursor.getString(cursor.getColumnIndex("sex"));
                        Log.d("MainActivity","contacts name is"+name);
                        Log.d("MainActivity","contacts phonenumber is"+phonenumber);
                        Log.d("MainActivity","contacts sex is"+sex);
                    }
                }
            }
        });
        Button updateData=(Button)findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.parse("content://com.example.seventh_second.provider/Contacts/"+newId);
                ContentValues values = new ContentValues();
                values.put("name","小芳");
                values.put("phonenumber","12385697845");
                values.put("sex","女");
                getContentResolver().update(uri,values,null,null);
            }
        });
        Button deleteData=(Button)findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.seventh_second.provider/Contacts/"+newId);
                getContentResolver().delete(uri,null,null);
            }
        });
    }
}
