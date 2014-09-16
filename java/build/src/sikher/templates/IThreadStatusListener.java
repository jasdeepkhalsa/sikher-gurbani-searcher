package sikher.templates;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 17.01.2006
 * Time: 20:47:08
 * $Header$
 * $Log$
 * Description:
 */
public interface IThreadStatusListener extends EventListener {

    /**
     * Status was changed
     * @param event - status event
     */
    public void statusChanged( ThreadStatusEvent event );
}