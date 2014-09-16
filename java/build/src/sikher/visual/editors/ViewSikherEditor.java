package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.01.2006
 * Time: 19:33:04
 * $Header$
 * $Log$
 * Description:
 */
public class ViewSikherEditor extends AbstractSikherEditor{
    public ViewSikherEditor(StatusBar statusBar) {
        super(statusBar);
        setControlName( sikher.visual.forms.SikherProperties.getString("editor.view.name") );
        _FinitComponents();
    }

    private void _FinitComponents(){
        JTextPane _textPane = new JTextPane();

        //set hymn document
        HymnDocument _doc = new HymnDocument();
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
