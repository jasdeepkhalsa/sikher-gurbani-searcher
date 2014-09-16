package sikher.visual.sidepanel;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:41:01
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/IControlListener.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: IControlListener.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public interface IControlListener extends EventListener {
    public void controlEventOccured( SikherControlEvent event );
}
