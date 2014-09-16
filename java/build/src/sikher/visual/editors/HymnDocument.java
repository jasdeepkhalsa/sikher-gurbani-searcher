package sikher.visual.editors;

import sikher.searchengine.SikherLine;
import sikher.searchengine.Hymn;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.BadLocationException;
import java.awt.*;

import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.FontSelector;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 30.01.2006
 * Time: 0:00:22
 * $Header$
 * $Log$
 * Description:
 */
public class HymnDocument extends AbstractSikherDocument{
    private MutableAttributeSet _FGurbaniStyle = null;
    private MutableAttributeSet _FTranslitStyle = null;
    private MutableAttributeSet _FTranslationStyle = null;

    public HymnDocument(){
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

    protected MutableAttributeSet FgetStyle(Hymn hymn, SikherLine line, String type) {
        MutableAttributeSet _style = null;

        if( type.equals( Hymn.TYPE_GURBANI ) ){
            _style = _FGurbaniStyle;
        }else if( type.equals( Hymn.TYPE_TRANSLIT ) ){
            _style = _FTranslitStyle;
        }else if( type.equals( Hymn.TYPE_TRANSLATION ) ){
            _style = _FTranslationStyle;
        }else{
            return null;
        }
        return _style;
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
}
