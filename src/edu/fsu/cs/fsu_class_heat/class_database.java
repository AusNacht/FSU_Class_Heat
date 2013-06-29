package edu.fsu.cs.fsu_class_heat;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class class_database extends ContentProvider {

    public final static String DBNAME = "NameDatabase";
    public final static String TABLE_NAMESTABLE = "namestable";
    public final static String COLUMN_BUILDING = "building_name";
    public final static String COLUMN_DAYS = "day_time";
    public final static String COLUMN_BEGIN = "begin_time";
    public final static String COLUMN_END = "end_time";
    
    public static final String AUTHORITY = "edu.fsu.cs.fsu_class_heat";
    public static final Uri CONTENT_URI = Uri.parse("content://edu.fsu.cs.fsu_class_heat.class_database/" + TABLE_NAMESTABLE);

    private static UriMatcher sUriMatcher;
    private MainDatabaseHelper mOpenHelper;

    private static final String SQL_CREATE_MAIN = 
    		"CREATE TABLE " +
            TABLE_NAMESTABLE +
            "(" +
            " _ID INTEGER PRIMARY KEY, " +
            COLUMN_BUILDING +
            " TEXT," +
            COLUMN_DAYS +
            " TEXT," +
            COLUMN_BEGIN +
            " TEXT," +
            COLUMN_END +
            " TEXT)";

    @Override
    public boolean onCreate( ) {

        mOpenHelper = new MainDatabaseHelper(getContext( ));
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String building_name = values.getAsString(COLUMN_BUILDING).trim( );
        String day_time = values.getAsString(COLUMN_BUILDING).trim( );
        String begin_time = values.getAsString(COLUMN_BEGIN).trim( );
        String end_time = values.getAsString(COLUMN_END).trim( );
        
        if(building_name.equals(""))
            return null;

        if(day_time.equals(""))
            return null;
        
        if(begin_time.equals(""))
            return null;

        if(end_time.equals(""))
        	return null;
        
        long id = mOpenHelper.getWritableDatabase().insert(TABLE_NAMESTABLE, null, values);

        return Uri.withAppendedPath(CONTENT_URI, "" + id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[ ] selectionArgs) {

        return mOpenHelper.getWritableDatabase( ).update(TABLE_NAMESTABLE, values, selection, selectionArgs);

    }

    @Override
    public int delete(Uri uri, String whereClause, String[ ] whereArgs) {

        return mOpenHelper.getWritableDatabase( ).delete(TABLE_NAMESTABLE, whereClause, whereArgs);

    }

    @Override
    public Cursor query(Uri table, String[ ] columns, String selection, String[ ] args, String orderBy) {

        return mOpenHelper.getReadableDatabase( ).query(TABLE_NAMESTABLE, columns, selection, args, null, null, orderBy);
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    protected static final class MainDatabaseHelper extends SQLiteOpenHelper {

        MainDatabaseHelper(Context context) {
            super(context, DBNAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_MAIN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        }
    }
}
