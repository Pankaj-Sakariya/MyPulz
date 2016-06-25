package com.example.mypulz.UICore.TableViewCell;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.DoctorListFragment.OnListFragmentInteractionListener;
import com.hedgehog.ratingbar.RatingBar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Common.CommonFunction;
import Common.Constant;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DoctorListRA extends RecyclerView.Adapter<DoctorListRA.ViewHolder> {

    private final JSONObject mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    AsyncTask HttpServiceCallBookAppointment = null;
    AsyncTask HttpServiceCallGetReview = null;

    ArrayList<String> arrayList_timeslot;
    ArrayAdapter<String> spinner_timeslot_adapter;
    Calendar c;
    int year, month, day;
    java.sql.Date currDate;
    String output;

    public DoctorListRA(JSONObject items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
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
        String contact_no = null;
        try {
            holder.mtv_doctor_name.setText("Dr " + mValues.getJSONArray("data").getJSONObject(position).getString("first_name") + " " + mValues.getJSONArray("data").getJSONObject(position).getString("last_name"));
            holder.mtv_address.setText(mValues.getJSONArray("data").getJSONObject(position).getString("business_address") + " " + mValues.getJSONArray("data").getJSONObject(position).getString("business_pincode"));
            holder.mtv_speciality.setText(mValues.getJSONArray("data").getJSONObject(position).getString("category_name"));
            holder.mtv_time.setText(mValues.getJSONArray("data").getJSONObject(position).getString("open_time") + " To " + mValues.getJSONArray("data").getJSONObject(position).getString("close_time"));
            holder.mrating_bar_doctor.setRating(Float.valueOf(mValues.getJSONArray("data").getJSONObject(position).getString("review_star")));
            holder.mtv_doctor_id.setText((mValues.getJSONArray("data").getJSONObject(position).getString("id")));
            contact_no = (mValues.getJSONArray("data").getJSONObject(position).getString("mobile_number"));
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
                final EditText edt_patient_name = (EditText) dialog.findViewById(R.id.edt_patient_name);
                final EditText edt_mobile_number = (EditText) dialog.findViewById(R.id.edt_mobile_number);
                final EditText edt_reason_for_visit = (EditText) dialog.findViewById(R.id.edt_reason_for_visit);
                final EditText edt_datetime = (EditText) dialog.findViewById(R.id.edt_datetime);
                final Spinner spinner_timeslot = (Spinner) dialog.findViewById(R.id.spinner_timeslot);

                c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);


                edt_datetime.setText(day + " " + (month + 1) + " " + year);
                edt_datetime.setText(new CommonFunction().changeDateFormat(edt_datetime.getText().toString(), "dd MM yyyy", "dd MMM, yyyy"));

                String str_customer_detail = new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, mfragment);
                JSONArray jsonArray_customer_detail = null;
                JSONArray jsonArray_timeslot = null;
                try {
                    jsonArray_customer_detail = new JSONArray(str_customer_detail);
                    edt_patient_name.setText(new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail, "first_name") + " " + new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail, "last_name"));
                    edt_mobile_number.setText(new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail, "mobile_number"));
                    // edt_reason_for_visit.setText (new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"mobile_number"));
                    jsonArray_timeslot = mValues.getJSONArray("data").getJSONObject(holder.getPosition()).getJSONArray("timeslot");
                    arrayList_timeslot = new CommonFunction().parseJsonArrayToMap(jsonArray_timeslot, "time_slot");
                    if (arrayList_timeslot.size() == 0) {
                        arrayList_timeslot.add(0, "No TimeSlot Available");
                        spinner_timeslot.setEnabled(false);
                    }

                    spinner_timeslot_adapter = new ArrayAdapter<String>(dialog.getContext(), android.R.layout.select_dialog_item, arrayList_timeslot);
                    spinner_timeslot.setAdapter(spinner_timeslot_adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // if button is clicked, close the custom dialog
                final JSONArray finalJsonArray_customer_detail = jsonArray_customer_detail;
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (edt_patient_name.getText().toString().length() == 0) {
                            edt_patient_name.setError("Please Enter Patient Name");
                            edt_patient_name.setFocusable(true);
                        } else if (edt_mobile_number.getText().toString().length() == 0) {
                            edt_mobile_number.setError("Please Enter Mobile Number");
                            edt_mobile_number.setFocusable(true);
                        } else if (edt_reason_for_visit.getText().toString().length() == 0) {
                            edt_reason_for_visit.setError("Please Enter Reason For Visit");
                            edt_reason_for_visit.setFocusable(true);
                        } else if (edt_datetime.getText().toString().length() == 0) {
                            edt_datetime.setError("Please Select Date");
                            edt_datetime.setFocusable(true);
                        } else if (spinner_timeslot.getSelectedItemPosition() == 0 && spinner_timeslot.getSelectedItem().toString().equalsIgnoreCase("No TimeSlot Available")) {
//                            edt_datetime.setError("Please Select Date");
//                            edt_datetime.setFocusable(true);
                            Toast.makeText(dialog.getContext(), "You can not book appointment for this doctor, please visit clinic.", Toast.LENGTH_LONG).show();
                        } else {
                            String bookAppointmentGetModel = LoginModel.BookAppointmentGetModel(
                                    holder.mtv_doctor_id.getText().toString(),
                                    new CommonFunction().parseStringFromJsonArray(finalJsonArray_customer_detail, "id"),
                                    edt_patient_name.getText().toString(),
                                    edt_mobile_number.getText().toString(),
                                    edt_reason_for_visit.getText().toString(),
                                    new CommonFunction().changeDateFormat(edt_datetime.getText().toString(), "dd MMM, yyyy", "yyyy-MM-dd"),
                                    String.valueOf(spinner_timeslot.getSelectedItemPosition() + 1)
                            );

                            System.out.println("!!!!pankaj_BookAppointment" + bookAppointmentGetModel);
                            httpServiceCall(bookAppointmentGetModel, dialog);
                            HttpServiceCallBookAppointment.execute(null);

                        }

                    }
                });
                edt_datetime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getCalenderView();
                        setcalanderview(dialog, edt_datetime);


                    }
                });

                dialog.show();
            }
        });

        holder.mbtn_doctor_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(holder.getPosition());
                final int currentPozition = (holder.getPosition());
                final Dialog dialog = new Dialog(mfragment);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.dilog_review_docter);
                // set the custom dialog components - text, image and button
                Button btn_register = (Button) dialog.findViewById(R.id.btn_register);
                final EditText edt_comment = (EditText) dialog.findViewById(R.id.edt_comment);
                RatingBar rating_bar_doctor = (RatingBar) dialog.findViewById(R.id.rating_bar_doctor);
                final RadioGroup mradioUserType = (RadioGroup) dialog.findViewById(R.id.radioUserType);
                final int[] ratingCount = new int[1];
                rating_bar_doctor.setClickable(true);
                rating_bar_doctor.setOnRatingChangeListener(
                        new RatingBar.OnRatingChangeListener() {
                            @Override
                            public void onRatingChange(int RatingCount) {
                                ratingCount[0] = RatingCount;
//                                Toast.makeText(mfragment.getApplicationContext(), "the fill star is" + RatingCount, Toast.LENGTH_LONG).show();
                            }
                        }
                );
                rating_bar_doctor.setStar(0);
                rating_bar_doctor.setmClickable(true);
                rating_bar_doctor.setStarImageSize(16f);
                rating_bar_doctor.setStarEmptyDrawable(mfragment.getResources().getDrawable(R.mipmap.ic_star_empty));
                rating_bar_doctor.setStarFillDrawable(mfragment.getResources().getDrawable(R.mipmap.ic_star_fill));
                // if button is clicked, close the custom dialog
                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        int selectedId = mradioUserType.getCheckedRadioButtonId();

                        RadioButton radioSelectedType;
                        // find the radiobutton by returned id
                        radioSelectedType = (RadioButton) dialog.findViewById(selectedId);
                        if (radioSelectedType.getText().toString().equalsIgnoreCase("Publish My Name")) {
                            selectedId = 1;
                        } else {
                            selectedId = 0;
                        }

                        String vendor_detail_id = null;
                        String user_id = null;
                        try {
                            vendor_detail_id = mValues.getJSONArray("data").getJSONObject(currentPozition).getString("id");
//                            user_id = mValues.getJSONArray("data").getJSONObject(currentPozition).getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        System.out.println("!!!!pankaj_vendor_detail_id" + vendor_detail_id);
                        try {
                            user_id = new CommonFunction().parseStringFromJsonArray(new JSONArray(new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, mfragment)), "id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        httpServiceCall(ratingCount[0], edt_comment.getText().toString(), selectedId, holder.mtv_doctor_id.getText().toString(), user_id, currentPozition);
                        HttpServiceCallGetReview.execute(null);
                    }
                });

                dialog.show();
            }
        });

        final String finalContact_no = contact_no;
        holder.mbtn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:0377778888"));
