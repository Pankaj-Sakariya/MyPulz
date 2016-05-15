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

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyAppointmentRecyclerViewAdapter extends RecyclerView.Adapter<MyAppointmentRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    public MyAppointmentRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
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
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).content);

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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;
        public Button mbtn_doctor_review;
        public AppCompatRatingBar mRatting;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
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
