package sikher.visual.forms;

import sikher.templates.VisualUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 21.12.2005
 * Time: 14:35:40
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/ModalDialog.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: ModalDialog.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class ModalDialog extends JDialog{
    public ModalDialog ( JFrame owner, String title){
        super ( owner, title, true );
        _FinitComponents();
    }

    private JButton jb_Ok;
    private JButton jb_Cancel;
    private JPanel jp_ButtonPanel;
    private JPanel jp_DialogPanel;
    private ArrayList _FButtons;

    public static int CANCEL = 0;
    public static int OK     = 1;

    private int _fResult;

    private void _FinitComponents (){
        _FButtons = new ArrayList();
        jb_Ok = new JButton( new OkAction() );
        jb_Cancel = new JButton( new CancelAction() );
        _FButtons.add( jb_Ok );
        _FButtons.add( jb_Cancel );

        jp_ButtonPanel = (JPanel) VisualUtils.getCommandRow( new JButton[]{jb_Ok, jb_Cancel} );

        Container _pane = getContentPane();
        _pane.add( jp_ButtonPanel, BorderLayout.PAGE_END );

        jp_DialogPanel = new JPanel( new BorderLayout() );
        _pane.add( jp_DialogPanel, BorderLayout.CENTER );
    }

    public void okPressed (){
        dispose();
    }
    public void cancelPressed (){
        dispose();
    }

    public JPanel getDialogPanel (){
        return jp_DialogPanel;
    }

    public int open (){
        centerOnParent();
        jb_Ok.requestFocus();
        setVisible (true);
        return _fResult;
    }

    private class OkAction extends AbstractAction{
        public OkAction (){
            putValue ( AbstractAction.NAME, SikherProperties.getString("generic-dialog.button.ok") );
        }

        public void actionPerformed( ActionEvent e ) {
            _fResult = OK;
            okPressed();
        }
    }

    private class CancelAction extends AbstractAction{
        public CancelAction (){
            putValue ( AbstractAction.NAME, SikherProperties.getString("generic-dialog.button.cancel") );
        }

        public void actionPerformed( ActionEvent e ) {
            _fResult = CANCEL;
            cancelPressed();
        }
    }

    /**
     * Set another name for Ok button.
     * @param name - new name
     */
    public void setOKName (String name){
        jb_Ok.setText( name );
    }

    /**
     * Set another name for Cancel button.
     * @param name - new name
     */
    public void setCancelName (String name){
        jb_Ok.setText( name );
    }

    public JButton getOkButton (){
        return jb_Ok;
    }

    public JButton getCancelButton (){
        return jb_Cancel;
    }

    public void addButton ( JButton button ){
        _FButtons.add( button );
        jp_ButtonPanel.removeAll();
        VisualUtils.getCommandRow( jp_ButtonPanel, (JButton[]) _FButtons.toArray( new JButton[_FButtons.size()] ), SwingUtilities.CENTER );
        jp_ButtonPanel.revalidate();
    }

    public void centerOnParent(){
        VisualUtils.centerOnParent( this );
    }

    public void centerOnScreen (){
        VisualUtils.centerOnScreen( this );
    }

    public void showErrorMessage( String title, String text ){
        JOptionPane.showMessageDialog(this, text, title, JOptionPane.ERROR_MESSAGE );
    }

}
