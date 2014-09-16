package sikher.visual.sidepanel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 09.12.2005
 * Time: 12:50:10
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/sidepanel/StatusBar.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: StatusBar.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class StatusBar extends JPanel {
    private JLabel jl_Message = null;
    private JProgressBar jpb_SearchProgress = null;

    public StatusBar(){
        super( new GridBagLayout() );
        setBorder( BorderFactory.createEtchedBorder() );
        GridBagConstraints _const = new GridBagConstraints();
        jl_Message = new JLabel(" ");
        _const.fill = GridBagConstraints.HORIZONTAL;
        _const.weightx = 0.5;
        _const.gridx = 0;
        _const.gridy = 0;
        add( jl_Message, _const );
        jpb_SearchProgress = new JProgressBar();
        jpb_SearchProgress.setStringPainted(false);
        jpb_SearchProgress.setPreferredSize( new Dimension( 100, getFont().getSize() + 12 ) );
        _const.gridx = 1;
        _const.gridy = 0;
        _const.weightx = 0.0;
        _const.fill = GridBagConstraints.NONE;
        _const.anchor = GridBagConstraints.PAGE_END;
        add( jpb_SearchProgress, _const );
    }

    public void setMessage( String text ){
        jl_Message.setText( text );
    }

    public JProgressBar getProgressBar(){
        return jpb_SearchProgress;
    }

    private class SeparatorIcon implements Icon{

        private int _FHeight = 0;
        private int _FWidth = 4;

        public SeparatorIcon( int height ){
            _FHeight = height;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int _x = _FWidth/2;

            g.setColor( UIManager.getColor("controlShadow") );
            g.drawLine( _x, 1, _x, _FHeight );
            g.setColor( UIManager.getColor("controlHighlight") );
            g.drawLine( _x+1, 1, _x+1, _FHeight );
        }

        public int getIconWidth() {
            return _FWidth;
        }

        public int getIconHeight() {
            return _FHeight;
        }
    }

}
