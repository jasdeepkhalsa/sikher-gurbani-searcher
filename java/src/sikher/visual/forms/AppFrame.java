package sikher.visual.forms;

import com.lowagie.text.*;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfWriter;
import sikher.searchengine.ISOLang;
import sikher.searchengine.XPathSearcher;
import sikher.searchengine.Hymn;
import sikher.searchengine.GurbaniType;
import sikher.templates.VisualUtils;
import sikher.visual.controls.SearchSikherControl;
import sikher.visual.controls.ViewSikherControl;
import sikher.visual.editors.*;
import sikher.visual.sidepanel.*;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileFilter;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 11:16:43
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/AppFrame.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: AppFrame.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class AppFrame extends JFrame {

    private SikherIni _FProperties;
    private OptionsDialog _FOptionsDialog = null;

    public static final String LOOK_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
    private MainPanel _FMainPanel;
    private JMenu _FExportMenu;
    private ArrayList<SikherComponent>_FSikherComponents = new ArrayList<SikherComponent>();

    public AppFrame(){
        setTitle(SikherProperties.getString( SikherProperties.FRAME_TITLE ));
        setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );

        addWindowListener( new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                _Fexit();
            }
        } );

        try {
            UIManager.setLookAndFeel( LOOK_WINDOWS );
            SwingUtilities.updateComponentTreeUI( this );
        }catch ( Exception e ) {
            System.err.println( e.getMessage() );
        }
    }

    private void _Fexit(){
        _FsaveProperties( SikherProperties.getString("inifile.name") );
        //done components
        for( SikherComponent _component : _FSikherComponents ){
            _component.done();
        }
        dispose();
    }

    public void loadComponents(){
        //init menu
        JMenuBar _menubar = _FcreateMenu();
        setJMenuBar( _menubar );
        //TODO: grab actions from main panel

        //load properties
        _FProperties = new SikherIni();
        _FloadProperties( SikherProperties.getString("inifile.name") );

        //init GUI
        //TODO: add to tab method getListenersToRegister for side panel
        _FMainPanel = new MainPanel();
        getContentPane().add( _FMainPanel );
        _FMainPanel.addChangeEditorListener( new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                Hymn[] _hymns = _FMainPanel.getSelectedEditor().getHymns();
                _FExportMenu.setEnabled( _hymns != null && _hymns.length > 0 );
            }
        } );

        //TODO: init searcher and add properties page for it
        Logger _logger = Logger.getAnonymousLogger( "resources.sikher" );
        XPathSearcher _searcher = new XPathSearcher( _logger );
        //set data directory
        _searcher.setDirectory("data/");

        StatusBar _statusBar = _FMainPanel.getStatusBar();

        //create controls
        SearchSikherControl _searchSikherControl = new SearchSikherControl( _searcher, _FProperties,_statusBar );
        _FMainPanel.addSikherControl( _searchSikherControl );
        _FSikherComponents.add( _searchSikherControl );

        ViewSikherControl _viewSikherControl = new ViewSikherControl( _searcher, _FProperties, _statusBar );
        _FMainPanel.addSikherControl( _viewSikherControl );
        _FSikherComponents.add( _viewSikherControl );

        //create editors
        final ResultSikherEditor _resultSikherEditor = new ResultSikherEditor( _searcher, _FProperties, _statusBar );
        _FMainPanel.addSikherEditor( _resultSikherEditor );
        _FSikherComponents.add( _resultSikherEditor );

        ViewSikherEditor _viewSikherEditor = new ViewSikherEditor( _searcher, _FProperties, _statusBar );
        _FMainPanel.addSikherEditor( _viewSikherEditor );
        _FSikherComponents.add( _viewSikherEditor );

        final AkandPaathEditor _akandPaathEditor = new AkandPaathEditor( _searcher, _FProperties, _statusBar );
        _FMainPanel.addSikherEditor( _akandPaathEditor );
        _FSikherComponents.add( _akandPaathEditor );

        //init actions
        SendHymnsAction _displayResultsAction = new SendHymnsAction( _resultSikherEditor, _FMainPanel );
        _searchSikherControl.addSikherListener( SearchSikherControl.EVENT_FIND, _displayResultsAction );

        LinkAction _linkAction = new LinkAction( _viewSikherEditor, _FMainPanel, _searcher );
        _resultSikherEditor.getResultDocEditor().addSikherListener( ResultDocEditor.EVENT_LINK, _linkAction );

        //send to akandpaath
        _resultSikherEditor.getResultDocEditor().addSendMenu( _akandPaathEditor, _FMainPanel );

        //init components
        for( SikherComponent _component : _FSikherComponents ){
            _component.init();
        }

        ISOLang _gurbani = _viewSikherControl.getGurbani();
        ISOLang _translit = _viewSikherControl.getTranslit();
        ISOLang _translation = _viewSikherControl.getTranslation();

        boolean _showGurbani = _viewSikherControl.isShowGurbani();
        boolean _showTranslit = _viewSikherControl.isShowTranslit();
        boolean _showTranslation = _viewSikherControl.isShowTranslation();
        boolean _showDetails = _viewSikherControl.isShowDetails();
        boolean _viewMode = _viewSikherControl.isViewMode();

        DataChangeListener _dataChangeListener = new DataChangeListener();
        _FOptionsDialog = new OptionsDialog( this, _FProperties );

        for( SikherComponent _component : _FSikherComponents ){
            if( _component instanceof SikherEditor ){
                SikherEditor _editor = (SikherEditor) _component;
                _editor.setProperty( SikherEditor.PROP_LANG_GURBANI, _gurbani );
                _editor.setProperty( SikherEditor.PROP_LANG_TRANSLIT, _translit );
                _editor.setProperty( SikherEditor.PROP_LANG_TRANSLATION, _translation );

                _editor.setProperty( SikherEditor.PROP_SHOW_GURBANI, _showGurbani );
                _editor.setProperty( SikherEditor.PROP_SHOW_TRANSLIT, _showTranslit );
                _editor.setProperty( SikherEditor.PROP_SHOW_TRANSLATION, _showTranslation );
                _editor.setProperty( SikherEditor.PROP_SHOW_DETAILS, _showDetails );

                _editor.setProperty( SikherEditor.PROP_VIEW_MODE, _viewMode );

                _viewSikherControl.addSikherListener( new ChangeViewAction( _editor ) );

                _editor.addSikherListener( AbstractSikherEditor.EVENT_DATA_CHANGED, _dataChangeListener );

                OptionsCard[] _optionCards = _editor.getOptionsCards();
                if( _optionCards != null ){
                    for( OptionsCard _card : _optionCards ){
                        _FOptionsDialog.addCard( _card.getTitle(), _card );
                    }
                }
            }
        }

        pack();
        VisualUtils.centerOnScreen( this );

        //lock size
        final int _origX = getSize().width;
        final int _origY = getSize().height;
        addComponentListener( new ComponentAdapter(){
            public void componentResized(ComponentEvent event) {
                setSize( (getWidth() < _origX) ? _origX : getWidth(), (getHeight() < _origY) ? _origY : getHeight());
            }
        } );
    }

    private JMenuBar _FcreateMenu(){
        JMenuBar _menubar = new JMenuBar();

        JMenu _menu = new JMenu( SikherProperties.getString( SikherProperties.MENU_FILE ) );
        _menu.add( new OptionsAction() );
        _menu.addSeparator();
        _FExportMenu = new JMenu(SikherProperties.getString("menu.export"));
        _menu.add( _FExportMenu );
        _FExportMenu.add( new Export2PdfAction() );
        //TODO: implement it
        /*_FExportMenu.add( new Export2HtmlAction() );*/
        _menu.addSeparator();
        //TODO: implement one day
        /*
        _menu.add( new PrintPreviewAction() );
        _menu.add( new PrintAction() );
        _menu.addSeparator();
        */
        _menu.add( new ExitAction() );
        _menubar.add( _menu );

        _menu = new JMenu( SikherProperties.getString(SikherProperties.MENU_HELP) );
        _menu.add( new AboutAction() );
        _menubar.add( _menu );
        return _menubar;
    }

    private void _FloadProperties( String filename ){
        try {
            _FProperties.load( filename );
        } catch (IOException e) {
            System.err.println("Ini file error: " + e.getMessage());
        }
    }

    private void _FsaveProperties( String filename ){
        try {
            _FProperties.store( filename );
        } catch (IOException e) {
            System.err.println("Ini file error: " + e.getMessage());
        }
    }

    public void showErrorMessage( String title, String text ){
        JOptionPane.showMessageDialog( this, text, title, JOptionPane.ERROR_MESSAGE );
    }

    //-------------------- listeners ------------------

    private class DataChangeListener implements IControlListener{
        public void controlEventOccured(SikherControlEvent event) {
            if( event.getSource() instanceof SikherEditor ){
                SikherEditor _editor = (SikherEditor) event.getSource();
                if( _editor.isShowing() ){
                    Hymn[] _hymns = (Hymn[]) event.getData();
                    _FExportMenu.setEnabled( _hymns != null && _hymns.length > 0 );
                }
            }
        }
    }

    //-------------------- actions --------------------

    private class OptionsAction extends AbstractAction{

        public OptionsAction(){
            putValue( NAME, SikherProperties.getString( "menu.options" ) );
        }

        public void actionPerformed(ActionEvent e) {
            _FOptionsDialog.open();
        }
    }

    private abstract class ExportAction extends AbstractAction{
        private JFileChooser _FFileDialog = null;
        private FileFilter _FFileFilter;
        private ExportFontDialog _FFontDialog = null;

        public ExportAction(){
            _FFileFilter = new FileFilter(){
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith( FgetFileExt() );
                }

                public String getDescription() {
                    return "*" + FgetFileExt();
                }
            };
        }

        public void actionPerformed(ActionEvent e) {

            //get file name
            if( _FFileDialog == null ){
                _FFileDialog = new JFileChooser();
                _FFileDialog.setDialogType( JFileChooser.SAVE_DIALOG );
                _FFileDialog.setFileFilter( _FFileFilter );
                _FFileDialog.setMultiSelectionEnabled( false );
            }
            int _res = _FFileDialog.showSaveDialog( AppFrame.this );
            if( _res == JFileChooser.APPROVE_OPTION ){
                File _file = _FFileDialog.getSelectedFile();
                String _path =_file.getAbsolutePath();
                if( !_path.endsWith( FgetFileExt() ) ){
                    _path += FgetFileExt();
                }
                SikherEditor _editor = _FMainPanel.getSelectedEditor();

                Document _doc = _editor.createDocument();
                try {
                    FgetWriter( _doc, _path );
                } catch (Exception e1) {
                    showErrorMessage( getValue(NAME).toString(), e1.getMessage() );
                    return;
                }
                _doc.open();
                try {
                    _editor.exportPdf( _doc );
                } catch (DocumentException e1) {
                    showErrorMessage( getValue(NAME).toString(), e1.getMessage() );
                }
                _doc.close();

                /*
                //ask for fonts
                if( _FFontDialog == null ){
                    _FFontDialog = new ExportFontDialog( AppFrame.this );
                }
                SikherEditor _editor = _FMainPanel.getSelectedEditor();

                ISOLang _gurbani = (ISOLang)_editor.getProperty( SikherEditor.PROP_LANG_GURBANI );
                ISOLang _translit = (ISOLang)_editor.getProperty( SikherEditor.PROP_LANG_TRANSLIT );
                ISOLang _translation = (ISOLang)_editor.getProperty( SikherEditor.PROP_LANG_TRANSLATION );

                if( _gurbani == null || _translit == null || _translation == null ){
                    //TODO: show some message
                    return;
                }
                boolean _showGurbani = (Boolean)_editor.getProperty( SikherEditor.PROP_SHOW_GURBANI );
                boolean _showTranslit = (Boolean)_editor.getProperty( SikherEditor.PROP_SHOW_TRANSLIT );
                boolean _showTranslation = (Boolean)_editor.getProperty( SikherEditor.PROP_SHOW_TRANSLATION );

                /*
                _FFontDialog.setGurbaniLang( _gurbani, _showGurbani,
                        _FProperties.getPdfFontPath( GurbaniType.GURBANI, _gurbani ) );
                _FFontDialog.setTranslitLang( _translit, _showTranslit,
                        _FProperties.getPdfFontPath( GurbaniType.TRANSLITERATION, _translit ) );
                _FFontDialog.setTranslationLang( _translation, _showTranslation,
                        _FProperties.getPdfFontPath( GurbaniType.TRANSLATION, _translation ) );

                if( _FFontDialog.open() == ModalDialog.OK ){

                    _FProperties.putPdfFont( _FFontDialog.getGurbaniFont(), GurbaniType.GURBANI, _gurbani );
                    _FProperties.putPdfFont( _FFontDialog.getTranslitFont(), GurbaniType.TRANSLITERATION, _translit );
                    _FProperties.putPdfFont( _FFontDialog.getTranslationFont(), GurbaniType.TRANSLATION, _translation );

                    //create document
                    Document _doc = _editor.createDocument();
                    try {
                        FgetWriter( _doc, _path );
                    } catch (Exception e1) {
                        showErrorMessage( getValue(NAME).toString(), e1.getMessage() );
                        return;
                    }
                    _doc.open();
                    try {
                        _editor.exportPdf( _doc );
                        //_editor.export( _doc, _FProperties.getFontSelector() );
                    } catch (DocumentException e1) {
                        showErrorMessage( getValue(NAME).toString(), e1.getMessage() );
                    }
                    _doc.close();
                }*/
            }
        }

        private void _FregisterFont( String path, String alias ){
            if( path != null && new File( path ).exists() ){
                FontFactory.register( path, alias );

            }
        }

        protected abstract String FgetFileExt();

        protected abstract DocWriter FgetWriter( Document doc, String filename )
                throws DocumentException, FileNotFoundException;
    }

    private class Export2PdfAction extends ExportAction{


        public Export2PdfAction(){
            putValue( NAME, SikherProperties.getString("menu.export-pdf") );
        }

        protected String FgetFileExt() {
            return ".pdf";
        }

        protected DocWriter FgetWriter( Document doc, String filename ) throws DocumentException, FileNotFoundException {
            DocWriter _writer = PdfWriter.getInstance( doc, new FileOutputStream( filename ) );
            //set header
            doc.setFooter(
                    new HeaderFooter( new Phrase(SikherProperties.getString("text.pdf-header")), false ) );
            return _writer;
        }
    }

    private class Export2HtmlAction extends ExportAction{

        public Export2HtmlAction(){
            putValue( NAME, SikherProperties.getString("menu.export-html") );
        }

        protected String FgetFileExt() {
            return ".html";
        }

        protected DocWriter FgetWriter(Document doc, String filename) throws DocumentException, FileNotFoundException {
            return HtmlWriter.getInstance( doc, new FileOutputStream( filename ) );
        }
    }

    private class PrintPreviewAction extends AbstractAction{
        public PrintPreviewAction(){
            putValue( NAME, SikherProperties.getString(SikherProperties.MENU_PRINTPREVIEW) );
        }

        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    private class PrintAction extends AbstractAction{
        public PrintAction(){
            putValue( NAME, SikherProperties.getString(SikherProperties.MENU_PRINT) );
        }

        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }

    private class ExitAction extends AbstractAction{
        public ExitAction(){
            putValue( NAME, SikherProperties.getString(SikherProperties.MENU_EXIT) );
        }

        public void actionPerformed(ActionEvent e) {
            _Fexit();
        }
    }

    private class AboutAction extends AbstractAction{
        public AboutAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString(SikherProperties.MENU_ABOUT) );
        }

        public void actionPerformed(ActionEvent e) {
            //TODO
        }
    }
}
