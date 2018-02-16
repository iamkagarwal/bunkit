package com.kartikdev.kartik.bunkit;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

/**
 * Created by Kartik on 7/11/2017.
 */

public class CursorAdapter extends ResourceCursorAdapter {
    Context c;
    String subjectName;
    char firstLetter;
    TextView firstLetterView;

    CursorAdapter(Context c,int layout,Cursor cursor,int flags)
    {
        super(c,layout,cursor,flags);
        this.c=c;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return inflater.inflate(R.layout.list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView subjectView = (TextView) view.findViewById(R.id.subject);
        subjectView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.SUBJECT_NAME)));

        subjectName=cursor.getString(cursor.getColumnIndex(DataBaseHelper.SUBJECT_NAME));
        firstLetter=subjectName.charAt(0);

        firstLetterView=(TextView) view.findViewById(R.id.firstLetter);
        firstLetterView.setText(firstLetter+"");

        TextView idView=(TextView)view.findViewById(R.id._id);
        idView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper._ID)));

        TextView percentView=(TextView)view.findViewById(R.id.percent);
        percentView.setText(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERCENT))+"%");

        double percent=Double.valueOf(cursor.getString(cursor.getColumnIndex(DataBaseHelper.PERCENT)));
        int minPercent=Integer.parseInt(cursor.getString(cursor.getColumnIndex(DataBaseHelper.MIN_PERCENT)));

        if(percent<minPercent)
        {
            percentView.setTextColor(c.getResources().getColor(R.color.red));
        }
        else
        {
            percentView.setTextColor(c.getResources().getColor(R.color.green));
        }


    }
}
