package sikher.visual.forms;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 21.12.2005
 * Time: 14:35:10
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/OptionsDialog.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: OptionsDialog.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class OptionsDialog extends ModalDialog {
    private SikherIni _FProperties;

    public OptionsDialog( JFrame owner, SikherIni prop ){
        super( owner, SikherProperties.getString("options-dialog.title") );
        _FProperties = prop;
        _FinitComponents();
    }

    public void okPressed (){
        _FapplyChanges();
        super.okPressed();
    }
    public void cancelPressed (){
        _FcancelChanges();
        super.cancelPressed();
    }

    public int open (){
        for( Component _comp : jp_Cards.getComponents() ){
            if( _comp instanceof OptionsCard ){
                OptionsCard _card = (OptionsCard) _comp;
                _card.init();
            }
        }
        return super.open();
    }

    private JList jl_Components;
    private JPanel jp_Cards;
    private JTextField jtf_Caption;

    private void _FinitComponents(){
        addButton( new JButton( new ApplyAction() ) );

        JPanel _mainPanel = getDialogPanel();

        JPanel _panel = new JPanel( new BorderLayout() );
        _mainPanel.add( _panel, BorderLayout.CENTER );

        jl_Components = new JList( new DefaultListModel() );
        jl_Components.setCellRenderer( new DefaultListCellRenderer(){
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component _comp = super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
                JLabel _label = (JLabel) _comp;
                if ( !(value instanceof Icon) ){
                    _label.setText( " " + ((value == null) ? "" : value.toString()) + "      " );
                }
                return _comp;
            }
        });
        jl_Components.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        jl_Components.addListSelectionListener( new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                changePanel();
            }
        });
        _panel.add( new JScrollPane( jl_Components ), BorderLayout.WEST );

        JPanel _panel2 = new JPanel( new BorderLayout() );
        _panel.add( _panel2, BorderLayout.CENTER );

        CardLayout _layout = new CardLayout(8, 8);
        jp_Cards = new JPanel(_layout);
        _panel2.add( jp_Cards, BorderLayout.CENTER );

        jtf_Caption = new JTextField();
        jtf_Caption.setEnabled(false);
        jtf_Caption.setBackground( jtf_Caption.getSelectionColor() );
        jtf_Caption.setDisabledTextColor ( jtf_Caption.getSelectedTextColor() );
        jtf_Caption.setFont( jtf_Caption.getFont().deriveFont( Font.BOLD ));
        jtf_Caption.setHorizontalAlignment(JTextField.CENTER);
        _panel2.add( jtf_Caption, BorderLayout.NORTH );

        FontsOptionsCard _card = new FontsOptionsCard( _FProperties );
        addCard( _card.getTitle(), _card );

        setSize( 460, 400 );
        setResizable(false);
    }

    public void addCard( String name, OptionsCard card ){
        DefaultListModel _model = (DefaultListModel) jl_Components.getModel();
        _model.addElement( name );
        jp_Cards.add( name, card );
        jl_Components.setSelectedIndex(0);
    }

    public void changePanel(){
        int _index = jl_Components.getSelectedIndex();
        String _name = jl_Components.getModel().getElementAt(_index).toString();
        jtf_Caption.setText( _name );
        CardLayout _layout = (CardLayout) jp_Cards.getLayout();
        _layout.show( jp_Cards, _name );
    }

    private void _FapplyChanges(){
        for( Component _comp : jp_Cards.getComponents() ){
            if( _comp instanceof OptionsCard ){
                OptionsCard _card = (OptionsCard) _comp;
                _card.apply();
            }
        }
    }

    private void _FcancelChanges(){
        for( Component _comp : jp_Cards.getComponents() ){
            if( _comp instanceof OptionsCard ){
                OptionsCard _card = (OptionsCard) _comp;
                _card.cancel();
            }
        }
    }

    //-------------- actions -----------------------
    private class ApplyAction extends AbstractAction{
        public ApplyAction(){
            putValue(AbstractAction.NAME, sikher.visual.forms.SikherProperties.getString("generic-dialog.button.apply"));
        }

        public void actionPerformed(ActionEvent e) {
            _FapplyChanges();
        }
    }
}
