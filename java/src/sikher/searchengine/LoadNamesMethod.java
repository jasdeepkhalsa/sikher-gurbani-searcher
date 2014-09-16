package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 14.01.2006
 * Time: 16:30:49
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/LoadNamesMethod.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: LoadNamesMethod.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class LoadNamesMethod extends FindMethod{
    private NumberedElement[] _FBooks;
    private AuthorElement[] _FAuthors;
    private NumberedElement[] _FRaags;
    private String _FLang;

    private final String DATA_SCRIPTURE = "//scripture";
    private final String DATA_AUTHOR = "//author";
    private final String DATA_RAAG = "//raag";

    public LoadNamesMethod(Logger logger, NumberedElement[] books, AuthorElement[] authors, NumberedElement[] raags) {
        super(logger);
        _FBooks = books;
        _FAuthors = authors;
        _FRaags = raags;
        _FLang = Locale.getDefault().getLanguage().toUpperCase();
    }

    public void find(XPath xpath, NodeInfo doc) {
        String _expression = "//data[@lang=\"" + _FLang + "\"]";
        NodeInfo[] _nodes = searchXPath2( xpath, doc, _expression );
        if( _nodes.length == 0 ){
            return;
        }

        //get book names
        _FfindElements( xpath, doc, DATA_SCRIPTURE, _FBooks, "book" );

        //get authors
        _FfindElements( xpath, doc, DATA_AUTHOR, _FAuthors, "author" );

        //get raag names
        _FfindElements( xpath, doc, DATA_RAAG, _FRaags, "raag" );
    }

    private void _FfindElements( XPath xpath, NodeInfo doc, String expr, NumberedElement[] elems, String type ){
        NodeInfo[] _nodes = searchXPath2( xpath, doc, expr );
        for( NodeInfo _node : _nodes ){
            String _value = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "number") );
            String _name = _node.getAttributeValue( _node.getNamePool().getFingerprint("", "name") );
            if( _value != null ){
                try{
                    int _number = Integer.valueOf(_value);
                    if( _number >= 1 ){
                        for( NumberedElement _elem : elems ){
                            int _elemNumber = _elem.getNumber();
                            if( _elemNumber == _number ){
                                _elem.setValue( _name );

                                if( type.equals("author") ){
                                    String _type =
                                            _node.getAttributeValue( _node.getNamePool().getFingerprint("", "type") );
                                    int _typeNumber = Integer.valueOf(_type);
                                    ((AuthorElement)_elem).setType( _typeNumber );
                                }
                                break;
                            }
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
}
