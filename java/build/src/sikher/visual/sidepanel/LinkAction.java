package sikher.visual.sidepanel;

import sikher.visual.editors.SikherEditor;
import sikher.visual.editors.ResultDocEditor;
import sikher.visual.editors.ViewSikherEditor;
import sikher.searchengine.XPathSearcher;
import sikher.searchengine.Hymn;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 18:49:37
 * $Header$
 * $Log$
 * Description:
 */
public class LinkAction extends SendHymnsAction{
    private XPathSearcher _FSearcher;

    public LinkAction(ViewSikherEditor editor, MainPanel mainPanel, XPathSearcher searcher) {
        super(editor, mainPanel);
        _FSearcher = searcher;
    }

    public void controlEventOccured( SikherControlEvent event ){
        if( event.getEventName().equals( ResultDocEditor.EVENT_LINK ) ){
            Object[] _data = event.getData();
            if( _data.length == 2 && _data[0] instanceof Hymn ){
                Hymn _hymn = (Hymn) _data[0];
                if( !_hymn.isLoaded() ){
                    _FSearcher.loadHymn( _hymn );
                }
                super.controlEventOccured(
                        new SikherControlEvent( event.getSource(),
                                                event.getEventName(),
                                                new Hymn[]{_hymn} ) );
                if( _data[1] instanceof Integer ){
                    ((ViewSikherEditor)FEditor).setPosition( (Integer)_data[1] );
                }
            }
        }
    }
}
