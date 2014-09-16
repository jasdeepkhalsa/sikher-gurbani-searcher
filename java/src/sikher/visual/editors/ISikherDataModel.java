package sikher.visual.editors;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.FontSelector;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 17:47:52
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ISikherDataModel.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ISikherDataModel.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public interface ISikherDataModel {
    public Hymn[] getHymns();
    public void setHymns(Hymn[] hymns);

    public void setGurbani(ISOLang gurbani);
    public ISOLang getGurbani();
    public void setTranslit(ISOLang translit);
    public ISOLang getTranslit();
    public void setTranslation(ISOLang translation);
    public ISOLang getTranslation();

    public void setViewType(boolean viewType);
    public boolean isViewType();

    public void setShowGurbani( boolean bShow );
    public boolean isShowGurbani();
    public void setShowTranslit( boolean bShow );
    public boolean isShowTranslit();
    public void setShowTranslation( boolean bShow );
    public boolean isShowTranslation();
    public void setShowDetails( boolean bShow );
    public boolean isShowDetails();

    public String copyAll();
    public String copyGurbani();
    public String copyTranslit();
    public String copyTranslation();
    public String copyDetails();

    public com.lowagie.text.Document createDocument();

    public void exportPdf( com.lowagie.text.Document doc,
                           com.lowagie.text.Font gurbaniFont,
                           com.lowagie.text.Font translitFont,
                           com.lowagie.text.Font translationFont,
                           com.lowagie.text.Font detailsFont )
            throws DocumentException; 
}
