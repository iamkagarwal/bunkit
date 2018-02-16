package com.kartikdev.kartik.bunkit;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
private static final int ADD_SUBJECT=0;
    String i;
    int flag=0;
    private static final int SUBJECT_DETAILS=1;
    //private DataBaseHelper mDbHelper;
//    SimpleCursorAdapter mAdapter;
    CursorAdapter mAdapter;
    static int edit=2;
    ListView list;
    Cursor c = null;
    static DataBaseHelper dataBase;
    TextView id,totalBunks,totalPer;
    String idInString,subjectName,minPer,bunkedClass,totalClass,percent;
    int idInInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

          dataBase = new DataBaseHelper(getApplicationContext());
        totalBunks=(TextView)findViewById(R.id.totalBunks);
        totalPer=(TextView)findViewById(R.id.avgPercent);
        list=(ListView)findViewById(R.id.list);
        mAdapter=new CursorAdapter(this,R.layout.list,c,0);

        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity.this,SubjectDetails.class);
                id=(TextView)view.findViewById(R.id._id);
                idInString=id.getText().toString();
                idInInt=Integer.parseInt(idInString);
                Log.i("BunkIt","id of selected subject"+idInInt);
                Cursor c=dataBase.getWritableDatabase().query(DataBaseHelper.NAME,DataBaseHelper.columns,DataBaseHelper._ID+"=?"
                        ,new String[]{idInString},null,null,null);
                Log.i("BunkIt","number of rows picked = "+c.getCount());
                    if(c!=null)
                    {
                        c.moveToFirst();
                        Log.i("BunkIt","id picked from database"+c.getInt(c.getColumnIndex(DataBaseHelper._ID)));
                        subjectName = c.getString(c.getColumnIndex(DataBaseHelper.SUBJECT_NAME));
                        minPer = c.getString(c.getColumnIndex(DataBaseHelper.MIN_PERCENT));
                        bunkedClass = c.getString(c.getColumnIndex(DataBaseHelper.BUNKED_CLASSES));
                        totalClass = c.getString(c.getColumnIndex(DataBaseHelper.TOTAL_CLASSES));
                        percent = c.getString(c.getColumnIndex(DataBaseHelper.PERCENT));
//                c.close();
                        intent.putExtra("subject name", subjectName);
                        intent.putExtra("minimum percentage", minPer);
                        intent.putExtra("bunked classes", bunkedClass);
                        intent.putExtra("total classes", totalClass);
                        intent.putExtra("attendance percentage", percent);
                        intent.putExtra("id", idInInt);
                        startActivityForResult(intent, SUBJECT_DETAILS);
                    }
            }
        });

                registerForContextMenu(list);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mf=getMenuInflater();
        mf.inflate(R.menu.context_menu,menu);
        TextView tv=v.findViewById(R.id._id);
//         i=tv.getText().toString();
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
//        TextView id1=findViewById(R.id._id);
//        Integer.parseInt(id1.getText().toString());
        int i=(int)info.id;

        switch (item.getItemId())
        {
            case R.id.deleteSubject:
               int row =  dataBase.getWritableDatabase().delete(DataBaseHelper.NAME,DataBaseHelper._ID+"=?",new String[]{String.valueOf(i)});
                c=AddSubject.readSubjectsNameAndPercentage();
                mAdapter.swapCursor(c);
                calculateTotal();
               // Toast.makeText(this,"No of rows deleted = "+ row,Toast.LENGTH_LONG).show();
                return true;
            case R.id.edit:

                Intent intent=new Intent(MainActivity.this,AddSubject.class);

                intent.putExtra("id",i);
               intent.putExtra("flag",1);

                startActivityForResult(intent,edit);
                return true;

            default:return false;
        }
    }

    void deleteMenu()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_subject_button,menu);
        return super.onCreateOptionsMenu(menu);


    }

    void calculateTotal()
    {
        double totalPercent=0,temp1=0.0;int totalBunkedClasses=0,temp2=0;
        Cursor read=dataBase.getWritableDatabase().query(DataBaseHelper.NAME,
                DataBaseHelper.columnPercentageAndBunked, null, new String[] {}, null, null,
                null);
        if(read!=null)
        {
            read.moveToFirst();
            int i=read.getCount();

            while (i>0) {
                temp2 = Integer.parseInt(read.getString(read.getColumnIndex(DataBaseHelper.BUNKED_CLASSES)));
                totalBunkedClasses = temp2 + totalBunkedClasses;
                read.moveToNext();
                i--;
            }


            totalBunks.setText(totalBunkedClasses+"");
        read.moveToFirst();
            i=read.getCount();
            while (i>0)
            {
                temp1=Double.parseDouble(read.getString(read.getColumnIndex(DataBaseHelper.PERCENT)));
                totalPercent = temp1+totalPercent;
                i--;
                read.moveToNext();
            }
            i=read.getCount();
            totalPercent=totalPercent/i;
            totalPercent = ((double) (Math.round(totalPercent * 100))) / 100;
            totalPer.setText((totalPercent)+"%");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK && requestCode == ADD_SUBJECT) {
            c = AddSubject.readSubjectsNameAndPercentage();

//            mAdapter=new SimpleCursorAdapter(this,R.layout.list,c,DataBaseHelper.columnSubjectNameandPercentage,new int[]{R.id._id,R.id.subject,R.id.percent},0);
//            list.setAdapter(mAdapter);
//            Toast.makeText(this, "cursor created", Toast.LENGTH_LONG).show();
            mAdapter.swapCursor(c);

//            c.close();


        }
        if (resultCode == RESULT_OK && requestCode == edit)
        {
            c = AddSubject.readSubjectsNameAndPercentage();
            mAdapter.swapCursor(c);
        }
        if (resultCode == RESULT_OK && requestCode == SUBJECT_DETAILS) {
            c = AddSubject.readSubjectsNameAndPercentage();
            mAdapter.swapCursor(c);

//            c.close();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.addButton:
                Intent intent=new Intent(MainActivity.this,AddSubject.class);
                startActivityForResult(intent,ADD_SUBJECT);
                break;
            case R.id.deleteButton:
                AddSubject.clearAll();
                totalBunks.setText("");
                totalPer.setText("");
                mAdapter.swapCursor(null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        c=AddSubject.readSubjectsNameAndPercentage();
        mAdapter.swapCursor(c);
        calculateTotal();

    }
}
