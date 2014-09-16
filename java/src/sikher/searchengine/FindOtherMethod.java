package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.logging.Logger;
import java.util.logging.Level;

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
            String _lang = null;
            String _type = null;

            String _expression = "//xmlGurbani";
            NodeInfo[] _nodes = searchXPath2( xpath, doc, _expression );
            if ( _nodes.length > 0 ){
                NodeInfo _info = _nodes[0];
                _lang = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "language") );
                _type = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "type") );
            }else{
                //some strange file
                break;
            }

            ISOLang _isolang = null;
            try{
                _isolang = ISOLang.valueOf( _lang.substring(0, 2).toUpperCase() );
            }catch(Exception e){
                FLogger.log( Level.WARNING, "exception.bad-iso", e );
                return;
            }
            GurbaniType _hymnType = null;
            try{
                _hymnType = GurbaniType.valueOf( _type.toUpperCase() );
            }catch( Exception e ){
                //TODO: log
                return;
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
                    _line.putText( _isolang, _hymnType, _text );
                }
            }
        }
    }
}
