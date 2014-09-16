package sikher.searchengine;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 29.11.2005
 * Time: 11:44:34
 * $Header$
 * $Log$
 * Description:
 */
public class Hymn extends LangTypeMap{
    private NumberedElement _FBook;
    private int _FPage;
    private int _FNumber;
    private AuthorElement _FAuthor;
    private NumberedElement _FMelody;
    private ISOLang _FLanguage;
    private String _FType;
    private Map<Integer, SikherLine> _FLines;
    private Map<Integer, SikherLine> _FExtraLines;

    private boolean _FLoaded;

    public Hymn( NumberedElement book, int page, int number,
                 AuthorElement author, NumberedElement melody, ISOLang language, String type ) {
        _FLines = new TreeMap<Integer, SikherLine>();
        _FExtraLines = new TreeMap<Integer, SikherLine>();
        _FBook = book;
        _FPage = page;
        _FNumber = number;
        _FAuthor = author;
        _FMelody = melody;
        _FLanguage = language;
        _FType = type;
        _FLoaded = false;
    }

    public void addLine(int number, String line){
        SikherLine _line = new SikherLine( this, number );
        _line.putText( getLanguage().getCode(), getType(), line );
        _FLines.put( number, _line );
    }

    public void addExtraLine(int number, String line, String lang, String type){
        if( _FLines.containsKey(number) ){
            return; //already loaded
        }
        SikherLine _line = _FExtraLines.get(number);
        if( _line == null ){
            _line = new SikherLine(this, number);
        }
        _line.putText( lang, type, line );
        _FExtraLines.put( number, _line );
    }

    public int getBookNumber() {
        return _FBook.getNumber();
    }

    public String getBook(){
        return _FBook.getValue();
    }

    public int getPage() {
        return _FPage;
    }

    public int getNumber() {
        return _FNumber;
    }

    public String getAuthor() {
        return _FAuthor.getValue();
    }

    public String getMelody() {
        return _FMelody.getValue();
    }

    public ISOLang getLanguage() {
        return _FLanguage;
    }

    public int getLinesCount(){
        return _FLines.size();
    }

    public SikherLine[] getLines(){
        return _FLines.values().toArray( new SikherLine[_FLines.size()] );
    }

    public SikherLine[] getHymn(){
        Map<Integer, SikherLine> _map = new TreeMap<Integer, SikherLine>();
        _map.putAll( _FLines );
        _map.putAll(_FExtraLines );
        return _map.values().toArray( new SikherLine[_map.size()] );
    }

    public String getType() {
        return _FType;
    }

    public boolean isLoaded() {
        return _FLoaded;
    }

    public void setLoaded(boolean loaded) {
        _FLoaded = loaded;
    }
}
