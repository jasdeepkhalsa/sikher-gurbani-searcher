package sikher.visual.editors;

import sikher.searchengine.Hymn;
import sikher.searchengine.SikherLine;
import sikher.searchengine.GurbaniType;
import sikher.visual.forms.SikherIni;

import javax.swing.text.MutableAttributeSet;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 13.04.2006
 * Time: 15:36:33
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/SikherLineDocumentEx.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: SikherLineDocumentEx.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class SikherLineDocumentEx extends AbstractSikherDocumentEx{
    public SikherLineDocumentEx( SikherIni ini, String editor ) {
        super( ini, editor );
        FDataModelDelegate = new LineDataModelDelegate( ini, editor );
    }

    protected class LineDataModelDelegate extends DataModelDelegate{

        private HashMap _FStyleMap[] = null;

        /**
         * Construstor
         *
         * @param ini - Sikher proeprties
         */
        public LineDataModelDelegate(SikherIni ini, String editor) {
            super(ini, editor);

            _FStyleMap = new HashMap[FTypeElems.length];
            for( int i=0; i<FTypeElems.length; i++ ){
                _FStyleMap[i] = new HashMap<Hymn, MutableAttributeSet>();
            }
        }

        protected void FonFontChanged(){
            _FupdateStyles();
            super.FonFontChanged();
        }

        public void setHymns( Hymn[] hymns ){
            FHymns = hymns;
            _FupdateStyles();
            super.setHymns( hymns );
        }

        private void _FupdateStyles(){
            for( int i=0; i<_FStyleMap.length; i++ ){
                _FStyleMap[i].clear();
            }

            if( FHymns != null ){
                for( Hymn _hymn : FHymns ){
                    for( int i=0; i<FTypeElems.length; i++ ){
                        MutableAttributeSet _attrset = FconvertToAttributes( FTypeElems[i] );
                        _attrset.addAttribute(LinkEditorKit.LINK, _hymn);
                        _FStyleMap[i].put( _hymn, _attrset );
                    }
                }
            }
        }

        protected MutableAttributeSet FgetStyle( SikherLine line, GurbaniType type ){
            MutableAttributeSet _style = null;
            int _index = FgetIndexByType( type );
            if( _index >= 0 ){
                _style = (MutableAttributeSet) _FStyleMap[_index].get( line.getHymn() );
            }
            _style.addAttribute( LinkEditorKit.LINE, line.getIndex() );
            return _style;
        }
    }
}
