package sikher.visual.sidepanel;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:37:52
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/ISikherControl.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ISikherControl.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public interface ISikherControl {
    public void addSikherListener( String eventName, IControlListener listener );
    public void removeSikherListener( String eventName, IControlListener listener );

    //for all events
    public void addSikherListener( IControlListener listener );
    public void removeSikherListener( IControlListener listener );
}
