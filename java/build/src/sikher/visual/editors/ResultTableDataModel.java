package sikher.visual.editors;

import com.lowagie.text.*;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPTable;
import sikher.searchengine.Hymn;
import sikher.searchengine.ISOLang;
import sikher.searchengine.SikherLine;
import sikher.templates.ExpandingTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 19:19:16
 * $Header$
 * $Log$
 * Description:
 */
public class ResultTableDataModel implements ISikherDataModel{

    private ExpandingTable _FTable;
    private HymnTableModel _FHymnTableModel;
    private Hymn[] _FHymns = {};

    public ResultTableDataModel(){
        _FHymnTableModel = new HymnTableModel();
        _FTable = new ExpandingTable( _FHymnTableModel );
    }

    public JTable getTable(){
        return _FTable;
    }

    public Hymn[] getHymns() {
        return _FHymns;
    }

    public void setHymns(Hymn[] hymns) {
        _FHymns = hymns;
        _FHymnTableModel.setHymns( hymns );
    }

    public void setGurbani(ISOLang gurbani) {
        _FHymnTableModel.setGurbani( gurbani );
    }

    public ISOLang getGurbani() {
        return _FHymnTableModel.getGurbani();
    }

    public void setTranslit(ISOLang translit) {
        _FHymnTableModel.setTranslit( translit );
    }

    public ISOLang getTranslit() {
        return _FHymnTableModel.getTranslit();
    }

    public void setTranslation(ISOLang translation) {
        _FHymnTableModel.setTranslation( translation );
    }

    public ISOLang getTranslation() {
        return _FHymnTableModel.getTranslation();
    }

    public void setViewType(boolean viewType) {
        //ignored
    }

    public boolean isViewType() {
        return false; //ignored
    }

