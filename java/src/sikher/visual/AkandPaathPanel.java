package sikher.visual;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import sikher.templates.VisualUtils;
import sikher.templates.PausedThread;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 21.12.2005
 * Time: 15:10:59
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/AkandPaathPanel.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: AkandPaathPanel.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class AkandPaathPanel extends SikherTab{
    //TODO: stop when mouse is in the area

    public AkandPaathPanel(sikher.visual.forms.SikherIni prop) {
        super(prop, new BorderLayout());
        _FinitComponents();
    }

    private void _FinitComponents(){
        JButton _prevButton = new JButton(new PrevAction());
        JButton _playButton = new JButton(new PlayAction());
        JButton _pauseButton = new JButton(new PauseAction());
        JButton _stopButton = new JButton(new StopAction());
        JButton _nextButton = new JButton(new NextAction());
        JButton _gotoButton = new JButton(new GotoAction());
        JSlider _slider = new JSlider(1, 10);

        JButton[] _buttons = new JButton[]{
                _prevButton, _playButton, _pauseButton, _stopButton, _nextButton, _gotoButton
        };
        JPanel _panel = (JPanel) VisualUtils.getCommandRow(_buttons);
        _panel.add( new JLabel("Pace") );
        _panel.add(_slider);
        add( _panel, BorderLayout.SOUTH );

        DrawPanel _drawPanel = new DrawPanel();
        add( _drawPanel, BorderLayout.CENTER );
        _FScrollingThread = new ScrollingThread( _drawPanel );
    }

    private ScrollingThread _FScrollingThread;

    public void init(){
        _FScrollingThread.setSleepTime(100);
        _FScrollingThread.pauseIt();
        _FScrollingThread.start();
    }
    public void done(){
        _FScrollingThread.stopIt();
    }

    private class ScrollingThread extends PausedThread {
        private DrawPanel _FDrawPanel;

        public ScrollingThread( DrawPanel drawPanel  ) {
            super("text_scroller");
            _FDrawPanel = drawPanel;
        }

        public void init() {
            _FDrawPanel.reset();
        }

        public void process() {
            //todo: inc y here
            _FDrawPanel.repaint();
        }

        public void done() {
        }
    }

    private class DrawPanel extends JPanel{
        private int _FY = 0;

        public DrawPanel(){
            setOpaque( false );
            //setFont( getFont().deriveFont(18f) );
        }

        public void reset(){
            _FY = getHeight();
        }

        private ArrayList<String> _FLines = new ArrayList<String>();

        public void addParagraph( String text, Font font ){
            Font _font = font != null ? font : getFont();
            Rectangle2D _rect;
            /*
            LineBreakMeasurer _measurer = new LineBreakMeasurer();
            TextLayout _layout = new TextLayout();
            _rect = _font.getStringBounds()
            */
        }

        private class LineInfo{
            public String line;
            public Font font;
            public int height;
        }

        private String _FText = "this is some text that should be displayed. " +
                "it is very long and should be broken into several lines." +
                "maybe in two lines";
        private HashMap<TextAttribute, Object> _FAttrMap =
                new HashMap<TextAttribute, Object>();
        {
            //_FAttrMap.put( TextAttribute.SIZE, 18f );
            _FAttrMap.put( TextAttribute.FONT, new Font("arial", Font.BOLD, 18) );
        }
        private AttributedString _FStyledText = new AttributedString(_FText, _FAttrMap);

        public void paintComponent(Graphics g){
            //g.clearRect(0, 0, getWidth(), getHeight()) ;
            super.paintComponent(g);
            Point pen = new Point(10, _FY--);
            Graphics2D g2d = (Graphics2D)g;
            FontRenderContext frc = g2d.getFontRenderContext();

            // let styledText be an AttributedCharacterIterator containing at least
            // one character
            LineBreakMeasurer measurer = new LineBreakMeasurer(_FStyledText.getIterator(), frc);
            float wrappingWidth = getSize().width - 15;

            TextLayout layout = null;
            float _height = 0f;
            while (measurer.getPosition() < _FText.length()) {

                layout = measurer.nextLayout(wrappingWidth);
                _height +=layout.getBounds().getHeight();

                pen.y += (layout.getAscent());
                float dx = layout.isLeftToRight() ?
                    0 : (wrappingWidth - layout.getAdvance());

                layout.draw(g2d, pen.x + dx, pen.y);
                pen.y += layout.getDescent() + layout.getLeading();
            }
            if( layout != null && _FY < -1f*_height ){
                reset();
            }

            /*
            Graphics _g = g.create();
            _g.clearRect(0, 0, getWidth(), getHeight()) ;
            //g.setFont( g.getFont().deriveFont(Font.BOLD, 12f) );
            ((Graphics2D)_g).drawGlyphVector(
                    new StandardGlyphVector(Font.decode("AnmolUni").deriveFont(Font.BOLD, 18f), "\u0a05",
                            new FontRenderContext(null, false, false)), 20f, _FY);
            //g.drawString("i'm scrolling!\u0a05", 20 ,_FY);
            _g.dispose();
            _FY--;
            if( _FY < 0 ){
                reset();
            } */
        }
    }

    private class PrevAction extends AbstractAction{
        public PrevAction(){
            putValue(NAME, "Prev");
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class PlayAction extends AbstractAction{
        public PlayAction(){
            putValue(NAME, "Play");
        }

        public void actionPerformed(ActionEvent e) {
            _FScrollingThread.resumeIt();
        }
    }

    private class PauseAction extends AbstractAction{
        public PauseAction(){
            putValue(NAME, "Pause");
        }

        public void actionPerformed(ActionEvent e) {
            _FScrollingThread.pauseIt();
        }
    }

    private class StopAction extends AbstractAction{
        public StopAction(){
            putValue(NAME, "Stop");
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class NextAction extends AbstractAction{
        public NextAction(){
            putValue(NAME, "Next");
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class GotoAction extends AbstractAction{
        public GotoAction(){
            putValue(NAME, "Go to");
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    public boolean supportsExport() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void export(Document doc) throws DocumentException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /*
    public AkandPaathPanel( SikherIni ini ){
        super( ini, new BorderLayout() );
        _FinitComponents();
    }

    private void _FinitComponents(){
        JPanel _panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        add( _panel , BorderLayout.NORTH );

        ScrollablePanel _scrollpanel = new ScrollablePanel();
        add( _scrollpanel, BorderLayout.CENTER );

        JButton _button = new JButton( new ScrollAction( _scrollpanel ) );
        _panel.add( _button );
    }

    private class ScrollAction extends AbstractAction{
        private ScrollablePanel _FPanel;
        private boolean _FStarted = false;

        public ScrollAction( ScrollablePanel panel ){
            _FPanel = panel;
            putValue( NAME, SikherProperties.getString("panel.akandpaath.button.start") );
        }

        public void actionPerformed(ActionEvent e) {
            if( _FStarted ){
                _FPanel.stopScrolling();
            }else{
                _FPanel.startScrolling();
            }
            _FStarted = !_FStarted;
        }
    }

    private class ScrollablePanel extends JPanel implements Runnable{

        private Thread _FThread;
        private int _FY = -1;
        private boolean _FPaused;

        public ScrollablePanel(){
            super();
            _FThread = new Thread(this);
            _FPaused = true;
            _FThread.start();
        }

        public void run() {
            Thread me = Thread.currentThread() ;
            while ( _FThread == me && _FY !=0  ){
                try{
                    Thread.currentThread().sleep(40) ;
                }
                catch(InterruptedException e){
                    break ;
                }
                if( !_FPaused ){
                    repaint() ;
                }
            }
        }

        public void startScrolling(){
            _FPaused = false;
            if( _FY < 0 ){
                _FY = getHeight();
            }
        }

        public void stopScrolling(){
            _FPaused = true;
        }

        public void paint(Graphics g){
            //((Graphics2D)g).setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
            g.clearRect(0,0,getWidth(),getHeight()) ;
            g.setFont( g.getFont().deriveFont(Font.BOLD, 16f) );
            g.drawString("i'm scrolling!", 20 ,_FY);
            _FY--;
        }

    }

    /*public class MyPanel extends JPanel implements Runnable{

        Thread thread ;
        int x ;

        public MyPanel(){
            super();
            x=getWidth() ;
            thread = new Thread(this) ;
            thread.start() ;
        }

        public void run(){
            Thread me = Thread.currentThread() ;
            while (thread == me){
                try{
                    Thread.currentThread().sleep(5) ;
                }
                catch(InterruptedException e){
                    break ;
                }
            repaint() ;
                }
        }

        public void paint(Graphics g){
            g.clearRect(0,0,getWidth(),getHeight()) ;
            g.drawString("Hello",x,100);
            x--;
        }
    }*/
    /*override the paintComponent or paint method.*/

}
