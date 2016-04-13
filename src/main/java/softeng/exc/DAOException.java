package softeng.exc;

/**
 * Created by Fernando on 4/13/2016.
 */
public class DAOException extends Exception {

    private final Exception originalException;

    public DAOException(Exception originalException, String msg) {
        super(msg);
        this.originalException = originalException;
    }
}