    public void setShowGurbani(boolean bShow) {
        _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[0] ), bShow );
    }

    public boolean isShowGurbani() {
        return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[0] ) );
    }

    public void setShowTranslit(boolean bShow) {
        _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[1] ), bShow );
    }

    public boolean isShowTranslit() {
        return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[1] ) );
    }

    public void setShowTranslation(boolean bShow) {
        _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[2] ), bShow );
    }

    public boolean isShowTranslation() {
        return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[2] ) );
    }

    public void setShowDetails(boolean bShow) {
        for( int i=3; i<=7; i++ ){
            _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[i] ), bShow );
        }
    }

    public boolean isShowDetails() {
        return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[3] ) );
    }

    public String copyAll() {
        StringBuffer _buffer = new StringBuffer();
        for( int i=0; i<_FTable.getRowCount(); i++ ){
            for( int j=0; j<_FTable.getColumnCount(); j++ ){
                if( _FTable.isColumnVisible( i ) ){
                    _buffer.append( _FTable.getValueAt(i, j) );
                    _buffer.append( '\t' );
                }
            }
            _buffer.append( '\n' );
        }
        return _buffer.toString();
    }

    public String copyGurbani() {
        return _FcopyColumns( new int[]{_FTable.getColumnIndex( _FHymnTableModel.COLUMNS[0] )} );
    }

    public String copyTranslit() {
        return _FcopyColumns( new int[]{_FTable.getColumnIndex( _FHymnTableModel.COLUMNS[1] )} );
    }

    public String copyTranslation() {
        return _FcopyColumns( new int[]{_FTable.getColumnIndex( _FHymnTableModel.COLUMNS[2] )} );
    }

    public String copyDetails() {
        return _FcopyColumns( new int[]{
                _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[3] ),
                _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[4] ),
                _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[5] ),
                _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[6] ),
                _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[7] )
        } );
    }

    private String _FcopyColumns( int[] indexes ){
        StringBuffer _buffer = new StringBuffer();
        for( int i=0; i<_FTable.getRowCount(); i++ ){
            for( int _index : indexes ){
                if( _FTable.isColumnVisible( _index ) ){
                    _buffer.append( _FTable.getValueAt(i, _index) );
                    _buffer.append( '\t' );
                }
            }
            _buffer.append( '\n' );
        }
        return _buffer.toString();
    }

    public Document createDocument() {
        return new Document( PageSize.A4.rotate(), 30, 30, 30, 30 );
    }

    public void export(Document doc, FontSelector fs) throws DocumentException {
        int _colCount = _FTable.getVisibleColumnCount();
        if( _colCount == 0 ) return;
        final int[] _headerWidths = new int[]{20, 20, 20, 10, 6, 5, 9, 10};

        PdfPTable _table = new PdfPTable( _colCount );
        _table.getDefaultCell().setPadding(3);
        _table.getDefaultCell().setBorderWidth(3);
        _table.getDefaultCell().setHorizontalAlignment( Element.ALIGN_CENTER );
        int[] _widths = new int[_colCount];
        for( int i = 0, j = 0; i < _FTable.getColumnCount(); i++ ){
            if( _FTable.isColumnVisible( 0 ) ){
                _table.addCell( _FHymnTableModel.COLUMNS[i] );
                _widths[j++] = _headerWidths[i];
            }
        }
        _table.setWidths( _widths );
        _table.setWidthPercentage( 100 );
        _table.setHeaderRows( 1 );

        _table.getDefaultCell().setBorderWidth(1);
        for( int i=0; i<_FTable.getRowCount(); i++ ){
            if( isShowGurbani() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[0] ) );
                Phrase _ph = fs.process( _value != null ? _value.toString() : "" );
                _table.addCell( _ph );
            }
            if( isShowTranslit() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[1] ) );
                Phrase _ph = fs.process( _value != null ? _value.toString() : "" );
                _table.addCell( _ph );
            }
            if( isShowTranslation() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[2] ) );
                Phrase _ph = fs.process( _value != null ? _value.toString() : "" );
                _table.addCell( _ph );
            }
            if( isShowDetails() ){
                for( int j=3; j<=7; j++ ){
                    Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[j] ) );
                    Phrase _ph = fs.process( _value != null ? _value.toString() : "" );
                    _table.addCell( _ph );
                }
            }
        }
        doc.add( _table );
    }

    private class HymnTableModel extends AbstractTableModel{

        private SikherLine[] _FLines;
        public final String[] COLUMNS = new String[]{
                    sikher.visual.forms.SikherProperties.getString("table.hymns.gurbani"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.transit"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.translation"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.book"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.page"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.line"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.author"),
                    sikher.visual.forms.SikherProperties.getString("table.hymns.raag")
            };

        private ISOLang _FGurbani = null;
        private ISOLang _FTranslit = null;
        private ISOLang _FTranslation = null;

        public HymnTableModel(){
            _FLines = new SikherLine[]{};
        }

        public void setHymns( Hymn[] hymns ){
            ArrayList<SikherLine> _list = new ArrayList<SikherLine>();
            for( Hymn _hymn : hymns ){
                SikherLine[] _lines = _hymn.getLines();
                for( SikherLine _line : _lines ){
                    _list.add( _line );
                }
            }
            _FLines = _list.toArray(new SikherLine[_list.size()]);
            fireTableDataChanged();
        }

        public void setDisplayOptions( ISOLang gurbani, ISOLang translit, ISOLang translation ){
            _FGurbani = gurbani;
            _FTranslit = translit;
            _FTranslation = translation;
            fireTableDataChanged();
        }

        public int getRowCount() {
            return _FLines.length;
        }

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public String getColumnName( int col ){
            return COLUMNS[col];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            SikherLine _line = _FLines[rowIndex];
            Hymn _hymn = _line.getHymn();
            switch(columnIndex){
                case 0:
                    if( _FGurbani == null ) return null;
                    return _line.getText( _FGurbani.getCode(), Hymn.TYPE_GURBANI );
                case 1:
                    if( _FTranslit == null ) return null;
                    return _line.getText( _FTranslit.getCode(), Hymn.TYPE_TRANSLIT );
                case 2:
                    if( _FTranslation == null ) return null;
                    return _line.getText( _FTranslation.getCode(), Hymn.TYPE_TRANSLATION );
                case 3:
                    return _hymn.getBook();
                case 4:
                    return _hymn.getPage();
                case 5:
                    return _line.getIndex();
                case 6:
                    return _hymn.getAuthor();
                case 7:
                    return _hymn.getMelody();
            }
            return null;
        }

        public ISOLang getGurbani() {
            return _FGurbani;
        }

        public void setGurbani(ISOLang gurbani) {
            _FGurbani = gurbani;
            fireTableDataChanged();
        }

        public ISOLang getTranslit() {
            return _FTranslit;
        }

        public void setTranslit(ISOLang translit) {
            _FTranslit = translit;
            fireTableDataChanged();
        }

        public ISOLang getTranslation() {
            return _FTranslation;
        }

        public void setTranslation(ISOLang translation) {
            _FTranslation = translation;
            fireTableDataChanged();
        }
    }
}
