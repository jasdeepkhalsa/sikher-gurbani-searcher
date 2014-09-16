package sikher.searchengine;

import sikher.visual.forms.SikherProperties;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 26.01.2006
 * Time: 15:39:37
 * $Header$
 * $Log$
 * Description:
 */
public enum GurbaniType {
    ALL_TYPES,
    GURBANI,
    TRANSLITERATION,
    TRANSLATION;

    public String xmlName(){
        return super.toString().toLowerCase();
    }

    public String toString(){
        String _value = super.toString();
        return SikherProperties.getString(_value, _value);
    }
}
