package sikher.visual.sidepanel;

import sikher.searchengine.Hymn;
import sikher.visual.editors.SikherEditor;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 19:02:28
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/SendHymnsAction.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SendHymnsAction.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SendHymnsAction implements IControlListener{
    protected SikherEditor FEditor;
    protected MainPanel FMainPanel;

    public SendHymnsAction( SikherEditor editor, MainPanel mainPanel ){
        FEditor = editor;
        FMainPanel = mainPanel;
    }

    public void controlEventOccured( SikherControlEvent event ) {
        if ( event.getType() == SikherControlEvent.TYPE_SEND ){
            Object[] _data = event.getData();
            if( _data instanceof Hymn[] ){
                Hymn[] _hymns = (Hymn[]) _data;
                if( _hymns != null ){
                    FEditor.setHymns( _hymns );
                }
            }
            FMainPanel.setSelectebEditor( FEditor );
        }
    }
}
