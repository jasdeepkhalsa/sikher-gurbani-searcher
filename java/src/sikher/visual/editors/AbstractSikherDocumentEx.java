package sikher.visual.editors;

import sikher.searchengine.*;
import sikher.visual.forms.SikherIni;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 13.04.2006
 * Time: 15:02:32
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/AbstractSikherDocumentEx.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: AbstractSikherDocumentEx.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class AbstractSikherDocumentEx extends DefaultStyledDocument implements ISikherDataModelEx {

    protected AbstractSikherDataModel FDataModelDelegate = null;

    public AbstractSikherDocumentEx( SikherIni ini, String editor ){
        FDataModelDelegate = new DataModelDelegate( ini, editor );
    }

    public AbstractSikherDocumentEx( AbstractSikherDataModel model ){
        FDataModelDelegate = model;
    }

    protected class DataModelDelegate extends AbstractSikherDataModel{

        protected MutableAttributeSet[] FStyles;

        /**
         * Construstor
         * @param ini - Sikher properties
         */
        public DataModelDelegate(SikherIni ini, String editor) {
            super(ini, editor);
            FSimpleDrawer = new DocumentHymnDrawer();
        }

        protected MutableAttributeSet FconvertToAttributes( TypeElem elem ){
            MutableAttributeSet _map = new SimpleAttributeSet();
            ISOLang _lang = elem.getLang();
            GurbaniType _type = elem.getType();
            if( _type != null &&_lang == null ){
            }else{
                _map.addAttribute( StyleConstants.FontFamily, FSikherIni.getFontName( _type, _lang, FEditorName ) );
                _map.addAttribute( StyleConstants.FontSize, FSikherIni.getFontSize( _type, _lang, FEditorName ) );
                int _style = FSikherIni.getFontStyle( _type, _lang, FEditorName );
                _map.addAttribute( StyleConstants.Bold, ( _style & Font.BOLD ) != 0 );
                _map.addAttribute( StyleConstants.Italic, ( _style & Font.ITALIC ) != 0 );
                _map.addAttribute( StyleConstants.Foreground, FSikherIni.getFontColor( _type, _lang, FEditorName ) );
            }
            return _map;
        }

        protected void FonFontChanged(){
            FStyles = new MutableAttributeSet[4];
            for( int i=0; i<FStyles.length; i++ ){
                FStyles[i] = FconvertToAttributes( FTypeElems[i] );
            }
            super.FonFontChanged();
        }

        protected MutableAttributeSet FgetStyle( SikherLine line, GurbaniType type ){
            MutableAttributeSet _style = null;
            int _index = FgetIndexByType( type );
            if( _index >= 0 ){
                _style = FStyles[_index];
            }
            return _style;
        }

        protected class DocumentHymnDrawer implements IHymnDrawer{

            public void clearText() throws Exception {
                AbstractSikherDocumentEx.this.remove(0, getLength());
            }

            public void drawLine( String text, SikherLine line, GurbaniType type ) throws Exception {
                MutableAttributeSet _style = FgetStyle( line, type );
                insertString( getLength(), text + "\n", _style );
            }

            public void drawSeparator() throws Exception {
                insertString( getLength(), "\n", null );
            }
        }
    }

    public Hymn[] getHymns() {
        return FDataModelDelegate.getHymns();
    }

    public void setHymns(Hymn[] hymns) {
        FDataModelDelegate.setHymns( hymns );
    }

    public void setLang(GurbaniType type, ISOLang lang) {
        FDataModelDelegate.setLang( type, lang );
    }

    public ISOLang getLang(GurbaniType type) {
        return FDataModelDelegate.getLang( type );
    }

    public void setViewType(boolean viewType) {
        FDataModelDelegate.setViewType( viewType );
    }

    public boolean isViewType() {
        return FDataModelDelegate.isViewType();
    }

    public void setShow(GurbaniType type, boolean show) {
        FDataModelDelegate.setShow( type, show );
    }

    public boolean getShow(GurbaniType type) {
        return FDataModelDelegate.getShow( type );
    }

    public String copyAll() {
        return FDataModelDelegate.copyAll();
    }

    public String copyGurbani() {
        return FDataModelDelegate.copyGurbani();
    }

    public String copyTranslit() {
        return FDataModelDelegate.copyTranslit();
    }

    public String copyTranslation() {
        return FDataModelDelegate.copyTranslation();
    }

    public String copyDetails() {
        return FDataModelDelegate.copyDetails();
    }

    public Document createDocument() {
        return FDataModelDelegate.createDocument();
    }

    public void exportPdf(Document doc) throws DocumentException {
        FDataModelDelegate.exportPdf( doc );
    }
}
