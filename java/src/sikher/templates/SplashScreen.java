package sikher.templates;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 11:24:47
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/templates/SplashScreen.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: SplashScreen.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description: show splash screen and load frame meanwhile
 */
public class SplashScreen {

    private JWindow _FSplashScreen = null;
    protected JFrame FFrame = null;
    private JProgressBar _FProgressBar = null;
    private Icon _FIcon = null;
    private boolean _FUseProgressbar = false;
    private long _FSleepTime = 2000;

    /**
     * Constructor
     * @param frame - application frame
     * @param icon - icon to be shown
     */
    public SplashScreen( JFrame frame, Icon icon, boolean useProgressbar ){
        _FIcon = icon;
        FFrame = frame;
        _FUseProgressbar = useProgressbar;
    }

    /**
     * Show GUI.
     * Show splash screen while loading the application.
     */
    public void showGUI() {
        createSplashScreen();

        //show splash screen
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showSplashScreen();
            }
        });
        long _start = System.currentTimeMillis();
        loadComponents(_FProgressBar);
        long _end = System.currentTimeMillis();

        long _sleep = _FSleepTime - (_end - _start);
        if ( _sleep > 0 ){
            try {
                Thread.sleep(_sleep);
            } catch (InterruptedException e) {
                System.err.print(e);
            }
        }

        //hide splash screen
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                showApp();
                hideSplash();
            }
        });
    }

    /**
     * Create splash screen GUI
     */
    public void createSplashScreen() {
        JLabel _splashLabel = new JLabel();
        Icon _ico = _FIcon;
        if ( _ico != null ){
            _splashLabel.setIcon( _ico );
        }

        _FSplashScreen = new JWindow(FFrame);
        //_FSplashScreen.setAlwaysOnTop( true );
        _FSplashScreen.setLayout( new BorderLayout(4, 4) );
        _FSplashScreen.getContentPane().add( _splashLabel, BorderLayout.CENTER );
        if ( isProgressbarUsed() ){
            _FProgressBar = new JProgressBar();
            _FSplashScreen.getContentPane().add( _FProgressBar, BorderLayout.SOUTH );
        }
        _FSplashScreen.pack();
        Rectangle _screenRect = FFrame.getGraphicsConfiguration().getBounds();
        _FSplashScreen.setLocation(
                _screenRect.x + _screenRect.width/2 - _FSplashScreen.getSize().width/2,
                _screenRect.y + _screenRect.height/2 - _FSplashScreen.getSize().height/2);
    }

    /**
     * Show the splash screen
     */
    public void showSplashScreen() {
        _FSplashScreen.setVisible(true);
    }

    /**
     * Pop down the spash screen
     */
    public void hideSplash() {
        _FSplashScreen.setVisible(false);
        _FSplashScreen = null;
    }

    /**
     * Show application form
     */
    public void showApp(){
        FFrame.setVisible(true);
    }

    /**
     * Override to load components from frame
     * @param progressBar - splash screen progressbar.
     * Parameter useProgressbar should be true to use progressbar.
     */
    public void loadComponents( JProgressBar progressBar ){
    }

    public boolean isProgressbarUsed(){
        return _FUseProgressbar;
    }

    public long getSleepTime() {
        return _FSleepTime;
    }

    public void setSleepTime(long sleepTime) {
        _FSleepTime = sleepTime;
    }
}
