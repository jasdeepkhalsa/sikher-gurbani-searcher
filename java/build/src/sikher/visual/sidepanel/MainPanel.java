package sikher.visual.sidepanel;

import sikher.templates.OutlookPanel;
import sikher.visual.editors.SikherEditor;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 13:49:31
 * $Header$
 * $Log$
 * Description:
 */
public class MainPanel extends JPanel {
    public MainPanel(){
        super( new BorderLayout() );
        _FinitComponents();
    }

    private OutlookPanel _FOutlookPanel = null;
    private JTabbedPane _FTabbedPane = null;
    private StatusBar _FStatusBar = null;

    private void _FinitComponents(){
        //left - outlook panel
        _FOutlookPanel = new OutlookPanel();
        add( _FOutlookPanel, BorderLayout.WEST );

        //center - tab panel
        _FTabbedPane = new JTabbedPane();
        add( _FTabbedPane, BorderLayout.CENTER );

        //bottom - status bar
        _FStatusBar = new StatusBar();
        add( _FStatusBar, BorderLayout.SOUTH );
    }

    public void addSikherControl( SikherComponent component ){
        _FOutlookPanel.addTab( component.getControlName(), component );
    }

    public void addSikherEditor( SikherEditor editor ){
        _FTabbedPane.addTab( editor.getControlName(), editor );
    }

    public StatusBar getStatusBar(){
        return _FStatusBar;
    }

    public void setSelectebEditor( SikherEditor editor ){
        _FTabbedPane.setSelectedComponent( editor );
    }
    public SikherEditor getSelectedEditor(){
        return (SikherEditor) _FTabbedPane.getSelectedComponent();
    }

    public void addChangeEditorListener( ChangeListener listener ){
        _FTabbedPane.addChangeListener( listener );   
    }

    public void removeChangeEditorListener( ChangeListener listener ){
        _FTabbedPane.removeChangeListener( listener );
    }
}
