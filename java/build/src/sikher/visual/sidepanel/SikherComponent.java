package sikher.visual.sidepanel;

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
 * $Header$
 * $Log$
 * Description:
 */
public class SikherComponent extends JPanel implements ISikherControl{

    private Map<String, ArrayList<IControlListener>> _FListenersMap = new HashMap<String, ArrayList<IControlListener>>();
    private ArrayList<IControlListener> _FListeners = new ArrayList<IControlListener>();

    private String _FControlName = "";
    private StatusBar _FStatusBar;

    public SikherComponent( StatusBar statusBar ){
        this( statusBar, new FlowLayout() );
    }

    public SikherComponent( StatusBar statusBar, LayoutManager layout ){
        super( layout );
        _FStatusBar = statusBar;
    }

    public void init(){}
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

    public String getControlName() {
        return _FControlName;
    }

    public void setControlName(String controlName) {
        _FControlName = controlName;
    }

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
}
