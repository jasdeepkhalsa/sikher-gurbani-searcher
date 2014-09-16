package sikher.visual.controls;

import sikher.visual.forms.SikherProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 09.12.2005
 * Time: 11:14:54
 * $Header$
 * $Log$
 * Description:
 */
public class KeyboardPanel extends JPanel {
    private final char[] GURMUKHI = {
            '\u0A05', '\u0A06', '\u0A07', '\u0A08', '\u0A09', '\u0A0A', '\u0A0F', '\u0A10', '\u0A13', '\u0A14', '\u0A15',
            '\u0A16', '\u0A17', '\u0A18', '\u0A19', '\u0A1A', '\u0A1B', '\u0A1C', '\u0A1D', '\u0A1E', '\u0A1F', '\u0A20',
            '\u0A21', '\u0A22', '\u0A23', '\u0A24', '\u0A25', '\u0A26', '\u0A27', '\u0A28', '\u0A2A', '\u0A2B', '\u0A2C',
            '\u0A2D', '\u0A2E', '\u0A2F', '\u0A30', '\u0A32', '\u0A33', '\u0A35', '\u0A36', '\u0A38', '\u0A39', '\u0A59',
            '\u0A5A', '\u0A5B', '\u0A5C', '\u0A5E', '\u0A72', '\u0A73', '\u0A02', '\u0A3C', '\u0A3E', '\u0A3F', '\u0A40',
            '\u0A41', '\u0A42', '\u0A47', '\u0A48', '\u0A4B', '\u0A4C', '\u0A4D', '\u0A70', '\u0A71', '\u0A74', '\u0A2C',
            '\u0A66', '\u0A67', '\u0A68', '\u0A69', '\u0A6A', '\u0A6B', '\u0A6C', '\u0A6D', '\u0A6E', '\u0A6F', 'X'
    };

    private JButton jb_Arrow = null;
    private JPopupMenu jpup_Popup = null;
    private JTextField jtf_Field = null;

    public KeyboardPanel( JTextField field ){
        super( new FlowLayout(FlowLayout.LEFT));
        jtf_Field = field;
        _FinitComponents();
    }

