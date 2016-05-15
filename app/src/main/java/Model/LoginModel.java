package Model;

import org.json.*;

/**
 * Created by Murtuza on 5/1/2016.
 */
public class LoginModel {

    public static String LoginPostModel(String mobile_number, String otp_number)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile_number",mobile_number);
            object.put("otp_number",otp_number);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String SignupGetModel(String first_name, String last_name, String mobile_number, String email_id,String is_user)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("first_name",first_name);
            object.put("last_name",last_name);
            object.put("mobile_number",mobile_number);
            object.put("email_id",email_id);
            if(is_user.equalsIgnoreCase("doctor"))
            {
                object.put("is_user","0");
            }
            else if(is_user.equalsIgnoreCase("patient"))
            {
                object.put("is_user","1");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
    public static String BookAppointmentGetModel(String first_name, String last_name, String mobile_number, String docterId,String date,String Time)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("first_name",first_name);
            object.put("last_name",last_name);
            object.put("mobile_number",mobile_number);
            object.put("docterId",docterId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String ReviewDocterGetModel(String docterId,String rating,String mode)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("rating",rating);
            object.put("mode",mode);
            object.put("docterId",docterId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return object.toString();
    }

    public static String MyAppointmentGetModel(String userId)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("userId",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}

