package com.example.babyneeds.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;

import androidx.annotation.Nullable;

import com.example.babyneeds.model.Details;
import com.example.babyneeds.util.UTIL;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class databaseHandler extends SQLiteOpenHelper {
    String formattedDate;
    public databaseHandler(@Nullable Context context) {
        super(context, UTIL.DATABASE_NAME,null,UTIL.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       // sqLiteDatabase=this.getWritableDatabase();
        String CREATE_TABLE="CREATE TABLE "+UTIL.TABLE_NAME+" ( "+UTIL.KEY_ID+" INTEGER PRIMARY KEY, "
                +UTIL.KEY_ITEM+" TEXT, "+UTIL.KEY_QUANTITY+" NUMBER, "+UTIL.KEY_SIZE+" NUMBER, "+UTIL.KEY_COLOR+" TEXT , "+UTIL.KEY_DATEADDED+"  LONG );";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    //    sqLiteDatabase.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //String DROP_TABLE="DROP TABLE IF EXISTS " ;
        //sqLiteDatabase.execSQL(DROP_TABLE,new String[]{UTIL.TABLE_NAME});
        onCreate(sqLiteDatabase);
    }
    public void addDetail(Details details)
    {
        ContentValues contentValues=new ContentValues();
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        contentValues.put(UTIL.KEY_ITEM,details.getItem());
        contentValues.put(UTIL.KEY_QUANTITY,details.getQty());
        contentValues.put(UTIL.KEY_COLOR,details.getColor());
        contentValues.put(UTIL.KEY_SIZE,details.getSize());
        contentValues.put(UTIL.KEY_DATEADDED,java.lang.System.currentTimeMillis());

        sqLiteDatabase.insert(UTIL.TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
    }
    public Details getDetail(int id)
    {
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(UTIL.TABLE_NAME,
                new String[]{UTIL.KEY_ID,UTIL.KEY_ITEM,UTIL.KEY_QUANTITY,UTIL.KEY_SIZE,UTIL.KEY_COLOR,UTIL.KEY_DATEADDED},
                UTIL.KEY_ID+"=?",new String[]{String.valueOf(id)},
                null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }
        Details details=new Details();
        details.setId(Integer.parseInt(cursor.getString(0)));
        details.setItem(cursor.getString(1));
        details.setQty(Integer.parseInt(cursor.getString(2)));
        details.setSize(Integer.parseInt(cursor.getString(3)));
        details.setColor(cursor.getString(4));

        DateFormat dateFormat=DateFormat.getDateInstance();
         formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(UTIL.KEY_DATEADDED))).getTime());
        details.setDate(formattedDate);

        return details;
    }
    public List<Details> getAlldetails()
    {
        List<Details> list=new ArrayList<>();
        String selectAll=" SELECT * FROM "+UTIL.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();

        Cursor cursor=sqLiteDatabase.query(UTIL.TABLE_NAME,
                new String[]{UTIL.KEY_ID,UTIL.KEY_ITEM,UTIL.KEY_QUANTITY,UTIL.KEY_SIZE,UTIL.KEY_COLOR,UTIL.KEY_DATEADDED},
                null,null,
                null,null,UTIL.KEY_DATEADDED+ " DESC");

        if(cursor.moveToFirst())
        {
            do {

                    Details details=new Details();
                    details.setId(Integer.parseInt(cursor.getString(0)));
                    details.setItem(cursor.getString(1));
                    details.setQty(Integer.parseInt(cursor.getString(2)));
                    details.setColor(cursor.getString(4));
                    details.setSize(Integer.parseInt(cursor.getString(3)));

                DateFormat dateFormat=DateFormat.getDateInstance();
                formattedDate=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(UTIL.KEY_DATEADDED))).getTime());
                details.setDate(formattedDate);
                    list.add(details);


            }while (cursor.moveToNext());
        }

        return list;
    }

    public int updateDetail(Details details)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(UTIL.KEY_ITEM,details.getItem());
        contentValues.put(UTIL.KEY_QUANTITY,details.getQty());
        contentValues.put(UTIL.KEY_COLOR,details.getColor());
        contentValues.put(UTIL.KEY_SIZE,details.getSize());

        contentValues.put(UTIL.KEY_DATEADDED,java.lang.System.currentTimeMillis());




       return sqLiteDatabase.update(UTIL.TABLE_NAME,contentValues,UTIL.KEY_ID+"=?",new String[]{String.valueOf(details.getId())});

    }
    public void delete(int id)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.delete(UTIL.TABLE_NAME,UTIL.KEY_ID+"=?",new String[]{String.valueOf(id)});
        sqLiteDatabase.close();
    }
    public int count()
    {
        String countQuery="SELECT * FROM "+UTIL.TABLE_NAME;
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery(countQuery,null);
        return cursor.getCount();
    }
}
