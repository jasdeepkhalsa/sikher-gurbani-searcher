package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.visual.editors.HymnLinkEvent;

import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 10.12.2005
 * Time: 22:06:55
 * $Header$
 * $Log$
 * Description:
 */
public abstract class AbstractHymnLinkListener implements HyperlinkListener {

    public void hyperlinkUpdate(HyperlinkEvent e) {
        JTextPane pane = (JTextPane)e.getSource();
        HyperlinkEvent.EventType type = e.getEventType();

        if (type == HyperlinkEvent.EventType.ENTERED) {
            pane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }else if (type == HyperlinkEvent.EventType.EXITED) {
            pane.setCursor(Cursor.getDefaultCursor());
        } else if (type == HyperlinkEvent.EventType.ACTIVATED){
            if ( e instanceof HymnLinkEvent ){
                sikher.visual.editors.HymnLinkEvent _hymnEvent = (HymnLinkEvent) e;
                Hymn _hymn = _hymnEvent.getHymn();
                int _number = _hymnEvent.getLineNumber();
                onActivateLink( _hymn, _number );
            }
        }
    }

    public abstract void onActivateLink( Hymn hymn, int lineNumber );
}
