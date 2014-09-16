package sikher.visual.editors;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;
import sikher.searchengine.Hymn;
import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.IControlListener;
import sikher.visual.forms.SikherProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 18:04:49
 * $Header$
 * $Log$
 * Description:
 */
public class ResultSikherEditor extends SikherEditor {

    public ResultSikherEditor( StatusBar statusBar ) {
        super( statusBar, new BorderLayout() );
        setControlName( SikherProperties.getString("editor.result.name") );
        _FinitComponents();
    }

    public ResultDocEditor getResultDocEditor(){
        return _FResultDocEditor;
    }

    public ResultTableEditor getResultTableEditor() {
        return _FResultTableEditor;
    }

    private JPanel _FCardPanel;
    private ResultDocEditor _FResultDocEditor;
    private ResultTableEditor _FResultTableEditor;

    private void _FinitComponents(){
        _FCardPanel = new JPanel( new CardLayout() );
        add( _FCardPanel, BorderLayout.CENTER );

        JPanel _setviewPanel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        _setviewPanel.setBorder( BorderFactory.createEtchedBorder() );
        add( _setviewPanel, BorderLayout.NORTH );

        ActionListener _switchListener = new SwitchViewListener();
        ButtonGroup _bg = new ButtonGroup();
        JRadioButton _docView = _FcreateSwitch( "panel.search.panel.results.radio.docview", _bg, _switchListener );
        _setviewPanel.add( _docView );
        JRadioButton _listView = _FcreateSwitch( "panel.search.panel.results.radio.listview", _bg, _switchListener );
        _setviewPanel.add( _listView );
        _docView.setSelected(true);

        _FResultDocEditor = new ResultDocEditor( getStatusBar() );
        _FCardPanel.add( _docView.getActionCommand(), _FResultDocEditor );

        _FResultTableEditor = new ResultTableEditor( getStatusBar() );
        _FCardPanel.add( _listView.getActionCommand(), _FResultTableEditor );
    }

    private JRadioButton _FcreateSwitch( String name, ButtonGroup bg, ActionListener listener ){
        JRadioButton _button = new JRadioButton( SikherProperties.getString(name) );
        bg.add( _button );
        _button.addActionListener( listener );
        return _button;
    }

    private AbstractSikherEditor _FgetVisibleEditor(){
        if( _FResultTableEditor.isShowing() ){
            return _FResultTableEditor;
        }else{
            return _FResultDocEditor;
        }
    }

    private class SwitchViewListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            CardLayout _layout = (CardLayout) _FCardPanel.getLayout();
            _layout.show( _FCardPanel, e.getActionCommand() );
        }
    }

    public void addSikherListener( IControlListener listener ){
        _FResultDocEditor.addSikherListener( listener );
        _FResultTableEditor.addSikherListener( listener );
    }

    public void removeSikherListener( IControlListener listener ){
        _FResultDocEditor.removeSikherListener( listener );
        _FResultTableEditor.removeSikherListener( listener );
    }

    public void addSikherListener( String eventName, IControlListener listener ){
        _FResultDocEditor.addSikherListener( eventName, listener );
        _FResultTableEditor.addSikherListener( eventName, listener );
    }

    public void removeSikherListener( String eventName, IControlListener listener ){
        _FResultDocEditor.removeSikherListener( eventName, listener );
        _FResultTableEditor.removeSikherListener( eventName, listener );
    }

    public Hymn[] getHymns() {
        return _FgetVisibleEditor().getHymns();
    }

    public void setHymns(Hymn[] hymns) {
        _FResultDocEditor.setHymns( hymns );
        _FResultTableEditor.setHymns( hymns );
    }

    public void setProperty(String key, Object value) {
        _FResultDocEditor.setProperty( key, value );
        _FResultTableEditor.setProperty( key, value );
    }

    public Object getProperty(String key) {
        return _FgetVisibleEditor().getProperty( key );
    }

    public Document createDocument() {
        return _FgetVisibleEditor().createDocument();
    }

    public void export(Document doc, FontSelector fs) throws DocumentException {
        _FgetVisibleEditor().export( doc, fs );
    }
}
