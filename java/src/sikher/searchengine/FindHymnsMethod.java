package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 14.01.2006
 * Time: 12:19:50
 */
public class FindHymnsMethod extends FindMethod{
    private String _FText;
    private SearchFlag _FSearchflag;
    private GurbaniType _FType;
    private int _FBook;
    private int _FWriter;
    private int _FRaag;
    private int _FStartpage;
    private int _FEndpage;
    private TreeMap<Integer, NumberedElement> _FBooks;
    private TreeMap<Integer, NumberedElement> _FAuthors;
    private TreeMap<Integer, NumberedElement> _FRaags;
    private Map<String, Hymn> _FHymnMap;

    public FindHymnsMethod( Logger logger, String text, SearchFlag searchflag, GurbaniType type,
                            int book, int writer, int raag, int startpage, int endpage,
                            TreeMap<Integer, NumberedElement> books,
                            TreeMap<Integer, NumberedElement> authors,
                            TreeMap<Integer, NumberedElement> raags){
        super( logger );
        _FText = text;
        _FSearchflag = searchflag;
        _FType = type;
        _FBook = book;
        _FWriter = writer;
        _FRaag = raag;
        _FStartpage = startpage;
        _FEndpage = endpage;
        _FBooks = books;
        _FAuthors = authors;
        _FRaags = raags;
        _FHymnMap = new TreeMap<String, Hymn>();
    }

