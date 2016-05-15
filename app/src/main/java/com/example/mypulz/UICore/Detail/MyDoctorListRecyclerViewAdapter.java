package com.example.mypulz.UICore.Detail;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.DoctorListFragment.OnListFragmentInteractionListener;
import com.example.mypulz.UICore.Detail.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    AsyncTask HttpServiceCallBookAppointment = null;
    public MyDoctorListRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
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
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

        holder.mBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("!!!!pankaj_click on list"+holder.mItem);
                final Dialog dialog = new Dialog(mfragment);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.dilog_book_appointment);

                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog
                        .findViewById(R.id.btn_book_appointment);
                EditText edt_patient_name=(EditText)dialog.findViewById(R.id.edt_patient_name);
                EditText edt_mobile_number=(EditText)dialog.findViewById(R.id.edt_mobile_number);
                EditText edt_reason_for_visit=(EditText)dialog.findViewById(R.id.edt_reason_for_visit);
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

                dialog.show();
            }
        });
      /*  holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    System.out.println("!!!!pankaj_click on list"+holder.mItem);
                    BookAppointmentFragment newFragment =  new BookAppointmentFragment();


                }
            }
        });*/
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Button mBookAppointment;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.tv_speciality);
            mBookAppointment = (Button)view.findViewById(R.id.btn_book_appointment);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
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
