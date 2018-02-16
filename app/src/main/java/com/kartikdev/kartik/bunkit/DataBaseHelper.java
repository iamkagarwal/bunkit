package com.kartikdev.kartik.bunkit;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kartik on 7/7/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    static final String NAME="SubjectData";
    static final String DB_NAME="SubjectDataBase";
    private static final int VERSION=3;
    final static String SUBJECT_NAME = "name";
    final static String MIN_PERCENT = "minPercent";
    final static String TOTAL_CLASSES = "totalClasses";
    final static String BUNKED_CLASSES = "bunkedClasses";
    final static String PERCENT="percent";
    public final Context mContext;
    final static String _ID = "_id";
    final static String[] columns = { _ID, SUBJECT_NAME,MIN_PERCENT,TOTAL_CLASSES,BUNKED_CLASSES,PERCENT };
    final static String[] columnSubjectNameandPercentage = { _ID, SUBJECT_NAME,PERCENT,MIN_PERCENT };
    final static String[] columnPercentageAndBunked = { _ID, PERCENT,BUNKED_CLASSES };

    final private static String CREATE_CMD = "CREATE TABLE SubjectData (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SUBJECT_NAME + " TEXT NOT NULL, "+ MIN_PERCENT+" TEXT NOT NULL, "+ TOTAL_CLASSES +
            " TEXT NOT NULL, "+BUNKED_CLASSES+" TEXT NOT NULL ,"+PERCENT+" TEXT NOT NULL )";

    public DataBaseHelper(Context c)
    {
        super(c,DB_NAME,null,VERSION);
        this.mContext=c;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DataBaseHelper.NAME);
        onCreate(sqLiteDatabase);
    }
}
