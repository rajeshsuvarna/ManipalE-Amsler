package com.visint.manipale_amsler.helper;

/**
 * Created by MAHE on 5/20/2016.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.logging.StreamHandler;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "me_amsler";

    // Login table name
    private static final String TABLE_LOGIN = "login";

    // Login Table Columns names
    public static final String KEY_ID = "idLogin";
    public static final String KEY_EMAIL = "username";
    public static final String KEY_TYPE = "type";
    //private static final String KEY_UID = "uid";
   // private static final String KEY_CREATED_AT = "created_at";
    Context ctx;

    public SQLiteHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("+ KEY_ID + " INTEGER, " + KEY_EMAIL + " TEXT, " + KEY_TYPE + " TEXT )";

        db.execSQL(CREATE_LOGIN_TABLE);


        Log.d(TAG, "Database tables created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String lid,String email, String utype)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, lid);
        values.put(KEY_EMAIL, email); // Name
        values.put(KEY_TYPE, utype); // PHONENO
        //values.put(KEY_UID, uid); // PHONENO
        //values.put(KEY_CREATED_AT, created_at); // Created At

        // Inserting Row
        long id = db.insert(TABLE_LOGIN, null, values);
       // Toast.makeText(ctx, "utype"+utype,Toast.LENGTH_SHORT).show();
       // db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails()
    {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Log.d(TAG, "cursor: " + cursor.getCount());
        if (cursor.getCount() > 0)
        {
            user.put("lid", cursor.getString(0));
            user.put("email", cursor.getString(1));
            user.put("utype", cursor.getString(2));
          //  user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }

    /**
     * Re crate database Delete all tables and create them again
     * */
    public void deleteUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

}
