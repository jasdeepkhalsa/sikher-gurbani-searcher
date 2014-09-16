package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.searchengine.XPathSearcher;
import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.SikherControlEvent;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.SikherProperties;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:59:19
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ResultDocEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ResultDocEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ResultDocEditor extends AbstractSikherEditor{

    public static final String EVENT_LINK = "event_link";
    public static final String EDITOR_NAME = "result-doc";

    public ResultDocEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ) {
        super( searcher, ini, statusBar, EDITOR_NAME );
        _FinitComponents();
    }

    private void _FinitComponents(){
        JTextPane _textPane = new JTextPane();

        //set editor kit
        LinkEditorKit _kit = new LinkEditorKit( _textPane );
        _textPane.setEditorKit( _kit );
        _textPane.addHyperlinkListener( new HymnLinkListener() );

        //set sikher document
        SikherLineDocumentEx _doc = new SikherLineDocumentEx( FSikherIni, EDITOR_NAME );
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

    public String getControlName(){
        return SikherProperties.getString( "editor.result-doc.name" );
    }

    private class HymnLinkListener extends AbstractHymnLinkListener {
        public void onActivateLink(Hymn hymn, int lineNumber) {
            fireControlEvent(
                    new SikherControlEvent( ResultDocEditor.this, EVENT_LINK, new Object[]{hymn, lineNumber} ) );
        }
    }
}
