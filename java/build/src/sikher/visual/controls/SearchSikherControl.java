package sikher.visual.controls;

import sikher.searchengine.*;
import sikher.visual.forms.SikherProperties;
import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.SikherControlEvent;
import sikher.templates.SpringUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 24.01.2006
 * Time: 14:16:38
 * $Header$
 * $Log$
 * Description:
 */
public class SearchSikherControl extends SikherControl {
    private XPathSearcher _FSearcher;

    public SearchSikherControl( XPathSearcher searcher, StatusBar statusbar ){
        super( searcher, statusbar, new BorderLayout() );
        setControlName( sikher.visual.forms.SikherProperties.getString("control.search.name") );
        _FSearcher = searcher;
        _FinitComponents();
    }

    public static final String EVENT_FIND = "event_find";

    public void init(){
        super.init();
        jtf_Text.requestFocus();
        Container _container = getFrame();
        if( _container instanceof JFrame ){
            ((JFrame)_container).getRootPane().setDefaultButton( jb_Search );
        }
    }

    protected void searcherInfoUpdated(){
        //init the components with values
        SearchFlag [] _flags = _FSearcher.getFlags();
        GurbaniType[] _types = _FSearcher.getTypes();
        NumberedElement[] _scriptures = _FSearcher.getBooks();
        AuthorElement[] _authors = _FSearcher.getAuthors();
        NumberedElement[] _raag = _FSearcher.getRaags();
        int _minPage = _FSearcher.getMinPage();
        int _maxPage = _FSearcher.getMaxPage();

        jcb_SearchFlags.setModel( new DefaultComboBoxModel( _flags ) );
        jcb_Types.setModel( new DefaultComboBoxModel( _types ) );
        jcb_Books.setModel( new DefaultComboBoxModel( _scriptures ) );
        jcb_Authors.setModel( new DefaultComboBoxModel( _authors ) );
        jcb_Raags.setModel( new DefaultComboBoxModel( _raag ) );
        jspr_From.setModel( new SpinnerNumberModel( _minPage, _minPage, _maxPage, 1 ) );
        jspr_To.setModel( new SpinnerNumberModel( _maxPage, _minPage, _maxPage, 1 ) );
        jspr_Exact.setModel( new SpinnerNumberModel( _minPage, _minPage, _maxPage, 1 ) );

    }

    private JTextField jtf_Text;
    private JButton jb_Search;

    private JComboBox jcb_SearchFlags = null;
    private JComboBox jcb_Types = null;
    private JComboBox jcb_Books = null;
    private JComboBox jcb_Authors = null;
    private JComboBox jcb_Raags = null;

    private JRadioButton jrb_All = null;
    private JRadioButton jrb_Range = null;
    private JRadioButton jrb_Exact = null;
    private JRadioButton jrb_Random = null;
    private JSpinner jspr_From = null;
    private JSpinner jspr_To = null;
    private JSpinner jspr_Exact = null;


    private void _FinitComponents(){
        //find panel
        JPanel _p = new JPanel( new GridBagLayout() );
        _p.setBorder( BorderFactory.createTitledBorder(SikherProperties.getString("control.search.panel.search")) );
        GridBagConstraints _const = new GridBagConstraints();

        JLabel _label = new JLabel(sikher.visual.forms.SikherProperties.getString("control.search.label.search"));
        _const.gridx = 0;
        _const.gridy = 0;
        _const.insets = new Insets(4, 4, 4, 4);
        _p.add( _label, _const );

        jtf_Text = new JTextField(15);
        _const.gridx = 1;
        _const.fill = GridBagConstraints.HORIZONTAL;
        _const.gridwidth = 2;
        _p.add( jtf_Text, _const );

        sikher.visual.controls.KeyboardPanel _keyboard = new sikher.visual.controls.KeyboardPanel( jtf_Text );
        _const.gridx = 0;
        _const.gridy = 1;
        _const.fill = GridBagConstraints.NONE;
        _p.add( _keyboard, _const );

        jb_Search = new JButton( new FindAction() );
        _const.gridx = 2;
        _const.gridwidth = 1;
        _const.anchor = GridBagConstraints.LAST_LINE_END;
        _p.add( jb_Search, _const );

        add( _p, BorderLayout.NORTH );

        //options panel
        _p = new JPanel(new SpringLayout() );
        _p.setBorder(BorderFactory.createTitledBorder(sikher.visual.forms.SikherProperties.getString("control.search.panel.options")));

        jcb_SearchFlags = _FcreateComboBox( "control.search.label.searchflag", _p );
        jcb_Types = _FcreateComboBox( "control.search.label.type", _p );
        jcb_Books = _FcreateComboBox( "control.search.label.book", _p );
        jcb_Authors = _FcreateComboBox( "control.search.label.author", _p );
        jcb_Raags = _FcreateComboBox( "control.search.label.raag", _p );

        _label = new JLabel( sikher.visual.forms.SikherProperties.getString("control.search.label.page") );
        JPanel _pagePanel = new JPanel( new GridLayout(4, 1) );
        SpringUtilities.setupRow( _p, _label, _pagePanel );

        ButtonGroup _bg = new ButtonGroup();
        jrb_All = new JRadioButton(sikher.visual.forms.SikherProperties.getString("control.search.radio.pageall"));
        jrb_All.setSelected(true);
        _pagePanel.add( _FcreatePagePanel( jrb_All, _bg, new JComponent[]{} ) );

        jrb_Range = new JRadioButton(sikher.visual.forms.SikherProperties.getString("control.search.radio.pagerange"));
        jspr_From = new JSpinner();
        jspr_To = new JSpinner();
        _pagePanel.add( _FcreatePagePanel( jrb_Range, _bg, new JComponent[]{
                new JLabel(sikher.visual.forms.SikherProperties.getString("control.search.label.from")),
                jspr_From,
                new JLabel(sikher.visual.forms.SikherProperties.getString("control.search.label.to")),
                jspr_To
        } ) );

        jrb_Exact = new JRadioButton(sikher.visual.forms.SikherProperties.getString("control.search.radio.pageexact"));
        jspr_Exact = new JSpinner();
        _pagePanel.add( _FcreatePagePanel( jrb_Exact, _bg, new JComponent[]{jspr_Exact} ) );

        jrb_Random = new JRadioButton(sikher.visual.forms.SikherProperties.getString("control.search.radio.pagerandom"));
        _pagePanel.add( _FcreatePagePanel( jrb_Random, _bg, new JComponent[]{} ) );

        SpringUtilities.makeCompactGrid( _p, 6, 2, 4, 4, 4, 4 );
        add( _p, BorderLayout.CENTER );
    }

