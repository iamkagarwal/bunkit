package com.kartikdev.kartik.bunkit;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Kartik on 7/7/2017.
 */

public class SubjectDetails extends Activity {

    String subjectNameFromIntent,minimumPercentFromIntent,bunkedClassesFromIntent,totalClassesFromIntent,percentageFromIntent;
    TextView subjectView,minimumView,bunkedView,totalView,percentView,disp1,disp2,disp3,disp4;
    int bunk,tot,idFromIntent,minimumPercentInInt,edit=0;
    Button bunkInc,bunkDec,totalInc,totalDec;
    Double percent,classesToAttend,classesToBunk;
    ProgressBar progress;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_details);





        subjectNameFromIntent=getIntent().getStringExtra("subject name");
        minimumPercentFromIntent=getIntent().getStringExtra("minimum percentage");
        bunkedClassesFromIntent=getIntent().getStringExtra("bunked classes");
        totalClassesFromIntent=getIntent().getStringExtra("total classes");
        percentageFromIntent=getIntent().getStringExtra("attendance percentage");
        idFromIntent=getIntent().getIntExtra("id",-1);
        percentView=findViewById(R.id.percentDisplay);


        bunkInc=(Button)findViewById(R.id.bunkedClassInc);
        bunkDec=(Button)findViewById(R.id.bunkedClassDec);
        totalInc=(Button)findViewById(R.id.totalInc);
        totalDec=(Button)findViewById(R.id.totalDec);
        disp1=findViewById(R.id.displayView1);
        disp2=findViewById(R.id.displayView2);
        disp3=findViewById(R.id.displayView3);
        disp4=findViewById(R.id.displayView4);


        subjectView=(TextView)findViewById(R.id.subjectName);
        minimumView=(TextView)findViewById(R.id.percentage);
        bunkedView=(TextView)findViewById(R.id.bunked);
        totalView=(TextView)findViewById(R.id.total);
        progress=findViewById(R.id.progressBar);



        bunkedView.setText(bunkedClassesFromIntent);
        bunk=Integer.parseInt(bunkedView.getText().toString());
        totalView.setText(totalClassesFromIntent);
        tot=Integer.parseInt(totalView.getText().toString());
        minimumView.setText(minimumPercentFromIntent+"%");
        subjectView.setText(subjectNameFromIntent);
        percentView.setText(percentageFromIntent+"%");
        percent=Double.valueOf(percentageFromIntent.toString());
        minimumPercentInInt=Integer.parseInt(minimumPercentFromIntent.toString());
        calculate();
        bunkInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bunkDec.setEnabled(true);
                bunk++;
                tot++;
                bunkedView.setText(bunk+"");
                totalView.setText(tot+"");
                calculate();
                if(bunk==tot)
                {

                }
                else
                {
                    totalDec.setEnabled(true);
                }
            }
        });

        totalInc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tot++;
                totalView.setText(tot+"");
                totalDec.setEnabled(true);
                calculate();
            }
        });

        bunkDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bunk>0&&tot>0)
                {

                    bunk--;
                    tot--;
                    if (bunk == 0)
                    {

                        bunkDec.setEnabled(false);
                        bunkedView.setText("0");
                    }
                    else
                    {
                        bunkedView.setText(bunk + "");
                    }

                    totalView.setText(tot + "");

                }
                calculate();

            }
        });

        totalDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tot--;
                if(tot<bunk)
                {
                    tot=bunk;

                }
                else {
                    if (tot >= 0) {
                        totalView.setText(tot + "");
                        if(tot==bunk)
                            totalDec.setEnabled(false);
                    } else {
                        tot = 0;
                        totalView.setText("0");
                        totalDec.setEnabled(false);
                    }
                    calculate();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("bunked",bunk);
        outState.putInt("total",tot);
        outState.putDouble("percent",percent);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        bunkedClassesFromIntent=String.valueOf(savedInstanceState.getInt("bunked"));
        totalClassesFromIntent=String.valueOf(savedInstanceState.getInt("total"));
        percentageFromIntent=String.valueOf(savedInstanceState.getDouble("percent"));
        bunkedView.setText(bunkedClassesFromIntent);
        bunk=Integer.parseInt(bunkedView.getText().toString());
        totalView.setText(totalClassesFromIntent);
        tot=Integer.parseInt(totalView.getText().toString());
        percentView.setText(percentageFromIntent+"%");
        calculate();

    }

    void calculate()
    {
        percent = (((tot - bunk) / Double.valueOf(tot)) * 100);
        percent = ((double) (Math.round(percent * 100))) / 100;
        percentView.setText(percent + "%");
        int progresspercent = (int) Math.round(percent);
        int i = 0;
        while (i <= progresspercent) {
            progress.setProgress(i);
            i++;
        }


            double minPerDivByHund = (minimumPercentInInt / 100.00);
            if (percent < minimumPercentInInt) {
                classesToAttend = (bunk - tot * (1 - minPerDivByHund)) / (1 - minPerDivByHund);
                classesToAttend = ((double) (Math.round(classesToAttend * 100))) / 100;
                int attend = (int) Math.ceil(classesToAttend);
                if (attend < 0)
                    attend = 0;
                disp1.setText("You need ");
                disp2.setText(attend + " ");
                disp3.setText("classes for safe");
                disp4.setText("attendance");

            } else {
                classesToBunk = (tot - (minPerDivByHund * tot) - bunk) / minPerDivByHund;
                classesToBunk = ((double) (Math.round(classesToBunk * 100))) / 100;
                int bunk = (int) Math.floor(classesToBunk);
                if (bunk < 0) {
                    bunk = 0;
                }
                disp1.setText("You have ");
                disp2.setText(bunk + " ");
                disp3.setText("safe bunks available");
                disp4.setText("");

            }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.edit_button,menu);
//        return super.onCreateOptionsMenu(menu);
//
//
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//
//            case R.id.edit:
//                Intent intent=new Intent(SubjectDetails.this,AddSubject.class);
//                intent.putExtra("id",idFromIntent);
//                intent.putExtra("flag",1);
//                intent.putExtra("subject name", subjectNameFromIntent);
//                intent.putExtra("minimum percentage", minimumPercentFromIntent);
//                intent.putExtra("bunked classes", bunkedView.getText().toString());
//                intent.putExtra("total classes", totalView.getText().toString());
//                startActivityForResult(intent,edit);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == edit)
//        {
//
//        }
//    }



    @Override
    protected void onPause()
    {
        super.onPause();
        percent= (((tot-bunk)/Double.valueOf(tot))*100);
        percent = ((double)(Math.round(percent*100)))/100;
        ContentValues cv=new ContentValues();
        cv.put(DataBaseHelper.BUNKED_CLASSES,bunk);
        cv.put(DataBaseHelper.TOTAL_CLASSES,tot);
        cv.put(DataBaseHelper.PERCENT,percent);

        MainActivity.dataBase.getWritableDatabase().update(DataBaseHelper.NAME,cv,DataBaseHelper._ID+"=?",new String[]{idFromIntent+""});
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);

    }
}
