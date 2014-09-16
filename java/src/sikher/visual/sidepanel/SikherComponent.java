package sikher.visual.sidepanel;

import sikher.searchengine.XPathSearcher;
import sikher.searchengine.SearcherListener;
import sikher.visual.forms.SikherIni;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:46:58
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/SikherComponent.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherComponent.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class SikherComponent extends JPanel implements ISikherControl{

    private Map<String, ArrayList<IControlListener>> _FListenersMap = new HashMap<String, ArrayList<IControlListener>>();
    private ArrayList<IControlListener> _FListeners = new ArrayList<IControlListener>();

    private StatusBar _FStatusBar;
    protected XPathSearcher FSearcher;
    protected SikherIni FSikherIni;

    public SikherComponent( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ){
        this( searcher, ini, statusBar, new FlowLayout() );
    }

    public SikherComponent( XPathSearcher searcher, SikherIni ini, StatusBar statusBar, LayoutManager layout ){
        super( layout );
        _FStatusBar = statusBar;
        FSearcher = searcher;
        FSearcher.addSearcherListener( new XPathSearcherListener() );
        FSikherIni = ini;
    }

    public void init(){
        searcherInfoUpdated();
    }
    public void done(){}

    public void addSikherListener( String eventName, IControlListener listener ) {
        ArrayList<IControlListener> _listeners;
        if( _FListenersMap.containsKey( eventName ) ){
            _listeners = _FListenersMap.get( eventName );
        }else{
            _listeners = new ArrayList<IControlListener>();
            _FListenersMap.put( eventName, _listeners );
        }
        if( !_listeners.contains( listener ) ){
            _listeners.add( listener );
        }
    }

    public void removeSikherListener( String eventName, IControlListener listener ) {
        ArrayList<IControlListener> _listeners;
        if( _FListenersMap.containsKey( eventName ) ){
            _listeners = _FListenersMap.get( eventName );
        }else{
            _listeners = new ArrayList<IControlListener>();
            _FListenersMap.put( eventName, _listeners );
        }
        _listeners.remove( listener );
    }

    //for all events
    public void addSikherListener(IControlListener listener) {
        if( !_FListeners.contains( listener ) ){
            _FListeners.add( listener );
        }
    }

    public void removeSikherListener(IControlListener listener) {
        _FListeners.remove( listener );
    }

    public void fireControlEvent( SikherControlEvent event ){
        String _eventName = event.getEventName();

        ArrayList<IControlListener> _listeners = new ArrayList<IControlListener>();
        _listeners.addAll( _FListeners );
        if( _FListenersMap.containsKey( _eventName ) ){
            _listeners.addAll( _FListenersMap.get( _eventName ) );
        }else{
            //noone was registered, think about it
        }
        for( IControlListener _listener : _listeners ){
            _listener.controlEventOccured( event );
        }
    }

    public abstract String getControlName();

    public StatusBar getStatusBar() {
        return _FStatusBar;
    }

    public void showErrorMessage( String title, String message ){
        Container _frame = getFrame();
        JOptionPane.showMessageDialog( _frame, message, title, JOptionPane.ERROR_MESSAGE );
    }

    public Container getFrame(){
        Container _frame = this;
        do{
            _frame = _frame.getParent();
        }while( !(_frame instanceof JFrame) && _frame != null );
        if( _frame == null ){
            _frame = this;
        }
        return _frame;
    }

    protected void searcherInfoUpdated(){}

    private class XPathSearcherListener implements SearcherListener {
        public void infoUpdated() {
            searcherInfoUpdated();
        }
    }
}
