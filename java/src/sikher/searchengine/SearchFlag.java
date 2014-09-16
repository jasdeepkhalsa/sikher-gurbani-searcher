package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 05.12.2005
 * Time: 20:05:29
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/SearchFlag.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: SearchFlag.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public enum SearchFlag {
    ALLWORDS,
    ANYWORD,
    NOWORDS,
    EXACTPHRASE,
    STARTSWITH;

    public String toString(){
        String _value = super.toString();
        return sikher.visual.forms.SikherProperties.getString(_value, _value);
    }
}
