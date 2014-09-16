package sikher.visual.editors;

import com.lowagie.text.*;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.FontSelector;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.SikherLine;
import sikher.visual.forms.SikherProperties;

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
 * $Header$
 * $Log$
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

    /**
     * true - Split view
     * false - Combined view
     */
    private boolean _FViewType = true;

    protected MutableAttributeSet FDetails = null;

    public AbstractSikherDocument(){
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
                    new Object[]{ isShowGurbani() && _gurbani != null, _gurbani, Hymn.TYPE_GURBANI },
                    new Object[]{ isShowTranslit() && _translit != null, _translit, Hymn.TYPE_TRANSLIT },
                    new Object[]{ isShowTranslation() && _translation != null, _translation, Hymn.TYPE_TRANSLATION }
            };
            if( isViewType() ){
                for( Hymn _hymn : getHymns() ){
                    SikherLine[] _lines = FgetLines( _hymn );
                    for( SikherLine _line : _lines ){
                        for( Object[] _lt : _langTypes ){
                            boolean _bShow = (Boolean)_lt[0];
                            if( _bShow ){
                                ISOLang _lang = (ISOLang) _lt[1];
                                String _type = (String) _lt[2];
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
                    String _type = (String) _lt[2];
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
    protected abstract MutableAttributeSet FgetStyle( Hymn hymn, SikherLine line, String type );
    protected abstract void FinsertCombiDetails() throws BadLocationException;

    private void _FinsertString( SikherLine line, ISOLang lang, String type, MutableAttributeSet style )
            throws BadLocationException {
        String _value = line.getText( lang.getCode(), type );
        insertString(getLength(), _value, style);
        insertString(getLength(), "\n", null);
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
        return _lang != null && isShowGurbani() ? Fcopy( _lang.getCode(), Hymn.TYPE_GURBANI ) : "";
    }

    public String copyTranslit() {
        ISOLang _lang = getTranslit();
        return _lang != null && isShowTranslit() ? Fcopy( _lang.getCode(), Hymn.TYPE_TRANSLIT ) : "";
    }

    public String copyTranslation() {
        ISOLang _lang = getTranslation();
        return _lang != null && isShowTranslation() ? Fcopy( _lang.getCode(), Hymn.TYPE_TRANSLATION ) : "";
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

    private String Fcopy( String lang, String type ){
        String _text = "";
        for( Hymn _hymn : getHymns() ){
            SikherLine[] _lines = FgetLines( _hymn );
            for( SikherLine _line : _lines ){
                _text += _line.getText( lang, type ) + "\n";
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
                new Object[]{ _bShowGurbani, _gurbani, Hymn.TYPE_GURBANI },
                new Object[]{ _bShowTranslit, _translit, Hymn.TYPE_TRANSLIT },
                new Object[]{ _bShowTranslation, _translation, Hymn.TYPE_TRANSLATION }
        };

        if( isViewType() ){
            for( Hymn _hymn : getHymns() ){
                SikherLine[] _lines = FgetLines( _hymn );
                for( SikherLine _line : _lines ){

                    Paragraph _paragraph = _FcreateDefault();

                    for( Object[] _lt : _langTypes ){
                        boolean _bShow = (Boolean)_lt[0];
                        if ( !_bShow ) continue;
                        String _lang = ((ISOLang) _lt[1]).getCode();
                        String _type = (String) _lt[2];
                        Phrase _ph = fs.process( _line.getText( _lang, _type ) + '\n' );
                        _paragraph.add( _ph );
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
                String _lang = ((ISOLang) _lt[1]).getCode();
                String _type = (String) _lt[2];

                Paragraph _paragraph = _FcreateDefault();

                for( Hymn _hymn : getHymns() ){
                    SikherLine[] _lines = FgetLines( _hymn );
                    for( SikherLine _line : _lines ){
                        Phrase _ph = fs.process( _line.getText( _lang, _type ) + '\n' );
                        _paragraph.add( _ph );
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

    private Paragraph _FcreateDefault(){
        Paragraph _paragraph = new Paragraph();
        _paragraph.setAlignment( Element.ALIGN_CENTER );
        _paragraph.setSpacingBefore( 15f );
        _paragraph.setSpacingAfter( 15f );
        return _paragraph;
    }

    protected abstract void FinsertCombiDetails( Paragraph paragraph, FontSelector fs );
}
