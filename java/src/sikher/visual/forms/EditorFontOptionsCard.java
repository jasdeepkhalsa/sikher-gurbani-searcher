package sikher.visual.forms;

import sikher.searchengine.ISOLang;
import sikher.searchengine.GurbaniType;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 27.03.2006
 * Time: 21:54:54
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/forms/EditorFontOptionsCard.java,v 1.1 2006/04/21 13:05:40 mamonts Exp $
 * $Log: EditorFontOptionsCard.java,v $
 * Revision 1.1  2006/04/21 13:05:40  mamonts
 * First commit
 *
 * Description:
 */
public class EditorFontOptionsCard extends OptionsCard{

    public EditorFontOptionsCard( SikherIni prop, String editor ) {
        super( prop, new GridBagLayout() );
        setEditor( editor );
        _FinitComponents();
    }

    private String _FEditor = "editor";

    public void init() {
        _FchangeLangScheme();
    }

    public void apply() {
        String[] _keys = _FTempMap.keySet().toArray( new String[_FTempMap.size()] );
        for( String _key : _keys ){
            if( _key.startsWith( SikherIni.KEY_FONT_NAME ) ){
                FProperties.putProperty( _key, (String)_FTempMap.get(_key) );
            }else if( _key.startsWith( SikherIni.KEY_FONT_STYLE ) ){
                FProperties.putProperty( _key, String.valueOf( _FTempMap.get( _key ) ) );
            }else if( _key.startsWith( SikherIni.KEY_FONT_SIZE ) ){
                FProperties.putProperty( _key, String.valueOf( _FTempMap.get( _key ) ) );
            }else if( _key.startsWith( SikherIni.KEY_FONT_COLOR ) ){
                FProperties.putProperty( _key, String.valueOf( ((Color)_FTempMap.get( _key )).getRGB() ) );
            }
        }
        super.apply();
        _FTempMap.clear();
    }

    public void cancel(){
        _FTempMap.clear();
    }

    private Map<String, Object>_FTempMap = new TreeMap<String, Object>();
    private ISOLang[][] _FLangs = new ISOLang[3][];
    private ISOLang[] _FCurrentLangs = new ISOLang[3];

    public void setLangs( GurbaniType type, ISOLang[] langs ){
        if( type != null ){
            int _index = type.ordinal();
            if( _index < _FLangs.length ){
                _FLangs[_index] = langs;
                if( jcb_Types.getSelectedItem() == type ){
                    _FchangeLangScheme();
                }
            }
        }
    }

    private JComboBox jcb_Types = null;
    private JComboBox jcb_Languages = null;
    private JList jlst_FontName = null;
    private JList jlst_TypeFace = null;
    private JList jlst_Size = null;
    private JPanel jpnl_FontColor = null;
    private JLabel jl_Preview = null;

