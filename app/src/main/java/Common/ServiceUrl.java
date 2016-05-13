package Common;

import android.support.annotation.NonNull;

/**
 * Created by Murtuza on 5/1/2016.
 */
public class ServiceUrl {

    @NonNull
    public static String ServerUrl = "http://w3tutorialschool.com/";
    public static String ServerPath = ServerUrl + "clients/test_server/mypulz/web/site/";

    @NonNull
    public static String Login =  ServerPath + "customer-login";
    public static String Signup = ServerPath + "customer-signup";
}
