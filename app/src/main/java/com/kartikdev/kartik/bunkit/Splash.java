package com.kartikdev.kartik.bunkit;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.os.Handler;


/**
 * Created by Kartik on 7/14/2017.
 */

public class Splash extends Activity {
private final int SPLASH_LENGTH=800;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences setting=this.getSharedPreferences("appInfo",0);
        boolean firstTime=setting.getBoolean("first_time",true);
        if(firstTime)
        {
            SharedPreferences.Editor editor=setting.edit();
            editor.putBoolean("first_time",false);
            editor.commit();
        }

        setContentView(R.layout.splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        },SPLASH_LENGTH);



    }



}
