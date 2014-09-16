package sikher.templates;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableColumn;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 19:44:30
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/templates/ExpandingTable.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: ExpandingTable.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class ExpandingTable extends JTable {

    private Map<Integer, SizeRequirements> _FSizeMap = null;

    public ExpandingTable( TableModel tm ){
        super( tm );
        _FSizeMap = new HashMap<Integer, SizeRequirements>();
    }

    /**
     * Hide column with specified index
     * @param index - column index
     */
    public void setColumnVisible( int index, boolean visible ){
        if( index >=0 && index < getColumnCount() ){

            TableColumn _col = getColumnModel().getColumn(index);
            int _min = _col.getMinWidth();
            int _max = _col.getMaxWidth();
            int _pref = _col.getPreferredWidth();

            if( _min == 0 && _max == 0 && _pref == 0 && !visible ){
                return; //already hidden
            }
            if( (_min != 0 || _max != 0 || _pref != 0) && visible ){
                return; // already visible
            }

            SizeRequirements _size = _FSizeMap.get(index);
            if( _size == null ){
                _size = new SizeRequirements();
                _FSizeMap.put(index, _size);
            }

            _col.setMinWidth( _size.minimum );
            _col.setMaxWidth( _size.maximum );
            _col.setPreferredWidth( _size.preferred );
            _size.minimum = _min;
            _size.maximum = _max;
            _size.preferred = _pref;
        }
    }

    public boolean isColumnVisible( int index ){
        if( index >=0 && index < getColumnCount() ){
            TableColumn _col = getColumnModel().getColumn(index);
            int _min = _col.getMinWidth();
            int _max = _col.getMaxWidth();
            int _pref = _col.getPreferredWidth();
            return _min != 0 || _max != 0 || _pref != 0;
        }
        return false;
    }

    /**
     * Get column index by column name
     * @param name - column name
     * @return column index or -1 if column with such name
     * wasn't found
     */
    public int getColumnIndex( String name ){
        return getColumnModel().getColumnIndex( name );
    }

    public int getVisibleColumnCount(){
        int _count = 0;
        for( int i=0; i<getColumnCount(); i++ ){
            if( isColumnVisible( i ) ){
                _count++;
            }
        }
        return  _count;
    }
}
