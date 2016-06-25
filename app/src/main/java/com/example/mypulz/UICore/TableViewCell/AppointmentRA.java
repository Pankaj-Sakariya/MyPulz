package com.example.mypulz.UICore.TableViewCell;

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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypulz.R;
import com.example.mypulz.UICore.Detail.MyAppointmentFragment.OnListFragmentInteractionListener;
import com.hedgehog.ratingbar.RatingBar;

import org.json.JSONException;
import org.json.JSONObject;

import Common.CommonFunction;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

/**
 * {@link RecyclerView.Adapter} that can display a  and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class AppointmentRA extends RecyclerView.Adapter<AppointmentRA.ViewHolder>   {

    private final JSONObject  mValues;
    private final OnListFragmentInteractionListener mListener;
    private final FragmentActivity mfragment;
    AsyncTask HttpServiceCallGetReview = null;
    public AppointmentRA(JSONObject items, OnListFragmentInteractionListener listener, FragmentActivity fragment) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        try{
            holder.mtv_doctor_name.setText("Dr "+mValues.getJSONArray("data").getJSONObject(position).getString("vendor_name"));
            holder.mtv_date.setText(mValues.getJSONArray("data").getJSONObject(position).getString("appointment_date"));
            holder.mtv_time.setText(mValues.getJSONArray("data").getJSONObject(position).getString("time_slot"));
            holder.mtv_speciality.setText(mValues.getJSONArray("data").getJSONObject(position).getString("reason_for_visit"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                final EditText edt_comment=(EditText)dialog.findViewById(R.id.edt_comment);
                RatingBar rating_bar_doctor =(RatingBar)dialog.findViewById(R.id.rating_bar_doctor);
                final RadioGroup mradioUserType  = (RadioGroup) dialog.findViewById(R.id.radioUserType);

                rating_bar_doctor.setClickable(true);
                rating_bar_doctor.setOnRatingChangeListener(
                        new RatingBar.OnRatingChangeListener() {
                            @Override
                            public void onRatingChange(int RatingCount) {
                                Toast.makeText(mfragment.getApplicationContext(),"the fill star is"+RatingCount,Toast.LENGTH_LONG).show();
                            }
                        }
                );
                rating_bar_doctor.setStar(5);
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
                        String vendor_detail_id = null;
                        String user_id = null;
                        try {
                            vendor_detail_id = mValues.getJSONArray("data").getJSONObject(currentPozition).getString("vendor_detail_id");
                            user_id = mValues.getJSONArray("data").getJSONObject(currentPozition).getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        httpServiceCall(holder.mRatting.getRating(),edt_comment.getText().toString(),selectedId,vendor_detail_id,user_id,currentPozition);
                        HttpServiceCallGetReview.execute(null);
                    }
                });

                dialog.show();
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
    public class ViewHolder extends RecyclerView.ViewHolder    {
        public final View mView;
        public final TextView mtv_doctor_name,mtv_date,mtv_time,mtv_speciality;
        public final TextView mContentView;

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
