package sikher.visual.editors;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;
import sikher.searchengine.Hymn;
import sikher.visual.forms.SikherIni;
import sikher.visual.sidepanel.SikherComponent;
import sikher.visual.sidepanel.StatusBar;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 18:04:11
 * $Header$
 * $Log$
 * Description:
 */
public abstract class SikherEditor extends SikherComponent {
    public SikherEditor(StatusBar statusBar) {
        super(statusBar);
    }

    public SikherEditor(StatusBar statusBar, LayoutManager layout) {
        super(statusBar, layout);
    }

    /** Set/get content methods **/
    public abstract Hymn[] getHymns();
    public abstract void setHymns(Hymn[] hymns);

    /** Change property methods **/
    public static final String PROP_LANG_GURBANI = "lang_gurbani";
    public static final String PROP_LANG_TRANSLIT = "lang_translit";
    public static final String PROP_LANG_TRANSLATION = "lang_translation";

    public static final String PROP_SHOW_GURBANI = "show_gurbani";
    public static final String PROP_SHOW_TRANSLIT = "show_translit";
    public static final String PROP_SHOW_TRANSLATION = "show_translation";
    public static final String PROP_SHOW_DETAILS = "show_details";

    public static final String PROP_VIEW_MODE = "view_mode";

    public abstract void setProperty( String key, Object value );
    public abstract Object getProperty( String key );

    /** Export methods **/
    public abstract com.lowagie.text.Document createDocument();
    public abstract void export( com.lowagie.text.Document doc, FontSelector fs ) throws DocumentException;

    //TODO: add listener for different property changes
}
