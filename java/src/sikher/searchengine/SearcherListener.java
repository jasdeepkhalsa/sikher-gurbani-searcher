package sikher.searchengine;

import java.util.EventListener;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 25.01.2006
 * Time: 15:20:10
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/searchengine/SearcherListener.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: SearcherListener.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public interface SearcherListener extends EventListener {
    public void infoUpdated();
}
