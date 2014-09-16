package sikher.visual.editors;

import com.lowagie.text.*;
import sikher.searchengine.GurbaniType;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.SikherLine;
import sikher.visual.forms.ISikherPropertyListener;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.SikherProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 13.04.2006
 * Time: 11:31:47
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/AbstractSikherDataModel.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: AbstractSikherDataModel.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class AbstractSikherDataModel implements ISikherDataModelEx{

    protected SikherIni FSikherIni = null;

    //hymns properties
    protected Hymn[] FHymns = {};
    protected TypeElem[] FTypeElems;
    /**
     * true - Split view
     * false - Combined view
     */
    protected boolean FViewType = true;

    protected String FSplitPattern = "G1-I1-T1-D1";
    protected String FCombiPattern = "G0-I0-T0-D0";

    protected IHymnDrawer FSimpleDrawer = null;
    protected IHymnDrawer FPdfDrawer = null;

    protected String FEditorName;
    /**
     * Construstor
     * @param ini - Sikher proeprties
     */
    public AbstractSikherDataModel( SikherIni ini, String editor ){
        FEditorName = editor;
        FSikherIni = ini;
        FSikherIni.addPropertyListener( new ISikherPropertyListener() {
            public void onPropertyChanged( String key ) {
                for( TypeElem _elem : FTypeElems ){
                    ISOLang _lang = _elem.getLang();
                    GurbaniType _type = _elem.getType();
                    if( _lang == null && _type != null ){
                        continue;
                    }
                    if( FSikherIni.createFontKey( SikherIni.KEY_FONT_NAME, _type, _lang, FEditorName ).equals( key ) ||
                        FSikherIni.createFontKey( SikherIni.KEY_FONT_STYLE, _type, _lang, FEditorName ).equals( key ) ||
                        FSikherIni.createFontKey( SikherIni.KEY_FONT_SIZE, _type, _lang, FEditorName ).equals( key ) ||
                        FSikherIni.createFontKey( SikherIni.KEY_FONT_COLOR, _type, _lang, FEditorName ).equals( key ) ){
                        FonFontChanged();
                        break;
                    }
                }
            }
        });

        //init fonts
        FTypeElems = new TypeElem[]{
                new TypeElem( GurbaniType.GURBANI ),
                new TypeElem( GurbaniType.TRANSLITERATION ),
                new TypeElem( GurbaniType.TRANSLATION ),
                new TypeElem( null )
        };
    }

    public String getSplitPattern() {
        return FSplitPattern;
    }

    public void setSplitPattern(String splitPattern) {
        this.FSplitPattern = splitPattern;
    }

    public String getCombiPattern() {
        return FCombiPattern;
    }

    public void setCombiPattern(String combiPattern) {
        this.FCombiPattern = combiPattern;
    }

    /**
     * Inherited classes may implement this method to set
     * additional font attributes
     */

    protected void FonFontChanged(){
        FupdateContent( FSimpleDrawer );
    }

    public Hymn[] getHymns() {
        return FHymns;
    }

    public void setHymns( Hymn[] hymns ) {
        FHymns = hymns;
        FupdateContent( FSimpleDrawer );
    }

    protected int FgetIndexByType( GurbaniType type ){
        int _index = -1;
        if( type != null ){
            switch( type ){
                case GURBANI:
                    _index = 0;
                    break;
                case TRANSLITERATION:
                    _index = 1;
                    break;
                case TRANSLATION:
                    _index = 2;
                    break;
            }
        }else{//details
            _index = 3;
        }
        return _index;
    }

    public void setLang( GurbaniType type, ISOLang lang ) {
        int _index = FgetIndexByType( type );
        if( _index >=0 && _index <3  ){
            FTypeElems[_index].setLang( lang );
            FonFontChanged();
        }
    }

    public ISOLang getLang(GurbaniType type) {
        int _index = FgetIndexByType( type );
        if( _index >=0 && _index <3  ){
            return FTypeElems[_index].getLang();
        }
        return null;
    }

    /**
     * Update displayed content
     */
    protected void FupdateContent( IHymnDrawer drawer ){
        String _pattern = isViewType() ? FSplitPattern : FCombiPattern;
        if( _pattern == null || _pattern.equals("") || drawer == null ){
            return;
        }

        int _lineOrders[][] = new int[][]{ {-1, 0}, {-1, 0}, {-1, 0}, {-1, 0} };
        int i=0;
        StringTokenizer _st = new StringTokenizer( _pattern, "-", false );
        while( _st.hasMoreTokens() ){
            String _token = _st.nextToken();
            int _index;
            if( _token.startsWith( "G" ) ){
                _index = 0;
            }else if( _token.startsWith( "I" ) ){
                _index = 1;
            }else if( _token.startsWith( "T" ) ){
                _index = 2;
            }else if( _token.startsWith( "D" ) ){
                _index = 3;
            }else{
                continue;
            }
            try{
                int _count = Integer.valueOf( _token.substring( 1 ) );
                _lineOrders[i][0] = _index;
                _lineOrders[i][1] = _count;
            }catch( Exception e ){}
            i++;
        }

        //grab all lines
        SikherLine[] _lines = FgetLines();

        int _pos[] = new int[]{0, 0, 0, 0};
        try{
            drawer.clearText();

            int _finished = 0;
            while( _finished != 0xf ){
                for( i=0; i<_lineOrders.length; i++ ){
                    int _index = _lineOrders[i][0];
                    if( _index < 0 ){
                        continue;
                    }

                    TypeElem _elem = FTypeElems[_index];
                    if( _elem.getType() != null && _elem.getLang() == null || !_elem.isShow() ){
                        _finished = 1<<i | _finished;
                        continue;
                    }

                    int _count = _lineOrders[i][1];
                    int j = 0;

                    for( ; j+_pos[i] < _lines.length; j++ ){
                        if( _count > 0 && j == _count ){
                            break;
                        }
                        //boolean _endLine = /*!( _count > 0 && j<_count-1 || j+_pos[i]<_lines.length-1 ) &&*/ i==_lineOrders.length-1;
                        if( _elem.getType() != null ){
                            String _text = _lines[j+_pos[i]].getText( _elem.getLang(), _elem.getType() );
                            drawer.drawLine( _text, _lines[j+_pos[i]], _elem.getType() );
                        }else{
                            FdrawDetails( drawer, _lines[j+_pos[i]] );
                        }
                    }
                    _pos[i] += j;
                    if( _pos[i] >= _lines.length ){
                        _finished = 1<<i | _finished;
                    }
                    if( _count == 0 ){
                        //put separator
                        drawer.drawSeparator();
                    }
                }
                drawer.drawSeparator();
            }
        }catch( Exception e ){
            //TODO: log
        }

    }

    protected SikherLine[] FgetLines(){
        ArrayList<SikherLine> _FLines = new ArrayList<SikherLine>();
        for ( Hymn _hymn : FHymns ) {
            _FLines.addAll( Arrays.asList( _hymn.getLines() ) );
        }
        return _FLines.toArray( new SikherLine[_FLines.size()] );
    }

    protected void FdrawDetails( IHymnDrawer drawer, SikherLine line ) throws Exception {
        String _details = FgetLineDetails( line.getHymn(), line.getIndex() );
        drawer.drawLine( _details, line, null );
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

    public void setViewType( boolean viewType ) {
        FViewType = viewType;
        FupdateContent( FSimpleDrawer );
    }

    public boolean isViewType() {
        return FViewType;
    }

    public void setShow( GurbaniType type, boolean show ) {
        int _index = FgetIndexByType( type );
        if( _index >= 0 ){
            FTypeElems[_index].setShow( show );
            FupdateContent( FSimpleDrawer );
        }
    }

    public boolean getShow(GurbaniType type) {
        int _index = FgetIndexByType( type );
        if( _index >= 0 ){
            return FTypeElems[_index].isShow();
        }
        return false;
    }

    public String copyAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String copyGurbani() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String copyTranslit() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String copyTranslation() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String copyDetails() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Document createDocument() {
        return new com.lowagie.text.Document( PageSize.A4, 30, 30, 30, 30 );
    }

    public void exportPdf( Document doc ) throws DocumentException {
        FupdateContent( new PdfDrawer( doc ) );
    }

    public interface IHymnDrawer{
        public void clearText() throws Exception;
        public void drawLine( String text, SikherLine line ,GurbaniType type ) throws Exception;
        public void drawSeparator()throws Exception;
    }

    private class PdfDrawer implements IHymnDrawer{
        private Document _FDoc = null;
        private Paragraph _FParagraph = null;
        private com.lowagie.text.Font[] _FFonts = new Font[FTypeElems.length];

        public PdfDrawer( Document doc ){
            _FDoc = doc;
            for( int i=0; i<FTypeElems.length; i++ ){
                 _FFonts[i] = FSikherIni.getPdfFont( FTypeElems[i].getType(), FTypeElems[i].getLang(), FEditorName );
            }
        }

        public void clearText() throws Exception {
            //do nothing
        }

        public void drawLine( String text, SikherLine line, GurbaniType type ) throws DocumentException {
            if( _FDoc != null ){
                if( _FParagraph == null ){
                    _FParagraph = new Paragraph();
                    _FParagraph.setAlignment( Element.ALIGN_CENTER );
                    _FParagraph.setSpacingBefore( 15f );
                    _FParagraph.setSpacingAfter( 15f );

                }
                int _index = FgetIndexByType( type );
                if( _index >= 0 ){
                    com.lowagie.text.Font _font = _FFonts[_index];
                    Phrase _ph;
                    if( _font != null ){
                        _ph = new Phrase( text + '\n', _font );
                    }else{
                        _ph = new Phrase( text + '\n' );
                    }
                    _FParagraph.add( _ph );
                }
            }
        }

        public void drawSeparator() throws Exception {
            if( _FParagraph != null ){
                _FDoc.add( _FParagraph );
                _FParagraph = null;
            }
        }
    }

    protected class TypeElem{
        private GurbaniType _FType;
        private boolean _FShow = true;
        private ISOLang _FLang = null;

        public TypeElem( GurbaniType type ){
            setType( type );
        }

        public GurbaniType getType() {
            return _FType;
        }

        public void setType(GurbaniType type) {
            _FType = type;
        }

        public boolean isShow() {
            return _FShow;
        }

        public void setShow(boolean show) {
            _FShow = show;
        }

        public ISOLang getLang() {
            return _FLang;
        }

        public void setLang(ISOLang lang) {
            _FLang = lang;
        }
    }
}
