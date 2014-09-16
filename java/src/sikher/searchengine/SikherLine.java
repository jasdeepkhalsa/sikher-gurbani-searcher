package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 07.12.2005
 * Time: 16:25:26
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/SikherLine.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: SikherLine.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class SikherLine extends LangTypeMap{

    private Hymn _FHymn = null;
    private int _FIndex = 0;

    public SikherLine( Hymn hymn, int index ){
        _FHymn = hymn;
        _FIndex = index;
    }

    public int getIndex() {
        return _FIndex;
    }

    public Hymn getHymn() {
        return _FHymn;
    }
}
