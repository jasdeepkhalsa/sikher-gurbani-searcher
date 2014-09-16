package sikher.visual.forms;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 23.12.2005
 * Time: 12:27:58
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/ExportOptionsCard.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ExportOptionsCard.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ExportOptionsCard extends sikher.visual.forms.OptionsCard {

    public ExportOptionsCard( SikherIni prop) {
        super(prop, new BorderLayout());
        //_FinitComponents();
    }

    public void init() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /*
    private JList jlt_FontDirs;
    private JButton jb_Remove;
    private JFileChooser _FFileDialog;

    private void _FinitComponents(){
        JPanel _panel = new JPanel( new BorderLayout() );
        _panel.setBorder( BorderFactory.createTitledBorder(
                SikherProperties.getString("export-options.fontdirs-panel.title" ) ) );
        add( _panel, BorderLayout.CENTER );

        jlt_FontDirs = new JList( new DefaultListModel() );
        jlt_FontDirs.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        jlt_FontDirs.addListSelectionListener( new LangSelectionListener() );
        _panel.add( new JScrollPane( jlt_FontDirs ), BorderLayout.CENTER );

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

        _FFileDialog = new JFileChooser();
        _FFileDialog.setMultiSelectionEnabled( false );
        _FFileDialog.setFileSelectionMode(  JFileChooser.DIRECTORIES_ONLY );
    }

    public void init() {
        String[] _dirs = FProperties.getFontDirs();
        DefaultListModel _model = (DefaultListModel) jlt_FontDirs.getModel();
        _model.removeAllElements();
        for( String _dir : _dirs ){
            _model.addElement( _dir );
        }
        jb_Remove.setEnabled( _dirs.length > 0 );
    }

    public void apply() {
        //do nothing
    }

    private void _FAddDir(){
        if( _FFileDialog.showOpenDialog( this ) == JFileChooser.APPROVE_OPTION ){
            //add new value
            String _dir = _FFileDialog.getSelectedFile().getAbsolutePath();
            try {
                FProperties.addFontDir( _dir );
            } catch (Exception e) {
                //TODO: show error message
            }

            DefaultListModel _model = (DefaultListModel) jlt_FontDirs.getModel();
            if( !_model.contains( _dir ) ){
                _model.addElement( _dir );
            }
            jlt_FontDirs.setSelectedValue( _dir, true );
        }
    }

    private class AddLangAction extends AbstractAction{

        public AddLangAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("export-options.button.addlang") );
        }

        public void actionPerformed(ActionEvent e) {
            _FAddDir();
        }
    }

    private class RemoveLangAction extends AbstractAction{
        public RemoveLangAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("export-options.button.removelang") );
        }

        public void actionPerformed(ActionEvent e) {
            String _lang = (String) jlt_FontDirs.getSelectedValue();
            if( _lang == null ){
                return;
            }
            FProperties.removeFontDir( _lang );
            DefaultListModel _model = (DefaultListModel) jlt_FontDirs.getModel();
            _model.removeElement( _lang );
        }
    }

    private class LangSelectionListener implements ListSelectionListener {
        public void valueChanged( ListSelectionEvent e) {
            String _dir = (String) jlt_FontDirs.getSelectedValue();
            jb_Remove.setEnabled( _dir != null );
        }
    }*/
}
