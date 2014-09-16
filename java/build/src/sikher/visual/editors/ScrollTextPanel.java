package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.SikherLine;
import sikher.templates.PausedThread;
import sikher.templates.IThreadStatusListener;
import sikher.visual.forms.SikherProperties;

import javax.swing.*;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.font.LineBreakMeasurer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 05.02.2006
 * Time: 11:10:48
 * $Header$
 * $Log$
 * Description:
 */
public class ScrollTextPanel extends JPanel implements ISikherDataModel {

    private ScrollingThread _FThread;
    private JLabel jl_Page;
    private int _FPage = -1;
    private TextScrollPanel _FPnlScroll;
    private final Object SYNC = new Object();

    public ScrollTextPanel(){
        super( new BorderLayout() );

        //init components
        JPanel _pagePanel = new JPanel( new FlowLayout() );
        jl_Page = new JLabel();
        setPage( _FPage );
        jl_Page.setFont( jl_Page.getFont().deriveFont( 18f ) );
        _pagePanel.add( jl_Page );
        add( _pagePanel, BorderLayout.NORTH );
        _FPnlScroll = new TextScrollPanel();
        add( _FPnlScroll, BorderLayout.CENTER );

        //init attributes
        Font _font = getFont().deriveFont( Font.BOLD, 18 );
        _FGurbaniAttrs.put( TextAttribute.FONT, new Font( "AmnolUni", Font.BOLD, 18 ) );
        _FGurbaniAttrs.put( TextAttribute.FOREGROUND, Color.blue );
        _FTranslitAttrs.put( TextAttribute.FONT, _font );
        _FTranslitAttrs.put( TextAttribute.FOREGROUND, Color.red );
        _FTranslationAttrs.put( TextAttribute.FONT, _font );
        _FTranslationAttrs.put( TextAttribute.FOREGROUND, Color.black );
        _FDetailsAttrs.put( TextAttribute.FONT, _font );
        _FDetailsAttrs.put( TextAttribute.FOREGROUND, Color.gray );


        //init thread
        _FThread = new ScrollingThread();
        _FThread.setSleepTime( 100 );
        _FThread.pauseIt();
        _FThread.startIt();
    }

    public void stopScrolling(){
        _FThread.pauseIt();
        //rewind to the beginning
        synchronized( SYNC ){
            _FY = 0;
            _FPnlScroll.repaint();
        }
    }

    public void pauseScrolling(){
        _FThread.pauseIt();
    }

    public void startScrolling(){
        _FThread.resumeIt();
    }

    public void prevPage(){
        synchronized( SYNC ){
            if( _FPage > 0 ){
                ArrayList<Integer> _keys = new ArrayList<Integer>( _FPageMap.keySet() );
                int _index = _keys.indexOf( _FPage ) - 1;
                if( _index >= 0 ){
                    _FPage = _keys.get( _index );
                    _FY = _FPageMap.get( _FPage );
                    setPage( _FPage );
                    repaint();
                }
            }
        }
    }

    public void nextPage(){
        synchronized( SYNC ){
            if( _FPage > 0 ){
                ArrayList<Integer> _keys = new ArrayList<Integer>( _FPageMap.keySet() );
                int _index = _keys.indexOf( _FPage ) + 1;
                if( _index < _keys.size() ){
                    _FPage = _keys.get( _index );
                    _FY = _FPageMap.get( _FPage );
                    setPage( _FPage );
                    repaint();
                }
            }
        }
    }

    public void gotoPage( int page ){
        //TODO
    }

    public void setPace( int pace ){
        //TODO:
    }

    public int getPace(){
        return -1; //TODO
    }

    public void killThread(){
        _FThread.stopIt();
    }

    public void addThreadListener( IThreadStatusListener listener ){
        _FThread.addThreadStatusListener( listener );
    }

    public void removeThreadListener( IThreadStatusListener listener ){
        _FThread.removeThreadStatusListener( listener );
    }

    public void setPage( int page ){
        _FPage = page;
        if( page < 1 ){
            jl_Page.setText( "--- page <Unknown> ---");
        }else{
            jl_Page.setText( "--- page " + page + " ---");
        }
    }

