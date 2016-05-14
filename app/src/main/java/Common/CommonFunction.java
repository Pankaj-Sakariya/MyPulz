package Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pankaj Sakariya on 12/05/2016.
 */
public class CommonFunction {

    public String alertMessage = "Alert";
    public String please_syncMessage = "Please Upload offline data";
    public static ProgressDialog p = null;
    public void showAlertDialog(String Message, String title, Context context) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(Message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
//            .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public JSONArray parseJsonArray(String arrayName, JSONObject response) {
        JSONArray jsonArray = new JSONArray();
        if (response.has("data")) {
            JSONObject data = new JSONObject();
            try {
                data = response.getJSONObject("data");
                if (data.has(arrayName)) {

                    jsonArray = data.getJSONArray(arrayName);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return jsonArray;
    }

    public void saveSharedPreference(String key_name, String key_value, Context context) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("main", 0);
        SharedPreferences.Editor e = sp.edit();
        e.putString(key_name, key_value);
        e.commit();
    }

    public String getSharedPreference(String key_name,Context context) {

        SharedPreferences sp = context.getApplicationContext().getSharedPreferences("main", 0);
        String key_value = sp.getString(key_name,"No Value Found");
        return key_value;
    }


    public boolean validateEmailAddress(String emailAddress){
        String  expression="^[\\w\\-]([\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

    public static void showActivityIndicator(Activity activity,String Message)
    {
        p = new ProgressDialog(activity);
        p.setMessage(Message);
        p.setCancelable(false);
        p.show();
    }
    public static void HideActivityIndicator(Activity activity)
    {
        p.dismiss();
    }


    public ArrayList parseJsonArrayToMap(JSONArray jsonArray,String key_name){
//        Map<Integer,String> mapList = null;

        ArrayList<String> arrayList = new ArrayList<String>();
        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                JSONObject j = jsonArray.getJSONObject(i);
                if(j.has(key_name))
                {
//                    mapList.put(i,j.getString(key_name));
                    arrayList.add(i,j.getString(key_name));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return arrayList;
    }


    public String parseStringFromJsonArray(JSONArray jsonArray,String key_name){
//        Map<Integer,String> mapList = null;

        String key_value = null;
        for(int i=0;i<jsonArray.length();i++)
        {
            try {
                JSONObject j = jsonArray.getJSONObject(i);
                if(j.has(key_name))
                {
                    key_value = j.getString(key_name);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return key_value;
    }





}
