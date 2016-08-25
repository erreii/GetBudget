package erreii.takenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * SOmer UNAL
 * Created by Asus on 8/16/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NewNotes.db";

    public static final String CATEGORIES_TABLE_NAME = "categories";
    public static final String CATEGORIES_COLUMN_ID = "cat_id";
    public static final String CATEGORIES_COLUMN_NAME = "categories_name";
    public static final String CATEGORIES_COLUMN_CREATION_TIME = "categry_createtime";


    public static final String NOTES_TABLE_NAME = "notes";
    public static final String NOTES_COLUMN_ID = "id";
    public static final String NOTES_COLUMN_SUBJECT = "subject";
    public static final String NOTES_COLUMN_NOTE = "note";
    public static final String NOTES_COLUMN_CREATION_TIME = "creationtime";
    public static final String NOTES_COLUMN_CATEGORY = "category";



    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table notes (id integer primary key, subject text, note text, creationtime text, category text)");
        db.execSQL("create table categories (cat_id integer primary key, categories_name text, note text, categry_createtime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notes");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }

    public boolean insertNote(String subject,String note,String creationTime,String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("subject",subject);
        contentValues.put("note",note);
        contentValues.put("creationtime",creationTime);
        contentValues.put("category",category);
        db.insert("notes",null,contentValues);
        return true;
    }

    public boolean insertCategory(String name,String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categories_name",name);
        contentValues.put("categry_createtime",time);
        db.insert("categories",null,contentValues);
        return true;
    }

    public Cursor getData(int id,String whichTable){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from "+whichTable+" where id="+id+"", null);
        return res;
    }

    public int numberOfRows(String whichTable){
        SQLiteDatabase db = this.getReadableDatabase();
        int numberOfRows = (int) DatabaseUtils.queryNumEntries(db, whichTable);
        return numberOfRows;
    }

    public boolean updateCategory (Integer id, String name, String time)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categories_name", name);
        contentValues.put("categry_createtime", time);
        db.update("categories", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean updateNotes (Integer id, String subject, String note,String creationTime,String category)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("subject", subject);
        contentValues.put("note", note);
        contentValues.put("creationtime", creationTime);
        contentValues.put("category", category);
        db.update("notes", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNote (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notes",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public Integer deleteCategory(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("categories",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllNotes()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notes", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTES_COLUMN_NOTE)));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllCategories()
    {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from categories", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(NOTES_COLUMN_NOTE)));
            res.moveToNext();
        }
        return array_list;
    }


}
