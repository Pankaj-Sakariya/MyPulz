package com.example.mypulz.UICore.Detail;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.DoctorListFragment.OnListFragmentInteractionListener;
import com.example.mypulz.UICore.Detail.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Common.CommonFunction;
import Common.Constant;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDoctorListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoctorListRecyclerViewAdapter.ViewHolder> {

    private final JSONObject mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    AsyncTask HttpServiceCallBookAppointment = null;
    Calendar c;
    int year, month, day;
    java.sql.Date currDate;
    String output;
    public MyDoctorListRecyclerViewAdapter(JSONObject items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
        mValues = items;
        mListener = listener;
        mfragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doctorlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try{
            holder.mtv_doctor_name.setText("Dr "+mValues.getJSONArray("data").getJSONObject(position).getString("first_name") +" "+ mValues.getJSONArray("data").getJSONObject(position).getString("last_name"));
            holder.mtv_address.setText(mValues.getJSONArray("data").getJSONObject(position).getString("business_address")+" "+ mValues.getJSONArray("data").getJSONObject(position).getString("business_pincode"));
            holder.mtv_speciality.setText(mValues.getJSONArray("data").getJSONObject(position).getString("first_name"));
            holder.mtv_time.setText(mValues.getJSONArray("data").getJSONObject(position).getString("open_time") +" To "+ mValues.getJSONArray("data").getJSONObject(position).getString("close_time"));
            //holder.mtv_doctor_id.setText(position.);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.mBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(holder.getPosition());
                final Dialog dialog = new Dialog(mfragment);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.dilog_book_appointment);

                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog
                        .findViewById(R.id.btn_book_appointment);
                EditText edt_patient_name=(EditText)dialog.findViewById(R.id.edt_patient_name);
                EditText edt_mobile_number=(EditText)dialog.findViewById(R.id.edt_mobile_number);
                EditText edt_reason_for_visit=(EditText)dialog.findViewById(R.id.edt_reason_for_visit);
                EditText edt_datetime=(EditText)dialog.findViewById(R.id.edt_datetime);

                String str_customer_detail =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, mfragment);
                JSONArray jsonArray_customer_detail = null;
                try {
                    jsonArray_customer_detail = new JSONArray(str_customer_detail);
                    edt_patient_name.setText(new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"first_name"));
                    edt_mobile_number.setText(new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"last_name"));
                    edt_reason_for_visit.setText (new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"mobile_number"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                edt_datetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getCalenderView();
                        setcalanderview();
                    }
                });

                dialog.show();
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void getCalenderView() {
        // TODO Auto-generated method stub

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        currDate = new java.sql.Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date()); // Now use today date.
        output = sdf.format(c1.getTime());


    }

    public void setcalanderview()
    {

        DatePickerDialog dpd = new DatePickerDialog(mfragment,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedyear,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub

                        year = selectedyear;
                        month = monthOfYear;
                        day = dayOfMonth;
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + (month + 1) + "-" + day);

                            DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                            String selectedDate = outputFormatter.format(date); // Output
                            if (selectedDate.compareTo(currDate.toString()) >= 0 && selectedDate.compareTo(output.toString()) <= 0) {
                                DateFormat outputFormatter1 = new SimpleDateFormat(
                                        "dd MMM, yyyy");
                                String date_formating = outputFormatter1.format(date);


                            } else {
                                // new CommonFunction().showAlertDialog("Invaid Date","Selecct Proper Date",activity);
                            }
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                }, year, month, day);
        dpd.getDatePicker().setMinDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, 1);
        dpd.getDatePicker().setMaxDate(c.getTimeInMillis());
        c.add(Calendar.MONTH, -1);
        dpd.show();
        dpd.setCancelable(false);
        dpd.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getItemCount() {
        try {
            return mValues.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mtv_doctor_name;
        public final TextView mtv_address;
        public final TextView mtv_speciality;
        public final TextView mtv_doctor_id;
        public final TextView mid;
        public final TextView mtv_time;
        public final AppCompatRatingBar mrating_bar_doctor;


       // public final TextView mtv_doctor_name;
        public Button mBookAppointment;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mtv_doctor_name = (TextView) view.findViewById(R.id.tv_doctor_name);
            mtv_address = (TextView) view.findViewById(R.id.tv_address);
            mtv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
            mid = (TextView) view.findViewById(R.id.id);
            mtv_time =   (TextView) view.findViewById(R.id.tv_time);
            mtv_doctor_id = (TextView) view.findViewById(R.id.tv_doctor_id);
            mBookAppointment = (Button) view.findViewById(R.id.btn_book_appointment);
            mrating_bar_doctor = (AppCompatRatingBar) view.findViewById(R.id.rating_bar_doctor);

        }

    }

    private void httpServiceCall() {
        CommonFunction.showActivityIndicator(mfragment,mfragment.getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallBookAppointment = new AsyncTask() {
            JSONObject response;
            String bookAppointmentGetModel = LoginModel.BookAppointmentGetModel("","","","","","");
            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.BookAppointment(mfragment,bookAppointmentGetModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }
                    @Override
                    public void callbackSuccess(Object result) {

                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {

                super.onPostExecute(o);
                CommonFunction.HideActivityIndicator(mfragment);


            }
        };
    }
}
