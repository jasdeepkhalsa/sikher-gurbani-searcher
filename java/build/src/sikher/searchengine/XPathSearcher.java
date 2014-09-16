package sikher.searchengine;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 17:32:16
 * $Header$
 * $Log$
 * Description:
 */
public class XPathSearcher {

    private String _FDirectory = null;
    private Logger _Flogger = null;

    private TreeMap<Integer, NumberedElement> _FBooks = new TreeMap<Integer, NumberedElement>();
    private TreeMap<Integer, NumberedElement> _FAuthors = new TreeMap<Integer, NumberedElement>();
    private TreeMap<Integer, NumberedElement> _FRaags = new TreeMap<Integer, NumberedElement>();
    private int _FMinPage;
    private int _FMaxPage;

    private GurbaniType[] _FTypes = {};
    private ISOLang[] _FGurbanies = {};
    private ISOLang[] _FTranslations = {};
    private ISOLang[] _FTransliterations = {};

    private ArrayList<SearcherListener> _FListeners = new ArrayList<SearcherListener>();

    /**
     * Constructor
     */
    public XPathSearcher( Logger logger ){   //TODO: advance logging
        _Flogger = logger;
    }

    public void setDirectory( String directory  ){
        _FDirectory = directory;

        FindSupportedMethod _method = new FindSupportedMethod( _Flogger );
        _method.iterateFiles( directory );

        _FGurbanies = _method.getGurbanies();
        _FTranslations = _method.getTranslations();
        _FTransliterations = _method.getTransliterations();
        _FTypes = GurbaniType.values();

        _FBooks = _method.getScriptures();
        _FAuthors = _method.getAuthors();
        _FRaags = _method.getRaags();
        _FMinPage = _method.getMinPage();
        _FMaxPage = _method.getMaxPage();

        //load names
        LoadNamesMethod _loadNamesMethod = new LoadNamesMethod( _Flogger, getBooks(), getAuthors(), getRaags() );
        _loadNamesMethod.iterateFiles( directory );

        //fire event
        fireInfoUpdated();
    }

    public String getDirectory(){
        return _FDirectory;
    }

    public Hymn[] searchHymns(String text, SearchFlag searchflag, GurbaniType type,
                              int book, int writer, int raag, int startpage, int endpage){
        FindHymnsMethod _findHymnsMethod =
                new FindHymnsMethod( _Flogger, text, searchflag, type, book, writer, raag, startpage, endpage,
                        _FBooks, _FAuthors, _FRaags );
        _findHymnsMethod.iterateFiles( _FDirectory );
        Hymn[] _hymns = _findHymnsMethod.getHymns();
        new FindOtherMethod( _Flogger, _hymns ).iterateFiles( _FDirectory );
        return _hymns;
    }

    public void loadHymn( Hymn hymn ){
        new LoadHymnMethod( _Flogger, hymn ).iterateFiles( _FDirectory );
    }

    public void addSearcherListener( SearcherListener listener ){
        if( !_FListeners.contains(listener) ){
            _FListeners.add( listener );
        }
    }

    public void removeSearchListener( SearcherListener listener ){
        _FListeners.remove( listener );
    }

    public void fireInfoUpdated(){
        for( SearcherListener _listener : _FListeners ){
            _listener.infoUpdated();
        }
    }

    public SearchFlag[] getFlags(){
        return SearchFlag.values();
    }

    public AuthorElement[] getAuthors() {
        return _FAuthors.values().toArray(new AuthorElement[_FAuthors.size()]);
    }

    public NumberedElement[] getRaags() {
        return _FRaags.values().toArray(new NumberedElement[_FRaags.size()]);
    }

    public GurbaniType[] getTypes() {
        return _FTypes;
    }

    public ISOLang[] getGurbanies() {
        return _FGurbanies;
    }

    public ISOLang[] getTranslations() {
        return _FTranslations;
    }

    public ISOLang[] getTransliterations() {
        return _FTransliterations;
    }

    public int getMinPage() {
        return _FMinPage;
    }

    public int getMaxPage() {
        return _FMaxPage;
    }

    public NumberedElement[] getBooks(){
        return _FBooks.values().toArray(new NumberedElement[_FBooks.size()]);
    }
}
