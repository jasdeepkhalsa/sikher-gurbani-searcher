package sikher.searchengine;

import net.sf.saxon.om.NamespaceConstant;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.xpath.XPathEvaluator;
import org.xml.sax.InputSource;

import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 13.12.2005
 * Time: 19:40:56
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/FindMethod.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: FindMethod.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description: look for smthing using XPath
 */
public abstract class FindMethod {
    protected Logger FLogger;

    public FindMethod( Logger logger ){
        FLogger = logger;
    }

    public abstract void find( XPath xpath, NodeInfo doc );

    public void iterateFiles( String dir ){
        File _dir = new File( dir );
        File[] _files  = _dir.listFiles();
        if ( _files == null ){
            return;
        }

        //create xpath
        XPathFactory _xpf;
        try {
            _xpf = XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON);
        } catch (XPathFactoryConfigurationException e) {
            FLogger.log(Level.SEVERE, "exception.xpathfactoryconfiguration", e);
            return;
        }
        XPath _xpath = _xpf.newXPath();

        for( File _file : _files ){
            String _filename = _file.getAbsolutePath();
            FileInputStream _filestream;
            try {
                _filestream = new FileInputStream(new File(_filename));
                InputSource _insource = new InputSource(_filestream);
                SAXSource _ss = new SAXSource(_insource);
                NodeInfo _doc = ((XPathEvaluator) _xpath).setSource(_ss);
                try {
                    find( _xpath, _doc );
                } finally {
                    _insource.setByteStream(null);
                    _filestream.close();
                }
            }catch (IOException e) {
                FLogger.log( Level.WARNING, "exception.io", e );
            } catch (net.sf.saxon.trans.XPathException e) {
                FLogger.log( Level.WARNING, "exception.xpath", e );
            }
        }
    }

    public NodeInfo[] searchXPath2( XPath xpath, NodeInfo doc, String expression ){
        ArrayList _nodes = new ArrayList();
        try{
            // Compile the XPath expressions used by the application
            XPathExpression _findLine = xpath.compile(expression);

            // Find the lines containing the requested word
            _nodes.addAll((ArrayList) _findLine.evaluate(doc, XPathConstants.NODESET));
        }catch (XPathExpressionException e) {
            FLogger.log( Level.WARNING, "exception.xpath", e );
        }
        return (NodeInfo[]) _nodes.toArray(new NodeInfo[_nodes.size()]);
    }
}
