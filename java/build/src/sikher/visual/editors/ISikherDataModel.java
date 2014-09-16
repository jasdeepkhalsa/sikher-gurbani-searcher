package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.visual.forms.SikherIni;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:47:52
 * $Header$
 * $Log$
 * Description:
 */
public interface ISikherDataModel {
    public abstract Hymn[] getHymns();
    public abstract void setHymns(Hymn[] hymns);

    /*
    public abstract void setDisplayOptions(ISOLang gurbani, ISOLang translit, ISOLang translation,
                                           boolean showGurbani, boolean showTranslit,
                                           boolean showTranslation, boolean showDetails, boolean viewType);
    */
    public abstract void setGurbani(ISOLang gurbani);
    public abstract ISOLang getGurbani();
    public abstract void setTranslit(ISOLang translit);
    public abstract ISOLang getTranslit();
    public abstract void setTranslation(ISOLang translation);
    public abstract ISOLang getTranslation();

    public abstract void setViewType(boolean viewType);
    public abstract boolean isViewType();

    public abstract void setShowGurbani( boolean bShow );
    public abstract boolean isShowGurbani();
    public abstract void setShowTranslit( boolean bShow );
    public abstract boolean isShowTranslit();
    public abstract void setShowTranslation( boolean bShow );
    public abstract boolean isShowTranslation();
    public abstract void setShowDetails( boolean bShow );
    public abstract boolean isShowDetails();

    public abstract String copyAll();
    public abstract String copyGurbani();
    public abstract String copyTranslit();
    public abstract String copyTranslation();
    public abstract String copyDetails();

    public abstract com.lowagie.text.Document createDocument();
    public abstract void export( com.lowagie.text.Document doc, FontSelector fs ) throws DocumentException;
}
