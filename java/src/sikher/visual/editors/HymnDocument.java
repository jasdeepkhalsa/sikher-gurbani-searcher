package sikher.visual.editors;

import sikher.searchengine.SikherLine;
import sikher.searchengine.Hymn;
import sikher.searchengine.HymnType;
import sikher.visual.forms.SikherIni;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import java.awt.*;

import com.lowagie.text.*;
import com.lowagie.text.pdf.FontSelector;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.01.2006
 * Time: 0:00:22
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/HymnDocument.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: HymnDocument.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class HymnDocument extends AbstractSikherDocument{
    private MutableAttributeSet _FGurbaniStyle = null;
    private MutableAttributeSet _FTranslitStyle = null;
    private MutableAttributeSet _FTranslationStyle = null;

    public HymnDocument( SikherIni ini ){
        super( ini );
        _FGurbaniStyle= new SimpleAttributeSet();
        _FGurbaniStyle.addAttribute( StyleConstants.Foreground, Color.blue );
        _FTranslitStyle= new SimpleAttributeSet();
        _FTranslitStyle.addAttribute( StyleConstants.Foreground, Color.red );
        _FTranslationStyle= new SimpleAttributeSet();
        _FTranslationStyle.addAttribute( StyleConstants.Foreground, Color.black );
    }

    protected SikherLine[] FgetLines(Hymn hymn) {
        return hymn.getHymn();
    }

    protected MutableAttributeSet FgetStyle(Hymn hymn, SikherLine line, HymnType type) {
        switch( type ){
            case GURBANI:
                return _FGurbaniStyle;
            case TRANSLITERATION:
                return _FTranslitStyle;
            case TRANSLATION:
                return _FTranslationStyle;
            default:
                return null;
        }
    }

    protected void FinsertCombiDetails() throws BadLocationException {
        if( isShowDetails() ){
            for( Hymn _hymn : getHymns() ){
                insertString( getLength(), FgetHymnDetails( _hymn ), FDetails );
                insertString( getLength(), "\n", null );
            }
        }
    }

    public String copyDetails() {
        String _text = "";
        if( isShowDetails() ){
            if( isViewType() ){
                _text = super.copyDetails();
            }else{
                for( Hymn _hymn : getHymns() ){
                    _text += FgetHymnDetails( _hymn ) + '\n';
                }
            }
        }
        return _text;
    }

    protected void FinsertCombiDetails( Paragraph paragraph, FontSelector fs ) {
        for( Hymn _hymn : getHymns() ){
            paragraph.add( fs.process( FgetHymnDetails( _hymn ) ) );
        }
    }

    protected void FinsertCombiDetails(Paragraph paragraph, com.lowagie.text.Font detailsFont) {
        for( Hymn _hymn : getHymns() ){
            paragraph.add( new Phrase( FgetHymnDetails( _hymn ), detailsFont ) );
        }
    }
}
