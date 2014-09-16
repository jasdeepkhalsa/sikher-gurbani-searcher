package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.SikherControlEvent;
import sikher.visual.sidepanel.MainPanel;
import sikher.visual.forms.SikherProperties;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:10:55
 * $Header$
 * $Log$
 * Description:
 */
public class AbstractSikherEditor extends SikherEditor{

    public AbstractSikherEditor(StatusBar statusBar) {
        super(statusBar, new GridLayout(1, 1));
        _FinitComponents();
    }

    public static final String EVENT_DATA_CHANGED = "event_data_changed";

    private ISikherDataModel _FDataModel = null;
    private JComponent _FComponent = null;
    private JPopupMenu _FPopupMenu = null;
    private JMenu _FSendMenu = null;

    private void _FinitComponents(){
        _FPopupMenu = new JPopupMenu();

        //add copy actions
        JMenu _menu = new JMenu( SikherProperties.getString("editor.menu.copy") );
        _FPopupMenu.add( _menu );
        _menu.add( new CopyAllAction() );
        _menu.add( new CopyGurbaniAction() );
        _menu.add( new CopyTranslitAction() );
        _menu.add( new CopyTranslationAction() );
        _menu.add( new CopyDetailsAction() );
    }

    public void setDataModel( JComponent component, ISikherDataModel dm ){
        if( _FComponent != null ){
            _FComponent.setComponentPopupMenu( null );
        }
        _FComponent = component;
        _FComponent.setComponentPopupMenu( _FPopupMenu );
        _FDataModel = dm;
    }

    public Hymn[] getHymns() {
        return _FDataModel.getHymns();
    }

    public void setHymns(Hymn[] hymns) {
        _FDataModel.setHymns( hymns );
        fireControlEvent( new SikherControlEvent( this, EVENT_DATA_CHANGED, hymns ) );
    }

    public void setProperty(String key, Object value) {
        if( key.equalsIgnoreCase( PROP_LANG_GURBANI ) ){
            _FDataModel.setGurbani( (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLIT ) ){
            _FDataModel.setTranslit( (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLATION ) ){
            _FDataModel.setTranslation( (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_GURBANI ) ){
            _FDataModel.setShowGurbani( (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLIT ) ){
            _FDataModel.setShowTranslit( (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLATION ) ){
            _FDataModel.setShowTranslation( (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_DETAILS ) ){
            _FDataModel.setShowDetails( (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_VIEW_MODE ) ){
            _FDataModel.setViewType( (Boolean)value );
        }
    }

    public Object getProperty( String key ) {
        if( key.equalsIgnoreCase( PROP_LANG_GURBANI ) ){
            return _FDataModel.getGurbani();
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLIT ) ){
            return _FDataModel.getTranslit();
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLATION ) ){
            return _FDataModel.getTranslation();
        }else if( key.equalsIgnoreCase( PROP_SHOW_GURBANI ) ){
            return _FDataModel.isShowGurbani();
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLIT ) ){
            return _FDataModel.isShowTranslit();
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLATION ) ){
            return _FDataModel.isShowTranslation();
        }else if( key.equalsIgnoreCase( PROP_SHOW_DETAILS ) ){
            return _FDataModel.isShowDetails();
        }else if( key.equalsIgnoreCase( PROP_VIEW_MODE ) ){
            return _FDataModel.isViewType();
        }
        return null;
    }

    public Document createDocument() {
        return _FDataModel.createDocument();
    }

    public void export(Document doc, FontSelector fs) throws DocumentException {
        _FDataModel.export( doc, fs );
    }

    /** Popup dialog methods**/
    public void addSendMenu( SikherEditor editor, MainPanel mainPanel ){
        if( _FSendMenu == null  ){
            _FSendMenu = new JMenu( SikherProperties.getString("editor.menu.send") );
        }
        if( _FPopupMenu.getComponentIndex( _FSendMenu ) < 0 ){
            _FPopupMenu.add( _FSendMenu );
        }
        _FSendMenu.add( new SendAction( editor, mainPanel ) );
    }

    public void removeSendMenu( SikherEditor editor ){
        if( _FSendMenu != null ){
            for( Component _comp : _FSendMenu.getComponents() ){
                if( _comp instanceof JMenuItem ){
                    Action _action = ((JMenuItem)_comp).getAction();
                    if( _action instanceof SendAction ){
                        if( ((SendAction)_action).getEditor() == editor ){
                            _FSendMenu.remove( _comp );
                        }
                    }
                }
            }
            if( _FSendMenu.getComponentCount() == 0 ){
                _FPopupMenu.remove( _FSendMenu );
            }
        }
    }

    /**
     * Place a String on the clipboard, and make this class the
     * owner of the Clipboard's contents.
     */
    public void setClipboardContents( String text ){
        StringSelection _stringSelection = new StringSelection( text );
        Clipboard _clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try{
            _clipboard.setContents( _stringSelection, null );
        }catch( IllegalStateException e){}
    }

    private class CopyAllAction extends AbstractAction{
        public CopyAllAction(){
            putValue( NAME, SikherProperties.getString("editor.menu.copy-all") );
        }

        public void actionPerformed(ActionEvent e) {
            setClipboardContents( _FDataModel.copyAll() );
        }
    }

    private class CopyGurbaniAction extends AbstractAction{
        public CopyGurbaniAction(){
            putValue( NAME, SikherProperties.getString("editor.menu.copy-gurbani") );
        }

        public void actionPerformed(ActionEvent e) {
            setClipboardContents( _FDataModel.copyGurbani() );
        }
    }

    private class CopyTranslitAction extends AbstractAction{
        public CopyTranslitAction(){
            putValue( NAME, SikherProperties.getString("editor.menu.copy-translit") );
        }

        public void actionPerformed(ActionEvent e) {
            setClipboardContents( _FDataModel.copyTranslit() );
        }
    }

    private class CopyTranslationAction extends AbstractAction{
        public CopyTranslationAction(){
            putValue( NAME, SikherProperties.getString("editor.menu.copy-translation") );
        }

        public void actionPerformed(ActionEvent e) {
            setClipboardContents( _FDataModel.copyTranslation() );
        }
    }

    private class CopyDetailsAction extends AbstractAction{
        public CopyDetailsAction(){
            putValue( NAME, SikherProperties.getString("editor.menu.copy-details") );
        }

        public void actionPerformed(ActionEvent e) {
            setClipboardContents( _FDataModel.copyDetails() );
        }
    }

    private class SendAction extends AbstractAction{
        private SikherEditor _FEditor;
        private MainPanel _FMainPanel;

        public SendAction( SikherEditor editor, MainPanel mainPanel ){
            _FEditor = editor;
            _FMainPanel = mainPanel;
            putValue( NAME, SikherProperties.getString("editor.menu.send-to") + editor.getControlName() );
        }

        public void actionPerformed(ActionEvent e) {
            _FEditor.setHymns( getHymns() );
            _FMainPanel.setSelectebEditor( _FEditor );
        }

        public SikherEditor getEditor(){
            return _FEditor;
        }
    }
}
