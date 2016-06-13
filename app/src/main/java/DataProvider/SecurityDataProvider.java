package DataProvider;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import Common.ConnectionDetector;
import Common.Constant;
import Common.HttpConnection;
import Common.ServiceUrl;
import Interface.HttpCallback;

/**
 * Created by Murtuza on 5/1/2016.
 */


public class SecurityDataProvider {

    public static void Login(Activity activity, String InputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        try {

                HttpConnection.HttpConnect(activity, ServiceUrl.Login + "/?body=" + URLEncoder.encode(InputString, "UTF-8"), 1000, "", Constant.MethodNameGet, callback);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void Signup(Activity activity, String InputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        try {
            HttpConnection.HttpConnect(activity,ServiceUrl.Signup + "/?body=" + URLEncoder.encode(InputString,"UTF-8"),1000,"", Constant.MethodNameGet,callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void FindDoctor(Activity activity, String InputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        try {
            HttpConnection.HttpConnect(activity,ServiceUrl.FindDoctor + "/?body=" + URLEncoder.encode(InputString,"UTF-8"),1000,"", Constant.MethodNameGet,callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void BookAppointment(FragmentActivity activity, String InputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        try {
            HttpConnection.HttpConnect(activity,ServiceUrl.BookAppointment + "/?body=" + URLEncoder.encode(InputString,"UTF-8"),1000,"", Constant.MethodNameGet,callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void MyAppointment(Activity activity, String InputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        try {
            HttpConnection.HttpConnect(activity,ServiceUrl.MyAppointment + "/?body=" + URLEncoder.encode(InputString,"UTF-8"),1000,"", Constant.MethodNameGet,callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
