package com.example.mypulz.UICore.Detail;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MyAppointmentFragment.OnListFragmentInteractionListener;
import com.example.mypulz.UICore.Detail.dummy.DummyContent.DummyItem;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<MyAppointmentRecyclerViewAdapter.ViewHolder> {

    private final JSONObject  mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    public MyAppointmentRecyclerViewAdapter(JSONObject items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
        mValues = items;
        mListener = listener;
        mfragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_myappointment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        try{
            holder.mtv_doctor_name.setText("Dr "+mValues.getJSONArray("data").getJSONObject(position).getString("vendor_name"));
            holder.mtv_date.setText(mValues.getJSONArray("data").getJSONObject(position).getString("appointment_date"));
            holder.mtv_time.setText(mValues.getJSONArray("data").getJSONObject(position).getString("time_slot"));
            holder.mtv_speciality.setText(mValues.getJSONArray("data").getJSONObject(position).getString("reason_for_visit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        holder.mRatting.addOnAttachStateChangeListener(new );

        holder.mbtn_doctor_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("!!!!pankaj_click on list"+holder.mItem);
                final Dialog dialog = new Dialog(mfragment);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                dialog.setContentView(R.layout.dilog_review_docter);

                // set the custom dialog components - text, image and button

                Button btn_register = (Button) dialog.findViewById(R.id.btn_register);
                EditText edt_comment=(EditText)dialog.findViewById(R.id.edt_comment);


                // if button is clicked, close the custom dialog
                btn_register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        try {

            return mValues.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mtv_doctor_name,mtv_date,mtv_time,mtv_speciality;
        public final TextView mContentView;
        public DummyItem mItem;
        public Button mbtn_doctor_review;
        public AppCompatRatingBar mRatting;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mtv_doctor_name = (TextView) view.findViewById(R.id.tv_doctor_name);
            mtv_date = (TextView) view.findViewById(R.id.tv_date);
            mtv_time = (TextView) view.findViewById(R.id.tv_time);
            mtv_speciality = (TextView) view.findViewById(R.id.tv_speciality);
            mContentView = (TextView) view.findViewById(R.id.tv_time);
            mbtn_doctor_review = (Button) view.findViewById(R.id.btn_doctor_review);
            mRatting = (AppCompatRatingBar) view.findViewById(R.id.rating_bar_doctor);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
