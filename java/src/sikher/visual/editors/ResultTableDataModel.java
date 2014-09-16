package sikher.visual.editors;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.FontSelector;
import com.lowagie.text.pdf.PdfPTable;
import sikher.searchengine.*;
import sikher.templates.ExpandingTable;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.ISikherPropertyListener;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.ArrayList;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 19:19:16
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ResultTableDataModel.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ResultTableDataModel.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ResultTableDataModel implements ISikherDataModelEx{

    private ExpandingTable _FTable;
    private HymnTableModel _FHymnTableModel;
    private Hymn[] _FHymns = {};
    private SikherIni _FProperties;
    private String _FEditorName;

    public ResultTableDataModel( SikherIni prop, String editor ){
        _FProperties = prop;
        _FProperties.addPropertyListener( new ISikherPropertyListener() {
            public void onPropertyChanged(String key) {
                for( int i=0; i<4; i++ ){
                    ISOLang _lang = null;
                    GurbaniType _type = null;
                    if( i<_FLangs.length ){
                        _lang = _FLangs[i];
                        _type = GurbaniType.values()[i];
                    }
                    if( _lang == null && _type != null ){
                        continue;
                    }
                    if( _FProperties.createFontKey( SikherIni.KEY_FONT_NAME, _type, _lang, _FEditorName ).equals( key ) ||
                        _FProperties.createFontKey( SikherIni.KEY_FONT_STYLE, _type, _lang, _FEditorName ).equals( key ) ||
                        _FProperties.createFontKey( SikherIni.KEY_FONT_SIZE, _type, _lang, _FEditorName ).equals( key ) ||
                        _FProperties.createFontKey( SikherIni.KEY_FONT_COLOR, _type, _lang, _FEditorName ).equals( key ) ){
                        _FupdateContent();
                        break;
                    }
                }
            }
        });
        _FEditorName = editor;
        _FHymnTableModel = new HymnTableModel();
        _FTable = new ExpandingTable( _FHymnTableModel );

        for( int i=0; i<_FTable.getColumnCount(); i++ ){
            TableColumn _col = _FTable.getColumnModel().getColumn( i );
            GurbaniType _type = null;
            if( i < 3 ){
                _type = GurbaniType.values()[i];
            }
            _col.setCellRenderer( new AttributedCellRenderer( _type ) );
        }
    }

    private void _FupdateContent(){
        if( _FTable != null ){
            _FTable.clearSelection();
            _FTable.invalidate();
            _FTable.repaint();
        }
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

    public void setLang(GurbaniType type, ISOLang lang) {
        _FHymnTableModel.setLang( type, lang );
        _FupdateContent();
    }

    public ISOLang getLang(GurbaniType type) {
        return _FHymnTableModel.getLang( type );
    }

    public void setViewType(boolean viewType) {
        //ignored
    }

    public boolean isViewType() {
        return false; //ignored
    }

    public void setShow(GurbaniType type, boolean show) {
        if( type != null ){
            int _index = type.ordinal();
            if( _index < 3 ){
                _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[_index] ), show );
            }
        }else{
            for( int i=3; i<=7; i++ ){
                _FTable.setColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[i] ), show );
            }
        }
    }

    public boolean getShow(GurbaniType type) {
        if( type != null ){
            int _index = type.ordinal();
            if( _index < 3 ){
                return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[_index] ) );
            }
        }else{
            return _FTable.isColumnVisible( _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[3] ) );
        }
        return false;
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

    public void exportPdf(Document doc) throws DocumentException {
        //To change body of implemented methods use File | Settings | File Templates.
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
            /*
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
            } */
        }
        doc.add( _table );
    }

    public void exportPdf(Document doc, Font gurbaniFont, Font translitFont, Font translationFont, Font detailsFont)
            throws DocumentException {
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
            /*
            if( isShowGurbani() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[0] ) );
                Phrase _ph = new Phrase( _value != null ? _value.toString() : "", gurbaniFont );
                _table.addCell( _ph );
            }
            if( isShowTranslit() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[1] ) );
                Phrase _ph = new Phrase( _value != null ? _value.toString() : "", translitFont );
                _table.addCell( _ph );
            }
            if( isShowTranslation() ){
                Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[2] ) );
                Phrase _ph = new Phrase( _value != null ? _value.toString() : "", translationFont );
                _table.addCell( _ph );
            }
            if( isShowDetails() ){
                for( int j=3; j<=7; j++ ){
                    Object _value = _FTable.getValueAt( i, _FTable.getColumnIndex( _FHymnTableModel.COLUMNS[j] ) );
                    Phrase _ph = new Phrase( _value != null ? _value.toString() : "", detailsFont );
                    _table.addCell( _ph );
                }
            }*/
        }
        doc.add( _table );
    }

    private ISOLang[] _FLangs = new ISOLang[3];

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
                case 1:
                case 2:
                    if( _FLangs[columnIndex] == null ) return null;
                    return _line.getText( _FLangs[columnIndex], GurbaniType.values()[columnIndex] );
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

        public void setLang( GurbaniType type, ISOLang lang ){
            int _index = type.ordinal();
            if( _index < _FLangs.length ){
                _FLangs[_index] = lang;
                fireTableDataChanged();
            }
        }

        public ISOLang getLang( GurbaniType type ){
            int _index = type.ordinal();
            if( _index < _FLangs.length ){
                return _FLangs[_index];
            }
            return null;
        }
    }

    private class AttributedCellRenderer extends JLabel implements TableCellRenderer{
        private GurbaniType _FType;

        public AttributedCellRenderer( GurbaniType type ){
            _FType = type;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText( value != null ? value.toString() : "" );
            java.awt.Font _font = getFont();
            ISOLang _lang = null;
            if( _FType != null && _FType.ordinal()<_FLangs.length ){
                _lang = _FLangs[_FType.ordinal()];
            }
            String _fontName = _FProperties.getFontName( _FType, _lang, _FEditorName );
            int _style = _FProperties.getFontStyle( _FType, _lang, _FEditorName );
            int _size = _FProperties.getFontSize( _FType, _lang, _FEditorName );

            if( !_fontName.equals( _font.getFontName() ) ){
                _font = new java.awt.Font( _fontName, _style, _size );
            }
            if( _style != _font.getStyle() ){
                _font = _font.deriveFont( _style, _size );
            }
            if( _size != _font.getSize() ){
                _font = _font.deriveFont( _size*1f );
            }
            setFont( _font );
            Color _color = _FProperties.getFontColor( _FType, _lang, _FEditorName );
            if( _color != getForeground() ){
                setForeground( _color );
            }
            return this;
        }
    }
}
