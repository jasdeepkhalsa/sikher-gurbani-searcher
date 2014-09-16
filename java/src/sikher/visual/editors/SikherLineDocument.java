package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.searchengine.SikherLine;
import sikher.searchengine.HymnType;
import sikher.visual.forms.SikherIni;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.util.HashMap;

import com.lowagie.text.*;
import com.lowagie.text.pdf.FontSelector;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 29.01.2006
 * Time: 23:21:44
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/SikherLineDocument.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherLineDocument.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SikherLineDocument extends AbstractSikherDocument{

    private HashMap<Hymn,MutableAttributeSet> _FGurbaniStyleMap = null;
    private HashMap<Hymn,MutableAttributeSet> _FTranslitStyleMap = null;
    private HashMap<Hymn,MutableAttributeSet> _FTranslationStyleMap = null;

    public SikherLineDocument( SikherIni ini ){
        super( ini );
        _FGurbaniStyleMap = new HashMap<Hymn, MutableAttributeSet>();
        _FTranslitStyleMap = new HashMap<Hymn, MutableAttributeSet>();
        _FTranslationStyleMap = new HashMap<Hymn, MutableAttributeSet>();
    }

    public void setHymns( Hymn[] hymns ){
        HashMap[] _maps = new HashMap[]{
                _FGurbaniStyleMap,
                _FTranslitStyleMap,
                _FTranslationStyleMap
        };
        Color[] _colors = new Color[]{
                Color.blue,
                Color.red,
                Color.black
        };
        for( HashMap _map : _maps ){
            _map.clear();
        }
        for( Hymn _hymn : hymns ){
            for( int i=0; i<_maps.length; i++ ){
                MutableAttributeSet _attrset = new SimpleAttributeSet();
                _attrset.addAttribute(LinkEditorKit.LINK, _hymn);
                _attrset.addAttribute(StyleConstants.Foreground, _colors[i]);
                _maps[i].put( _hymn, _attrset );
            }
        }
        super.setHymns( hymns );
    }

    protected void FinsertCombiDetails(Paragraph paragraph, FontSelector fs) {
        for( Hymn _hymn: getHymns() ){
            SikherLine[] _lines = _hymn.getLines();
            for( SikherLine _line : _lines ){
                String _details = FgetLineDetails( _hymn, _line.getIndex() );
                paragraph.add( fs.process( _details + '\n' ) );
            }
        }
    }

    protected void FinsertCombiDetails(Paragraph paragraph, com.lowagie.text.Font detailsFont) {
        for( Hymn _hymn: getHymns() ){
            SikherLine[] _lines = _hymn.getLines();
            for( SikherLine _line : _lines ){
                String _details = FgetLineDetails( _hymn, _line.getIndex() );
                paragraph.add( new Phrase( _details + '\n', detailsFont ) );
            }
        }
    }

    protected SikherLine[] FgetLines(Hymn hymn) {
        return hymn.getLines();
    }

    protected MutableAttributeSet FgetStyle(Hymn hymn, SikherLine line, HymnType type) {
        MutableAttributeSet _style = null;
        switch( type ){
            case GURBANI:
                _style = _FGurbaniStyleMap.get( hymn );
                break;
            case TRANSLITERATION:
                _style = _FTranslitStyleMap.get( hymn );
                break;
            case TRANSLATION:
                _style = _FTranslationStyleMap.get( hymn );
                break;
            default:
                return null;
        }
        _style.addAttribute( LinkEditorKit.LINE, line.getIndex() );
        return _style;
    }

    protected void FinsertCombiDetails() throws BadLocationException {
        if( isShowDetails() ){
            for( Hymn _hymn: getHymns() ){
                SikherLine[] _lines = _hymn.getLines();
                for( SikherLine _line : _lines ){
                    String _details = FgetLineDetails( _hymn, _line.getIndex() );
                    insertString(getLength(), _details, FDetails);
                    insertString(getLength(), "\n", null);
                }
            }
        }
    }
}
