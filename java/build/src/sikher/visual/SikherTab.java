package sikher.visual;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;

import javax.swing.*;
import java.awt.*;

import sikher.visual.forms.SikherIni;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 22.12.2005
 * Time: 21:53:38
 * $Header$
 * $Log$
 * Description:
 */
public abstract class SikherTab extends JPanel {
    protected SikherIni FProperties;

    public SikherTab( sikher.visual.forms.SikherIni prop ){
        super();
        FProperties = prop;
    }

    public SikherTab( SikherIni prop, LayoutManager layout ){
        super( layout );
        FProperties = prop;
    }

    public abstract boolean supportsExport();

    public abstract void export( Document doc ) throws DocumentException;

    public void init(){}
    public void done(){}
}
