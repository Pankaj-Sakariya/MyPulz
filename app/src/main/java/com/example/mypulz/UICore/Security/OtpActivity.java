package com.example.mypulz.UICore.Security;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;

import Common.CommonFunction;
import Common.ConnectionDetector;
import Common.Constant;

public class OtpActivity extends Activity {

    Button btn_get_otp;
    EditText txtMobileNumber;
    Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_otp);
        activity = this;
        setView();
        setData();
    }
    private void setView() {
        btn_get_otp = (Button)findViewById(R.id.btn_get_otp);
        txtMobileNumber = (EditText) findViewById(R.id.txtMobileNumber);
    }
    private void setData() {

        String chk_remember_me_value =  new CommonFunction().getSharedPreference("chk_remember_me",activity);
        if(chk_remember_me_value.equalsIgnoreCase("1"))
        {
//            chk_remember_me.setChecked(true);
            txtMobileNumber.setText(new CommonFunction().getSharedPreference("username",activity));
        }
        else if (chk_remember_me_value.equalsIgnoreCase("0"))
        {
//            chk_remember_me.setChecked(false);
        }


        btn_get_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validation() == true)
                {

                    if(new ConnectionDetector(activity).isConnectingToInternet() == true) {

                        Intent i = new Intent(OtpActivity.this,LoginActivity.class);
                        i.putExtra(Constant.TAG_mobile_number,txtMobileNumber.getText().toString());
                        startActivity(i);
                        finish();
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
        return  flag;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
