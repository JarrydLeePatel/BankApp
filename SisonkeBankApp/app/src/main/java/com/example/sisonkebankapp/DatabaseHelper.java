package com.example.sisonkebankapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/*
* DATABASEHELPER CLASS
* */

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseVar {public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

    //CREATE SQL DB
        StringBuilder sql =
                new StringBuilder().append("CREATE TABLE \"").append(USER_TABLE).append("\" (\n").
                        append("\t\"").append(ID).append("\"\tINTEGER NOT NULL,\n").
                        append("\t\"").append(FIRSTNAME).append("\"\tTEXT NOT NULL,\n").
                        append("\t\"").append(SURNAME).append("\"\tTEXT NOT NULL,\n").
                        append("\t\"").append(EMAIL).append("\"\tTEXT NOT NULL UNIQUE,\n").
                        append("\t\"").append(PASSWORD).append("\"\tTEXT NOT NULL,\n").
                        append("\t\"").append(MOBILE).append("\"\tTEXT NOT NULL UNIQUE,\n").
                        append("\t\"").append(GENDER).append("\"\tTEXT NOT NULL,\n").
                        append("\t\"").append(CURRENT_BALANCE).append("\"\tREAL,\n").
                        append("\t\"").append(SAVINGS_BALANCE).append("\"\tREAL,\n").
                        append("\tPRIMARY KEY(\"").append(ID).append("\" AUTOINCREMENT));");
        db.execSQL(sql.toString());
    }@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        this.onCreate(db);
    }

//CHECK TO SEE IF THE USER IS REGISTERED ALREADY
    //SELECT ALL FROM TABLE WHERE EMAIL = EMAIL FIELD;
    public boolean isRegistered(String email){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+USER_TABLE+" WHERE EMAIL='"+email+"'", null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        db.close();
        return result;
    }


    //INPUT USER INTO DB
    public void addUser(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FIRSTNAME, userDetails.getNAME());
        values.put(SURNAME, userDetails.getSURNAME());
        values.put(EMAIL, userDetails.getEMAIL());
        values.put(PASSWORD, userDetails.getPASSWORD());
        values.put(MOBILE, userDetails.getMOBILE());
        values.put(GENDER, userDetails.getGENDER());
        values.put(CURRENT_BALANCE, userDetails.getCURRENT_BALANCE());
        values.put(SAVINGS_BALANCE, userDetails.getSAVINGS_BALANCE());
        db.insert(USER_TABLE, null, values);
        db.close();
    }

    //UPDATE BALANCE ACCORDING TO THE TRANSFER FUNCTION
    public int updateBalance(UserDetails userDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CURRENT_BALANCE, userDetails.getCURRENT_BALANCE());
        values.put(SAVINGS_BALANCE, userDetails.getSAVINGS_BALANCE());
        return db.update(USER_TABLE, values, EMAIL + "=?", new String[]{userDetails.getEMAIL()});

    }

    //GET USER DETAILS TO DISPLAY ON HOME AND ACCOUNT ACTIVITY
    public UserDetails getUserDetails(String email) {

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = new String[]{FIRSTNAME, SURNAME, EMAIL, PASSWORD, CURRENT_BALANCE, SAVINGS_BALANCE};
        String selection = EMAIL + "=?";
        String[] selectionArgs = {email};
        Cursor c = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null, null);
        UserDetails userDetails = new UserDetails();
        if (c != null && c.moveToFirst()) {
            userDetails.setNAME(c.getString(0));
            userDetails.setSURNAME(c.getString(1));
            userDetails.setEMAIL(c.getString(2));
            userDetails.setPASSWORD(c.getString(3));
            userDetails.setCURRENT_BALANCE(Double.parseDouble(c.getString(4)));
            userDetails.setSAVINGS_BALANCE(Double.parseDouble(c.getString(5)));
        }
        c.close();
        db.close();
        return userDetails;


    }

    //VALIDATE LOGIN ACCORDING TO THE USERNAME/EMAIL/PASSWORD/
    public UserDetails ValidateLogin(String un, String pwd) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = new String[]{FIRSTNAME, SURNAME, EMAIL, PASSWORD, CURRENT_BALANCE, SAVINGS_BALANCE, ID};

        String selection = EMAIL + "=? AND " + PASSWORD + "=?";

        String[] selectionArgs = {un, pwd};
        Cursor c = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null, null);

        UserDetails userDetails = new UserDetails();
        if (c != null && c.moveToFirst()) {
            userDetails.setNAME(c.getString(0));
            userDetails.setSURNAME(c.getString(1));
            userDetails.setEMAIL(c.getString(2));
            userDetails.setPASSWORD(c.getString(3));
            userDetails.setCURRENT_BALANCE(Double.parseDouble(c.getString(4)));
            userDetails.setSAVINGS_BALANCE(Double.parseDouble(c.getString(5)));
            userDetails.setID(Integer.parseInt(c.getString(6)));
        }
        c.close();
        db.close();
        return userDetails;
    }//validate Login
}
