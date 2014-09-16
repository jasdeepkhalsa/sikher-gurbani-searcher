package sikher.visual.editors;

import sikher.visual.forms.SikherIni;
import sikher.searchengine.SikherLine;
import sikher.searchengine.Hymn;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 18.04.2006
 * Time: 20:03:13
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/HymnDocumentEx.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: HymnDocumentEx.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class HymnDocumentEx extends AbstractSikherDocumentEx{
    public HymnDocumentEx( SikherIni ini, String editor ) {
        super( ini, editor );
        FDataModelDelegate = new HymnDataModelDelegate( ini, editor );
    }

     protected class HymnDataModelDelegate extends DataModelDelegate{

         /**
          * Construstor
          *
          * @param ini - Sikher properties
          */
         public HymnDataModelDelegate( SikherIni ini, String editor ) {
             super( ini, editor );
         }

         protected void FdrawDetails( AbstractSikherDataModel.IHymnDrawer drawer, SikherLine line )
                 throws Exception{
             if( isViewType() ){
                 super.FdrawDetails( drawer, line );
             }else{
                 if( line == line.getHymn().getHymn()[0] ){
                     String _details = FgetHymnDetails( line.getHymn() );
                     drawer.drawLine( _details, line, null );
                 }
             }
         }

         protected SikherLine[] FgetLines(){
             ArrayList<SikherLine> _lines = new ArrayList<SikherLine>();
             for ( Hymn _hymn : FHymns ) {
                 _lines.addAll( Arrays.asList( _hymn.getHymn() ) );
             }
             return _lines.toArray( new SikherLine[_lines.size()] );
         }
     }
}
