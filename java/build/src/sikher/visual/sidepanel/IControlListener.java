package sikher.visual.sidepanel;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:41:01
 * $Header$
 * $Log$
 * Description:
 */
public interface IControlListener extends EventListener {
    public void controlEventOccured( SikherControlEvent event );
}
