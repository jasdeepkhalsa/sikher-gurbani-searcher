package sikher.visual.editors;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.XPathSearcher;
import sikher.searchengine.GurbaniType;
import sikher.visual.forms.*;
import sikher.visual.sidepanel.MainPanel;
import sikher.visual.sidepanel.SikherControlEvent;
import sikher.visual.sidepanel.StatusBar;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:10:55
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/AbstractSikherEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: AbstractSikherEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class AbstractSikherEditor extends SikherEditor{

    public AbstractSikherEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar, String editorName ) {
        super( searcher, ini, statusBar, new GridLayout( 1, 1 ) );
        _FEditorName = editorName;
        _FinitComponents();
    }

    public static final String EVENT_DATA_CHANGED = "event_data_changed";

    private String _FEditorName = null;
    private ISikherDataModelEx _FDataModel = null;
    private JComponent _FComponent = null;
    private JPopupMenu _FPopupMenu = null;
    private JMenu _FSendMenu = null;

    private EditorFontOptionsCard _FFontOptionsCard = null;

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

        _FFontOptionsCard = new EditorFontOptionsCard( FSikherIni, _FEditorName );
        _FFontOptionsCard.setTitle( getControlName() );
    }

    public void setDataModel( JComponent component, ISikherDataModelEx dm ){
        if( _FComponent != null ){
            _FComponent.setComponentPopupMenu( null );
        }
        _FComponent = component;
        _FComponent.setComponentPopupMenu( _FPopupMenu );
        _FDataModel = dm;

        //_FFontOptionsCard.setDefaultFont( _FComponent.getFont(), _FComponent.getForeground() );
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
            _FDataModel.setLang( GurbaniType.GURBANI, (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLIT ) ){
            _FDataModel.setLang( GurbaniType.TRANSLITERATION, (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLATION ) ){
            _FDataModel.setLang( GurbaniType.TRANSLATION, (ISOLang) value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_GURBANI ) ){
            _FDataModel.setShow( GurbaniType.GURBANI, (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLIT ) ){
            _FDataModel.setShow( GurbaniType.TRANSLITERATION, (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLATION ) ){
            _FDataModel.setShow( GurbaniType.TRANSLATION, (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_SHOW_DETAILS ) ){
            _FDataModel.setShow( null, (Boolean)value );
        }else if( key.equalsIgnoreCase( PROP_VIEW_MODE ) ){
            _FDataModel.setViewType( (Boolean)value );
        }
    }

    public Object getProperty( String key ) {
        if( key.equalsIgnoreCase( PROP_LANG_GURBANI ) ){
            return _FDataModel.getLang( GurbaniType.GURBANI );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLIT ) ){
            return _FDataModel.getLang( GurbaniType.TRANSLITERATION );
        }else if( key.equalsIgnoreCase( PROP_LANG_TRANSLATION ) ){
            return _FDataModel.getLang( GurbaniType.TRANSLATION );
        }else if( key.equalsIgnoreCase( PROP_SHOW_GURBANI ) ){
            return _FDataModel.getShow( GurbaniType.GURBANI );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLIT ) ){
            return _FDataModel.getShow( GurbaniType.TRANSLITERATION );
        }else if( key.equalsIgnoreCase( PROP_SHOW_TRANSLATION ) ){
            return _FDataModel.getShow( GurbaniType.TRANSLATION );
        }else if( key.equalsIgnoreCase( PROP_SHOW_DETAILS ) ){
            return _FDataModel.getShow( null );
        }else if( key.equalsIgnoreCase( PROP_VIEW_MODE ) ){
            return _FDataModel.isViewType();
        }
        return null;
    }

    public Document createDocument() {
        return _FDataModel.createDocument();
    }

    public void exportPdf( Document doc ) throws DocumentException {
        _FDataModel.exportPdf( doc );
    }

    protected void searcherInfoUpdated(){
        _FFontOptionsCard.setLangs( GurbaniType.GURBANI, FSearcher.getGurbanies() );
        _FFontOptionsCard.setLangs( GurbaniType.TRANSLITERATION, FSearcher.getTransliterations() );
        _FFontOptionsCard.setLangs( GurbaniType.TRANSLATION, FSearcher.getTranslations() );
    }

    public OptionsCard[] getOptionsCards(){
        return new OptionsCard[]{ _FFontOptionsCard };
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
