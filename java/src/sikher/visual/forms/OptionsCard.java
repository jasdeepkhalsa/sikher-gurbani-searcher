package sikher.visual.forms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 23.12.2005
 * Time: 12:26:06
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/OptionsCard.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: OptionsCard.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class OptionsCard extends JPanel {
    protected String FTitle = "";
    protected SikherIni FProperties = null;
    protected ArrayList<OptionCardListener> _FListeners = new ArrayList<OptionCardListener>();

    public OptionsCard( SikherIni prop ){
        super();
        FProperties = prop;
    }

    public OptionsCard( SikherIni prop, LayoutManager layout ){
        super( layout );
        FProperties = prop;
    }

    public abstract void init();

    public void apply(){
        for( OptionCardListener _listener : _FListeners ){
            _listener.applyPressed();
        }
    }

    public void cancel(){
        //do smth
    }

    public void addOptionCardListener( OptionCardListener listener ){
        if( !_FListeners.contains( listener ) ){
            _FListeners.add( listener );
        }
    }

    public void removeOptionCardListener( OptionCardListener listener ){
        _FListeners.remove( listener );
    }

    public String getTitle()
    {
        return FTitle;
    }

    public void setTitle( String title ) {
        this.FTitle = title;
    }
}
