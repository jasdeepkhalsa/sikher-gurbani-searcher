package sikher.templates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 07.01.2006
 * Time: 14:09:26
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/templates/OutlookPanel.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: OutlookPanel.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class OutlookPanel extends JPanel {

    private GridBagConstraints _FConst;
    private ArrayList<OutlookTab> _FTabs;

    public OutlookPanel(){
        super( new GridBagLayout() );
        _FTabs = new ArrayList<OutlookTab>();
        _FConst = new GridBagConstraints();
        _FConst.fill = GridBagConstraints.HORIZONTAL;
        _FConst.anchor = GridBagConstraints.NORTH;
        _FConst.gridx = 0;
        _FConst.gridy = 0;
    }

    public void addTab( String name, JComponent comp ){
        OutlookTab _tab = new OutlookTab( name, comp );

        if( !_FTabs.isEmpty() ){
            _FConst.weighty = 0;
            comp.setVisible( false );
        }else{
            _FConst.weighty = 1;
        }
        _FConst.weightx = 1.0;
        add( _tab, _FConst );
        _FTabs.add( _tab );
        _FConst.gridy++;

        int _width = Math.max( (int)comp.getPreferredSize().getWidth(), (int)getPreferredSize().getWidth() );
        setPreferredSize( new Dimension( _width, (int)getPreferredSize().getHeight() ) );
    }

    private void _FsetVisible( OutlookTab tab ){
        GridBagLayout _layout = (GridBagLayout) getLayout();
        GridBagConstraints _const;

        for( OutlookTab _tab : _FTabs ){
            _const = _layout.getConstraints( _tab );
            if( _tab == tab ){
                _tab.getComponent().setVisible( true );
                _const.weighty = 1.0;
            }else{
                _tab.getComponent().setVisible( false );
                _const.weighty = 0.0;
            }
            _layout.setConstraints( _tab, _const );
        }

    }

    private class OutlookTab extends JPanel{
        private JComponent _FComponent;

        public OutlookTab( String name, JComponent comp ){
            super( new GridBagLayout() );
            _FComponent = comp;
            GridBagConstraints _const = new GridBagConstraints();

            if ( _FTabs.isEmpty() ){
                _const.anchor = GridBagConstraints.PAGE_START;
                _const.weighty = 0.0;
            }else{
                _const.anchor = GridBagConstraints.PAGE_END;
                _const.weighty = 1.0;
            }

            _const.fill = GridBagConstraints.HORIZONTAL;
            _const.gridx = 0;
            _const.gridy = 0;
            _const.weightx = 1.0;

            JButton _button = new JButton( name );
            _button.addActionListener( new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    _FsetVisible( OutlookTab.this );
                }
            } );
            add( _button, _const );

            _const.gridy++;
            add( comp, _const );

        }

        public JComponent getComponent(){
            return _FComponent;
        }
    }
}
