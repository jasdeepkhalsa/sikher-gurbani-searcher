package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.SikherProperties;
import sikher.searchengine.XPathSearcher;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.01.2006
 * Time: 19:33:04
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ViewSikherEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ViewSikherEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ViewSikherEditor extends AbstractSikherEditor{
    public static final String EDITOR_NAME = "view";

    public ViewSikherEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ) {
        super( searcher, ini, statusBar, EDITOR_NAME );
        _FinitComponents();
    }

    public String getControlName() {
        return SikherProperties.getString("editor.view.name");
    }

    private void _FinitComponents(){
        JTextPane _textPane = new JTextPane();

        //set hymn document
        HymnDocumentEx _doc = new HymnDocumentEx( FSikherIni, EDITOR_NAME );
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

    public void setPosition( int lineNumber ){
        //TODO: implement
    }
}
