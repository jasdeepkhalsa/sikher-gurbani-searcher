package sikher.visual.forms;

import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.Font;
import com.lowagie.text.DocumentException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.12.2005
 * Time: 10:20:10
 * $Header$
 * $Log$
 * Description:
 */
public class SikherIni extends Properties {

    private final String LANG_KEY = "language.";

    public void load( String filename ) throws IOException {
        FileInputStream _stream;
        _stream = new FileInputStream( new File( filename ) );
        try{
            load( _stream );
        }finally{
            _stream.close();
        }
        _FinitFontSelector();
    }

    public void store( String filename ) throws IOException {
        FileOutputStream _stream;
        _stream = new FileOutputStream( new File( filename ) );
        try{
            store( _stream, "Sikher ini file" );
        }finally{
            _stream.close();
        }
    }

    public String getFontPath( String lang ){
        return getProperty( LANG_KEY + lang.toUpperCase() );
    }

    public void setFontPath( String lang, String path ) throws DocumentException, IOException {
        _FFontSelector.addFont( new Font(BaseFont.createFont( path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED ), 12) );
        setProperty( LANG_KEY + lang.toUpperCase(), path );
    }

    public void removeFontPath( String lang ){
        remove( LANG_KEY + lang.toUpperCase() );
        //reinit selector
        _FFontSelector = new FontSelector();
        _FinitFontSelector();
    }

    public String[] getAllLangs(){
        ArrayList<String> _list = new ArrayList<String>();
        Enumeration _enum = propertyNames();
        while( _enum.hasMoreElements() ){
            String _key = _enum.nextElement().toString();
            if( _key.startsWith( LANG_KEY ) ){
                String _lang = _key.substring( _key.indexOf( LANG_KEY ) + LANG_KEY.length() );
                _list.add( _lang );
            }
        }
        return _list.toArray( new String[_list.size()] );
    }

    private FontSelector _FFontSelector = new FontSelector();

    public FontSelector getFontSelector() {
        return _FFontSelector;
    }

    private void _FinitFontSelector(){
        _FFontSelector.addFont( new Font(Font.TIMES_ROMAN, 12) );
        String[] _keys = getAllLangs();
        for( String _key : _keys ){
            String _path = getFontPath( _key );
            try {
                _FFontSelector.addFont( new Font(BaseFont.createFont( _path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED ), 12) );
            } catch ( Exception e ) {
                System.err.println( String.format("Can't load font \"{0}\". {1}", _path, e ) );
                remove( LANG_KEY + _key.toUpperCase() );
            }
        }
    }
}
