package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 19:15:40
 * $Header$
 * $Log$
 * Description:
 */
public class ResultTableEditor extends AbstractSikherEditor{
    public ResultTableEditor(StatusBar statusBar) {
        super(statusBar);
        _FinitComponents();
    }

    private void _FinitComponents(){
        ResultTableDataModel _dm = new ResultTableDataModel();

        JScrollPane _scroll;
        _scroll = new JScrollPane( _dm.getTable() );
        _scroll.setPreferredSize( new Dimension(400, 400) );
        add( _scroll );

        setDataModel( _dm.getTable(), _dm );
    }
}
