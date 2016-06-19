package com.example.mypulz.UICore.Detail;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mypulz.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Common.CommonFunction;
import Common.Constant;
import DataProvider.SecurityDataProvider;
import Interface.HttpCallback;
import Model.FindDoctorModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FindDoctorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FindDoctorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindDoctorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    JSONArray jsonArray_category = null;
    JSONArray jsonArray_area = null;
    JSONArray jsonArray_vendor = null;

    Spinner spinner_activity;
    AutoCompleteTextView txt_area_name,txt_vendor_name;

    ArrayList<String> arrayList_category_data;
    ArrayList<String> arrayList_area_data;
    ArrayList<String> arrayList_vendor_data;

    ArrayAdapter<String> spinner_activity_adapter;
    ArrayAdapter<String> txt_area_adapter;
    ArrayAdapter<String> txt_vendor_adapter;

    AsyncTask HttpServiceCallFindDoctor = null;

    Button btn_search;

    private OnFragmentInteractionListener mListener;

    public FindDoctorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FindDoctorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FindDoctorFragment newInstance(String param1, String param2) {
        FindDoctorFragment fragment = new FindDoctorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_doctor, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeWidget(view);

    }

    private void initializeWidget(View view) {

//        btn_search = (Button)view.findViewById(R.id.btn_search);
//        spinner_activity = (Spinner)view.findViewById(R.id.spinner_activity);
//        txt_area_name = (AutoCompleteTextView)view.findViewById(R.id.txt_area_name);
//        txt_vendor_name = (AutoCompleteTextView)view.findViewById(R.id.txt_vendor_name);
//        setData();

    }

    private void setData() {


        String str_category_data =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_category, getContext());
        String str_area_data =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_area, getContext());
        String str_vendor_data =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_vendor, getContext());
//       new CommonFunction().showAlertDialog(data,"Response",getContext());

        try {
            jsonArray_category = new JSONArray(str_category_data);
            jsonArray_area = new JSONArray(str_area_data);
            jsonArray_vendor = new JSONArray(str_vendor_data);

            arrayList_category_data = new CommonFunction().parseJsonArrayToMap(jsonArray_category,"category_name");
            arrayList_area_data = new CommonFunction().parseJsonArrayToMap(jsonArray_area,"area_name");
            arrayList_vendor_data = new CommonFunction().parseJsonArrayToMap(jsonArray_vendor,"full_name");
//            new CommonFunction().showAlertDialog(arrayList_vendor_data.toString(),"Response",getContext());

            spinner_activity_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, arrayList_category_data);
            spinner_activity.setAdapter(spinner_activity_adapter);

            txt_area_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, arrayList_area_data);
            txt_area_name.setAdapter(txt_area_adapter);

            txt_vendor_adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, arrayList_vendor_data);
            txt_vendor_name.setAdapter(txt_vendor_adapter);

            setonClickListener();

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setonClickListener() {

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                httpServiceCall();
                HttpServiceCallFindDoctor.execute(null);


            }
        });

    }

    private void httpServiceCall() {
        CommonFunction.showActivityIndicator(getActivity(),getResources().getString(R.string.title_for_activityIndicater));
        HttpServiceCallFindDoctor = new AsyncTask() {
            JSONObject response;
            String findDoctorGetModel = FindDoctorModel.FindDoctorGetModel(spinner_activity.getSelectedItem().toString(),txt_area_name.getText().toString(),txt_vendor_name.getText().toString());
            @Override
            protected Object doInBackground(Object[] params) {
                SecurityDataProvider.FindDoctor(getActivity(),findDoctorGetModel, new HttpCallback() {
                    @Override
                    public void callbackFailure(Object result) {
                        System.out.println(result);
                    }
                    @Override
                    public void callbackSuccess(Object result) {
                        CommonFunction.HideActivityIndicator(getActivity());
                        System.out.println(result);
                        try {
                            response = new JSONObject(result.toString());
                            System.out.println("pankaj"+response);
                            if(response.optJSONArray("data").length() == 0)
                            {
                                new CommonFunction().showAlertDialog("Search Result Not Found.","Not Found",getActivity());
                            }
                            else
                            {
                                DoctorListFragment dFragment = new DoctorListFragment();
                                dFragment.SearchDocterData = response;
                                changeFragment(dFragment);
                            }

                            //new CommonFunction().showAlertDialog(response.toString(),"Testing",getContext());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                return null;
            }
        };
    }




    public void changeFragment(Fragment newFragment)
    {
        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        // Commit the transaction
        transaction.commit();

    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