//                startActivity(callIntent);

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + finalContact_no));
                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (ActivityCompat.checkSelfPermission(mfragment, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mfragment.startActivity(callIntent);
            }
        });
    }

    private void httpServiceCall(final float rating, final String Comment, final int SelectedRadiobutton, final String vendor_detail_id , final String user_id,int Pozition) {
        CommonFunction.showActivityIndicator(mfragment, mfragment.getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallGetReview = new AsyncTask() {
            JSONObject response;
            String ReviewDocterPostModel = LoginModel.ReviewDocterPostModel(vendor_detail_id,user_id,Comment,String.valueOf(rating),String.valueOf(SelectedRadiobutton));

            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.SubmitReview(mfragment, ReviewDocterPostModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        System.out.println(result);
                        try {
                            response = new JSONObject(result.toString());

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
                CommonFunction.HideActivityIndicator(mfragment);
                try {
                    if (response.has("status")) {
                        if (response.getInt("status") == 1) {
                            if (response.has("message")) {
                                String Message = response.getString("message");

                            }
                        } else if (response.getInt("status") == 0) {
                            if (response.has("message")) {
                                String Message = response.getString("message");
                                // new CommonFunction().showAlertDialog(Message,"Testing",activity);
                                Toast.makeText(mfragment, Message, Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

//    @SuppressLint("SimpleDateFormat")
//    private void getCalenderView() {
//        // TODO Auto-generated method stub
//
//        c = Calendar.getInstance();
//        year = c.get(Calendar.YEAR);
//        month = c.get(Calendar.MONTH);
//        day = c.get(Calendar.DAY_OF_MONTH);
//        currDate = new java.sql.Date(System.currentTimeMillis());
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Calendar c1 = Calendar.getInstance();
//        c1.setTime(new Date()); // Now use today date.
//        output = sdf.format(c1.getTime());
//
//
//    }

    public void setcalanderview(Dialog dialog,final EditText edt_datetime)
    {
        final String selected_date = "";

        currDate = new java.sql.Date(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c1 = Calendar.getInstance();
        c1.setTime(new Date()); // Now use today date.
        output = sdf.format(c1.getTime());
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

                            System.out.println("!!!!pankaj_selected_date"+selectedDate);
                            if (selectedDate.compareTo(currDate.toString()) >= 0 && selectedDate.compareTo(output.toString()) <= 0) {
                                DateFormat outputFormatter1 = new SimpleDateFormat(
                                        "dd MMM, yyyy");
                                String date_formating = outputFormatter1.format(date);
                                System.out.println("!!!!pankaj_date"+date_formating);
                                edt_datetime.setText(date_formating);

                            } else {
//                                 new CommonFunction().showAlertDialog("Invaid Date","Selecct Proper Date",);
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
        public final ImageView mid;
        public final TextView mtv_time;
        public final AppCompatRatingBar mrating_bar_doctor;


       // public final TextView mtv_doctor_name;
        public Button mBookAppointment;
        public Button mbtn_doctor_review;
        public Button mbtn_call;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mtv_doctor_name = (TextView) view.findViewById(R.id.tv_doctor_name);
            mtv_address = (TextView) view.findViewById(R.id.tv_address);
            mtv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
            mid = (ImageView) view.findViewById(R.id.doctor_image);
            mtv_time =   (TextView) view.findViewById(R.id.tv_time);
            mtv_doctor_id = (TextView) view.findViewById(R.id.tv_doctor_id);
            mBookAppointment = (Button) view.findViewById(R.id.btn_book_appointment);
//            mrating_bar_doctor = (AppCompatRatingBar) view.findViewById(R.id.rating_bar_doctor);
            mbtn_doctor_review = (Button) view.findViewById(R.id.btn_doctor_review);
            mbtn_call = (Button) view.findViewById(R.id.btn_call);
             mrating_bar_doctor =(AppCompatRatingBar)view.findViewById(R.id.rating_bar_doctor);

            mrating_bar_doctor.setClickable(false);
//            mrating_bar_doctor.setOnRatingChangeListener(
//                    new RatingBar.OnRatingChangeListener() {
//                        @Override
//                        public void onRatingChange(int RatingCount) {
//                            Toast.makeText(mfragment.getApplicationContext(),"the fill star is"+RatingCount,Toast.LENGTH_LONG).show();
//                        }
//                    }
//            );
            mrating_bar_doctor.setRating(5);
//            mrating_bar_doctor.setmClickable(false);
//            mrating_bar_doctor.setStarImageSize(12f);
//            mrating_bar_doctor.setStarEmptyDrawable(mfragment.getResources().getDrawable(R.mipmap.ic_star_empty));
//            mrating_bar_doctor.setStarFillDrawable(mfragment.getResources().getDrawable(R.mipmap.ic_star_fill));
        }

    }
    private void httpServiceCall(final String mbookAppointmentGetModel, final Dialog dialog) {
        CommonFunction.showActivityIndicator(mfragment,mfragment.getResources().getString(R.string.title_for_activityIndicater));
        System.out.println("Here1");
        HttpServiceCallBookAppointment = new AsyncTask() {
            JSONObject response;
            String bookAppointmentGetModel = mbookAppointmentGetModel;
            @Override
            protected Object doInBackground(Object[] params) {
                System.out.println("Here");
                SecurityDataProvider.BookAppointment(mfragment,bookAppointmentGetModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println("!!!!pankaj_Failure"+result);
//                        dialog.dismiss();
                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        System.out.println("!!!!pankaj_Success"+result);
//                        dialog.dismiss();
                        try {
                            response = new JSONObject(result.toString());
                            System.out.println("pankaj"+response);
                            //new CommonFunction().showAlertDialog(response.toString(),"Testing",getContext());
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
                System.out.println("!!!!pankaj_Success"+response);
                if(response.has("status"))
                {
                    try {
                            if(response.get("status")==1)
                            {
                                if(response.has("message"))
                                {
                                    CommonFunction.HideActivityIndicator(mfragment);
                                    dialog.dismiss();
                                    String Message = response.get("message").toString();
                                    Snackbar.make(mfragment.getCurrentFocus(), Message, Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    Toast.makeText(mfragment,Message,Toast.LENGTH_LONG).show();
                                }

                            }
                            else if(response.get("status")==0)
                            {

                            }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    CommonFunction.HideActivityIndicator(mfragment);
                   // dialog.dismiss();
                    Toast.makeText(dialog.getContext(),"Please try after some time, something went wrong..",Toast.LENGTH_LONG).show();
                }

            }
        };
    }
}
