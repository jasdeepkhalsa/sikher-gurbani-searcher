package sikher.visual.editors;

import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.FontSelector;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.SikherLine;
import sikher.searchengine.HymnType;
import sikher.visual.forms.SikherProperties;
import sikher.visual.forms.SikherIni;

import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 18:10:53
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/AbstractSikherDocument.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: AbstractSikherDocument.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class AbstractSikherDocument extends DefaultStyledDocument
        implements ClipboardOwner, ISikherDataModel {

    private Hymn[] _FHymns = {};

    private boolean _FShowGurbani = false;
    private boolean _FShowTranslit = false;
    private boolean _FShowTranslation = false;
    private boolean _FShowDetails = false;

    private ISOLang _FGurbani = null;
    private ISOLang _FTranslit = null;
    private ISOLang _FTranslation = null;

    private java.awt.Font _FGurbaniFont = null;
    private java.awt.Font _FTranslitFont = null;
    private java.awt.Font _FTranslationFont = null;

    /**
     * true - Split view
     * false - Combined view
     */
    private boolean _FViewType = true;

    protected MutableAttributeSet FDetails = null;

    public AbstractSikherDocument( SikherIni ini ){
        FDetails = new SimpleAttributeSet();
        FDetails.addAttribute( StyleConstants.Foreground, Color.gray );
    }

    /**
     * Display hymns according to view options
     */
    public void display() {
        try {
            remove(0, getLength());

            //setup languages
            ISOLang _gurbani = getGurbani();
            ISOLang _translit = getTranslit();
            ISOLang _translation = getTranslation();

            Object[][] _langTypes = new Object[][]{
                    new Object[]{ isShowGurbani() && _gurbani != null, _gurbani, HymnType.GURBANI },
                    new Object[]{ isShowTranslit() && _translit != null, _translit, HymnType.TRANSLITERATION },
                    new Object[]{ isShowTranslation() && _translation != null, _translation, HymnType.TRANSLATION }
            };
            if( isViewType() ){
                for( Hymn _hymn : getHymns() ){
                    SikherLine[] _lines = FgetLines( _hymn );
                    for( SikherLine _line : _lines ){
                        for( Object[] _lt : _langTypes ){
                            boolean _bShow = (Boolean)_lt[0];
                            if( _bShow ){
                                ISOLang _lang = (ISOLang) _lt[1];
                                HymnType _type = (HymnType) _lt[2];
                                _FinsertString( _line, _lang, _type, FgetStyle( _hymn, _line, _type ));
                            }
                        }
                        //insert details
                        if( isShowDetails() ){
                            String _details = FgetLineDetails( _hymn, _line.getIndex() );
                            insertString(getLength(), _details, FDetails);
                            insertString(getLength(), "\n", null);
                        }
                        insertString(getLength(), "\n", null);
                    }
                }
            }else{
                for( Object[] _lt : _langTypes ){
                    boolean _bShow = (Boolean)_lt[0];
                    ISOLang _lang = (ISOLang) _lt[1];
                    HymnType _type = (HymnType) _lt[2];
                    if ( !_bShow ) continue;

                    for( Hymn _hymn : getHymns() ){
                        SikherLine[] _lines = FgetLines( _hymn );
                        for( SikherLine _line : _lines ){
                            _FinsertString( _line, _lang, _type, FgetStyle( _hymn, _line, _type ) );
                        }
                    }
                }
                //insert details
                FinsertCombiDetails();
            }

        } catch (BadLocationException e) {
            System.err.println( "[" + getClass() + ".display] Bad location error: " + e.toString() );
        }
    }

    protected String FgetHymnDetails( Hymn hymn ){
        return "["+ SikherProperties.getString("doc.sikher.hymn") + ": " + hymn.getNumber() + "] " +
               "["+ SikherProperties.getString("doc.sikher.page") + ": " + hymn.getPage() + "] " +
               "["+ SikherProperties.getString("doc.sikher.author") + ": " + hymn.getAuthor() + "] " +
               "["+ SikherProperties.getString("doc.sikher.melody") + ": " + hymn.getMelody() + "]";
    }

    protected String FgetLineDetails( Hymn hymn, int index ){
        return "["+ SikherProperties.getString("doc.sikher.line") + ": " + index + "] " + FgetHymnDetails( hymn );
    }

    protected abstract SikherLine[] FgetLines( Hymn hymn );
    protected abstract MutableAttributeSet FgetStyle( Hymn hymn, SikherLine line, HymnType type );
    protected abstract void FinsertCombiDetails() throws BadLocationException;

    private void _FinsertString( SikherLine line, ISOLang lang, HymnType type, MutableAttributeSet style )
            throws BadLocationException {
        /*
        String _value = line.getText( lang, type );
        insertString(getLength(), _value, style);
        insertString(getLength(), "\n", null);
        */
    }


    public void lostOwnership(Clipboard clipboard, Transferable contents) {}

    public Hymn[] getHymns() {
        return _FHymns;
    }

    public void setHymns(Hymn[] hymns) {
        _FHymns = hymns;
        display();
    }

    public void setDisplayOptions(ISOLang gurbani, ISOLang translit, ISOLang translation, boolean showGurbani, boolean showTranslit, boolean showTranslation, boolean showDetails, boolean viewType) {
        _FGurbani = gurbani;
        _FTranslit = translit;
        _FTranslation = translation;
        _FShowGurbani = showGurbani;
        _FShowTranslit = showTranslit;
        _FShowTranslation = showTranslation;
        _FShowDetails = showDetails;
        _FViewType = viewType;
        display();
    }

    public void setGurbani(ISOLang gurbani) {
        _FGurbani = gurbani;
        display();
   }

    public ISOLang getGurbani() {
        return _FGurbani;
    }

    public void setTranslit(ISOLang translit) {
        _FTranslit = translit;
        display();
    }

    public ISOLang getTranslit() {
        return _FTranslit;
    }

    public void setTranslation(ISOLang translation) {
        _FTranslation = translation;
        display();
    }

    public ISOLang getTranslation() {
        return _FTranslation;
    }

    public void setViewType(boolean viewType) {
        _FViewType = viewType;
        display();
    }

    public boolean isViewType() {
        return _FViewType;
    }

    public void setShowGurbani(boolean bShow) {
        _FShowGurbani = bShow;
        display();
    }

    public boolean isShowGurbani() {
        return _FShowGurbani;
    }

    public void setShowTranslit(boolean bShow) {
        _FShowTranslit = bShow;
        display();
    }

    public boolean isShowTranslit() {
        return _FShowTranslit;
    }

    public void setShowTranslation(boolean bShow) {
        _FShowTranslation = bShow;
        display();
    }

    public boolean isShowTranslation() {
        return _FShowTranslation;
    }

    public void setShowDetails(boolean bShow) {
        _FShowDetails = bShow;
        display();
    }

    public boolean isShowDetails() {
        return _FShowDetails;
    }

    public String copyAll() {
        try {
            return getText(0, getLength());
        } catch (BadLocationException e) {
            System.err.println( "[" + getClass() + ".copyAll] Bad location error: " + e.toString() );
            return "";
        }
    }

    public String copyGurbani() {
        ISOLang _lang = getGurbani();
        return _lang != null && isShowGurbani() ? Fcopy( _lang, HymnType.GURBANI ) : "";
    }

    public String copyTranslit() {
        ISOLang _lang = getTranslit();
        return _lang != null && isShowTranslit() ? Fcopy( _lang, HymnType.TRANSLITERATION ) : "";
    }

    public String copyTranslation() {
        ISOLang _lang = getTranslation();
        return _lang != null && isShowTranslation() ? Fcopy( _lang, HymnType.TRANSLATION ) : "";
    }

    public String copyDetails() {
        String _text = "";
        if( isShowDetails() ){
            for( Hymn _hymn: getHymns() ){
                SikherLine[] _lines = FgetLines( _hymn );
                for( SikherLine _line : _lines ){
                    _text += FgetLineDetails( _hymn, _line.getIndex() ) + '\n';
                }
            }
        }
        return _text;
    }

    private String Fcopy( ISOLang lang, HymnType type ){
        String _text = "";
        for( Hymn _hymn : getHymns() ){
            SikherLine[] _lines = FgetLines( _hymn );
            for( SikherLine _line : _lines ){
                //_text += _line.getText( lang, type ) + "\n";
            }
        }
        return _text;
    }

    public com.lowagie.text.Document createDocument(){
        return new com.lowagie.text.Document( PageSize.A4, 30, 30, 30, 30 );
    }
    public void export(Document doc, FontSelector fs) throws DocumentException {
        ISOLang _gurbani = getGurbani();
        ISOLang _translit = getTranslit();
        ISOLang _translation = getTranslation();

        boolean _bShowGurbani = isShowGurbani() && _gurbani != null;
        boolean _bShowTranslit = isShowTranslit() && _translit != null;
        boolean  _bShowTranslation = isShowTranslation() && _translation != null;

        Object[][] _langTypes = new Object[][]{
                new Object[]{ _bShowGurbani, _gurbani, HymnType.GURBANI },
                new Object[]{ _bShowTranslit, _translit, HymnType.TRANSLITERATION },
                new Object[]{ _bShowTranslation, _translation, HymnType.TRANSLATION }
        };

        if( isViewType() ){
            for( Hymn _hymn : getHymns() ){
                SikherLine[] _lines = FgetLines( _hymn );
                for( SikherLine _line : _lines ){

                    Paragraph _paragraph = _FcreateDefault();

                    for( Object[] _lt : _langTypes ){
                        boolean _bShow = (Boolean)_lt[0];
                        if ( !_bShow ) continue;
                        ISOLang _lang = ((ISOLang) _lt[1]);
                        HymnType _type = (HymnType) _lt[2];
                        /*
                        Phrase _ph = fs.process( _line.getText( _lang, _type ) + '\n' );
                        _paragraph.add( _ph );
                        */
                    }

                    //insert details
                    if( isShowDetails() ){
                        String _details = FgetLineDetails( _hymn, _line.getIndex() );
                        Phrase _ph = fs.process( _details );
                        _paragraph.add( _ph );
                    }
                    doc.add( _paragraph );
                }
            }
        }else{
            for( Object[] _lt : _langTypes ){
                boolean _bShow = (Boolean)_lt[0];
                if ( !_bShow ) continue;
                ISOLang _lang = ((ISOLang) _lt[1]);
                HymnType _type = (HymnType) _lt[2];

                Paragraph _paragraph = _FcreateDefault();

                for( Hymn _hymn : getHymns() ){
                    SikherLine[] _lines = FgetLines( _hymn );
                    for( SikherLine _line : _lines ){
                        /*
                        Phrase _ph = fs.process( _line.getText( _lang, _type ) + '\n' );
                        _paragraph.add( _ph );
                        */
                    }
                }
                doc.add( _paragraph );
            }
            //insert details
            if( isShowDetails() ){
                Paragraph _paragraph  = _FcreateDefault();
                FinsertCombiDetails( _paragraph, fs );
                doc.add( _paragraph );
            }
        }
    }

    public void exportPdf( com.lowagie.text.Document doc,
                           com.lowagie.text.Font gurbaniFont,
                           com.lowagie.text.Font translitFont,
                           com.lowagie.text.Font translationFont,
                           com.lowagie.text.Font detailsFont )
            throws DocumentException{

        ISOLang _gurbani = getGurbani();
        ISOLang _translit = getTranslit();
        ISOLang _translation = getTranslation();

        boolean _bShowGurbani = isShowGurbani() && _gurbani != null;
        boolean _bShowTranslit = isShowTranslit() && _translit != null;
        boolean  _bShowTranslation = isShowTranslation() && _translation != null;

        Object[][] _langTypes = new Object[][]{
                new Object[]{ _bShowGurbani, _gurbani, HymnType.GURBANI, gurbaniFont },
                new Object[]{ _bShowTranslit, _translit, HymnType.TRANSLITERATION, translitFont },
                new Object[]{ _bShowTranslation, _translation, HymnType.TRANSLATION, translationFont }
        };

        if( isViewType() ){
            for( Hymn _hymn : getHymns() ){
                SikherLine[] _lines = FgetLines( _hymn );
                for( SikherLine _line : _lines ){

                    Paragraph _paragraph = _FcreateDefault();

                    for( Object[] _lt : _langTypes ){
                        boolean _bShow = (Boolean)_lt[0];
                        if ( !_bShow ) continue;
                        ISOLang _lang = ((ISOLang) _lt[1]);
                        HymnType _type = (HymnType) _lt[2];
                        com.lowagie.text.Font _font = (Font) _lt[3];
                        Phrase _ph = null;
                        /*
                        if( _font != null ){
                            _ph =  new Phrase( _line.getText( _lang, _type ) + '\n', _font );
                        }else{
                            _ph =  new Phrase( _line.getText( _lang, _type ) + '\n' );
                        }
                        _paragraph.add( _ph );
                        */
                    }

                    //insert details
                    if( isShowDetails() ){
                        String _details = FgetLineDetails( _hymn, _line.getIndex() );
                        Phrase _ph = new Phrase( _details, detailsFont );
                        _paragraph.add( _ph );
                    }
                    doc.add( _paragraph );
                }
            }
        }else{
            for( Object[] _lt : _langTypes ){
                boolean _bShow = (Boolean)_lt[0];
                if ( !_bShow ) continue;
                ISOLang _lang = ((ISOLang) _lt[1]);
                HymnType _type = (HymnType) _lt[2];
                com.lowagie.text.Font _font = (Font) _lt[3];
                Paragraph _paragraph = _FcreateDefault();

                for( Hymn _hymn : getHymns() ){
                    SikherLine[] _lines = FgetLines( _hymn );
                    for( SikherLine _line : _lines ){
                        Phrase _ph = null;
                        /*
                        if( _font != null ){
                            _ph =  new Phrase( _line.getText( _lang, _type ) + '\n', _font );
                        }else{
                            _ph =  new Phrase( _line.getText( _lang, _type ) + '\n' );
                        }
                        _paragraph.add( _ph );
                        */
                    }
                }
                doc.add( _paragraph );
            }
            //insert details
            if( isShowDetails() ){
                Paragraph _paragraph  = _FcreateDefault();
                FinsertCombiDetails( _paragraph, detailsFont );
                doc.add( _paragraph );
            }
        }
    }

    private Paragraph _FcreateDefault(){
        Paragraph _paragraph = new Paragraph();
        _paragraph.setAlignment( Element.ALIGN_CENTER );
        _paragraph.setSpacingBefore( 15f );
        _paragraph.setSpacingAfter( 15f );
        return _paragraph;
    }

    protected abstract void FinsertCombiDetails( Paragraph paragraph, FontSelector fs );
    protected abstract void FinsertCombiDetails( Paragraph paragraph, com.lowagie.text.Font detailsFont );
}
