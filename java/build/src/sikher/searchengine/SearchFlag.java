package sikher.searchengine;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 05.12.2005
 * Time: 20:05:29
 * $Header$
 * $Log$
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
