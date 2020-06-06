package com.example.seventh_second;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class MyContentProvider extends ContentProvider {
    public static final int CONSTACTS_DIR=0;
    public static final int CONSTACTS_ITEM=1;
    public static final String AUTHORITY="com.example.seventh_second.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"Contacts",CONSTACTS_DIR);
        uriMatcher.addURI(AUTHORITY,"Contacts/#",CONSTACTS_ITEM);
    }
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int deletedRows=0;
        switch (uriMatcher.match(uri)){
            case CONSTACTS_DIR:
                deletedRows=db.delete("Contacts",selection,selectionArgs);
                break;
            case CONSTACTS_ITEM:
                String constactsId=uri.getPathSegments().get(1);
                deletedRows=db.delete("Contacts","id=?",new String[]{constactsId});
                Log.d("MainActivity","已被删除");
                break;
                default:
                    break;
        }
        return deletedRows;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        switch (uriMatcher.match(uri)){
            case CONSTACTS_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.seventh_second.provider.Contacts";
            case CONSTACTS_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.seventh_second.provider.Contacts";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri uriReturn=null;
        switch(uriMatcher.match(uri)){
            case CONSTACTS_DIR:
            case CONSTACTS_ITEM:
                Long newContactsId=db.insert("Contacts",null,values);
                uriReturn=Uri.parse("content://"+AUTHORITY+"/Contacts/"+newContactsId);
                Log.d("MainActivity","插入成功");
                break;
                default:
                    break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbHelper = new MyDatabaseHelper(getContext(),"Contacts.db",null,2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        SQLiteDatabase db  = dbHelper.getReadableDatabase();
        Cursor cursor =  null;
        Log.d("MainActivity","本次查询结果：");
        switch (uriMatcher.match(uri)){
                case CONSTACTS_DIR:
                    cursor=db.query("Contacts",projection,selection,selectionArgs,null,null,sortOrder);
                    break;
                case CONSTACTS_ITEM:
                    String constactsId=uri.getPathSegments().get(1);
                    cursor=db.query("Contacts",projection,"id=?",new String[]{constactsId},null,null,sortOrder);
                    break;
                default:
                    break;
        }
       return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        int updateRows=0;
        switch (uriMatcher.match(uri)){
            case CONSTACTS_DIR:
                updateRows=db.update("Contacts",values,selection,selectionArgs);
                break;
            case CONSTACTS_ITEM:
                String constactsId=uri.getPathSegments().get(1);
                updateRows=db.update("Contacts",values,"id=?",new String[]{constactsId});
                break;
                default:
                    break;
        }
        Log.d("MainActivity","修改成功");
        return updateRows;
    }
}
