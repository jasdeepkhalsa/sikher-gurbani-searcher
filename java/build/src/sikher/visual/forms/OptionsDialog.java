package sikher.visual.forms;

import sikher.templates.VisualUtils;

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
 * $Header$
 * $Log$
 * Description:
 */
public class OptionsDialog extends JDialog {
    private SikherIni _FProperties;

    public OptionsDialog( JFrame owner, SikherIni prop ){
        super( owner, SikherProperties.getString("options-dialog.title"), true );
        _FProperties = prop;
        _FinitComponents();
    }

    private JList jl_Components;
    private JPanel jp_Cards;
    private JTextField jtf_Caption;

    private void _FinitComponents(){
        JButton[] _buttons = {
                new JButton( new OkAction() ),
                new JButton( new CancelAction() ),
                new JButton( new ApplyAction() ),
                new JButton( new HelpAction() )
        };
        add( VisualUtils.getCommandRow(_buttons ), BorderLayout.SOUTH );

        JPanel _panel = new JPanel( new BorderLayout() );
        add( _panel, BorderLayout.CENTER );

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

        //add panels
        addCard( sikher.visual.forms.SikherProperties.getString("export-options.name"),
                new sikher.visual.forms.ExportOptionsCard( (JFrame) getOwner(), _FProperties ) );
        jl_Components.setSelectedIndex(0);

        setSize( 460, 400 );
        setResizable(false);
        VisualUtils.centerOnParent( this );
    }

    public void addCard( String name, sikher.visual.forms.OptionsCard card ){
        DefaultListModel _model = (DefaultListModel) jl_Components.getModel();
        _model.addElement( name );
        jp_Cards.add( name, card );
        card.init();
    }

    public void changePanel(){
        int _index = jl_Components.getSelectedIndex();
        String _name = jl_Components.getModel().getElementAt(_index).toString();
        jtf_Caption.setText( _name );
        CardLayout _layout = (CardLayout) jp_Cards.getLayout();
        _layout.show( jp_Cards, _name );
    }

    private void _FapplyChanges() {
    }

    //-------------- actions -----------------------
    private class OkAction extends AbstractAction{
        public OkAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("generic-dialog.button.ok") );
        }

        public void actionPerformed(ActionEvent e) {
            _FapplyChanges();
            dispose();
        }
    }

    private class CancelAction extends AbstractAction{
        public CancelAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("generic-dialog.button.cancel") );
        }

        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    private class ApplyAction extends AbstractAction{
        public ApplyAction(){
            putValue(AbstractAction.NAME, sikher.visual.forms.SikherProperties.getString("generic-dialog.button.apply"));
        }

        public void actionPerformed(ActionEvent e) {
            _FapplyChanges();
        }
    }

    private class HelpAction extends AbstractAction{
        public HelpAction(){
            putValue( AbstractAction.NAME, sikher.visual.forms.SikherProperties.getString("generic-dialog.button.help") );
        }

        public void actionPerformed(ActionEvent e) {
            //TODO: implement.
        }
    }
}
