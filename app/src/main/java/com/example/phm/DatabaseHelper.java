package com.example.phm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "my_health.db";

    private static final String BODY_TEMP_TABLE = "body_temp_table";
    private static final String BLOOD_PRESSURE_TABLE = "blood_pressure_table";
    private static final String GLYCEMIC_INDEX_TABLE = "glycemic_index_table";
    private static final String HEART_RATE_TABLE = "heart_rate_table";
    private static final String BODY_WEIGHT_TABLE = "body_weight_table";
    private static final String SLEEP_TIME_TABLE = "sleep_time_table";
    //All the above table have the same underneath colum names
    private static final String dateCol = "date";
    private static final String infoCol = "info";
    private static final String optionalNoteCol = "optionalNote";
    //
    //columns for body temperature and boyd weight scale
    private static final String tempScaleCol = "scale";
    private static final String weightScaleCol = "scale";

    //importance level table
    private static final String IMPORTANCE_TABLE = "importance_table";
    private static final String importanceLevel = "importance_level";
    private static final String info_name = "info_name";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //all tables are created here
    @Override
    public void onCreate(SQLiteDatabase db) {
        //BODY TEMP TABLE CREATION QUERY
        String bodyTempCreation = "CREATE TABLE " + BODY_TEMP_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + tempScaleCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(bodyTempCreation);

        //BLOOD PRESSURE TABLE CREATION QUERY
        String bloodPressureCreation = "CREATE TABLE " + BLOOD_PRESSURE_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(bloodPressureCreation);

        //GLYCEMIC INDEX TABLE CREATION QUERY
        String glycemicIndexCreation = "CREATE TABLE " + GLYCEMIC_INDEX_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(glycemicIndexCreation);

        //HEART RATE TABLE CREATION QUERY
        String heartRateCreation = "CREATE TABLE " + HEART_RATE_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(heartRateCreation);

        //BODY WEIGHT TABLE CREATION QUERY
        String bodyWeightCreation = "CREATE TABLE " + BODY_WEIGHT_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + weightScaleCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(bodyWeightCreation);

        //SLEEP TIME TABLE CREATION QUERY
        String sleepTimeCreation = "CREATE TABLE " + SLEEP_TIME_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + dateCol + " DATE, "
                + infoCol + " TEXT, "
                + optionalNoteCol + " TEXT DEFAULT NULL)";
        db.execSQL(sleepTimeCreation);

        //IMPORTANCE TABLE
        String importance = "CREATE TABLE " + IMPORTANCE_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + info_name + " TEXT, "
                + importanceLevel + " TEXT)";
        //importance table is populated with initial values
        String populate = "REPLACE INTO " + IMPORTANCE_TABLE +" VALUES(1,'body_temperature_table','2'), " +
                "(2,'blood_pressure_table','3'), "+
                "(3,'glycemic_index_table','5'), " +
                "(4,'sleep_time_table','1'), "+
                "(5,'body_weight_table','2'), "+
                "(6,'heart_rate_table','2')";
        db.execSQL(importance);
        db.execSQL(populate);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BODY_TEMP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BLOOD_PRESSURE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GLYCEMIC_INDEX_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + HEART_RATE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + BODY_WEIGHT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SLEEP_TIME_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + IMPORTANCE_TABLE);
        onCreate(db);
    }

    //insertions for each table are found here, you need to pass date, info, and optional note as
    //parameters, + scale if you are the body temperature table or the body weight table
    public boolean insertBodyTemp(String date, String temp, String scale, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, temp);
        contentValues.put(tempScaleCol, scale);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(BODY_TEMP_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    public boolean insertBloodPressure(String date, String pressure, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, pressure);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(BLOOD_PRESSURE_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertGlycemicIndex(String date, String glycemicLevel, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, glycemicLevel);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(GLYCEMIC_INDEX_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertHeartRate(String date, String heartRateBpm, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, heartRateBpm);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(HEART_RATE_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertBodyWeight(String date, String bodyWeight, String scale, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, bodyWeight);
        contentValues.put(weightScaleCol, scale);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(BODY_WEIGHT_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertSleepTime(String date, String sleepTime, String optionalNote) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(dateCol, date);
        contentValues.put(infoCol, sleepTime);
        contentValues.put(optionalNoteCol, optionalNote);
        long result = db.insert(SLEEP_TIME_TABLE, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }


    //Method created to retrieve ids, or ColumnIndex: 0 of the query passed as parameter
    //returns Arraylist<String>
    public ArrayList<String> getIds(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0) + ". ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }

    //Method created to retrieve dates of the query passed as parameter
    //returns Arraylist<String>
    public ArrayList<String> getDates(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("date")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }

    //Method created to retrieve infos of the query passed as parameter
    //returns Arraylist<String>
    public ArrayList<String> getInfo(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("info")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }

    //Method created to retrieve optionalNotes of the query passed as parameter
    //returns Arraylist<String>
    public ArrayList<String> getOptionalNote(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(cursor.getColumnIndex("optionalNote")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }

    //retrieve the importance level of the query you pass as parameters.
    //of course we are talking about the importance_table
    //returns Arraylist<String>
    public ArrayList<String> getImportanceLevel(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                arrayList.add(cursor.getString(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return arrayList;
    }

    //get average of the info of the query you pass as parameter
    //returns float
    public float getAvgInfo(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<>();
        float avg = 0;
        try {
            Cursor cursor = db.rawQuery(query, null);
            int numRows = cursor.getCount();
            if (numRows == 0)
                return avg;
            else {
                cursor.moveToNext();
                arrayList.add(cursor.getString(0));
                avg = Float.parseFloat(arrayList.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return avg;
    }

    //get number of rows of the table you pass as parameter
    //return long
    public long getNumRows(String table_name) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, table_name);
        db.close();
        return count;
    }

    //counts the rows of the table on today's date.
    //needed to verify if users has inserted something today
    //returns long
    public long countRows(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String query = "SELECT * FROM " + tableName + " WHERE date = '" + today + "'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }


    //updates Record, you need to pass all values
    //return boolean
    public boolean updateRec(String id, String date, String info, String optionalNote, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", date);
        contentValues.put("info", info);
        contentValues.put("optionalNote", optionalNote);
        if (!isThisDateValid(date)) {
            return false;
        } else {
            db.update(table_name, contentValues, "id = ? ", new String[]{id});
            return true;
        }
    }

    //check if date respects a valid format
    public boolean isThisDateValid(String dateToValidate) {
        if (dateToValidate == null) {
            return false;
        }
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        sdf.setLenient(false);
        try {
            sdf.parse(dateToValidate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //deletes data
    public Integer deleteData(String id, String table_name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name, "id = ?", new String[]{id});
    }

    //update query to change importance of a table
    //returns void
    public void updateImportance(String infoName, String importanceLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("importance_level", importanceLevel);
        db.update(IMPORTANCE_TABLE, cv, "info_name = ?", new String[]{infoName});
    }
}
