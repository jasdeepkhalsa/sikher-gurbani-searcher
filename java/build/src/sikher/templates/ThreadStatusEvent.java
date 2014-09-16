package sikher.templates;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 17.01.2006
 * Time: 20:47:33
 * $Header$
 * $Log$
 * Description:
 */
public class ThreadStatusEvent extends EventObject {

    public static final int STATUS_NONE     = 0;
    public static final int STATUS_STARTED  = 1;
    public static final int STATUS_STOPPED  = 2;
    public static final int STATUS_PAUSED   = 3;
    public static final int STATUS_RESUMED  = 4;


    private int _FStatus = STATUS_NONE;

    /**
     * Constructor
     * @param source - event creator
     * @param status - thread status
     */
    public ThreadStatusEvent( Object source, int status ){
        super( source );
        _FStatus = status;
    }

    /**
     * Get thread status
     * @return status
     */
    public int getStatus(){
        return _FStatus;
    }
}