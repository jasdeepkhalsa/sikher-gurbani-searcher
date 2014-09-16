package sikher.visual.forms;

import sikher.templates.SplashScreen;
import sikher.visual.forms.AppFrame;
import sikher.visual.forms.SikherProperties;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 11:42:15
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/SikherSplashScreen.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherSplashScreen.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SikherSplashScreen extends SplashScreen {
    /**
     * Constructor
     *
     * @param frame - application frame
     */
    public SikherSplashScreen(AppFrame frame) {
        super(frame, SikherProperties.createImageIcon( SikherProperties.SPLASH_IMAGE ), true);
    }

    /**
     * Create GUI components for frame
     * @param progressBar
     */
    public void loadComponents( JProgressBar progressBar ){
        ((AppFrame)FFrame).loadComponents();
    }
}
