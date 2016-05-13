package DataProvider;

import android.app.Activity;
import android.support.annotation.NonNull;

import Common.Constant;
import Common.HttpConnection;
import Common.ServiceUrl;
import Interface.HttpCallback;

/**
 * Created by Murtuza on 5/1/2016.
 */


public class SecurityDataProvider {

    public static void Init(Activity activity, String inputString, @NonNull HttpCallback callback) {
        // Run callback callback.run();
        HttpConnection.HttpConnect(activity,ServiceUrl.Init,1000,inputString, Constant.MethodName,callback);
    }
}
