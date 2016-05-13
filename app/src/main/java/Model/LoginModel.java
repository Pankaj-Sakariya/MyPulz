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

    public static String SignupGetModel(String first_name, String last_name, String mobile_number, String email_address,String is_customer)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("first_name",first_name);
            object.put("last_name",last_name);
            object.put("mobile_number",mobile_number);
            object.put("email_address",email_address);
            if(is_customer.equalsIgnoreCase("doctor"))
            {
                object.put("is_customer","0");
            }
            else if(is_customer.equalsIgnoreCase("patient"))
            {
                object.put("is_customer","1");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return object.toString();
    }
}

