package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 07.12.2005
 * Time: 16:25:26
 * $Header$
 * $Log$
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
