package sikher.visual.editors;

import sikher.searchengine.Hymn;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import javax.swing.text.Element;
import javax.swing.event.HyperlinkEvent;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 09.12.2005
 * Time: 20:27:20
 * $Header$
 * $Log$
 * Description:
 */
public class LinkEditorKit extends StyledEditorKit{

    public static final Object LINK = new StringBuffer("sikher.link");
    public static final Object LINE = new StringBuffer("sikher.line");

    private MouseListener _FLinkHandler;
    private LinkMotionHandler _FLinkMotionHandler;

    public LinkEditorKit(){
        _FLinkHandler = new LinkHandler();
        _FLinkMotionHandler = new LinkMotionHandler();
    }

    public LinkEditorKit( JTextPane pane ){
        this();
        install( pane );
    }

    public void install(JTextPane pane){
        super.install( pane );
        pane.addMouseListener( _FLinkHandler );
        pane.addMouseMotionListener( _FLinkMotionHandler );
    }

    public void deinstall( JTextPane pane ){
        pane.removeMouseMotionListener( _FLinkMotionHandler );
        pane.removeMouseListener( _FLinkHandler );
        super.deinstall( pane );
    }


    private Element characterElementAt(MouseEvent e){
        JTextPane p = (JTextPane)e.getComponent();

        Position.Bias[] bias = new Position.Bias[1];
        int position = p.getUI().viewToModel(p, e.getPoint(), bias);

        if (bias[0] == Position.Bias.Backward && position != 0)
            --position;

        Element _elem = ((StyledDocument)p.getDocument()).getCharacterElement(position);

        // should test whether really inside
        return _elem;
    }

    private class LinkHandler extends MouseAdapter {           
        private Element activeElement;


        public void mousePressed(MouseEvent e){
            if (!SwingUtilities.isLeftMouseButton(e)){
                return;
            }

            JTextPane _pane = (JTextPane)e.getComponent();

            if (_pane.isEditable()){
                return;
            }

            Element _elem = characterElementAt(e);

            if (_elem != null && _elem.getAttributes().getAttribute(LINK) != null){
                activeElement = _elem;
            }
        }

        public void mouseReleased(MouseEvent e){
            if (!SwingUtilities.isLeftMouseButton(e) || activeElement == null)
                return;

            JTextPane _pane = (JTextPane)e.getComponent();

            Element _elem = characterElementAt(e);

            if ( !_pane.isEditable() && _elem == activeElement ){ // too restrictive, should find attribute run
                activeElement = null;
                Hymn _hymn = (Hymn) _elem.getAttributes().getAttribute(LINK);
                int _line = (Integer)_elem.getAttributes().getAttribute(LINE);
                _pane.fireHyperlinkUpdate(
                        new HymnLinkEvent(_pane, HyperlinkEvent.EventType.ACTIVATED, _hymn, _line));
            }
        }
    }

    private class LinkMotionHandler implements MouseMotionListener{
        private boolean _FIn = false;

        public void mouseDragged(MouseEvent e) {}

        public void mouseMoved(MouseEvent e) {
            JTextPane _pane = (JTextPane)e.getComponent();
            Element _elem = characterElementAt(e);
            if ( _elem != null && _elem.getAttributes().getAttribute(LINK) != null){
                if ( !_FIn ){
                    _FIn = true;
                    _pane.fireHyperlinkUpdate( new HymnLinkEvent( _pane, HyperlinkEvent.EventType.ENTERED ) );
                }
            }else{
                if ( _FIn ){
                    _FIn = false;
                    _pane.fireHyperlinkUpdate( new sikher.visual.editors.HymnLinkEvent( _pane, HyperlinkEvent.EventType.EXITED ) );
                }
            }
        }
    }
}
