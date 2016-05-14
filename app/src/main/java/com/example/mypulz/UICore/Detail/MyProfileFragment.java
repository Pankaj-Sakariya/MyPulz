package com.example.mypulz.UICore.Detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.mypulz.R;

import org.json.JSONArray;
import org.json.JSONException;

import Common.CommonFunction;
import Common.Constant;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    EditText edt_first_name,edt_last_name,edt_mobile_number,edt_email_id;
    JSONArray jsonArray_customer_detail;

    private OnFragmentInteractionListener mListener;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
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
        return inflater.inflate(R.layout.fragment_my_profile, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeWidget(view);
    }

    private void initializeWidget(View view) {

        edt_first_name = (EditText)view.findViewById(R.id.edt_first_name);
        edt_last_name = (EditText)view.findViewById(R.id.edt_last_name);
        edt_email_id = (EditText)view.findViewById(R.id.edt_email_id);
        edt_mobile_number = (EditText)view.findViewById(R.id.edt_mobile_number);

        setData();
    }

    private void setData() {

        String str_customer_detail =  new CommonFunction().getSharedPreference(Constant.TAG_jArray_customer_detail, getContext());
        System.out.println("!!!!pankaj_customer_detail"+str_customer_detail);
//        new CommonFunction().showAlertDialog(str_customer_detail,"Response",getContext());
        String str_first_name = null;
        String str_last_name = null;
        String str_mobile_number = null;
        String str_email_id = null;


        try {
            jsonArray_customer_detail = new JSONArray(str_customer_detail);
            str_first_name = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"first_name");
            str_last_name = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"last_name");
            str_mobile_number = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"mobile_number");
            str_email_id = new CommonFunction().parseStringFromJsonArray(jsonArray_customer_detail,"email_id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        edt_first_name.setText(str_first_name);
        edt_last_name.setText(str_last_name);
        edt_mobile_number.setText(str_mobile_number);
        edt_email_id.setText(str_email_id);
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
