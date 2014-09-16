package sikher;

import sikher.visual.forms.AppFrame;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 28.11.2005
 * Time: 11:15:48
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/Main.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: Main.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class Main {
    public static void main( String args[] ){
        AppFrame _frame = new AppFrame();
        sikher.visual.forms.SikherSplashScreen _screen  = new sikher.visual.forms.SikherSplashScreen( _frame );
        _screen.showGUI();
    }
}
