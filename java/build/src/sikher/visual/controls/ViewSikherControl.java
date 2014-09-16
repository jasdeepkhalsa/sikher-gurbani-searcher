package sikher.visual.controls;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.forms.SikherProperties;
import sikher.visual.sidepanel.SikherControlEvent;
import sikher.templates.SpringUtilities;
import sikher.searchengine.XPathSearcher;
import sikher.searchengine.ISOLang;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 25.01.2006
 * Time: 13:26:11
 * $Header$
 * $Log$
 * Description:
 */
public class ViewSikherControl extends SikherControl {

    public ViewSikherControl(XPathSearcher searcher, StatusBar statusBar) {
        super(searcher, statusBar, new BorderLayout());
        setControlName( SikherProperties.getString("control.view.name") );
        _FinitComponents();
    }

    protected void searcherInfoUpdated(){
        _FinitComboBox( jcb_Gurbani, FSearcher.getGurbanies(), EVENT_LANG_GURBANI );
        _FinitComboBox( jcb_Translation, FSearcher.getTranslations(), EVENT_LANG_TRANSLATION );
        _FinitComboBox( jcb_Transliteration, FSearcher.getTransliterations(), EVENT_LANG_TRANSLATION );
    }

    private void _FinitComboBox( JComboBox comboBox, ISOLang[] _langs, String eventName ){
        comboBox.setModel( new DefaultComboBoxModel(_langs) );
        ISOLang _lang = (ISOLang) comboBox.getSelectedItem();
        fireControlEvent( new SikherControlEvent( this, eventName, _lang ) );
    }

    /**
     * Event names
     */
    public static final String EVENT_LANG_GURBANI       = "event_lang_gurbani";
    public static final String EVENT_LANG_TRANSLIT      = "event_lang_translit";
    public static final String EVENT_LANG_TRANSLATION   = "event_lang_translation";

    public static final String EVENT_SHOW_GURBANI       = "event_show_gurbani";
    public static final String EVENT_SHOW_TRANSLIT      = "event_show_translit";
    public static final String EVENT_SHOW_TRANSLATION   = "event_show_translation";
    public static final String EVENT_SHOW_DETAILS       = "event_show_details";

    public static final String EVENT_VIEW_MODE          = "event_view_mode";

    /**
     * GUI components
     */
    private JComboBox jcb_Gurbani = null;
    private JComboBox jcb_Translation = null;
    private JComboBox jcb_Transliteration = null;
    private JCheckBox jckb_Gurbani = null;
    private JCheckBox jckb_Translation = null;
    private JCheckBox jckb_Transliteration = null;
    private JCheckBox jckb_Details = null;
    private JRadioButton jrb_SplitView = null;
    private JRadioButton jrb_CombiView = null;

    private void _FinitComponents(){
        JPanel _p = new JPanel( new BorderLayout() );
        _p.setBorder(BorderFactory.createTitledBorder(
                SikherProperties.getString("control.view.panel.options")));

        JPanel _panel1 = new JPanel(new SpringLayout());
        _p.add(_panel1, BorderLayout.CENTER);

        ChangeLangListener _changeLangListener = new ChangeLangListener();
        jcb_Gurbani = _FcreateComboBox( "control.view.label.gurbani", _panel1, _changeLangListener );
        jcb_Translation = _FcreateComboBox( "control.view.label.translation", _panel1, _changeLangListener);
        jcb_Transliteration = _FcreateComboBox( "control.view.label.transliteration", _panel1, _changeLangListener );
        SpringUtilities.makeCompactGrid( _panel1, 3, 2, 4, 4, 4, 4 );

        JPanel _panel2 = new JPanel( new GridLayout(4, 1) );
        _p.add(_panel2, BorderLayout.SOUTH);

        ShowButtonListener _showlistener = new ShowButtonListener();
        jckb_Gurbani = _FcreateShowOption( "control.view.checkbox.gurbani", _showlistener, true );
        _panel2.add( jckb_Gurbani );

        jckb_Transliteration = _FcreateShowOption( "control.view.checkbox.transliteration", _showlistener, true );
        _panel2.add( jckb_Transliteration );

        jckb_Translation = _FcreateShowOption( "control.view.checkbox.translation", _showlistener, true );
        _panel2.add( jckb_Translation );

        jckb_Details = _FcreateShowOption( "control.view.checkbox.details", _showlistener, true );
        _panel2.add( jckb_Details );
        add( _p, BorderLayout.CENTER );

        //view panel
        _p = new JPanel( new GridLayout(2, 1) );
        _p.setBorder(BorderFactory.createTitledBorder(SikherProperties.getString("control.view.panel.viewmode")));

        ButtonGroup _bg = new ButtonGroup();
        ActionListener _switchListener = new SwitchViewListener();
        jrb_SplitView = _FcreateSwitch( "control.view.radio.splitview", _bg, _switchListener );
        jrb_SplitView.setSelected(true);
        _p.add( jrb_SplitView );
        jrb_CombiView = _FcreateSwitch( "control.view.radio.combiview", _bg, _switchListener );
        _p.add( jrb_CombiView );
        add( _p, BorderLayout.SOUTH );
    }

