package sikher.visual.forms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 23.12.2005
 * Time: 12:26:06
 * $Header$
 * $Log$
 * Description:
 */
public abstract class OptionsCard extends JPanel {
    protected SikherIni FProperties = null;
    protected JFrame FFrame = null;

    public OptionsCard( JFrame owner, SikherIni prop ){
        super();
        FProperties = prop;
        FFrame = owner;
    }

    public OptionsCard( JFrame owner, sikher.visual.forms.SikherIni prop, LayoutManager layout ){
        super( layout );
        FProperties = prop;
        FFrame = owner;
    }

    public abstract void init();
    public abstract void apply();
}
