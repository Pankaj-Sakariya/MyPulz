package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.ProgressBar;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MainActivity;

import Common.CommonFunction;
import Common.Constant;


public class SplashActivity extends Activity {

    boolean internet_flag=false;
    ProgressBar p;
    long sp_time = 3000;
    long ms = 0;
    boolean sp_Activity = true;
    boolean pause = false;
    @Nullable
    AsyncTask HttpServiceCallInit = null;
    @Nullable
    Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        activity = this;
        setView();
        setData();
       // httpServiceCall();
        //HttpServiceCallInit.execute(null);
        reDirection();
    }

    private void reDirection() {
        Thread my = new Thread() {
            public void run() {
                try {
                    while (sp_Activity && ms < sp_time) {
                        if (!pause)
                            ms = ms + 100;
                        sleep(100);
                    }
                }
                catch (Exception e) {
                } finally {
                    internet_flag = true;
                    if(new CommonFunction().getSharedPreference(Constant.TAG_login_verified,activity).equalsIgnoreCase("0"))
                    {
                        Intent i = new Intent(SplashActivity.this, OtpActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else if(new CommonFunction().getSharedPreference(Constant.TAG_login_verified,activity).equalsIgnoreCase("1"))
                    {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Intent i = new Intent(SplashActivity.this, OtpActivity.class);
                        startActivity(i);
                        finish();

                    }


                }
            }
        };
        my.start();
    }

    private void setView() {

    }
    private void setData() {

    }
    private void httpServiceCall() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}




