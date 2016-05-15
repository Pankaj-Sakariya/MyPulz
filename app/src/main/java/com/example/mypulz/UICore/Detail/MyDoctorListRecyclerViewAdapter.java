package com.example.mypulz.UICore.Detail;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.DoctorListFragment.OnListFragmentInteractionListener;
import com.example.mypulz.UICore.Detail.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDoctorListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoctorListRecyclerViewAdapter.ViewHolder> {

    private final List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;

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
                dialog.setContentView(R.layout.dilog_book_appointment);
                dialog.setTitle("Book Appointment");




                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog
                        .findViewById(R.id.btn_book_appointment);
                EditText tvName=(EditText)dialog.findViewById(R.id.edt_patient_name);
                EditText tvPhNo=(EditText)dialog.findViewById(R.id.edt_mobile_number);
                EditText tvEmail=(EditText)dialog.findViewById(R.id.edt_reason_for_visit);

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
}
