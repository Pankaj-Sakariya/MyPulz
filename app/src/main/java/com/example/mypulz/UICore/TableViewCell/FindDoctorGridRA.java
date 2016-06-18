package com.example.mypulz.UICore.TableViewCell;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypulz.R;

import com.example.mypulz.UICore.Detail.DoctorListFragment;
import com.example.mypulz.UICore.Detail.FindDoctorFragment;
import com.example.mypulz.UICore.Detail.FindDoctorGridFragment;
import com.example.mypulz.UICore.Detail.MainActivity;
import com.example.mypulz.UICore.Detail.dummy.DummyContent.DummyItem;

import java.util.List;

import Common.CommonFunction;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link FindDoctorGridFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class FindDoctorGridRA extends RecyclerView.Adapter<FindDoctorGridRA.ViewHolder> {

    private final List<DummyItem> mValues;
    private final FindDoctorGridFragment.OnListFragmentInteractionListener mListener;
    private final FragmentActivity mFragment;

    public FindDoctorGridRA(List<DummyItem> items, FindDoctorGridFragment.OnListFragmentInteractionListener listener, FragmentActivity fragment) {
        mValues = items;
        mListener = listener;
        mFragment =  fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mcard_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FindDoctorFragment dFragment = new FindDoctorFragment();
                changeFragment(dFragment);
//                new CommonFunction().changeFragment(dFragment,dFragment.getFragmentManager().beginTransaction());
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

      public void changeFragment(Fragment newFragment)
    {
        // Create new transaction

        android.support.v4.app.FragmentTransaction transaction;
        transaction = mFragment.getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.setTransitionStyle(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // Commit the transaction
        transaction.commit();

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final CardView mcard_view;

        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mcard_view = (CardView) view.findViewById(R.id.card_view);

        }

    }
}
