package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Common.CommonFunction;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

public class SignupActivity extends Activity {

    Activity activity = null;
    AsyncTask HttpServiceCallSignup = null;
    RadioGroup radioUserType;
    RadioButton radioSelectedType;
    EditText edt_first_name,edt_last_name, edt_mobile_number,edt_email_address;
    TextView btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signup);
        activity = this;
        setView();
        setData();

    }

    private void setView() {
        radioUserType = (RadioGroup) findViewById(R.id.radioUserType);
        edt_first_name = (EditText)findViewById(R.id.edt_first_name);
        edt_last_name = (EditText)findViewById(R.id.edt_last_name);
        edt_mobile_number = (EditText)findViewById(R.id.edt_mobile_number);
        edt_email_address = (EditText)findViewById(R.id.edt_email_address);
        btn_register = (TextView)findViewById(R.id.btn_register);

    }
    private void setData() {


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation() == true)
                {
                    /// get selected radio button from radioGroup
                    int selectedId = radioUserType.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    radioSelectedType = (RadioButton) findViewById(selectedId);

//                    Toast.makeText(SignupActivity.this,
//                            radioSelectedType.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();
                    httpServiceCall();
                    HttpServiceCallSignup.execute(null);
                }

            }
        });

    }


    private boolean validation()
    {
        boolean flag = true;
        if(edt_first_name.getText().length() == 0)
        {
            flag = false;
            edt_first_name.setError("Please Enter First Name");
            edt_first_name.setFocusable(true);
        }
        else if(edt_last_name.getText().length() == 0)
        {
            flag = false;
            edt_last_name.setError("Please Enter Last Name");
            edt_last_name.setFocusable(true);
        }
        else if(edt_mobile_number.getText().length() < 10)
        {
            flag = false;
            edt_mobile_number.setError("Please Enter Valid Mobile Number");
            edt_mobile_number.setFocusable(true);
        }
        else if(edt_email_address.getText().length() == 0)
        {
            flag = false;
            edt_email_address.setError("Please Enter Email Address");
            edt_email_address.setFocusable(true);
        }
        else if(!new CommonFunction().validateEmailAddress(edt_email_address.getText().toString()))
        {
            flag = false;
            edt_email_address.setError("Please Enter Valid Email Address");
            edt_email_address.setFocusable(true);
        }
        else if(radioUserType.getCheckedRadioButtonId()==-1)
        {
            flag = false;
            Toast.makeText(getApplicationContext(), "Please select User Type", Toast.LENGTH_SHORT).show();

        }
        return flag;

    }




    private void httpServiceCall() {
        CommonFunction.showActivitityIndicater(activity,getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallSignup = new AsyncTask() {
            JSONObject response;

            String signupGetModel = LoginModel.SignupGetModel(edt_first_name.getText().toString(),
                                                              edt_last_name.getText().toString(),
                                                              edt_mobile_number.getText().toString(),
                                                              edt_email_address.getText().toString(),
                                                              radioSelectedType.getText().toString());

            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.Signup(activity, signupGetModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        System.out.println(result);
                        CommonFunction.HideActivitityIndicater(activity);
                        try {
                            response = new JSONObject(result.toString());
                            JSONArray jsonArray_customer_detail,jsonArray_category;
                            System.out.println("pankaj"+response);
                            try {

                                if(response.has("status") && response.getString("status")=="1")
                                {
                                    if(response.has("message"))
                                    {
                                        String Message = response.getString("message");
                                        new CommonFunction().showAlertDialog(Message,"Testing",activity);
                                    }
                                    Intent i = new Intent(SignupActivity.this,LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else if(response.has("status") && response.getString("status")=="0")
                                {
                                    if(response.has("message")) {
                                        String Message = response.getString("message");
                                        new CommonFunction().showAlertDialog(Message,"Testing",activity);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
