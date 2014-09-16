package sikher.visual.editors;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import sikher.searchengine.Hymn;
import sikher.searchengine.XPathSearcher;
import sikher.visual.forms.OptionsCard;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.SikherProperties;
import sikher.visual.sidepanel.IControlListener;
import sikher.visual.sidepanel.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 18:04:49
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ResultSikherEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ResultSikherEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ResultSikherEditor extends SikherEditor {

    public ResultSikherEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ) {
        super( searcher, ini, statusBar, new BorderLayout() );
        _FinitComponents();
    }

    public String getControlName(){
        return SikherProperties.getString("editor.result.name");
    }

    public void init(){
        super.init();
        _FResultDocEditor.init();
        _FResultTableEditor.init();
    }

    public void done(){
        super.done();
        _FResultDocEditor.done();
        _FResultTableEditor.done();
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

        _FResultDocEditor = new ResultDocEditor( FSearcher, FSikherIni, getStatusBar() );
        _FCardPanel.add( _docView.getActionCommand(), _FResultDocEditor );

        _FResultTableEditor = new ResultTableEditor( FSearcher, FSikherIni, getStatusBar() );
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

    public OptionsCard[] getOptionsCards(){
        OptionsCard[] _cards1 = _FResultDocEditor.getOptionsCards();
        OptionsCard[] _cards2 = _FResultTableEditor.getOptionsCards();
        List<OptionsCard> _list = new ArrayList<OptionsCard>();
        if( _cards1 != null ){
            _list.addAll( Arrays.asList( _cards1 ) );
        }
        if( _cards2 != null ){
            _list.addAll( Arrays.asList( _cards2 ) );
        }
        return _list.toArray( new OptionsCard[_list.size()] );
    }

    public Document createDocument() {
        return _FgetVisibleEditor().createDocument();
    }

    public void exportPdf(Document doc) throws DocumentException {
        _FgetVisibleEditor().exportPdf( doc );
    }
}
