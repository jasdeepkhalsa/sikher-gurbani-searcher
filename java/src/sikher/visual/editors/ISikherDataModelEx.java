package sikher.visual.editors;

import sikher.searchengine.ISOLang;
import sikher.searchengine.Hymn;
import sikher.searchengine.GurbaniType;
import com.lowagie.text.DocumentException;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 13.04.2006
 * Time: 12:28:24
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ISikherDataModelEx.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ISikherDataModelEx.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public interface ISikherDataModelEx {
    public Hymn[] getHymns();
    public void setHymns(Hymn[] hymns);

    public void setLang( GurbaniType type, ISOLang lang );
    public ISOLang getLang( GurbaniType type );

    public void setViewType(boolean viewType);
    public boolean isViewType();

    public void setShow( GurbaniType type, boolean show );
    public boolean getShow( GurbaniType type );

    public String copyAll();
    public String copyGurbani();
    public String copyTranslit();
    public String copyTranslation();
    public String copyDetails();

    public com.lowagie.text.Document createDocument();

    public void exportPdf( com.lowagie.text.Document doc ) throws DocumentException;
}
