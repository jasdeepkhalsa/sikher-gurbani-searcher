package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.SikherControlEvent;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:59:19
 * $Header$
 * $Log$
 * Description:
 */
public class ResultDocEditor extends AbstractSikherEditor{

    public static final String EVENT_LINK = "event_link";

    public ResultDocEditor(StatusBar statusBar) {
        super(statusBar);
        _FinitComponents();
    }

    private void _FinitComponents(){
        JTextPane _textPane = new JTextPane();

        //set editor kit
        LinkEditorKit _kit = new LinkEditorKit( _textPane );
        _textPane.setEditorKit( _kit );
        _textPane.addHyperlinkListener( new HymnLinkListener() );

        //set sikher document
        SikherLineDocument _doc = new SikherLineDocument();
        _textPane.setDocument( _doc );

        //set attributes
        SimpleAttributeSet _attribs = new SimpleAttributeSet();
        StyleConstants.setAlignment(_attribs , StyleConstants.ALIGN_CENTER);
        _textPane.setParagraphAttributes( _attribs, false );
        _textPane.setEditable( false );

        JScrollPane _scroll;
        _scroll = new JScrollPane( _textPane );
        _scroll.setPreferredSize( new Dimension(400, 400) );
        add( _scroll );

        setDataModel( _textPane, _doc );
    }

    private class HymnLinkListener extends AbstractHymnLinkListener {
        public void onActivateLink(Hymn hymn, int lineNumber) {
            fireControlEvent(
                    new SikherControlEvent( ResultDocEditor.this, EVENT_LINK, new Object[]{hymn, lineNumber} ) );
        }
    }
}