    public void find(XPath xpath, NodeInfo doc) {
        //find type amd language
        String _expression = "//xmlGurbani";
        String _type = null;
        if ( _FType.ordinal() != 0 ){
            _type = _FType.xmlName();
            _expression += "[contains(lower-case(@type), '" + _type + "')]";
        }

        NodeInfo[] _nodes = searchXPath2( xpath, doc, _expression );

        if( _nodes.length == 0 ){
            return;
        }

        String _langAttr = _nodes[0].getAttributeValue( _nodes[0].getNamePool().getFingerprint("", "language") );
        ISOLang _lang;
        try{
            _lang = ISOLang.valueOf(_langAttr.substring(0, 2).toUpperCase());
        }catch(Exception e){
            FLogger.log( Level.WARNING, "exception.bad-iso", e );
            return;
        }
        if( _type == null ){
            _type = _nodes[0].getAttributeValue( _nodes[0].getNamePool().getFingerprint("", "type") );
        }
        GurbaniType _hymnType;
        try{
            _hymnType = GurbaniType.valueOf( _type.toUpperCase() );
        }catch( Exception e ){
            //TODO: log
            return;
        }

        _expression = makeXPath( _FText, _FSearchflag, _FBook, _FWriter, _FRaag, _FStartpage, _FEndpage );

        _nodes = searchXPath2( xpath, doc, _expression );

        for (NodeInfo _node : _nodes) {
            String _line = _node.getStringValue();
            String _sLineNumber = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );
            NodeInfo _hymnNode = _node.getParent();
            String _sNumber = _hymnNode.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );
            String _sAuthor = _hymnNode.getAttributeValue( _node.getNamePool().getFingerprint("", "author") );
            String _sMelody = _hymnNode.getAttributeValue( _node.getNamePool().getFingerprint("", "melody") );
            NodeInfo _pageNode = _hymnNode.getParent();
            String _sPageNumber = _pageNode.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );
            NodeInfo _bookNode = _pageNode.getParent();
            String _sBookNumer = _bookNode.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );

            try{
                int _lineNumber = Integer.valueOf(_sLineNumber);
                int _number = Integer.valueOf(_sNumber);
                AuthorElement _author = (AuthorElement) _FAuthors.get( Integer.valueOf(_sAuthor) );
                NumberedElement _melody = _FRaags.get( Integer.valueOf(_sMelody) );
                int _pageNumber = Integer.valueOf(_sPageNumber);
                NumberedElement _book = _FBooks.get(Integer.valueOf(_sBookNumer));
                String _key = "book=" + _book.getNumber() + ", page=" + _pageNumber + ", hymn=" + _number;
                Hymn _hymn = _FHymnMap.get( _key );
                if ( _hymn == null ){
                    _hymn = new Hymn( _book, _pageNumber, _number, _author, _melody );
                    _FHymnMap.put( _key, _hymn );
                }
                _hymn.addLine( _lineNumber, _line, _lang, _hymnType );
            }catch( NumberFormatException e ){
                FLogger.log( Level.WARNING, "exception.numberformat", e );
            }
        }
    }

    public String makeXPath(String text, SearchFlag searchflag,
                            int book, int author, int raag, int startpage, int endpage){
        String _expression = "";
        String _text = text.toLowerCase();

        if( book > 0 ){
            _expression += "//book[@number>=" + book + "]";
        }
        if (startpage > 0){
            _expression += "//page[@number>=" + startpage;
            if ( endpage > 0 ){
                _expression += " and @number<=" + endpage;
            }
            _expression += "]";
        }

        if( author != 0 || raag != 0 ){
            _expression += "//hymn[";
            //TODO: think out smth for authors
            /*
            if ( author > 0 ){
                _expression += "@author=" + author;
            }else if ( author == EAuthor.ALL_GURUS.getIndex() ){
                _expression += "@author>=" + EAuthor.GURU_NAVAK_DEV.getIndex() + " and @author<=" + EAuthor.GURU_TEG_BAHADUR.getIndex();
            }else if ( author == EAuthor.ALL_BHAGATS.getIndex() ){
                _expression += "@author>=" + EAuthor.SAINT_BAINI.getIndex() + " and @author<=" + EAuthor.SAINT_TRILOCHAN.getIndex();
            }else if ( author == EAuthor.ALL_DIVINES.getIndex() ){
                _expression += "@author>=" + EAuthor.SHEIKH_FARID.getIndex() + " and @author<=" + EAuthor.BALVAND_AND_SATA.getIndex();
            }else if( author == EAuthor.ALL_POETS.getIndex() ){
                _expression += "@author>=" + EAuthor.SAINT_KABIR_GURU_ARJAN_DEV_JI.getIndex() + "and @author<=" + EAuthor.BHAI_GURDAAS.getIndex();
            }*/
            if ( raag > 0){
                if ( author != 0 ){
                    _expression += " and";
                }
                _expression += " @melody=" + raag;
            }
            _expression += "]";
        }

        // Get all elements where pet contains the string cat, ignoring case XPATH 1.0
        //xpath = "//*[contains(translate(@pet,'abcdefghijklmnopqrstuvwxyz'," + " 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'CAT')]";
        _expression += "//line[";
        switch (searchflag) {
            case ALLWORDS:
                _expression += FbreakIntoWords(_text, "and");
                break;
            case ANYWORD:
                _expression += FbreakIntoWords(_text, "or");
                break;
            case EXACTPHRASE:
                _expression += "contains(lower-case(child::text()), '" + _text + "')";
                break;
            case NOWORDS:
                _expression += "not(" + FbreakIntoWords(_text, "or") + ")";
                break;
            case STARTSWITH:
                StringBuffer _buffer = new StringBuffer();
                _buffer.append( "^[(]*" );
                for( char _ch : _text.toCharArray() ){
                    if ( _ch != ' ' ){
                        _buffer.append( _ch );
                        _buffer.append( "\\w*[ ,.;:\\-()|]+" );
                    }
                }
                _buffer.setCharAt( _buffer.length()-1, '*' );
                _text = _buffer.toString();
                _expression += "matches(lower-case(child::text()), '" + _text + "')";
                break;
        }
        _expression += "]";
        return _expression;
    }

    protected String FbreakIntoWords( String text, String condition ){
        String _res = "";
        String[] _strs = text.split(" ");
        for( int i=0; i<_strs.length; i++ ){
            if ( i > 0 ){
                _res += " " + condition;
            }
            _res += " contains(lower-case(child::text()), '%s'".replaceAll("%s", _strs[i]) + ")";
        }
        return _res;
    }

    public Hymn[] getHymns(){
        return _FHymnMap.values().toArray( new Hymn[_FHymnMap.size()] );
    }
}

