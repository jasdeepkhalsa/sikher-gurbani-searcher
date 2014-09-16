package sikher.visual.forms;

import sikher.templates.VisualUtils;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: Lilu
 * Date: 12.04.2006
 * Time: 16:03:36
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/FontsOptionsCard.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: FontsOptionsCard.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class FontsOptionsCard extends OptionsCard{
    public FontsOptionsCard( SikherIni prop ) {
        super( prop, new GridBagLayout() );
        setTitle( SikherProperties.getString( "options.fonts.name" ) );
        _FinitComponents();
    }

    public void init() {
        String[] _pathes = FProperties.getFontFilesAndDirs();
        MapRootNode _root = (MapRootNode) jtr_Fonts.getModel().getRoot();
        _root.removeAllChildren();

        for( String _path : _pathes ){
            File _file = new File( _path );
            if( _file.exists() && !_file.isDirectory() ){
                String _dir = _file.getPath();
                DefaultMutableTreeNode _child = (DefaultMutableTreeNode) _root.getChild( _dir );
                if( _child == null ){
                    _child = new DefaultMutableTreeNode( _dir );
                }
                _root.add( _child );
                _child.add( new DefaultMutableTreeNode( _file.getName() ) );
            }
        }
    }

    private JTree jtr_Fonts;

    private void _FinitComponents(){
        setBorder( BorderFactory.createEtchedBorder() );
        GridBagConstraints _const = new GridBagConstraints();
        _const.insets = new Insets( 4, 4, 4, 4 );
        _const.weightx = 1.0;
        _const.weighty = 1.0;
        _const.gridwidth = 3;
        _const.gridheight = 6;
        _const.fill = GridBagConstraints.BOTH;
        jtr_Fonts = new JTree( new MapRootNode() );
        jtr_Fonts.setBorder( BorderFactory.createEtchedBorder() );
        jtr_Fonts.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
        jtr_Fonts.setRootVisible( false );
        add( jtr_Fonts, _const );

        JButton _addButton = new JButton( new AddAction() );
        JButton _removeButton = new JButton( new RemoveAction() );
        VisualUtils.equalizeSizes( new JButton[]{ _addButton, _removeButton } );

        _const.gridx = 3;
        _const.gridheight = 1;
        _const.gridwidth = 1;
        _const.weighty = 0.0;
        _const.weightx = 0.0;
        _const.fill = GridBagConstraints.NONE;
        add( _addButton, _const );

        _const.gridy = 1;
        add( _removeButton, _const );
    }

    private class AddAction extends AbstractAction{

        public AddAction(){
            putValue( NAME, SikherProperties.getString( "options.fonts.button.add" ) );
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class RemoveAction extends AbstractAction{
        public RemoveAction(){
            putValue( NAME, SikherProperties.getString( "options.fonts.button.remove" ) );
        }

        public void actionPerformed(ActionEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class MapRootNode extends DefaultMutableTreeNode{
        private Map<String, MutableTreeNode>_FChildMap = new TreeMap<String, MutableTreeNode>();

        public void add( MutableTreeNode node ){
            super.add( node );
            _FChildMap.put( node.toString(), node );
        }

        public void remove( MutableTreeNode node ){
            super.remove( node );
            _FChildMap.remove( node.toString() );
        }

        public MutableTreeNode getChild( String name ){
            return _FChildMap.get( name );
        }
    }
}

