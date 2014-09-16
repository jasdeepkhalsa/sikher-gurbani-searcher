package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.forms.SikherIni;
import sikher.visual.forms.SikherProperties;
import sikher.searchengine.XPathSearcher;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 02.02.2006
 * Time: 19:15:40
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/editors/ResultTableEditor.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ResultTableEditor.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ResultTableEditor extends AbstractSikherEditor{
    public static final String EDITOR_NAME = "result-table";

    public ResultTableEditor( XPathSearcher searcher, SikherIni ini, StatusBar statusBar ) {
        super( searcher, ini, statusBar, EDITOR_NAME );
        _FinitComponents();
    }

    private void _FinitComponents(){
        ResultTableDataModel _dm = new ResultTableDataModel( FSikherIni, EDITOR_NAME );

        JScrollPane _scroll;
        _scroll = new JScrollPane( _dm.getTable() );
        _scroll.setPreferredSize( new Dimension(400, 400) );
        add( _scroll );

        setDataModel( _dm.getTable(), _dm );
    }

    public String getControlName(){
        return SikherProperties.getString( "editor.result-table.name" );
    }
}
