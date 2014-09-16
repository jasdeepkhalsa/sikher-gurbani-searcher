package sikher.templates;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 13:16:57
 * $Header$
 * $Log$
 * Description:
 */
public class VisualUtils {

    public static void centerOnParent(Window window){
        Dimension _parent = window.getParent().getSize();
        Dimension _window = window.getSize();

        int _xCoord = window.getParent().getLocationOnScreen().x + (_parent.width/2 - _window.width/2);
        int _yCoord = window.getParent().getLocationOnScreen().y + (_parent.height/2 - _window.height/2);

        //Ensure that no part of window will be off-screen
        Dimension _screen = Toolkit.getDefaultToolkit().getScreenSize();
        int _xOffScreenExcess = _xCoord + _window.width - _screen.width;
        if ( _xOffScreenExcess > 0 ) {
            _xCoord = _xCoord - _xOffScreenExcess;
        }
        if (_xCoord < 0 ) {
            _xCoord = 0;
        }
        int _yOffScreenExcess = _yCoord + _window.height - _screen.height;
        if ( _yOffScreenExcess > 0 ) {
            _yCoord = _yCoord - _yOffScreenExcess;
        }
        if (_yCoord < 0) {
            _yCoord = 0;
        }

        window.setLocation( _xCoord, _yCoord );
    }

    public static void centerOnScreen ( Window window ){
        Dimension _window = window.getSize();
        Dimension _screen = Toolkit.getDefaultToolkit().getScreenSize();
        int _xCoord = (_screen.width - _window.width)/2;

        int _yCoord = (_screen.height - _window.height)/2;

        window.setLocation( _xCoord, _yCoord );
    }
    /**
    * Make a horizontal row of buttons of equal size, whch are equally spaced,
    * and aligned on the right.
    *
    * <P>The returned component has border spacing only on the top (of the size
    * recommended by the Look and Feel Design Guidelines).
    * All other spacing must be applied elsewhere ; usually, this will only mean
    * that the dialog's top-level panel should use.
    *
    * @param buttons contains <code>JButton</code> objects.
    */
    public static JComponent getCommandRow(JPanel panel, JButton[] buttons, int direction){
        equalizeSizes( buttons );
        LayoutManager _layout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout( _layout );
        panel.setBorder(  BorderFactory.createEmptyBorder(3, 0, 0, 0) );
        if ( direction == SwingUtilities.CENTER || direction == SwingUtilities.RIGHT ){
            panel.add( Box.createHorizontalGlue() );
        }
        for ( int i=0; i<buttons.length; i++ ){
            panel.add( buttons[i] );
            if ( i != buttons.length-1 ) {
                panel.add( Box.createHorizontalStrut(4) );
            }
        }
        if ( direction == SwingUtilities.CENTER || direction == SwingUtilities.LEFT ){
            panel.add( Box.createHorizontalGlue() );
        }
        return panel;
    }

    public static JComponent getCommandRow(JButton[] aButtons){
        return getCommandRow( new JPanel(), aButtons, SwingUtilities.CENTER );
    }

    /**
    * Sets the items in <code>components</code> to the same size.
    *
    * Sets each component's preferred and maximum sizes.
    * The actual size is determined by the layout manager, whcih adjusts
    * for locale-specific strings and customized fonts. (See this
    * <a href="http://java.sun.com/products/jlf/ed2/samcode/prefere.html">Sun doc</a>
    * for more information.)
    *
    * @param components contains <code>JComponent</code> objects.
     */
    public static void equalizeSizes(Component[] components) {
        Dimension _targetSize = new Dimension(0,0);
        synchronized( components ){
            for (Component _comp : components) {
                Dimension _compSize = _comp.getPreferredSize();
                double _width = Math.max(_targetSize.getWidth(), _compSize.getWidth());
                double _height = Math.max(_targetSize.getHeight(), _compSize.getHeight());
                _targetSize.setSize(_width, _height);
            }
            setSizes(components, _targetSize);
        }
    }

    private static void setSizes(Component[] components, Dimension dimension){
        for (Component _comp : components) {
            _comp.setPreferredSize((Dimension) dimension.clone());
            _comp.setMaximumSize((Dimension) dimension.clone());
        }
    }

    public static void equalizeHeight ( JComponent[] components ){
        Dimension _targetSize = new Dimension(0,0);
        synchronized( components ){
            for (JComponent _comp : components) {
                Dimension _compSize = _comp.getPreferredSize();
                double _width = _compSize.getWidth();
                double _height = Math.max(_targetSize.getHeight(), _compSize.getHeight());
                _targetSize.setSize(_width, _height);
            }
            setSizes(components, _targetSize);
        }
    }

    public static void equalizeWidth ( Component[] components ){
        Dimension _targetSize = new Dimension(0,0);
        synchronized( components ){
            for ( Component _comp : components ) {
                Dimension _compSize = _comp.getPreferredSize();
                double _width = Math.max( _targetSize.getWidth(), _compSize.getWidth() );
                double _height = _compSize.getHeight();
                _targetSize.setSize( _width, _height );
            }
            for (Component _comp : components) {
                int _height = (int) _comp.getPreferredSize().getHeight();
                _comp.setPreferredSize( new Dimension( _targetSize.width, _height ) );
                _comp.setMaximumSize( new Dimension( _targetSize.width, _height ) );
            }
        }
    }
}


