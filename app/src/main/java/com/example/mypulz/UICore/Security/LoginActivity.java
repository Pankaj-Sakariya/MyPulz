package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Common.CommonFunction;
import Common.Constant;
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
        CommonFunction.showActivityIndicator(activity, getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallLogin = new AsyncTask() {
            JSONObject response;
            String loginPostModel = LoginModel.LoginPostModel(txtMobileNumber.getText().toString(), txtOtpPassword.getText().toString());

            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.Login(activity, loginPostModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }

                    @Override
                    public void callbackSuccess(Object result) {

                        System.out.println(result);
                        try {
                            response = new JSONObject(result.toString());
                            System.out.println("pankaj" + response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);
                CommonFunction.HideActivityIndicator(activity);

                JSONArray jsonArray_customer_detail, jsonArray_category, jsonArray_vendor, jsonArray_area;

                try {
                    if (response.has("status")) {
                        if (response.getInt("status") == 1) {
                            if (response.has("message")) {
                                String Message = response.getString("message");

                                /** Parse Json Array Using Common Function**/
                                jsonArray_customer_detail = new CommonFunction().parseJsonArray(Constant.TAG_jArray_customer_detail, response);
                                jsonArray_category = new CommonFunction().parseJsonArray(Constant.TAG_jArray_category, response);
                                jsonArray_vendor = new CommonFunction().parseJsonArray(Constant.TAG_jArray_vendor, response);
                                jsonArray_area = new CommonFunction().parseJsonArray(Constant.TAG_jArray_area, response);

                                /** Save array in preference as string Using Common Function**/
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_customer_detail, jsonArray_customer_detail.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_category, jsonArray_category.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_vendor, jsonArray_vendor.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_area, jsonArray_area.toString(), activity);
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else if (response.getInt("status") == 0) {
                            if (response.has("message")) {
                                String Message = response.getString("message");
                                //                            new CommonFunction().showAlertDialog(Message,"Testing",activity);
                                Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}

