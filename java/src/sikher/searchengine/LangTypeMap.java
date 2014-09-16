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
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/LangTypeMap.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: LangTypeMap.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class LangTypeMap implements Serializable {
    private Map<String, String> _FTexts;

    public LangTypeMap(){
        _FTexts = new TreeMap<String, String>();
    }

    public void putText( ISOLang lang, GurbaniType type, String text ){
        _FTexts.put( _FgetKey( lang, type ), text );
    }

    public String getText( ISOLang lang, GurbaniType type ){
        String _text = _FTexts.get( _FgetKey( lang, type ) );
        if( _text == null ){
            _text = SikherProperties.getString( "text.notfound.default" );
        }
        return _text;
    }

    private String _FgetKey( ISOLang lang, GurbaniType type ){
        return "lang=" + lang.getCode() + ",type=" + type.toString().toLowerCase();
    }
}
