package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Common.CommonFunction;
import Common.ConnectionDetector;
import Common.Constant;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

public class LoginActivity extends Activity {

    AsyncTask HttpServiceCallLogin = null;
    Activity activity = null;
    Button btnlogin,btnsignup;
    TextView txtOtpPassword,txtMobileNumber;
    CheckBox chk_remember_me;
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
        chk_remember_me = (CheckBox)findViewById(R.id.chk_remember_me);



//        txtOtpPassword.setText("12345");
//        txtMobileNumber.setText("1234567890");

    }
    private void setData() {

        Intent i = getIntent();
        String str_mobile_number = i.getStringExtra(Constant.TAG_mobile_number);
        txtMobileNumber.setText(str_mobile_number);

        Toast.makeText(LoginActivity.this,"Your OTP is : 12345",Toast.LENGTH_LONG).show();

        txtOtpPassword.setText("12345");

        String chk_remember_me_value =  new CommonFunction().getSharedPreference("chk_remember_me",activity);
        if(chk_remember_me_value.equalsIgnoreCase("1"))
        {
            chk_remember_me.setChecked(true);
//            txtMobileNumber.setText(new CommonFunction().getSharedPreference("username",activity));
        }
        else if (chk_remember_me_value.equalsIgnoreCase("0"))
        {
            chk_remember_me.setChecked(false);
        }

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

                    if(new ConnectionDetector(activity).isConnectingToInternet() == true) {
                        if(chk_remember_me.isChecked())
                        {
                            new CommonFunction().saveSharedPreference("chk_remember_me","1",activity);
                            new CommonFunction().saveSharedPreference("username",txtMobileNumber.getText().toString(),activity);
                        }
                        else
                        {
                            new CommonFunction().saveSharedPreference("chk_remember_me","0",activity);
                        }
                        httpServiceCall();
                        HttpServiceCallLogin.execute(null);
                    }
                    else
                    {
                        Toast.makeText(activity, "Something went wrong, either check you Internet connection or try after some time", Toast.LENGTH_LONG).show();
                    }
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
        if(txtMobileNumber.getText().length() > 10)
        {
            flag = false;
            txtMobileNumber.setError("Please Enter Valid Mobile Number");
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

                JSONArray jsonArray_customer_detail, jsonArray_category, jsonArray_vendor,jsonArray_city, jsonArray_area;

                try {
                    if (response.has("status")) {
                        if (response.getInt("status") == 1) {
                            if (response.has("message")) {
                                String Message = response.getString("message");

                                /** Parse Json Array Using Common Function**/
                                jsonArray_customer_detail = new CommonFunction().parseJsonArray(Constant.TAG_jArray_customer_detail, response);
                                jsonArray_category = new CommonFunction().parseJsonArray(Constant.TAG_jArray_category, response);
                                jsonArray_vendor = new CommonFunction().parseJsonArray(Constant.TAG_jArray_vendor, response);
                                jsonArray_city = new CommonFunction().parseJsonArray(Constant.TAG_jArray_city, response);
                                jsonArray_area = new CommonFunction().parseJsonArray(Constant.TAG_jArray_area, response);

                                /** Save array in preference as string Using Common Function**/
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_customer_detail, jsonArray_customer_detail.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_category, jsonArray_category.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_vendor, jsonArray_vendor.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_city, jsonArray_city.toString(), activity);
                                new CommonFunction().saveSharedPreference(Constant.TAG_jArray_area, jsonArray_area.toString(), activity);

                                new CommonFunction().saveSharedPreference(Constant.TAG_login_verified, "1", activity);

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }

                        } else if (response.getInt("status") == 0) {
                            if (response.has("message")) {
                                String Message = response.getString("message");
                                //                            new CommonFunction().showAlertDialog(Message,"Testing",activity);
                                Toast.makeText(activity, Message, Toast.LENGTH_LONG).show();
                                new CommonFunction().saveSharedPreference(Constant.TAG_login_verified, "0", activity);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

