package sikher.visual.controls;

import sikher.visual.sidepanel.StatusBar;
import sikher.visual.sidepanel.SikherComponent;
import sikher.searchengine.XPathSearcher;
import sikher.searchengine.SearcherListener;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 25.01.2006
 * Time: 15:14:14
 * $Header$
 * $Log$
 * Description:
 */
public class SikherControl extends SikherComponent {
    protected XPathSearcher FSearcher;

    public SikherControl(XPathSearcher searcher, StatusBar statusBar) {
        this(searcher, statusBar, new FlowLayout());
    }

    public SikherControl(XPathSearcher searcher, StatusBar statusBar, LayoutManager layout) {
        super(statusBar, layout);
        FSearcher = searcher;
        FSearcher.addSearcherListener( new XPathSearcherListener() );
    }

    public void init(){
        searcherInfoUpdated();
    }

    protected void searcherInfoUpdated(){}

    private class XPathSearcherListener implements SearcherListener{
        public void infoUpdated() {
            searcherInfoUpdated();
        }
    }
}
