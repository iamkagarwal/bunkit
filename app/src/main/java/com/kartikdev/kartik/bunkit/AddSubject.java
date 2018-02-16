package com.kartikdev.kartik.bunkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Kartik on 7/7/2017.
 */

public class AddSubject extends Activity {
EditText name,minPercent,totalLectures,bunkedLectures;
    Button submit,cancel;
    String Name;
    int MinPercent,bunked,total;
    Double percent;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subject);


        name = findViewById(R.id.name);
        minPercent = findViewById(R.id.min);
        totalLectures = findViewById(R.id.totalClasses);
        bunkedLectures = findViewById(R.id.bunkedClasses);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);
        final TextView txt = new TextView(this);
        if (getIntent().getIntExtra("flag", 0) == 1) {
            Cursor cursor = MainActivity.dataBase.getWritableDatabase().query(DataBaseHelper.NAME, DataBaseHelper.columns
                    ,DataBaseHelper._ID + "=?",new String[]{String.valueOf(getIntent().getIntExtra("id", -1))}, null, null, null);
            cursor.moveToFirst();
            name.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.SUBJECT_NAME)));
            minPercent.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.MIN_PERCENT)));
            bunkedLectures.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.BUNKED_CLASSES)));
            totalLectures.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.TOTAL_CLASSES)));
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().getIntExtra("flag", 0) == 1) {
                    updateSubject();
                    Intent intent = new Intent();
                    intent.putExtra("SubjectName", Name);
                    intent.putExtra("MinPercent", MinPercent);
                    intent.putExtra("Bunked", bunked);
                    intent.putExtra("Total", total);
                    intent.putExtra("CalPercent", percent);
//                    new String[]{String.valueOf(id)}
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    if (name.getText().toString().matches("") || minPercent.getText().toString().matches("") || bunkedLectures
                            .getText().toString().matches("") || totalLectures.getText().toString().matches("")) {
                        new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
                                ("Fields cannot be left blank")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    } else {

                        Name = name.getText().toString();
                        MinPercent = Integer.parseInt(minPercent.getText().toString());
                        bunked = Integer.parseInt(bunkedLectures.getText().toString());
                        total = Integer.parseInt(totalLectures.getText().toString());
//
                        percent = (((total - bunked) / Double.valueOf(total)) * 100);
                        percent = ((double) (Math.round(percent * 100))) / 100;


                        if (MinPercent > 100 && bunked > total) {
//
                            new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
                                    ("Minimum percentage cannot be greater than 100 and Bunked classes cannot be more than total classes")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();

                        } else if (MinPercent > 100) {
//
                            new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
                                    ("Minimum percentage cannot be greater than 100")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        } else if (bunked > total) {
//
                            new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
                                    ("Bunked classes cannot be more than total classes")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        } else {

                            insertSubject();
                            setResult(RESULT_OK, null);
                            finish();
                        }
                    }

                }
            }
        });
    }


    protected static void clearAll() {

       int rows =  MainActivity.dataBase.getWritableDatabase().delete(DataBaseHelper.NAME, null, null);
        Log.i("bunkit","rows deleted = "+rows);

    }

    public void updateSubject(){

        Name = name.getText().toString();
        MinPercent = Integer.parseInt(minPercent.getText().toString());
        bunked = Integer.parseInt(bunkedLectures.getText().toString());
        total = Integer.parseInt(totalLectures.getText().toString());
        percent = (((total - bunked) / Double.valueOf(total)) * 100);
        percent = ((double) (Math.round(percent * 100))) / 100;
if(bunked>total)
{
    new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
            ("Bunked classes cannot be more than total classes")
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
}
else if(MinPercent>100)
{
    new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
            ("Minimum percentage cannot be greater than 100")
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
}
        else if (MinPercent > 100 && bunked > total)
        {
            new AlertDialog.Builder(AddSubject.this).setTitle("Input not correct").setMessage
                    ("Minimum percentage cannot be greater than 100 and Bunked classes cannot be more than total classes")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
        else {

    ContentValues cv = new ContentValues();
    cv.put(DataBaseHelper.SUBJECT_NAME, Name);
    cv.put(DataBaseHelper.BUNKED_CLASSES, bunked);
    cv.put(DataBaseHelper.TOTAL_CLASSES, total);
    cv.put(DataBaseHelper.MIN_PERCENT, MinPercent);
    cv.put(DataBaseHelper.PERCENT, percent);

    int idFromIntent = getIntent().getIntExtra("id", -1);
    String id = String.valueOf(idFromIntent);
    int i = MainActivity.dataBase.getWritableDatabase().update(DataBaseHelper.NAME, cv,
            DataBaseHelper._ID + "=" + idFromIntent, new String[]{});
    Log.i("Bunkit", "No of rows updated = " + i);
    cv.clear();
}
    }

    public void insertSubject()
    {
        ContentValues cv=new ContentValues();

        cv.put(DataBaseHelper.SUBJECT_NAME,Name);


        cv.put(DataBaseHelper.MIN_PERCENT,MinPercent);


        cv.put(DataBaseHelper.BUNKED_CLASSES,bunked);


        cv.put(DataBaseHelper.TOTAL_CLASSES,total);

        cv.put(DataBaseHelper.PERCENT,percent);
        Log.i("bunkIt","before insertion"+cv.get(DataBaseHelper.PERCENT));
        MainActivity.dataBase.getWritableDatabase().insert(DataBaseHelper.NAME,null,cv);
//        Cursor cursor = readSubjects();
//        Toast.makeText(this,"Total rows in db = "+cursor.getCount(),Toast.LENGTH_LONG).show();
        cv.clear();

    }
    protected static Cursor readSubjects() {
        return MainActivity.dataBase.getWritableDatabase().query(DataBaseHelper.NAME,
                DataBaseHelper.columns, null, new String[] {}, null, null,
                null);
    }

    protected static Cursor readSubjectsNameAndPercentage() {
        return MainActivity.dataBase.getWritableDatabase().query(DataBaseHelper.NAME,
                DataBaseHelper.columnSubjectNameandPercentage, null, new String[] {}, null, null,
                null);
    }


}
