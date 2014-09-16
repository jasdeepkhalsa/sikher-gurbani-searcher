package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 14.01.2006
 * Time: 12:23:34
 */
public class FindOtherMethod extends FindMethod{
    private Hymn[] _FHymns;

    public FindOtherMethod( Logger logger, Hymn[] hymns ){
        super( logger );
        _FHymns = hymns;
    }

    public void find(XPath xpath, NodeInfo doc) {
        for( Hymn _hymn : _FHymns ){
            String _lang = _hymn.getLanguage().getCode();
            String _type = _hymn.getType();

            String _expression = "//xmlGurbani[not(starts-with(lower-case(@language), \""+ _lang.toLowerCase() +"\")" +
                " and @type=\"" + _type.toLowerCase() + "\")]";
            NodeInfo[] _nodes = searchXPath2( xpath, doc, _expression );
            if ( _nodes.length > 0 ){
                NodeInfo _info = _nodes[0];
                _lang = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "language") );
                _type = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "type") );
            }else{
                //some strange file
                break;
            }
            SikherLine[] _lines = _hymn.getLines();
            for( SikherLine _line : _lines ){
                _expression = "//book[@number=" + _hymn.getBookNumber() + "]" +
                         "//page[@number=" + _hymn.getPage() + "]" +
                         "//hymn[@number=" + _hymn.getNumber() + "]"+
                         "//line[@number=" +  _line.getIndex() + "]";
                _nodes = searchXPath2( xpath, doc ,_expression );
                for( NodeInfo _node: _nodes ){
                    String _text = _node.getStringValue();
                    _line.putText( _lang.substring(0, 2), _type, _text);
                }
            }
        }
    }
}
