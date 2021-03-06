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
    public static String BookAppointmentGetModel( String vendor_detail_id, String user_id,String customer_name,  String mobile_number,String reason_for_visit,String appointment_date,String time_slot_id)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("vendor_detail_id",vendor_detail_id);
            object.put("user_id",user_id);
            object.put("customer_name",customer_name);
            object.put("mobile_number",mobile_number);
            object.put("reason_for_visit",reason_for_visit);
            object.put("appointment_date",appointment_date);
            object.put("time_slot_id",time_slot_id);
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
            object.put("user_id",userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    public static String ReviewDocterPostModel(String vendor_detail_id,String user_id,String comment,String review_star,String is_publish)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("vendor_detail_id",vendor_detail_id);
            object.put("user_id",user_id);
            object.put("comment",comment);
            object.put("review_star",review_star);
            object.put("is_publish",is_publish);
            System.out.println("!!!!review_json"+object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object.toString();
    }
}

