package sikher.searchengine;

import net.sf.saxon.om.NodeInfo;

import javax.xml.xpath.XPath;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 14.01.2006
 * Time: 12:25:34
 */
public class LoadHymnMethod extends FindMethod{
    private Hymn _FHymn;

    public LoadHymnMethod( Logger logger, Hymn hymn ){
        super( logger );
        _FHymn = hymn;
    }

    public void find(XPath xpath, NodeInfo doc) {
        String _expression;

        String _lang;
        String _type;

        _expression = "//xmlGurbani";
        NodeInfo[] _nodes = searchXPath2( xpath, doc, _expression );
        if ( _nodes.length > 0 ){
            NodeInfo _info = _nodes[0];
            _lang = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "language") );
            _type = _info.getAttributeValue( _info.getNamePool().getFingerprint("", "type") );
        }else{
            //some strange file
            return;
        }

        ISOLang _isolang;
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

        _expression = "//book[@number=" + _FHymn.getBookNumber() + "]" +
                 "//page[@number=" + _FHymn.getPage() + "]" +
                 "//hymn[@number=" + _FHymn.getNumber() + "]//line";

        _nodes = searchXPath2( xpath, doc ,_expression );
        for( NodeInfo _node: _nodes ){
            try{
                int _number = Integer.valueOf(
                    _node.getAttributeValue(_node.getNamePool().getFingerprint("", "number")));
                _FHymn.addExtraLine( _number, _node.getStringValue().trim(), _isolang, _hymnType );
            }catch(NumberFormatException e ){
                FLogger.log( Level.WARNING, "exception.numberformat", e );
            }
        }
    }
}
