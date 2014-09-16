package sikher.searchengine;

import sikher.visual.forms.SikherProperties;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 11.12.2005
 * Time: 22:12:24
 * $Header$
 * $Log$
 * Description:
 */
public class LangTypeMap implements Serializable {
    public static final String TYPE_GURBANI     = "gurbani";
    public static final String TYPE_TRANSLIT    = "transliteration";
    public static final String TYPE_TRANSLATION = "translation";

    private Map<String, String> _FTexts;

    public LangTypeMap(){
        _FTexts = new TreeMap<String, String>();
    }

    public void putText( String lang, String type, String text ){
        _FTexts.put(_FgetKey(lang, type), text);
    }

    public String getText( String lang, String type ){
        String _text = _FTexts.get(_FgetKey(lang, type));
        if( _text == null ){
            _text = SikherProperties.getString("text.notfound.default");
        }
        return _text;
    }

    private String _FgetKey( String lang, String type ){
        return "lang=" + lang.toLowerCase() + ",type=" + type.toLowerCase();
    }
}
