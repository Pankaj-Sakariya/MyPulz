package Model;

/**
 * Created by Murtuza on 5/8/2016.
 */
public class HttpResponse<T> {
    String success;
    String message;
    Class <T> data;
    String sessid;
    private HttpResponse()
    {

    }
    private HttpResponse(String success, String message,Class <T> data,String sessid) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.sessid = sessid;
    }
}
