package sikher.searchengine;

import sikher.visual.forms.SikherProperties;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 15:39:37
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/GurbaniType.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: GurbaniType.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public enum GurbaniType {
    GURBANI,
    TRANSLITERATION,
    TRANSLATION,
    ALL_TYPES;

    public String xmlName(){
        return super.toString().toLowerCase();
    }

    public String toString(){
        String _value = super.toString();
        return SikherProperties.getString(_value, _value);
    }
}
