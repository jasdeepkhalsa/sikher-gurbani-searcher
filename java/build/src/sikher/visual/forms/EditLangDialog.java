package sikher.visual.forms;

import sikher.templates.SpringUtilities;
import sikher.searchengine.ISOLang;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 27.12.2005
 * Time: 18:23:09
 * $Header$
 * $Log$
 * Description:
 */
public class EditLangDialog extends sikher.visual.forms.ModalDialog {
    public EditLangDialog( JFrame owner ) {
        super(owner, "");
        _FinitComponents();
    }

    private JComboBox jcb_Lang;
    private JTextField jtf_FontPath;

    private void _FinitComponents(){
        JPanel _mainPanel = getDialogPanel();
        _mainPanel.setLayout( new BorderLayout() );
        _mainPanel.setBorder( BorderFactory.createEtchedBorder() );

        JPanel _p = new JPanel( new SpringLayout() );
        _mainPanel.add( _p, BorderLayout.NORTH );

        ISOLang[] _langs = new ISOLang[ISOLang.values().length-1];
        System.arraycopy( ISOLang.values(), 1, _langs, 0, _langs.length );
        Arrays.sort(_langs, new Comparator<ISOLang>(){
            public int compare(ISOLang o1, ISOLang o2) {
                return o1.toString().compareTo( o2.toString() );
            }
        });
        jcb_Lang = new JComboBox( _langs );
        SpringUtilities.setupRow( _p,
                new JLabel(sikher.visual.forms.SikherProperties.getString("editlang-dialog.label.lang")), jcb_Lang );

        JPanel _panel = new JPanel( new BorderLayout() );
        jtf_FontPath = new JTextField(20);
        _panel.add( jtf_FontPath, BorderLayout.CENTER );
        _panel.add( new JButton( new BrowseAction() ), BorderLayout.EAST );

        SpringUtilities.setupRow( _p,
                new JLabel(sikher.visual.forms.SikherProperties.getString("editlang-dialog.label.font")), _panel);

        SpringUtilities.makeCompactGrid( _p, 2, 2, 4, 4, 4, 4 );

        pack();
    }

    public void okPressed(){
        if( getLang().equals( ISOLang.NONE ) ){
            showErrorMessage( getTitle(), sikher.visual.forms.SikherProperties.getString("editlang-dialog.error.nolang") );
        }else if( getFontPath().trim().equals("") ){
            showErrorMessage( getTitle(), sikher.visual.forms.SikherProperties.getString("editlang-dialog.error.nofont") );
        }else{
            super.okPressed();
        }
    }

    public void setDialogType( boolean bNew ){
        setTitle( bNew ?sikher.visual.forms.SikherProperties.getString("editlang-dialog.title.new") :
                sikher.visual.forms.SikherProperties.getString("editlang-dialog.title.edit") );
        if( bNew ){
            jcb_Lang.setSelectedIndex(0);
            jtf_FontPath.setText("");
        }
    }

    public void setLang( String lang ){
        jcb_Lang.setSelectedItem( ISOLang.valueOf( lang ) );
    }

    public String getLang(){
        return ((ISOLang)jcb_Lang.getSelectedItem()).getCode();
    }

    public void setFontPath( String fontpath ){
        jtf_FontPath.setText( fontpath );
    }

    public String getFontPath(){
        return jtf_FontPath.getText();
    }

    private class BrowseAction extends AbstractAction{
        private JFileChooser _FFileChooser;

        public BrowseAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("editlang-dialog.button.browse") );
            _FFileChooser = new JFileChooser();
            _FFileChooser.setMultiSelectionEnabled( false );
            _FFileChooser.setFileFilter( new FileFilter(){

                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".ttf");
                }

                public String getDescription() {
                    return "*.ttf";
                }
            } );
        }

        public void actionPerformed(ActionEvent e) {
            int _res = _FFileChooser.showOpenDialog( EditLangDialog.this );
            if( _res == JFileChooser.APPROVE_OPTION ){
                setFontPath( _FFileChooser.getSelectedFile().getAbsolutePath() );
            }
        }
    }

}
