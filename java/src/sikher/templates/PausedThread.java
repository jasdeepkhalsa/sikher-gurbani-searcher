package sikher.templates;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 17.01.2006
 * Time: 20:46:31
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/templates/PausedThread.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: PausedThread.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public abstract class PausedThread extends Thread{

    private ArrayList<IThreadStatusListener> _FListeners = null;

    public PausedThread ( String name ){
        super ( name );
        _FListeners = new ArrayList<IThreadStatusListener>();
    }

    public PausedThread ( Runnable target, String name ){
        super( target, name );
        _FListeners = new ArrayList<IThreadStatusListener>();
    }

    public PausedThread ( ThreadGroup group, Runnable target, String name ){
        super ( group, target, name );
    }

    private boolean _F_Stopped = false;
    private boolean _FStopping = false;
    private boolean _F_Paused = false;
    private boolean _FPausing = false;
    private long _F_SleepTime = 0;

    public void run (){
        init();
        fireStatusChanged( ThreadStatusEvent.STATUS_STARTED );
        while ( !isStopped() ){
            if ( !isPaused() && !_FPausing && !_FStopping ){
                process();
            }
            if( _FPausing ){
                _F_Paused = true;
                fireStatusChanged( ThreadStatusEvent.STATUS_PAUSED );
                _FPausing = false;
            }
            else if ( _FStopping ){
                _FStopping = false;
                _F_Stopped = true;
                fireStatusChanged( ThreadStatusEvent.STATUS_STOPPED );
            }else{
                try {
                    Thread.sleep( getSleepTime() );
                } catch (InterruptedException e) {
                    System.err.println( "[" + getClass().getName() + "]: "+ e );
                }
            }
        }

        done();
    }

    public abstract void init ();
    public abstract void process ();
    public abstract void done ();

    public void startIt (){
        start();
    }
    public void stopIt (){
        _FStopping = true;
        _F_Stopped = true;
        fireStatusChanged( ThreadStatusEvent.STATUS_STOPPED );
    }

    public void pauseIt (){
        _FPausing = true;
    }

    public void resumeIt (){
        _F_Paused = false;
        fireStatusChanged( ThreadStatusEvent.STATUS_RESUMED );
    }

    public boolean isStopped (){
        return _F_Stopped;
    }

    public boolean isPaused (){
        return _F_Paused;
    }

    public long getSleepTime (){
        return _F_SleepTime;
    }
    public void setSleepTime ( long time ){
        _F_SleepTime = time;
    }

    public void fireStatusChanged( int status ){
        for ( IThreadStatusListener _listener : _FListeners ){
            _listener.statusChanged( new ThreadStatusEvent( this, status ) );
        }
    }

    public void addThreadStatusListener( IThreadStatusListener listener ){
        _FListeners.add( listener );
    }

    public void removeThreadStatusListener( IThreadStatusListener listener ){
        _FListeners.remove( listener );
    }
}
