package sikher.visual.forms;

import sikher.templates.VisualUtils;
import sikher.visual.forms.EditLangDialog;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import com.lowagie.text.DocumentException;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 23.12.2005
 * Time: 12:27:58
 * $Header$
 * $Log$
 * Description:
 */
public class ExportOptionsCard extends sikher.visual.forms.OptionsCard {

    public ExportOptionsCard( JFrame owner, SikherIni prop) {
        super( owner, prop, new BorderLayout());
        _FinitComponents();
    }

    private JList jlt_Langs;
    private JTextField jtf_Path;
    private JButton jb_Remove;
    private sikher.visual.forms.EditLangDialog _FEditLangDialog;

    private void _FinitComponents(){
        JPanel _panel = new JPanel( new BorderLayout() );
        _panel.setBorder( BorderFactory.createTitledBorder(
                sikher.visual.forms.SikherProperties.getString("export-options.lang-panel.title") ) );
        add( _panel, BorderLayout.CENTER );

        jlt_Langs = new JList( new DefaultListModel() );
        jlt_Langs.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        jlt_Langs.addListSelectionListener( new LangSelectionListener() );
        jlt_Langs.addMouseListener( new DoubleClickListener() );
        _panel.add( new JScrollPane( jlt_Langs ), BorderLayout.CENTER );

        jb_Remove = new JButton( new RemoveLangAction() );
        JButton[] _buttons = new JButton[]{
                new JButton( new AddLangAction() ), jb_Remove };
        VisualUtils.equalizeSizes( _buttons );
        Box _box = Box.createVerticalBox();
        for( JButton _button : _buttons ){
            _box.add( _button );
            _box.add( Box.createVerticalStrut(4) );
        }
        _panel.add( _box, BorderLayout.EAST );

        _panel = new JPanel();
        _panel.setLayout( new BoxLayout( _panel, BoxLayout.X_AXIS ) );
        _panel.add( Box.createHorizontalStrut(4) );
        _panel.add( new JLabel( SikherProperties.getString("export-options.label.font") ) );
        _panel.add( Box.createHorizontalStrut(4) );
        jtf_Path = new JTextField(10);
        jtf_Path.setEditable( false );
        _panel.add( jtf_Path );
        _panel.add( Box.createHorizontalStrut(4) );
        add( _panel, BorderLayout.SOUTH );

        _FEditLangDialog = new EditLangDialog( FFrame );
    }

    public void init() {
        String[] _langs = FProperties.getAllLangs();
        DefaultListModel _model = (DefaultListModel) jlt_Langs.getModel();
        _model.removeAllElements();
        for( String _lang : _langs ){
            _model.addElement( _lang );
        }
        jb_Remove.setEnabled( _langs.length > 0 );
    }

    public void apply() {
        //do nothing
    }

    private void _FAddLang( boolean bNew ){
        _FEditLangDialog.setDialogType( bNew );
        int _res = _FEditLangDialog.open();
        if( _res == sikher.visual.forms.ModalDialog.OK ){
            //add new value
            String _lang = _FEditLangDialog.getLang();
            String _font = _FEditLangDialog.getFontPath();
            try {
                FProperties.setFontPath( _lang, _font );
            } catch (Exception e) {
                //TODO: show error message
            }

            DefaultListModel _model = (DefaultListModel) jlt_Langs.getModel();
            if( !_model.contains( _lang ) ){
                _model.addElement( _lang );
            }
            jlt_Langs.setSelectedValue( _lang, true );
        }
    }

    private class AddLangAction extends AbstractAction{

        public AddLangAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("export-options.button.addlang") );
        }

        public void actionPerformed(ActionEvent e) {
            _FAddLang( true );
        }
    }

    private class RemoveLangAction extends AbstractAction{
        public RemoveLangAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("export-options.button.removelang") );
        }

        public void actionPerformed(ActionEvent e) {
            String _lang = (String) jlt_Langs.getSelectedValue();
            if( _lang == null ){
                return;
            }
            FProperties.removeFontPath( _lang );
            DefaultListModel _model = (DefaultListModel) jlt_Langs.getModel();
            _model.removeElement( _lang );
        }
    }

    private class LangSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            String _lang = (String) jlt_Langs.getSelectedValue();
            if ( _lang != null ){
                String _filename = FProperties.getFontPath( _lang );
                jtf_Path.setText( _filename != null ? _filename : "" );
                jb_Remove.setEnabled( true );
            }else{
                jtf_Path.setText("");
                jb_Remove.setEnabled( false );
            }
        }
    }

    private class DoubleClickListener extends MouseAdapter{
        public void mouseClicked( MouseEvent me ){
            if( me.getButton() == MouseEvent.BUTTON1 && me.getClickCount() == 2 ){
                String _lang = (String) jlt_Langs.getSelectedValue();
                if( _lang != null ){
                    String _filename = FProperties.getFontPath( _lang );
                    _FEditLangDialog.setLang( _lang );
                    _FEditLangDialog.setFontPath( _filename );
                    _FAddLang( false );
                    jtf_Path.setText( _FEditLangDialog.getFontPath() );
                }
            }
        }
    }
}
