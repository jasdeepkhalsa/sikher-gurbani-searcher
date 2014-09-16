package sikher.visual.editors;

import sikher.searchengine.Hymn;

import javax.swing.event.HyperlinkEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 10.12.2005
 * Time: 22:36:29
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/HymnLinkEvent.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: HymnLinkEvent.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class HymnLinkEvent extends HyperlinkEvent {
    private Hymn _FHymn;
    private int _FLineNumber;

    public HymnLinkEvent( Object source, EventType type, Hymn hymn, int lineNumber ) {
        super(source, type, null);
        _FLineNumber = lineNumber;
        _FHymn = hymn;
    }

    public HymnLinkEvent( Object source, EventType type ){
        super(source, type, null);
    }

    public Hymn getHymn() {
        return _FHymn;
    }

    public int getLineNumber() {
        return _FLineNumber;
    }
}
