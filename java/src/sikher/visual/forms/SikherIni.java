package sikher.visual.forms;

import sikher.searchengine.GurbaniType;
import sikher.searchengine.ISOLang;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.ArrayList;
import java.awt.*;
import java.awt.Font;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import sun.font.FontManager;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.12.2005
 * Time: 10:20:10
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/SikherIni.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherIni.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SikherIni extends Properties {

    public static final String KEY_FONT_NAME = "font.name.";
    public static final String KEY_FONT_STYLE = "font.style.";
    public static final String KEY_FONT_SIZE = "font.size.";
    public static final String KEY_FONT_COLOR = "font.color.";

    public static final String KEY_FONT_PDF_NAME = "font.pdf.name.";
    public static final String KEY_FONT_PDF_STYLE = "font.pdf.style.";
    public static final String KEY_FONT_PDF_SIZE = "font.pdf.size.";
    public static final String KEY_FONT_PDF_COLOR = "font.pdf.color.";

    public static final String KEY_FONT_PATH = "font.path";

    public static final String DEFAULT_FONT_NAME = "Default";

    private Map<String, java.awt.Font>_FFontCashe = new TreeMap<String, java.awt.Font>();
    private ArrayList<String> _FPathList = new ArrayList<String>();
    private java.awt.Font _FDefaultFont = null;

    private String _FDefaultFontName = SikherProperties.getString( "font.default" );
    private com.lowagie.text.Font _FDefaultPdfFont = null;
    private ArrayList<ISikherPropertyListener> _FListeners = new ArrayList<ISikherPropertyListener>();
    private Map<String, String> _FFontMap = new TreeMap<String, String>();

    public SikherIni(){
        _FinitDefaultFont();
    }

    public void load( String filename ) throws IOException {
        FileInputStream _stream;
        _stream = new FileInputStream( new File( filename ) );
        try{
            load( _stream );
        }finally{
            _stream.close();
        }

        _FinitFontMap();
    }

    public void store( String filename ) throws IOException {
        FileOutputStream _stream = new FileOutputStream( new File( filename ) );
        try{
            store( _stream, "Sikher ini file" );
        }finally{
            _stream.close();
        }
    }

    //!-------------- Default font ----------------------------
    private void _FinitFontMap(){
        _FFontMap.clear();

        String _fontDir = FontManager.getFontPath( true );
        File[] _fontFiles = new File( _fontDir ).listFiles();
        if( _fontFiles != null ){
            for( File _fontFile : _fontFiles ){
                if( _fontFile.getName().toLowerCase().endsWith( ".ttf" ) ){
                    try {
                        Font _font = Font.createFont( Font.TRUETYPE_FONT, _fontFile );
                        _FFontMap.put( _font.getFontName(), _fontFile.getAbsolutePath() );
                    } catch ( Exception e ) {}
                }
            }
        }

    }

    private void _FinitDefaultFont(){
        //create default font
        java.awt.Font _font = new Font( _FDefaultFontName, Font.PLAIN, 12 );

        String _path = _FFontMap.get( _FDefaultFontName );
        //create default pdf font
        //TODO: inspect the error
        /*
        try {
            _FDefaultPdfFont = new com.lowagie.text.Font(
                    BaseFont.createFont( _path, BaseFont.IDENTITY_H, BaseFont.EMBEDDED ) );
        } catch ( Exception e ) {
            //TODO: log
            e.printStackTrace();
            _FDefaultPdfFont = new com.lowagie.text.Font( com.lowagie.text.Font.NORMAL, 12f );
        }*/
    }

    public String getDefaultFontName(){
        return _FDefaultFontName;
    }

    public java.awt.Font getDefaultFont(){
        return _FDefaultFont.deriveFont( java.awt.Font.PLAIN );
    }

    public com.lowagie.text.Font getDefaultPdfFont(){
        return _FDefaultPdfFont;
    }

    //!--------------------------------------------------------
    //!---------------- Font utils ----------------------------

    public java.awt.Font updateFont( java.awt.Font font, int size ){
        if( font.getSize() != size ){
            return font.deriveFont( (float)size );
        }
        return font;
    }

    public void setFontName( GurbaniType type, ISOLang lang, String editor, String name ){
        String _key = createFontKey( KEY_FONT_NAME, type, lang, editor );
        putProperty( _key, name );
    }

    public String getFontName( GurbaniType type, ISOLang lang, String editor ){
        if( type == null || lang != null ){
            String _name = getProperty( createFontKey( KEY_FONT_NAME, type, lang, editor ) );
            if( _name != null ){
                return _name;
            }
        }
        return _FDefaultFontName;
    }

    public void setFontColor( GurbaniType type, ISOLang lang, String editor, Color color ){
        String _key = createFontKey( KEY_FONT_COLOR, type, lang, editor );
        String _value = String.valueOf( color.getRGB() );
        putProperty( _key, _value );
    }

    public Color getFontColor( GurbaniType type, ISOLang lang, String editor ){
        if( type == null || lang != null ){
            String _key = createFontKey( KEY_FONT_COLOR, type, lang, editor );
            String _value = getProperty( _key );
            if( _value != null ){
                try{
                    return new Color( Integer.valueOf( _value ) );
                }catch( Exception e ){}
            }
        }
        return Color.black;
    }

    public void setFontStyle( GurbaniType type, ISOLang lang, String editor, int style ){
        String _key = createFontKey( KEY_FONT_STYLE, type, lang, editor );
        String _value = String.valueOf( style );
        putProperty( _key, _value );
    }

    public int getFontStyle( GurbaniType type, ISOLang lang, String editor ){
        if( type == null || lang != null ){
            String _key = createFontKey( KEY_FONT_STYLE, type, lang, editor );
            String _value = getProperty( _key );
            if( _value != null ){
                try{
                    int _style = Integer.valueOf( _value );
                    if( _style >=0 && _style <= java.awt.Font.BOLD + java.awt.Font.ITALIC ){
                        return _style;
                    }
                }catch( Exception e ){}
            }
        }
        return java.awt.Font.PLAIN;
    }

    public void setFontSize( GurbaniType type, ISOLang lang, String editor, int size ){
        String _key = createFontKey( KEY_FONT_SIZE, type, lang, editor );
        String _value = String.valueOf( size );
        putProperty( _key, _value );
    }

    public int getFontSize( GurbaniType type, ISOLang lang, String editor ){
        if( type == null || lang != null ){
            String _key = createFontKey( KEY_FONT_SIZE, type, lang, editor );
            String _value = getProperty( _key );
            if( _value != null ){
                try{
                    int _size = Integer.valueOf( _value );
                    if( _size > 0 ){
                        return _size;
                    }
                }catch( Exception e ){}
            }
        }
        return 12;
    }
    //!--------------------------------------------------------

    public String[] getFontFilesAndDirs(){
        return _FPathList.toArray( new String[_FPathList.size()] );
    }

    public String[] getFontNames(){
        return _FFontMap.keySet().toArray( new String[_FFontMap.size()] );
    }

    public void putProperty( String key, String value ){
        put( key, value );
        firePropertyChanged( key );
    }

    public void firePropertyChanged( String key ){
        for( ISikherPropertyListener _listener : _FListeners ){
            _listener.onPropertyChanged( key );
        }
    }

    public void addPropertyListener( ISikherPropertyListener listener ){
        if( !_FListeners.contains( listener ) ){
            _FListeners.add( listener );
        }
    }

    public void removePropertyListener( ISikherPropertyListener listener ){
        _FListeners.remove( listener );
    }


    public String createFontKey( String key, GurbaniType type, ISOLang lang, String editor ){
        if( type == null ){
            return key + "details." + editor;
        }else{
            return key + type.toString().toLowerCase() + "." +
                    lang.getCode().toLowerCase() + "." + editor;
        }
    }

    //!-------------------- pdf section ----------------------

    /*
    public void putPdfFont( String path, GurbaniType type, ISOLang lang ){
        String _key = createFontKey( KEY_FONT_PDF_NAME, type, lang );
        String _alias = type.toString();
        if( path != null ){
            putProperty( _key, path );
            //FontFactory.register( path, _alias );
        }else{
            if( containsKey( _key ) ){
                remove( _key );
            }
        }
    }*/

    public com.lowagie.text.Font getPdfFont( GurbaniType type, ISOLang lang, String editor ){
        String _key = createFontKey( KEY_FONT_NAME, type, lang, editor );
        String _path = _FFontMap.get( getProperty( _key ) );
        if( _path != null && !_path.equals( "" ) ){
            com.lowagie.text.Font _font = null;
            try {
                _font = new com.lowagie.text.Font(
                        BaseFont.createFont( _path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED ) );
            } catch ( Exception e ) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                _font = new com.lowagie.text.Font();
            }
            _font.setStyle( getFontStyle( type, lang, editor ) );
            _font.setSize( getFontSize( type, lang, editor )*1f );
            _font.setColor( getFontColor( type, lang, editor ) );
            return _font;
        }
        return null;
    }

    //!-------------------------------------------------------
}