    private ArrayList<ScrollingString> _FStyledText = new ArrayList<ScrollingString>();
    private TreeMap<Integer, Integer> _FPageMap = new TreeMap<Integer, Integer>();
    private int _FY = 0;
    private Image _FBuffer = null;

    private class TextScrollPanel extends JPanel{
        private int _FPos = 0;
        private TreeMap<Integer, Integer>_FPosMap = new TreeMap<Integer, Integer>();
        private ArrayList<Integer> _FPosKeyList = new ArrayList<Integer>();
        private int _FPosIndex = -1;

        public TextScrollPanel(){
            setBackground( UIManager.getColor("window") );
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            int _drawWidth = getWidth();
            int _drawHeight = getHeight();
            float _wrappingWidth = getSize().width - 40;

            if( !_FStyledText.isEmpty() && ( _FBuffer == null || _drawWidth != _FBuffer.getWidth( null ) ) ){

                _FPos = 0;

                //draw
                FontRenderContext _frc = new FontRenderContext( null, true, false );
                TextLayout _layout = null;
                int _height = 0;

                for( int i = 0; i < _FStyledText.size(); i++ ){
                    ScrollingString _string = _FStyledText.get( i );
                    LineBreakMeasurer _measurer = new LineBreakMeasurer( _string.getIterator(), _frc );
                    while ( _measurer.getPosition() < _string.getText().length() ) {
                        _layout = _measurer.nextLayout(_wrappingWidth );
                        _height += _layout.getAscent() + _layout.getDescent() + _layout.getLeading();

                    }
                    _height += 10;
                }

                _FBuffer = createImage( _drawWidth, _height );
                Graphics2D _g2d = (Graphics2D)_FBuffer.getGraphics();
                _g2d.setColor( UIManager.getColor("window") );
                _g2d.fillRect( 0, 0, _drawWidth, _height );
                Point _pen = new Point( 10, 0 );

                _FPageMap.clear();
                _FPosMap.clear();
                _FPosKeyList.clear();
                int _page = -1;

                for( int i = 0; i < _FStyledText.size(); i++ ){
                    ScrollingString _string = _FStyledText.get( i );
                    LineBreakMeasurer _measurer = new LineBreakMeasurer( _string.getIterator(), _frc );

                    while ( _measurer.getPosition() < _string.getText().length() ) {
                        _layout = _measurer.nextLayout(_wrappingWidth );

                        if( _page != _string.getPage() ){
                            _page = _string.getPage();
                            _FPageMap.put( _page, _pen.y );
                            _FPosMap.put( _pen.y, _page );
                        }
                        _pen.y += _layout.getAscent();
                        float _dy = _layout.getDescent() + _layout.getLeading();
                        _pen.x = (int)(_drawWidth/2 - _layout.getBounds().getWidth()/2);
                        _layout.draw( _g2d, _pen.x , _pen.y );
                        _pen.y += _dy;
                    }
                    _pen.y += 10;
                }
                _FPosKeyList.addAll( _FPosMap.keySet() );
                _FPosIndex = _FPosKeyList.size() > 0 ? 0 : -1;
            }

            if( _FBuffer != null ){
                synchronized( SYNC ){

                    if( _FPosIndex >= 0 ){
                        int _pos = _FPosKeyList.get( _FPosIndex );
                        if( _pos - _FY == 0 ){
                            _FPosIndex = _FPosKeyList.size() - 1 > _FPosIndex ? _FPosIndex + 1 : -1;
                            int _page = _FPosMap.get( _pos );
                            setPage( _page );
                        }
                    }

                    _FBuffer.getGraphics().clipRect( 0, -_FY, _drawWidth, _drawHeight );
                    g.drawImage( _FBuffer, 0, -_FY , null );

                    if( _FY > _FBuffer.getHeight( null ) ){
                        pauseScrolling();
                    }
                }
            }
        }
    }

    private class ScrollingThread extends PausedThread{
        public ScrollingThread() {
            super("Scrolling Thread");
        }

        public void init() {}

        public void process() {
            _FY++;
            repaint();
        }

        public void done() {}
    }

    private Hymn[] _FHymns = {};
    private ISOLang _FGurbani = null;
    private ISOLang _FTranslit = null;
    private ISOLang _FTranslation = null;
    private boolean _FShowGurbani = false;
    private boolean _FShowTranslit = false;
    private boolean _FShowTranslation = false;
    private boolean _FShowDetails = false;

