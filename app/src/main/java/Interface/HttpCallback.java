package Interface;

/**
 * Created by Murtuza on 5/2/2016.
 */
public interface HttpCallback {
    public void callbackFailure(Object result);
    public void callbackSuccess(Object result);
}
