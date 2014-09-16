package sikher.templates;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 17.01.2006
 * Time: 20:47:08
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/templates/IThreadStatusListener.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: IThreadStatusListener.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public interface IThreadStatusListener extends EventListener {

    /**
     * Status was changed
     * @param event - status event
     */
    public void statusChanged( ThreadStatusEvent event );
}