    private void _FinitComponents(){
        add( new JLabel(SikherProperties.getString("control.search.label.openkeyboard")));

        jb_Arrow = new JButton( new BevelArrowIcon( BevelArrowIcon.DOWN, true, false ) );
        jb_Arrow.setPressedIcon( new BevelArrowIcon( BevelArrowIcon.DOWN, true, true ) );
        jb_Arrow.setBorderPainted( false );
        jb_Arrow.setMargin( new Insets(1, 1, 1, 1) );
        jb_Arrow.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent event ){
                FshowPopup();
            }
        });
        add( jb_Arrow );

        jpup_Popup = new JPopupMenu();
        JPanel _keyboard = new JPanel(new GridLayout(7, 11));
        GurmukhiListener _listener = new GurmukhiListener();
        for (char _ch : GURMUKHI) {
            JButton _button = new JButton(String.valueOf(_ch));
            _button.setMargin( new Insets(1, 1, 1, 1) );
            _button.addActionListener( _listener );
            if( _ch == 'X' ){
                _button.setForeground( Color.red );
                _button.setFont( _button.getFont().deriveFont( Font.BOLD ) );
                _button.setToolTipText(SikherProperties.getString("control.search.tooltip.closekeyboard"));
            }
            _keyboard.add( _button );
        }
        _keyboard.setSize(20, 30);
        jpup_Popup.add( _keyboard );
    }

    private void FshowPopup() {
        int _x = jb_Arrow.getX();
        int _y = jb_Arrow.getY() + jb_Arrow.getHeight()+4;
        if( !jpup_Popup.isVisible() ){
            jpup_Popup.show(this, _x, _y);
        }
    }

    private class GurmukhiListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            String _letter = e.getActionCommand();
            if( _letter.equals("X") ){
                jpup_Popup.setVisible(false);
            }else{
                jtf_Field.setText( jtf_Field.getText() + _letter );
            }
        }
    }

    private class BevelArrowIcon implements Icon {
          public static final int UP    = 0;         // direction
          public static final int DOWN  = 1;

          private static final int DEFAULT_SIZE = 11;

          private Color edge1;
          private Color edge2;
          private Color fill;
          private int size;
          private int direction;

          public BevelArrowIcon(int direction, boolean isRaisedView, boolean isPressedView) {
            if (isRaisedView) {
              if (isPressedView) {
                init( UIManager.getColor("controlLtHighlight"),
                      UIManager.getColor("controlDkShadow"),
                      UIManager.getColor("controlShadow"),
                      DEFAULT_SIZE, direction);
              } else {
                init( UIManager.getColor("controlHighlight"),
                      UIManager.getColor("controlShadow"),
                      UIManager.getColor("control"),
                      DEFAULT_SIZE, direction);
              }
            } else {
              if (isPressedView) {
                init( UIManager.getColor("controlDkShadow"),
                      UIManager.getColor("controlLtHighlight"),
                      UIManager.getColor("controlShadow"),
                      DEFAULT_SIZE, direction);
              } else {
                init( UIManager.getColor("controlShadow"),
                      UIManager.getColor("controlHighlight"),
                      UIManager.getColor("control"),
                      DEFAULT_SIZE, direction);
              }
            }
          }

          public BevelArrowIcon(Color edge1, Color edge2, Color fill,
                           int size, int direction) {
            init(edge1, edge2, fill, size, direction);
          }


          public void paintIcon(Component c, Graphics g, int x, int y) {
            switch (direction) {
              case DOWN: drawDownArrow(g, x, y); break;
              case   UP: drawUpArrow(g, x, y);   break;
            }
          }

          public int getIconWidth() {
            return size;
          }

          public int getIconHeight() {
            return size;
          }


          private void init(Color edge1, Color edge2, Color fill,
                           int size, int direction) {
            this.edge1 = edge1;
            this.edge2 = edge2;
            this.fill = fill;
            this.size = size;
            this.direction = direction;
          }

          private void drawDownArrow(Graphics g, int xo, int yo) {
            g.setColor(edge1);
            g.drawLine(xo, yo,   xo+size-1, yo);
            g.drawLine(xo, yo+1, xo+size-3, yo+1);
            g.setColor(edge2);
            g.drawLine(xo+size-2, yo+1, xo+size-1, yo+1);
            int x = xo+1;
            int y = yo+2;
            int dx = size-6;
            while (y+1 < yo+size) {
              g.setColor(edge1);
              g.drawLine(x, y,   x+1, y);
              g.drawLine(x, y+1, x+1, y+1);
              if (0 < dx) {
                g.setColor(fill);
                g.drawLine(x+2, y,   x+1+dx, y);
                g.drawLine(x+2, y+1, x+1+dx, y+1);
              }
              g.setColor(edge2);
              g.drawLine(x+dx+2, y,   x+dx+3, y);
              g.drawLine(x+dx+2, y+1, x+dx+3, y+1);
              x += 1;
              y += 2;
              dx -= 2;
            }
            g.setColor(edge1);
            g.drawLine(xo+(size/2), yo+size-1, xo+(size/2), yo+size-1);
          }

          private void drawUpArrow(Graphics g, int xo, int yo) {
            g.setColor(edge1);
            int x = xo+(size/2);
            g.drawLine(x, yo, x, yo);
            x--;
            int y = yo+1;
            int dx = 0;
            while (y+3 < yo+size) {
              g.setColor(edge1);
              g.drawLine(x, y,   x+1, y);
              g.drawLine(x, y+1, x+1, y+1);
              if (0 < dx) {
                g.setColor(fill);
                g.drawLine(x+2, y,   x+1+dx, y);
                g.drawLine(x+2, y+1, x+1+dx, y+1);
              }
              g.setColor(edge2);
              g.drawLine(x+dx+2, y,   x+dx+3, y);
              g.drawLine(x+dx+2, y+1, x+dx+3, y+1);
              x -= 1;
              y += 2;
              dx += 2;
            }
            g.setColor(edge1);
            g.drawLine(xo, yo+size-3,   xo+1, yo+size-3);
            g.setColor(edge2);
            g.drawLine(xo+2, yo+size-2, xo+size-1, yo+size-2);
            g.drawLine(xo, yo+size-1, xo+size, yo+size-1);
          }
    }
}
