package Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Pankaj Sakariya on 14/05/2016.
 */
public class FindDoctorModel {

    public static String FindDoctorGetModel(String category, String area_name, String vendor_name)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("category",category);
            object.put("area_name",area_name);
            object.put("vendor_name",vendor_name);
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
}
