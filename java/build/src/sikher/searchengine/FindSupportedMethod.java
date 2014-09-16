package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.*;

import sikher.visual.forms.SikherProperties;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 14.01.2006
 * Time: 13:15:43
 * $Header$
 * $Log$
 * Description:
 */
public class FindSupportedMethod extends FindMethod{
    private final String SEARCH_LANG_TYPE = "//xmlGurbani";
    private final String SEARCH_AUTHOR_RAAG = "//hymn";
    private final String SEARCH_PAGE = "//page";
    private final String SEARCH_BOOK = "//book";

    private TreeSet<ISOLang> _FGurbanies = new TreeSet<ISOLang>();
    private TreeSet<ISOLang> _FTranslations = new TreeSet<ISOLang>();
    private TreeSet<ISOLang> _FTransliterations = new TreeSet<ISOLang>();

    private TreeMap<Integer, NumberedElement> _FScriptures = new TreeMap<Integer, NumberedElement>();
    private TreeMap<Integer, NumberedElement> _FAuthors = new TreeMap<Integer, NumberedElement>();
    private TreeMap<Integer, NumberedElement> _FRaags = new TreeMap<Integer, NumberedElement>();

    private int _FMinPage = 1;
    private int _FMaxPage = 1;

    public FindSupportedMethod(Logger logger) {
        super(logger);

        //add default book values
        _FScriptures.put( SikherReserved.ALL_BOOKS,
                new NumberedElement(SikherReserved.ALL_BOOKS, SikherProperties.getString("ALL_BOOKS")) );

        _FAuthors.put( SikherReserved.ALL_AUTHORS,
                new AuthorElement(SikherReserved.ALL_AUTHORS, sikher.visual.forms.SikherProperties.getString("ALL_AUTHORS")) );
        _FRaags.put( SikherReserved.ALL_RAAGS,
                new NumberedElement(SikherReserved.ALL_RAAGS, sikher.visual.forms.SikherProperties.getString("ALL_RAAG")) );
    }

    public void find(XPath xpath, NodeInfo doc) {

        //grab lang info
        NodeInfo[] _nodes = searchXPath2( xpath, doc, SEARCH_LANG_TYPE );
        for( NodeInfo _node : _nodes ){
            String _iso = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "language") );
            String _type = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "type") );
            if ( _iso == null || _type == null ){
                FLogger.log( Level.WARNING, "error.no-type-lang" );
                continue;
            }
            if ( _iso.length() < 2 ){
                FLogger.log( Level.WARNING, "error.bad-iso" );
                continue;
            }
            String _lang = _iso.substring(0, 2).toUpperCase();
            ISOLang _isoLang;
            try{
                _isoLang = ISOLang.valueOf(_lang);
            }catch(IllegalArgumentException e){
                FLogger.log( Level.WARNING, "exception.bad-iso", e );
                continue;
            }
            if( _type.equalsIgnoreCase("gurbani") ){
                if( !_FGurbanies.contains(_isoLang) ){
                    _FGurbanies.add(_isoLang);
                }
            }else if( _type.equalsIgnoreCase("translation") ){
                if( !_FTranslations.contains(_isoLang) ){
                    _FTranslations.add(_isoLang);
                }
            }else if( _type.equalsIgnoreCase("transliteration") ){
                if( !_FTransliterations.contains(_isoLang) ){
                    _FTransliterations.add(_isoLang);
                }
            }
        }

        _FfindElements( xpath, doc, SEARCH_BOOK, "number", "book", _FScriptures );
        _FfindElements( xpath, doc, SEARCH_AUTHOR_RAAG, "author", "author", _FAuthors );
        _FfindElements( xpath, doc, SEARCH_AUTHOR_RAAG, "melody", "raag", _FRaags );

        //find min/max page
        _nodes = searchXPath2( xpath, doc, SEARCH_PAGE );
        for(NodeInfo _node : _nodes){
            String _page = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );
            if ( _page != null ){
                try{
                    int _number = Integer.valueOf(_page);
                    if( _number > _FMaxPage ){
                        _FMaxPage = _number;
                    }else if ( _number < _FMinPage ){
                        _FMinPage = _number;
                    }
                }catch(NumberFormatException ne){
                     FLogger.log( Level.WARNING, "exception.pagenumber", ne );
                }
            }
        }

    }

    private void _FfindElements( XPath xpath, NodeInfo doc, String expr, String attr,
                                       String type, TreeMap<Integer, NumberedElement> map ){
        NodeInfo[] _nodes = searchXPath2( xpath, doc, expr );
        for( NodeInfo _node : _nodes ){
            String _value = _node.getAttributeValue( _node.getNamePool().getFingerprint("", attr) );
            if( _value != null ){
                try{
                    int _number = Integer.valueOf(_value);
                    if( _number >= 1 ){
                        NumberedElement _elem = _FcreateElement( type, _number );
                        if( _elem != null ){
                            map.put( _number, _elem );
                        }
                    }else{
                        FLogger.log( Level.WARNING, "error." + type + "number" );
                    }
                }catch(NumberFormatException ne){
                    FLogger.log( Level.WARNING, "exception." + type + "number", ne );
                }
            }
        }
    }


    private NumberedElement _FcreateElement( String type, int number ){
        if( type.equalsIgnoreCase("book") ){
            return new NumberedElement( number, sikher.visual.forms.SikherProperties.getString("DEFAULT_BOOK") + number );
        }else if( type.equalsIgnoreCase("author") ){
            return new AuthorElement( number, sikher.visual.forms.SikherProperties.getString("DEFAULT_AUTHOR") + number );
        }else if( type.equalsIgnoreCase("raag") ){
            return new NumberedElement( number, sikher.visual.forms.SikherProperties.getString("DEFAULT_RAAG") + number );
        }
        return null;
    }

    public ISOLang[] getGurbanies() {
        return _FGurbanies.toArray( new ISOLang[_FGurbanies.size()] );
    }

    public ISOLang[] getTranslations() {
        return _FTranslations.toArray( new ISOLang[_FTransliterations.size()] );
    }

    public ISOLang[] getTransliterations() {
        return _FTransliterations.toArray( new ISOLang[_FTransliterations.size()] );
    }

    public ISOLang[] getLanguages() {
        TreeSet<ISOLang> _set = new TreeSet<ISOLang>();
        _set.addAll( _FGurbanies );
        _set.addAll( _FTranslations );
        _set.addAll( _FTransliterations );
        return _set.toArray( new ISOLang[_set.size()] );
    }

    public TreeMap<Integer, NumberedElement> getScriptures() {
        return _FScriptures;
    }

    public TreeMap<Integer, NumberedElement> getAuthors() {
        return _FAuthors;
    }

    public TreeMap<Integer, NumberedElement> getRaags() {
        return _FRaags;
    }

    public int getMinPage() {
        return _FMinPage;
    }

    public int getMaxPage() {
        return _FMaxPage;
    }
}
