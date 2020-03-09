package com.example.siscomputera36;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by janez0266 on 20/02/20.
 */
public class DBHelper  extends SQLiteOpenHelper {
    //version number to upgrade database version
    //each time if you Add, Edit table, you need to change the
    //version number.
    private static final int DATABASE_VERSION = 5;

    // Database Name
    private static final String DATABASE_NAME = "siscomputer.db";

    public DBHelper(Context context) {                        // constructor
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {               // aqui se crea la base de datos
        //All necessary tables you like to create will create here

        String CREATE_TABLE_CLIENTES = "CREATE TABLE clientes ("  // se crea una variable
                //tipo string con datos
                + "key_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + "nombre TEXT, "
                + "telefono TEXT, "
                + "email TEXT, "
                + "nota TEXT)";

        db.execSQL(CREATE_TABLE_CLIENTES);               //se crea una tabla

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS clientes");

        // Create tables again
        onCreate(db);

    }

}