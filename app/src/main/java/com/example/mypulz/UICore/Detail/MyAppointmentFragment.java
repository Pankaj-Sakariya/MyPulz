package com.example.mypulz.UICore.Detail;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypulz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Common.CommonFunction;
import Common.Constant;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.LoginModel;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyAppointmentFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    AsyncTask HttpServiceCallMyAppointment = null;
    RecyclerView recyclerView  = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyAppointmentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyAppointmentFragment newInstance(int columnCount) {
        MyAppointmentFragment fragment = new MyAppointmentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myappointment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            httpServiceCall(recyclerView);
            HttpServiceCallMyAppointment.execute(null);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void httpServiceCall(final RecyclerView recyclerView) {
        CommonFunction.showActivityIndicator(getActivity(),getResources().getString(R.string.title_for_activityIndicater));
        try {
            HttpServiceCallMyAppointment = new AsyncTask() {
                JSONObject response;
                final JSONArray finalJsonArray_customer_detail = new JSONArray(new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, getActivity()));

                String myAppointmentGetModel = LoginModel.MyAppointmentGetModel(new CommonFunction().parseStringFromJsonArray(finalJsonArray_customer_detail,"id"));
                @Override
                protected Object doInBackground(Object[] params) {
                    SecurityDataProvider.MyAppointment(getActivity(),myAppointmentGetModel, new HttpCallback() {
                        @Override
                        public void callbackFailure(Object result) {
                            System.out.println(result);
                        }
                        @Override
                        public void callbackSuccess(Object result) {
                            CommonFunction.HideActivityIndicator(getActivity());
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
                    CommonFunction.HideActivityIndicator(getActivity());
                    if(response != null)
                    {
                        if(response.optJSONArray("data").length() == 0)
                        {
                            new CommonFunction().showAlertDialog("Search Result Not Found.","Not Found",getActivity());
                        }
                        else
                        {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    recyclerView.setAdapter(new MyAppointmentRecyclerViewAdapter(response, mListener,getActivity()));
                                }

                            });

                        }
                    }

                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name

    }
}