    private JComboBox _FcreateComboBox( String name, JPanel panel ){
        JLabel _label = new JLabel( SikherProperties.getString(name) );
        JComboBox _combobox = new JComboBox();
        SpringUtilities.setupRow( panel, _label, _combobox );
        return _combobox;
    }

    private JPanel _FcreatePagePanel( JRadioButton button, ButtonGroup bg, JComponent[] components ){
        bg.add( button );
        JPanel _panel = new JPanel( new FlowLayout(FlowLayout.LEFT, 4, 4) );
        _panel.add( button );
        for( JComponent _comp : components ){
            _panel.add( _comp );
        }
        return _panel;
    }

    private class FindAction extends AbstractAction{
        public FindAction(){
            putValue( NAME, sikher.visual.forms.SikherProperties.getString("control.search.button.find") );
        }

        public void actionPerformed(ActionEvent e) {
            jb_Search.setEnabled( false );
            final JProgressBar _progressBar = getStatusBar().getProgressBar();
            _progressBar.setValue(0);
            _progressBar.setIndeterminate( true );

            new Thread( new Runnable(){
                public void run() {
                    String _text = jtf_Text.getText();
                    SearchFlag _flag = (SearchFlag) jcb_SearchFlags.getSelectedItem();
                    GurbaniType _type = (GurbaniType) jcb_Types.getSelectedItem();
                    NumberedElement _book = (NumberedElement) jcb_Books.getSelectedItem();
                    AuthorElement _author = (AuthorElement) jcb_Authors.getSelectedItem();
                    NumberedElement _raag = (NumberedElement) jcb_Raags.getSelectedItem();

                    if( _book == null || _author == null || _raag == null ){
                        done( 0 );
                        SwingUtilities.invokeLater( new Runnable(){
                            public void run() {
                                showErrorMessage(
                                        sikher.visual.forms.SikherProperties.getString("control.search.operation.search"),
                                        sikher.visual.forms.SikherProperties.getString("control.search.error.noinfo"));
                            }
                        } );
                        return;
                    }
                    int _startPage = 0;
                    int _endPage = 0;
                    if( jrb_All.isSelected() ){
                        _startPage = 0;
                        _endPage = 0;
                    }else if( jrb_Range.isSelected() ){
                        _startPage = (Integer)jspr_From.getValue();
                        _endPage = (Integer)jspr_To.getValue();
                    }else if( jrb_Exact.isSelected() ){
                        _startPage = (Integer)jspr_Exact.getValue();
                        _endPage = (Integer)jspr_Exact.getValue();
                    }else if( jrb_Random.isSelected() ){
                        int _minPage = _FSearcher.getMinPage();
                        int _maxPage = _FSearcher.getMaxPage();
                        int _page = (int) (_minPage + Math.random()*(_maxPage-_minPage));
                        _startPage = _page;
                        _endPage = _page;
                    }

                    int _found = 0;

                    Hymn[] _hymns = _FSearcher.searchHymns( _text, _flag, _type,
                            _book.getNumber(), _author.getNumber(), _raag.getNumber(), _startPage, _endPage );
                    for( Hymn _hymn : _hymns ){
                        _found += _hymn.getLinesCount();
                    }

                    //fire the event
                    fireControlEvent( new SikherControlEvent( SearchSikherControl.this, EVENT_FIND, _hymns ) );
                    done( _found );
                }

                public void done( int found ){
                    final String _resMessage = sikher.visual.forms.SikherProperties.getString("control.search.status.resultsfound",
                                            new Object[]{String.valueOf(found)});
                    SwingUtilities.invokeLater( new Runnable(){
                        public void run() {
                            getStatusBar().setMessage( _resMessage );
                            _progressBar.setIndeterminate( false );
                            _progressBar.setValue(0);
                            jb_Search.setEnabled( true );
                        }
                    } );
                }
            }).start();
        }
    }
}
