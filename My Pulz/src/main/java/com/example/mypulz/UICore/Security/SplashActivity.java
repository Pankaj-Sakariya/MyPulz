package com.example.onlinetestmodel.UICore.Security;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Type;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.mypulz.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.HttpResponse;


public class SplashActivity extends AppCompatActivity {

    @Nullable
    AsyncTask HttpServiceCallInit = null;
    @Nullable
    Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        activity = this;
        setView();
        setData();
        httpServiceCall();
        HttpServiceCallInit.execute(null);

    }
    private void setView() {

    }
    private void setData() {

    }
    private void httpServiceCall() {
        HttpServiceCallInit = new AsyncTask() {
            @Nullable
            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.Init(activity,"", new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        Gson gson = new Gson();
                        Type listType = new TypeToken<HttpResponse<InitGetModel<Country>>>() {}.getType();
                        HttpResponse<InitGetModel<Country>> yourClassList = new Gson().fromJson(result, listType);

                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        System.out.println(result);
                    }
                });
                return null;
            }
        };
    }
}




