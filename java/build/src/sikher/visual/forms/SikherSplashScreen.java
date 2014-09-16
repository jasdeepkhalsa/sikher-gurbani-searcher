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
 * $Header$
 * $Log$
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
