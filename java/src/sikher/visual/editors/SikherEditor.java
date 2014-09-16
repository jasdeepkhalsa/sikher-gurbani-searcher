package sikher.visual.editors;

import com.lowagie.text.DocumentException;
import sikher.searchengine.Hymn;
import sikher.searchengine.XPathSearcher;
import sikher.visual.forms.OptionsCard;
import sikher.visual.forms.SikherIni;
import sikher.visual.sidepanel.SikherComponent;
import sikher.visual.sidepanel.StatusBar;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 18:04:11
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/SikherEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public abstract class SikherEditor extends SikherComponent {
    public SikherEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ) {
        super( searcher, ini, statusBar );
    }

    public SikherEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar, LayoutManager layout ) {
        super( searcher, ini, statusBar, layout );
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

    public abstract void exportPdf( com.lowagie.text.Document doc ) throws DocumentException;

    /** Options **/
    public OptionsCard[] getOptionsCards(){
        return null;
    }

    //TODO: add listener for different property changes
}
