package sikher.visual.sidepanel;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:37:52
 * $Header$
 * $Log$
 * Description:
 */
public interface ISikherControl {
    public void addSikherListener( String eventName, IControlListener listener );
    public void removeSikherListener( String eventName, IControlListener listener );

    //for all events
    public void addSikherListener( IControlListener listener );
    public void removeSikherListener( IControlListener listener );
}
