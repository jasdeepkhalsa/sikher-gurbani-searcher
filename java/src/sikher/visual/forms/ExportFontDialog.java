package sikher.visual.forms;

import sikher.searchengine.ISOLang;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 23.03.2006
 * Time: 14:49:16
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/ExportFontDialog.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ExportFontDialog.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ExportFontDialog extends ModalDialog{

    public ExportFontDialog( JFrame owner ) {
        super( owner, SikherProperties.getString( "dialog.exportfont.title" ) );
        _FinitComponents();
    }

    private OptionsPanel jpnl_Gurbani;
    private OptionsPanel jpnl_Translit;
    private OptionsPanel jpnl_Translation;
    private OptionsPanel jpnl_Details;

    private void _FinitComponents(){
        JPanel _mainPanel = getDialogPanel();
        _mainPanel.setLayout( new BorderLayout() );

        JPanel _panel = new JPanel( new GridLayout( 4, 1 ) );
        _mainPanel.add( _panel, BorderLayout.NORTH );

        jpnl_Gurbani = new OptionsPanel( SikherProperties.getString( "dialog.exportfont.panel.gurbani" ) );
        _panel.add( jpnl_Gurbani );

        jpnl_Translit = new OptionsPanel( SikherProperties.getString( "dialog.exportfont.panel.translit" ) );
        _panel.add( jpnl_Translit );

        jpnl_Translation = new OptionsPanel( SikherProperties.getString( "dialog.exportfont.panel.translation" ) );
        _panel.add( jpnl_Translation );

        jpnl_Details = new OptionsPanel( SikherProperties.getString( "dialog.exportfont.panel.details" ) );
        _panel.add( jpnl_Details );

        pack();
    }

    public void setGurbaniLang( ISOLang lang, boolean visible, String fontPath ){
        jpnl_Gurbani.setOptions( lang, visible, fontPath );
    }

    public void setTranslitLang( ISOLang lang, boolean visible, String fontPath ){
        jpnl_Translit.setOptions( lang, visible, fontPath );
    }

    public void setTranslationLang( ISOLang lang, boolean visible, String fontPath ){
        jpnl_Translation.setOptions( lang, visible, fontPath );
    }

    public void setDetailsLang( ISOLang lang, boolean visible, String fontPath ){
        jpnl_Details.setOptions( lang, visible, fontPath );
    }

    public String getGurbaniFont(){
        return jpnl_Gurbani.getFontPath();
    }

    public String getTranslitFont(){
        return jpnl_Translit.getFontPath();
    }

    public String getTranslationFont(){
        return jpnl_Translation.getFontPath();
    }

    public String getDetailsFont(){
        return jpnl_Details.getFontPath();
    }

    private class OptionsPanel extends JPanel{
        public OptionsPanel( String title ){
            super( new GridBagLayout() );
            setBorder( BorderFactory.createTitledBorder( title ) );
            _FinitComponents();
        }

        public void setOptions( ISOLang lang, boolean visible, String fontPath ){
            jtf_Language.setText( lang.toString() );
            jtf_FontPath.setText( fontPath );
            setOptionsEnabled( visible );
        }

        public String getFontPath(){
            return jtf_FontPath.getText();
        }

        public boolean isOptionsEnabled(){
            return jtf_Language.isEnabled();
        }

        private JTextField jtf_Language = null;
        private JTextField jtf_FontPath = null;
        private JButton jb_Browse = null;
        private JLabel jl_Preview = null;

        private void _FinitComponents(){
            GridBagConstraints _const = new GridBagConstraints();
            _const.gridx = 0;
            _const.gridy = 0;
            _const.insets = new Insets( 4, 4, 4, 4 );
            _const.anchor = GridBagConstraints.WEST;
            _const.weightx = 0.0;
            add( new JLabel( SikherProperties.getString( "dialog.exportfont.label.language" ) ), _const );

            _const.gridx = 1;
            _const.weightx = 1.0;
            _const.fill = GridBagConstraints.HORIZONTAL;
            jtf_Language = new JTextField( 20 );
            jtf_Language.setEditable( false );
            add( jtf_Language, _const );

            _const.gridx = 0;
            _const.gridy = 1;
            _const.fill = GridBagConstraints.NONE;
            _const.weightx = 0.0;
            add( new JLabel( SikherProperties.getString( "dialog.exportfont.label.font" ) ), _const );

            _const.gridx = 1;
            _const.fill = GridBagConstraints.HORIZONTAL;
            _const.weightx = 1.0;
            jtf_FontPath = new JTextField( 20 );
            add( jtf_FontPath, _const );

            _const.gridx = 2;
            _const.fill = GridBagConstraints.NONE;
            _const.anchor = GridBagConstraints.LINE_END;
            _const.weightx = 0.0;
            jb_Browse = new JButton( new BrowseAction() );
            add( jb_Browse, _const );

            _const.gridx = 0;
            _const.gridy = 2;
            _const.gridwidth = 3;
            _const.fill = GridBagConstraints.HORIZONTAL;
            JPanel _previewPanel = new JPanel( new FlowLayout() );
            _previewPanel.setBorder( BorderFactory.createEtchedBorder() );
            jl_Preview = new JLabel();
            _previewPanel.add( jl_Preview );
            add( _previewPanel, _const );
        }

        private void setOptionsEnabled( boolean enabled ){
            jtf_Language.setEnabled( enabled );
            jtf_FontPath.setEnabled( enabled );
            jb_Browse.setEnabled( enabled );
            jl_Preview.setEnabled( enabled );
        }

        private class BrowseAction extends AbstractAction{
            private JFileChooser _FFileDialog = null;

            public BrowseAction(){
                putValue( NAME, SikherProperties.getString( "dialog.exportfont.button.browse" ) );
                FileFilter _filter = new FileFilter(){
                    public boolean accept(File f) {
                        return f.isDirectory() || f.getName().endsWith( ".ttf" );
                    }

                    public String getDescription() {
                        return "*" + ".ttf";
                    }
                };
                _FFileDialog = new JFileChooser();
                _FFileDialog.setDialogType( JFileChooser.OPEN_DIALOG );
                _FFileDialog.setFileFilter( _filter );
                _FFileDialog.setMultiSelectionEnabled( false );

            }

            public void actionPerformed(ActionEvent e) {
                int _res = _FFileDialog.showOpenDialog( ExportFontDialog.this );
                if( _res == JFileChooser.APPROVE_OPTION ){
                    File _file = _FFileDialog.getSelectedFile();
                    String _path =_file.getAbsolutePath();
                    jtf_FontPath.setText( _path );
                }
            }
        }
    }
}
