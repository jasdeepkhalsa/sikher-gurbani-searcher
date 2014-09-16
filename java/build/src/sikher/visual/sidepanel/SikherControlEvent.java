package sikher.visual.sidepanel;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 15:42:28
 * $Header$
 * $Log$
 * Description:
 */
public class SikherControlEvent extends EventObject {
    private int _FType;
    private String _FEventName;

    private Object[] _FData = null;

    private Object _FNewValue = null;

    /**
     * Constructs a send event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public SikherControlEvent(Object source, String eventName, Object[] data) {
        super(source);
        _FEventName = eventName;
        _FType = TYPE_SEND;
        _FData = data;
    }

    /**
     * Constructs a change event
     * @param source
     * @param newValue
     */
    public SikherControlEvent( Object source, String eventName, Object newValue ){
        super(source);
        _FEventName = eventName;
        _FType = TYPE_CHANGE;
        _FNewValue = newValue;
    }

    public static final int TYPE_SEND = 0;
    public static final int TYPE_CHANGE = 1;

    public int getType() {
        return _FType;
    }

    public String getEventName() {
        return _FEventName;
    }

    public Object[] getData() {
        return _FData;
    }

    public Object getNewValue() {
        return _FNewValue;
    }
}
