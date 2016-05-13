package Common;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import Interface.HttpCallback;

/**
 * Created by Murtuza on 5/1/2016.
 */



public class  HttpConnection  {
    @Nullable
    AsyncTask asyncTask = null;
    public static void HttpConnect(Activity activity, String Url, int ConnectionTimeOut, String InputString, String Method, @NonNull HttpCallback callback)
    {
        String responseText = null;
        try {


//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // convert inputstream to string
//            if(inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(Url);
            //HttpPost httppostreq = new HttpPost(Url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
//            if(Method == Constant.MethodNameGet) {
//                httppostreq.setEntity(new StringEntity(InputString, "UTF-8"));
//            }

            HttpResponse httpresponse = httpclient.execute(httpGet);
            StatusLine statusLine = httpresponse.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                try {
                    responseText = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");
                    System.out.println("!!!!pankaj_responseText"+responseText);
                    callback.callbackSuccess(responseText.substring(responseText.indexOf("{"), responseText.lastIndexOf("}") + 1));
                } catch (NullPointerException np) {
                    callback.callbackFailure(np);
                }
            }
        }
        catch (ClientProtocolException e1)
        {
        // TODO Auto-generated catch block
            e1.printStackTrace();
            callback.callbackFailure(e1);
        }
        catch (IOException e1)
        {
        // TODO Auto-generated catch block

            e1.printStackTrace();
            callback.callbackFailure(e1);
        }
        catch (NullPointerException np) {
        }
    }



    public static String HttpsConnect(String Url,int ConnectionTimeOut,String InputString,String Method)
    {
        StringBuilder OutputString = new StringBuilder();
        try {
            //KeyStore keyStore = new KeyStore().getKey();
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            //tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            URL url = new URL(Url);
            HttpsURLConnection connect = (HttpsURLConnection)url.openConnection();
            connect.setSSLSocketFactory(context.getSocketFactory());
            InputStream in = connect.getInputStream();
            String UrlParamiter = InputString;
            connect.setRequestMethod(Method);
            connect.setRequestProperty("USER-AGENTS","Mozilla/5.0");
            connect.setRequestProperty("ACCEPT-LANGUAGE","en-US,en;0.5");
            connect.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(connect.getOutputStream());
            dataOutputStream.writeBytes(UrlParamiter);
            dataOutputStream.flush();
            dataOutputStream.close();
            int responsecode = connect.getResponseCode();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line = "";
            while((line = bufferedReader.readLine())!= null)
            {
                OutputString.append(line);
            }
            bufferedReader.close();
            System.out.println(OutputString.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } /*catch (KeyStoreException e) {
            e.printStackTrace();
        }*/ catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return OutputString.toString();
    }
}

