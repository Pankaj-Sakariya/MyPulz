package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MainActivity;

import org.json.JSONObject;

import Common.CommonFunction;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

public class LoginActivity extends Activity {

    AsyncTask HttpServiceCallLogin = null;
    Activity activity = null;
    Button btnlogin,btnsignup;
    TextView txtOtpPassword,txtMobileNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        activity = this;
        setView();
        setData();
        //HttpServiceCallInit.execute(null);
    }
    private void setView() {
        btnlogin = (Button)findViewById(R.id.btnlogin);
        btnsignup = (Button)findViewById(R.id.btnsignup);
        txtOtpPassword = (TextView)findViewById(R.id.txtOtpPassword);
        txtMobileNumber = (TextView)findViewById(R.id.txtMobileNumber);

        txtOtpPassword.setText("12345");
        txtMobileNumber.setText("1234567890");

    }
    private void setData() {

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation() == true)
                {
                    httpServiceCall();
                    HttpServiceCallLogin.execute(null);
                }

            }
        });
    }

    private boolean validation()
    {
        boolean flag = true;
        if(txtMobileNumber.getText().length() == 0)
        {
            flag = false;
            txtMobileNumber.setError("Please Enter Mobile Number");
            txtMobileNumber.setFocusable(true);
        }
        else if(txtOtpPassword.getText().length() == 0)
        {
            flag = false;
            txtOtpPassword.setError("Please Enter OTP");
            txtOtpPassword.setFocusable(true);
        }
        return  flag;
    }
    private void httpServiceCall() {
        CommonFunction.showActivitityIndicater(activity,getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallLogin = new AsyncTask() {
            JSONObject response;
            String loginPostModel = LoginModel.LoginPostModel(txtMobileNumber.getText().toString(),txtOtpPassword.getText().toString());
            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.Login(activity,loginPostModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        CommonFunction.HideActivitityIndicater(activity);
                        System.out.println(result);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
//                        try {
//                            response = new JSONObject(result.toString());
//                            JSONArray jsonArray_customer_detail,jsonArray_category;
//                            System.out.println("pankaj"+response);
//                            try {
//                                if(response.has("status")) {
//                                    if (response.getString("status") == "1") {
//                                        if (response.has("message")) {
//                                            String Message = response.getString("message");
//                                            /** Parse Json Array Using Common Function**/
//                                            jsonArray_customer_detail = new CommonFunction().parseJsonArray(Constant.TAG_jArray_customer_detail, response);
//                                            jsonArray_category = new CommonFunction().parseJsonArray(Constant.TAG_jArray_category, response);
//                                            /** Save array in preference as string Using Common Function**/
//                                            new CommonFunction().saveSharedPreference(Constant.TAG_jArray_customer_detail, jsonArray_customer_detail.toString(), activity);
//                                            new CommonFunction().saveSharedPreference(Constant.TAG_jArray_category, jsonArray_category.toString(), activity);
//                                        }
//
//                                    }
//                                }
//                                else if(response.has("status") && response.getString("status")=="0")
//                                {
//                                    if(response.has("message")) {
//                                        String Message = response.getString("message");
//                                        new CommonFunction().showAlertDialog(Message,"Testing",activity);
//                                    }
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                return null;
            }

        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