    //attributes
    private HashMap<TextAttribute, Object> _FGurbaniAttrs = new HashMap<TextAttribute, Object>();
    private HashMap<TextAttribute, Object> _FTranslitAttrs = new HashMap<TextAttribute, Object>();
    private HashMap<TextAttribute, Object> _FTranslationAttrs = new HashMap<TextAttribute, Object>();
    private HashMap<TextAttribute, Object> _FDetailsAttrs = new HashMap<TextAttribute, Object>();

    public Hymn[] getHymns() {
        return _FHymns;
    }

    public void setHymns(Hymn[] hymns) {
        synchronized( SYNC ){
            stopScrolling();
            _FHymns = hymns;
            _FStyledText.clear();
            _FBuffer = null;
        }

        if( _FHymns == null || _FHymns.length == 0 ) return;

        ISOLang _gurbani = getGurbani();
        ISOLang _translit = getTranslit();
        ISOLang _translation = getTranslation();
        if( _gurbani == null || _translit == null || _translation == null ) return;

        //parse to lines
        for( Hymn _hymn : _FHymns ){
            SikherLine[] _lines = _hymn.getLines();
            for( SikherLine _line : _lines ){
                _FStyledText.add(
                        new ScrollingString( _line.getText( _gurbani.getCode(), Hymn.TYPE_GURBANI ),
                                _FGurbaniAttrs, _hymn.getPage() ) );
                _FStyledText.add(
                        new ScrollingString( _line.getText( _translit.getCode(), Hymn.TYPE_TRANSLIT ),
                                _FTranslitAttrs, _hymn.getPage() ) );
                _FStyledText.add( new ScrollingString(
                        _line.getText( _translation.getCode(), Hymn.TYPE_TRANSLATION ),
                        _FTranslationAttrs, _hymn.getPage() ) );
                String _details =
                        "["+ SikherProperties.getString("doc.sikher.hymn") + ": " + _hymn.getNumber() + "] " +
                        "["+ SikherProperties.getString("doc.sikher.page") + ": " + _hymn.getPage() + "] " +
                        "["+ SikherProperties.getString("doc.sikher.author") + ": " + _hymn.getAuthor() + "] " +
                        "["+ SikherProperties.getString("doc.sikher.melody") + ": " + _hymn.getMelody() + "]" +
                        "["+ SikherProperties.getString("doc.sikher.line") + ": " + _line.getIndex() + "] ";
                _FStyledText.add( new ScrollingString( _details, _FDetailsAttrs, _hymn.getPage() ) );
            }
        }
        repaint();
    }

    private class ScrollingString extends AttributedString{

        private String _FText = "";
        private int _FPage;

        public ScrollingString( String text, Map<? extends AttributedCharacterIterator.Attribute, ?> attributes, int page ) {
            super( text, attributes );
            _FText = text;
            _FPage = page;
        }

        public String getText(){
            return _FText;
        }

        public int getPage() {
            return _FPage;
        }
    }

    public ISOLang getGurbani() {
        return _FGurbani;
    }

    public void setGurbani(ISOLang gurbani) {
        _FGurbani = gurbani;
    }

    public ISOLang getTranslit() {
        return _FTranslit;
    }

    public void setTranslit(ISOLang translit) {
        _FTranslit = translit;
    }

    public ISOLang getTranslation() {
        return _FTranslation;
    }

    public void setTranslation(ISOLang translation) {
        _FTranslation = translation;
    }

    public void setViewType(boolean viewType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isViewType() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean isShowGurbani() {
        return _FShowGurbani;
    }

    public void setShowGurbani(boolean showGurbani) {
        _FShowGurbani = showGurbani;
    }

    public boolean isShowTranslit() {
        return _FShowTranslit;
    }

    public void setShowTranslit(boolean showTranslit) {
        _FShowTranslit = showTranslit;
    }

    public boolean isShowTranslation() {
        return _FShowTranslation;
    }

    public void setShowTranslation(boolean showTranslation) {
        _FShowTranslation = showTranslation;
    }

    public boolean isShowDetails() {
        return _FShowDetails;
    }

    public void setShowDetails(boolean showDetails) {
        _FShowDetails = showDetails;
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void export(Document doc, FontSelector fs) throws DocumentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
