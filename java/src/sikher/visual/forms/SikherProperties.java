package sikher.visual.forms;

import sikher.Main;

import javax.swing.*;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 12:03:02
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/SikherProperties.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherProperties.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SikherProperties{

    public static final String RESOURCES_FILE       = "resources.sikher";
    //frame
    public static final String SPLASH_IMAGE         = "splash.image";
    public static final String FRAME_TITLE          = "frame.title";

    public static final String MENU_FILE            = "menu.file";
    public static final String MENU_PRINTPREVIEW    = "menu.printpreview";
    public static final String MENU_PRINT           = "menu.print";
    public static final String MENU_EXIT            = "menu.exit";
    public static final String MENU_HELP            = "menu.help";
    public static final String MENU_ABOUT           = "menu.about";

    public static final String PANEL_SEARCH_NAME    = "panel.search.name";


    /**
     * Constructor
     */
    private SikherProperties() {
        super();
    }

    protected static SikherProperties _FOurInstance = new SikherProperties();
    private ResourceBundle _FBundle = null;

    public static SikherProperties getInstance() {
        return _FOurInstance;
    }

    /**
     * Creates an icon from an image contained in the "images" directory.
     */
    public static ImageIcon createImageIcon(String filename) {
        URL _url = Main.class.getResource( getString(filename) );
        return _url != null ? new ImageIcon(_url) : null;
    }

    /**
     * This method returns a string from the program's resource bundle.
     */
    public static String getString(String key) {
        String _value = null;
        try {
            _value = getInstance().getResourceBundle().getString(key);
        } catch (MissingResourceException e) {
            System.err.println("java.util.MissingResourceException: Couldn't find value for: " + key);
        }
        return _value;
    }

    /**
     * Returns a string value by given key
     * @param key - resource key
     * @param defaultValue - value to return when the key is not found
     * @return string value
     */
    public static String getString(String key, String defaultValue){
        String _value = getString(key);
        return _value != null ? _value : defaultValue;
    }

    public static String getString(String key, Object[] args){
        String _value = getString(key);
        if( _value != null ){
            MessageFormat _formatter = new MessageFormat(_value);
            _value = _formatter.format( args );
        }
        return _value;
    }

    /**
     * Returns the resource bundle associated with this program. Used
     * to get accessable and internationalized strings.
     */
    public ResourceBundle getResourceBundle() {
        if(_FBundle == null) {
            _FBundle = ResourceBundle.getBundle(RESOURCES_FILE);
        }
        return _FBundle;
    }

}
