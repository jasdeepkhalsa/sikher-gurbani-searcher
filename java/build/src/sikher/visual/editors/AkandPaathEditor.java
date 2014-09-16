package sikher.visual.editors;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.forms.SikherProperties;
import sikher.templates.VisualUtils;
import sikher.templates.IThreadStatusListener;
import sikher.templates.ThreadStatusEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 05.02.2006
 * Time: 11:03:55
 * $Header$
 * $Log$
 * Description:
 */
public class AkandPaathEditor extends AbstractSikherEditor{
    public AkandPaathEditor(StatusBar statusBar) {
        super(statusBar);
        setControlName( SikherProperties.getString("editor.akand.name") );
        _FinitComponents();
    }

    public void done(){
        //kill thread
        _FScrollTextPanel.killThread();
    }

    private ScrollTextPanel _FScrollTextPanel;

    private final String AC_PREV = "prev";
    private final String AC_PLAY = "play";
    private final String AC_PAUSE = "pause";
    private final String AC_STOP = "stop";
    private final String AC_NEXT = "next";

    private void _FinitComponents(){
        JPanel _panel = new JPanel( new BorderLayout() );
        _FScrollTextPanel = new ScrollTextPanel();
        _panel.add( _FScrollTextPanel, BorderLayout.CENTER );

        PlayerListener _listener = new PlayerListener();
        JButton _btnPrev = _FcreateButton( "editor.akand.icon.prev", "editor.akand.icon.prev_p",
                "editor.akand.tooltip.prev", AC_PREV, _listener );
        JButton _btnPlay = _FcreateButton( "editor.akand.icon.play", "editor.akand.icon.play_p",
                "editor.akand.tooltip.play", AC_PLAY, _listener );
        JButton _btnPause = _FcreateButton( "editor.akand.icon.pause", "editor.akand.icon.pause_p",
                "editor.akand.tooltip.pause", AC_PAUSE, _listener );
        JButton _btnStop = _FcreateButton( "editor.akand.icon.stop", "editor.akand.icon.stop_p",
                "editor.akand.tooltip.stop", AC_STOP, _listener );
        JButton _btnNext = _FcreateButton( "editor.akand.icon.next", "editor.akand.icon.next_p",
                "editor.akand.tooltip.next", AC_NEXT, _listener );

        _FScrollTextPanel.addThreadListener( new ScrollThreadListener( _btnPlay, _btnPause, _btnStop ) );

        JButton[] _buttons = new JButton[]{ _btnPrev, _btnPlay, _btnPause, _btnStop, _btnNext };
        JPanel _buttonPanel = (JPanel) VisualUtils.getCommandRow( _buttons );

        JSlider _slider = new JSlider( 0, 10 );
        _slider.setMajorTickSpacing( 10 );
        _slider.setMinorTickSpacing( 1 );
        _slider.setPaintTicks( true );
        //_slider.setPreferredSize( new Dimension( 50, _slider.getHeight() ) );
        //_buttonPanel.add( _slider );

        _buttonPanel.setBorder( BorderFactory.createEtchedBorder() );
        _panel.add( _buttonPanel, BorderLayout.SOUTH );
        add( _panel );

        setDataModel( _FScrollTextPanel, _FScrollTextPanel );
    }

    private JButton _FcreateButton( String icon, String pressedIcon, String tooltip,
                                    String actionCmd, ActionListener listener ){
        JButton _button = new JButton( SikherProperties.createImageIcon( icon ) );
        _button.setPressedIcon( SikherProperties.createImageIcon( pressedIcon ) );
        _button.setMargin( new Insets(0, 0, 0, 0) );
        _button.setActionCommand( actionCmd );
        _button.addActionListener( listener );
        _button.setToolTipText( SikherProperties.getString( tooltip ) );
        return _button;
    }

    private class PlayerListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            String _command = e.getActionCommand();
            if( _command == AC_PREV ){
                _FScrollTextPanel.prevPage();
            }else if ( _command == AC_PLAY ){
                _FScrollTextPanel.startScrolling();
            }else if ( _command == AC_PAUSE ){
                _FScrollTextPanel.pauseScrolling();
            }else if( _command == AC_STOP ){
                _FScrollTextPanel.stopScrolling();
            }else if( _command == AC_NEXT ){
                _FScrollTextPanel.nextPage();
            }
        }
    }

    private class ScrollThreadListener implements IThreadStatusListener{
        private JButton _FBtnPlay;
        private JButton _FBtnPause;
        private JButton _FBtnStop;

        public ScrollThreadListener( JButton btnPlay, JButton btnPause, JButton btnStop ){
            _FBtnPlay = btnPlay;
            _FBtnPause = btnPause;
            _FBtnStop = btnStop;
        }

        public void statusChanged( ThreadStatusEvent event ) {
            int _status = event.getStatus();
            switch( _status ){
                case ThreadStatusEvent.STATUS_STARTED:
                    _FBtnStop.setEnabled( false );
                case ThreadStatusEvent.STATUS_PAUSED:
                    _FBtnPlay.setEnabled( true );
                    _FBtnPause.setEnabled( false );
                    break;
                case ThreadStatusEvent.STATUS_STOPPED:
                    _FBtnStop.setEnabled( true );
                case ThreadStatusEvent.STATUS_RESUMED:
                    _FBtnPlay.setEnabled( false );
                    _FBtnPause.setEnabled( true );
                    break;
            }
        }
    }
}