    private void _FinitComponents(){
        GridBagConstraints _const = new GridBagConstraints();

        _const.gridx = 0;
        _const.gridy = 0;
        _const.fill = GridBagConstraints.BOTH;
        _const.weightx = 1.;
        _const.weighty = 0.;
        _const.gridwidth = 3;
        JPanel _panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        _panel.setBorder( BorderFactory.createTitledBorder( "" ) );
        add( _panel, _const );
        _panel.add( new JLabel( SikherProperties.getString( "options.editorfont.label.type" ) ) );
        jcb_Types = new JComboBox( new Object[]{
                GurbaniType.GURBANI, GurbaniType.TRANSLITERATION, GurbaniType.TRANSLATION,
                SikherProperties.getString( "options.editorfont.cbitem.details" ) });
        jcb_Types.addItemListener( new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                _FchangeLangScheme();
            }
        });
        _panel.add( jcb_Types );
        _panel.add( new JLabel( SikherProperties.getString( "options.editorfont.label.language" ) ) );
        jcb_Languages = new JComboBox();
        _panel.add( jcb_Languages );

        _const.gridy = 1;
        _const.gridwidth = 1;
        _const.weighty = 1.;
        jlst_FontName = new JList( FProperties.getFontNames() );
        _FcreateListPanel( this, _const, "options.editorfont.panel.fontname",
                jlst_FontName, new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String _fontName = (String) jlst_FontName.getSelectedValue();
                int _style = jlst_TypeFace.getSelectedIndex();
                Integer _size = (Integer)jlst_Size.getSelectedValue();
                if( _size == null ) _size = 12;
                Font _font = new Font( _fontName, _style, _size );
                String _text = "";
                for( char _ch = 0; _ch < 0x0fff; _ch++ ){
                    if( _font.canDisplay( _ch ) && Character.isLetter( _ch ) ){
                        String _letter = "" + _ch;
                        _text += _letter.toUpperCase();
                        if( !_letter.toUpperCase().equals( _letter.toLowerCase() ) ){
                            _text += _letter.toLowerCase();
                        }
                    }
                    if( _text.length() == 6 ){
                        break;
                    }
                }
                jl_Preview.setFont( _font );
                jl_Preview.setText( _text );
                _FputProperty( SikherIni.KEY_FONT_NAME, _fontName );
            }
        });

        _const.gridx = 1;
        jlst_TypeFace = new JList( new String[]{
                SikherProperties.getString( "options.editorfont.typeface.plain" ),
                SikherProperties.getString( "options.editorfont.typeface.bold" ),
                SikherProperties.getString( "options.editorfont.typeface.italic" ),
                SikherProperties.getString( "options.editorfont.typeface.bolditalic" )
        } );
        _FcreateListPanel( this, _const, "options.editorfont.panel.typeface",
                jlst_TypeFace, new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int _style = jlst_TypeFace.getSelectedIndex();
                jl_Preview.setFont( jl_Preview.getFont().deriveFont( _style ) );
                _FputProperty( SikherIni.KEY_FONT_STYLE, _style );
            }
        });

        _const.gridx = 2;
        DefaultListModel _model = new DefaultListModel();
        jlst_Size = new JList( _model );
        Integer[] _sizes = new Integer[]{ 8, 10, 11, 12, 14, 16, 18 };
        for( Integer _size : _sizes ){
            _model.addElement( _size );
        }

        _FcreateListPanel( this, _const, "options.editorfont.panel.fontsize",
                jlst_Size, new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int _size = (Integer)jlst_Size.getSelectedValue();
                jl_Preview.setFont( jl_Preview.getFont().deriveFont( _size*1f ) );
                _FputProperty( SikherIni.KEY_FONT_SIZE, _size );
            }
        });

        _const.gridx = 0;
        _const.gridy = 2;
        _const.weighty = 0.;
        _panel = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
        add( _panel, _const );
        _panel.add( new JLabel( SikherProperties.getString( "options.editorfont.label.fontcolor" ) ) );

        jpnl_FontColor = new JPanel();
        jpnl_FontColor.setBorder( BorderFactory.createEtchedBorder() );
        jpnl_FontColor.setBackground( Color.black );
        _panel.add( jpnl_FontColor );

        JButton _btnChooseColor = new JButton(SikherProperties.getString("options.editorfont.button.fontcolor"));
        _btnChooseColor.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color _color = JColorChooser.showDialog( EditorFontOptionsCard.this,
                        SikherProperties.getString( "options.editorfont.colorchooser.title" ),
                        jpnl_FontColor.getBackground() );
                if( _color != null ){
                    jpnl_FontColor.setBackground( _color );
                    jl_Preview.setForeground( _color );
                    _FputProperty( SikherIni.KEY_FONT_COLOR, jl_Preview.getForeground() );
                }
            }
        });
        _panel.add( _btnChooseColor );

        _const.gridy = 3;
        _const.gridwidth = 3;
        _panel = new JPanel( new FlowLayout() );
        add( _panel, _const );
        _panel.setBorder( BorderFactory.createEtchedBorder() );
        jl_Preview = new JLabel( "AaBbCc" );
        _panel.add( jl_Preview );
    }

    private void _FcreateListPanel( JPanel panel, GridBagConstraints gbconst, String title,
                                    JList list, ListSelectionListener listener ){
        JPanel _panel = new JPanel( new GridLayout( 1, 1 ) );
        _panel.setBorder( BorderFactory.createTitledBorder( SikherProperties.getString( title ) ) );
        list.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        list.getSelectionModel().addListSelectionListener( listener );
        _panel.add( new JScrollPane( list ) );
        panel.add( _panel, gbconst );
    }

    private void _FputProperty( String key, Object value ){
        GurbaniType _type = null;
        Object _item = jcb_Types.getSelectedItem();
        if( _item != null ){
            ISOLang _lang = (ISOLang) jcb_Languages.getSelectedItem();
            if( _item instanceof GurbaniType ){
                _type = (GurbaniType) jcb_Types.getSelectedItem();
            }
            _FTempMap.put( FProperties.createFontKey( key, _type, _lang, _FEditor ), value );
        }
    }

    private void _FchangeLangScheme(){
        Object _item = jcb_Types.getSelectedItem();
        if( _item != null ){
            GurbaniType _type = null;
            ISOLang _lang = null;
            if( _item instanceof GurbaniType ){
                _type = (GurbaniType) jcb_Types.getSelectedItem();
                jcb_Languages.setModel( new DefaultComboBoxModel( _FLangs[_type.ordinal()] ) );
                jcb_Languages.setSelectedItem( _FCurrentLangs[_type.ordinal()] );

                if( jcb_Languages.getSelectedIndex() < 0 && jcb_Languages.getModel().getSize() > 0 ){
                    jcb_Languages.setSelectedIndex( 0 );
                }
                _lang = (ISOLang) jcb_Languages.getSelectedItem();
            }else{
                jcb_Languages.setModel( new DefaultComboBoxModel() );
            }
            String _fontName = (String) _FTempMap.get( FProperties.createFontKey( SikherIni.KEY_FONT_NAME, _type, _lang, _FEditor ) );
            if( _fontName == null ){
                _fontName = FProperties.getFontName( _type, _lang, _FEditor );
            }
            jlst_FontName.setSelectedValue( _fontName, true );
            Integer _style =(Integer) _FTempMap.get( FProperties.createFontKey( SikherIni.KEY_FONT_STYLE, _type, _lang, _FEditor ) );
            if( _style == null ){
                _style = FProperties.getFontStyle( _type, _lang, _FEditor );
            }
            jlst_TypeFace.setSelectedIndex( _style );
            Integer _size =(Integer) _FTempMap.get( FProperties.createFontKey( SikherIni.KEY_FONT_SIZE, _type, _lang, _FEditor ) );
            if( _size == null ){
                _size = FProperties.getFontSize( _type, _lang, _FEditor );
            }
            jlst_Size.setSelectedValue( _size, true );
            Color _color =(Color) _FTempMap.get( FProperties.createFontKey( SikherIni.KEY_FONT_COLOR, _type, _lang, _FEditor ) );
            if( _color == null ){
                _color = FProperties.getFontColor( _type, _lang, _FEditor );
            }
            jpnl_FontColor.setBackground( _color );
            jl_Preview.setForeground( _color );
        }
    }

    public String getEditor() {
        return _FEditor;
    }

    public void setEditor(String editor) {
        _FEditor = editor;
    }
}