    private JComboBox _FcreateComboBox( String name, JPanel panel, ItemListener listener ){
        JLabel _label = new JLabel( SikherProperties.getString(name) );
        JComboBox _combobox = new JComboBox();
        _combobox.addItemListener( listener );
        SpringUtilities.setupRow( panel, _label, _combobox );
        return _combobox;
    }

    private JCheckBox _FcreateShowOption( String name, ActionListener listener, boolean bSelected ){
        JCheckBox _box = new JCheckBox( SikherProperties.getString(name) );
        _box.setSelected( bSelected );
        _box.addActionListener( listener );
        return _box;
    }

    private JRadioButton _FcreateSwitch( String name, ButtonGroup bg, ActionListener listener ){
        JRadioButton _button = new JRadioButton( SikherProperties.getString(name) );
        bg.add( _button );
        _button.addActionListener( listener );
        return _button;
    }

    private void _FfireValueChange( String eventName, Object value ){
        fireControlEvent(
                new SikherControlEvent( this, eventName, value ) );
    }

    private class ShowButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JCheckBox _button = (JCheckBox) e.getSource();
            boolean _bSelected = _button.isSelected();
            String _eventName = null;
            if( _button == jckb_Gurbani ){
                _eventName = EVENT_SHOW_GURBANI;
            }else if( _button == jckb_Translation ){
                _eventName = EVENT_SHOW_TRANSLATION;
            }else if( _button == jckb_Transliteration ){
                _eventName = EVENT_SHOW_TRANSLIT;
            }else if( _button == jckb_Details ){
                _eventName = EVENT_SHOW_DETAILS;
            }else{
                return;
            }
            _FfireValueChange( _eventName, _bSelected );
        }
    }

    private class SwitchViewListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            JRadioButton _button = (JRadioButton) e.getSource();
            boolean _bSelected = _button.isSelected();
            String _eventName = null;
            if( _button == jrb_SplitView ){
                _eventName = EVENT_VIEW_MODE;
            }else if( _button == jrb_CombiView ){
                _eventName = EVENT_VIEW_MODE;
                _bSelected = !_bSelected;
            }else{
                return;
            }
            _FfireValueChange( _eventName, _bSelected );
        }
    }

    private class ChangeLangListener implements ItemListener{

        public void itemStateChanged(ItemEvent e) {
            if ( e.getStateChange() == ItemEvent.SELECTED ){
                JComboBox _comboBox = (JComboBox) e.getSource();
                String _eventName = null;
                if( _comboBox == jcb_Gurbani ){
                    _eventName = EVENT_LANG_GURBANI;
                }else if( _comboBox == jcb_Transliteration ){
                    _eventName = EVENT_LANG_TRANSLIT;
                }else if( _comboBox == jcb_Translation ){
                    _eventName = EVENT_LANG_TRANSLATION;
                }else{
                    return;
                }
                _FfireValueChange( _eventName, _comboBox.getSelectedItem() );
            }
        }
    }

    public ISOLang getGurbani(){
        return (ISOLang) jcb_Gurbani.getSelectedItem();
    }

    public ISOLang getTranslit(){
        return (ISOLang) jcb_Transliteration.getSelectedItem();
    }

    public ISOLang getTranslation(){
        return (ISOLang) jcb_Translation.getSelectedItem();
    }

    public boolean isShowGurbani(){
        return jckb_Gurbani.isSelected();
    }

    public boolean isShowTranslit(){
        return jckb_Transliteration.isSelected();
    }

    public boolean isShowTranslation(){
        return jckb_Translation.isSelected();
    }

    public boolean isShowDetails(){
        return jckb_Details.isSelected();
    }

    public boolean isViewMode(){
        return jrb_SplitView.isSelected();
    }
}
