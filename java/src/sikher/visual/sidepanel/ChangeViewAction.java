package sikher.visual.sidepanel;

import sikher.visual.controls.ViewSikherControl;
import sikher.visual.editors.SikherEditor;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.01.2006
 * Time: 18:37:00
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/ChangeViewAction.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ChangeViewAction.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ChangeViewAction implements IControlListener{
    private SikherEditor _FEditor;

    public ChangeViewAction( SikherEditor editor ){
        _FEditor = editor;
    }

    public void controlEventOccured(SikherControlEvent event) {
        String _eventName = event.getEventName();
        Object _value = event.getNewValue();
        String _key = null;
        if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_LANG_GURBANI ) ){
            _key = SikherEditor.PROP_LANG_GURBANI;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_LANG_TRANSLIT ) ){
            _key = SikherEditor.PROP_LANG_TRANSLIT;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_LANG_TRANSLATION ) ){
            _key = SikherEditor.PROP_LANG_TRANSLATION;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_SHOW_GURBANI ) ){
            _key = SikherEditor.PROP_SHOW_GURBANI;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_SHOW_TRANSLIT ) ){
            _key = SikherEditor.PROP_SHOW_TRANSLIT;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_SHOW_TRANSLATION ) ){
            _key = SikherEditor.PROP_SHOW_TRANSLATION;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_SHOW_DETAILS ) ){
            _key = SikherEditor.PROP_SHOW_DETAILS;
        }else if( _eventName.equalsIgnoreCase( ViewSikherControl.EVENT_VIEW_MODE ) ){
            _key = SikherEditor.PROP_VIEW_MODE;
        }

        if( _key != null ){
            _FEditor.setProperty( _key, _value );
        }
    }
}
