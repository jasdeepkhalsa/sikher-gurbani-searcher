package sikher.visual;// Decompiled by DJ v3.6.6.79 Copyright 2004 Atanas Neshkov  Date: 17.01.2006 14:33:32
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   NewsMaker.java

import java.applet.*;
import java.awt.*;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;

public class NewsMaker extends Applet
    implements Runnable
{

    public void init()
    {
        aWide = size().width;
        aHigh = size().height;
        DoC();
        String s = getParameter("SoundFile");
        if(s != null)
        {
            sFile = s;
            A = getAudioClip(getCodeBase(), sFile);
        }
        s = getParameter("Speed");
        if(s != null)
            Speed = Integer.parseInt(s);
        s = getParameter("HeadLines");
        if(s != null && s.equalsIgnoreCase("YES"))
            HeadLines = true;
        s = getParameter("Justification");
        if(s != null)
            if(s.equalsIgnoreCase("left"))
                howAlign = "left";
            else
            if(s.equalsIgnoreCase("center"))
                howAlign = "center";
            else
            if(s.equalsIgnoreCase("right"))
                howAlign = "right";
            else
                howAlign = "left";
        s = getParameter("FontSize");
        if(s != null)
            fontSize = Integer.parseInt(s);
        s = getParameter("FontName");
        if(s != null)
            fontName = s;
        s = getParameter("FontStyle");
        if(s != null)
        {
            if(s.equalsIgnoreCase("PLAIN"))
                theFont = new Font(fontName, 0, fontSize);
            else
            if(s.equalsIgnoreCase("BOLD"))
                theFont = new Font(fontName, 1, fontSize);
            else
            if(s.equalsIgnoreCase("ITALIC"))
                theFont = new Font(fontName, 2, fontSize);
            else
            if(s.equalsIgnoreCase("BOLDITALIC"))
                theFont = new Font(fontName, 3, fontSize);
            else
                theFont = new Font(fontName, 0, fontSize);
        } else
        {
            theFont = new Font(fontName, 0, fontSize);
        }
        FM = getFontMetrics(theFont);
        fontHigh = FM.getMaxAscent() + FM.getMaxDescent() + FM.getLeading();
        scrollPosY = fontHigh;
        s = getParameter("TextColor");
        if(s != null)
            textColor = parseC(s, textColor);
        s = getParameter("TextHotColor");
        if(s != null)
            linkColor = parseC(s, linkColor);
        s = getParameter("HeadLineColor");
        if(s != null)
            headColor = parseC(s, headColor);
        s = getParameter("HeadLineHotColor");
        if(s != null)
            hotColor = parseC(s, hotColor);
        s = getParameter("LeftMargin");
        if(s != null)
            leftMarg = Integer.parseInt(s);
        s = getParameter("RightMargin");
        if(s != null)
            rightMarg = Integer.parseInt(s);
        s = getParameter("TopMargin");
        if(s != null)
            topMarg = Integer.parseInt(s);
        s = getParameter("BottomMargin");
        if(s != null)
            botMarg = Integer.parseInt(s);
        sWide = aWide - leftMarg - rightMarg;
        sHigh = aHigh - topMarg - botMarg;
        s = getParameter("Pause");
        if(s != null)
            Dwell = Integer.parseInt(s);
        s = getParameter("BackImage");
        if(s != null)
            i_File = s;
        s = getParameter("BackgroundColor");
        if(s != null)
            backColor = parseC(s, backColor);
        if(i_File != null)
        {
            i_Back = getImage(getDocumentBase(), i_File);
            prepareImage(i_Back, this);
        }
        /*
        if(FreeVersion && !fromDisk)
        {
            doParse("###http://www.microticker.com");
            doParse("This is NewsMaker by MicroTicker.com... Free for non-profit, non-commercial use. Click here for more information.");
        }
        if(!pfN)
        {
            doParse("###http://www.codebrain.com");
            doParse("NOTICE WRONG OR MISSING NOTICE WRONG OR MISSING NOTICE WRONG OR MISSING NOTICE WRONG OR MISSING NOTICE WRONG OR MISSING NOTICE WRONG OR MISSING");
        }
        if(!pfK)
        {
            doParse("###");
            doParse("KEYCODE WRONG OR MISSING KEYCODE WRONG OR MISSING KEYCODE WRONG OR MISSING KEYCODE WRONG OR MISSING KEYCODE WRONG OR MISSING KEYCODE WRONG OR MISSING");
        } */
        file = "NewsMaker.txt";
        s = getParameter("Script");
        if(s != null)
            file = s;
        if(Script.isEmpty())
            getScript();
        for(int i = 0; i < Script.size(); i++)
        {
            String s1 = (String)Script.elementAt(i);
            s1 = s1.trim();
            if(s1.length() < 3 && s1.length() > 0)
                s1 = s1 + "^^^^^";
            if(s1.length() > 0)
                doParse(s1);
        }

    }

    private void doParse(String s)
    {
        int i = aWide - rightMarg - leftMarg;
        boolean flag = true;
        String s1 = "";
        String s2 = "";
        String s4 = "";
        String s6 = "";
        String s7 = "";
        boolean flag1 = false;
        int j = 0;
        Object obj = null;
        Object obj1 = null;
        if(s.substring(0, 3).equals("###"))
        {
            s = s.substring(3, s.length());
            s = s.trim();
            if(s.length() < 3)
                s = "@@@";
            if(s.substring(0, 3).equals("@@@"))
                s = "@@@";
            if(s.substring(s.length() - 3, s.length()).equals("@@@"))
                s = s.substring(0, s.length() - 3);
            String s8;
            String s9;
            if(s.indexOf("@@@") > 0 && s.length() > s.indexOf("@@@") + 3)
            {
                StringTokenizer stringtokenizer = new StringTokenizer(s, "@@@");
                s8 = stringtokenizer.nextToken();
                s9 = stringtokenizer.nextToken();
            } else
            {
                s8 = s;
                if(s.equals("") || s.equals("@@@"))
                    s8 = null;
                s9 = null;
            }
            v_URL.addElement(s8);
            v_Targ.addElement(s9);
            return;
        }
        if(s.equalsIgnoreCase("<linebreak>"))
        {
            s = "|    ";
            int k = v_lineCount - 1;
            if(k > -1)
            {
                String s10 = (String)v_Line.elementAt(k);
                s10 = s10 + s;
                v_Line.setElementAt(s10, k);
            }
            return;
        }
        boolean flag3 = true;
        if(HeadLines)
            s = "<b>" + s;
        while(flag)
        {
            if(j > s.length() - 2)
                flag = false;
            s1 = s1 + s.substring(j, j + 1);
            j++;
            if(FM.stringWidth(s1) >= i)
            {
                boolean flag2 = false;
                for(int l = s1.length() - 1; l > 0; l--)
                {
                    String s3 = s1.substring(l, l + 1);
                    if(s3.equals(" "))
                    {
                        String s5 = s1.substring(0, l) + "|";
                        s5 = s5.trim();
                        s7 = s7 + s5;
                        boolean flag4 = false;
                        s5 = "";
                        s1 = "";
                        s = s.substring(l, s.length());
                        if(FM.stringWidth(s) < i)
                        {
                            s6 = s;
                            s6 = s6.trim();
                        }
                        j = 0;
                        flag2 = true;
                    }
                    if(flag2)
                        break;
                }

            } else
            {
                s6 = s;
                s6 = s6.trim();
            }
        }
        v_Line.addElement(s7 + s6);
        v_lineCount++;
    }

    private void getScript()
    {
        try
        {
            /*
            URLConnection urlconnection = (new URL(getCodeBase(), file)).openConnection();
            DataInputStream datainputstream = new DataInputStream(urlconnection.getInputStream());
            */
            DataInputStream datainputstream = new DataInputStream( new FileInputStream(new File(file)) );
            String s;
            while((s = datainputstream.readLine()) != null)
                Script.addElement(s.trim());
            return;
        }
        catch(Exception _ex)
        {
            Script.removeAllElements();
        }
        Script.addElement("Could not get script...");
    }

    URL getURL(String s)
    {
        URL url = null;
        try
        {
            if(s.indexOf("http://") >= 0 || s.indexOf("https://") >= 0)
                url = new URL(s);
            else
                url = new URL(getDocumentBase(), s);
        }
        catch(MalformedURLException _ex) { }
        return url;
    }

    private Color parseC(String s, Color color)
    {
        Color color1;
        try
        {
            s = s.replace('#', ' ').trim();
            int i = Integer.valueOf(s.substring(0, 2), 16).intValue();
            int j = Integer.valueOf(s.substring(2, 4), 16).intValue();
            int k = Integer.valueOf(s.substring(4, 6), 16).intValue();
            color1 = new Color(i, j, k);
        }
        catch(Exception _ex)
        {
            color1 = color;
        }
        return color1;
    }

    public void render()
    {
        if(i_Buff != null)
        {
            Graphics g = i_Buff.getGraphics();
            g.setColor(backColor);
            g.fillRect(0, 0, aWide, aHigh);
            if(i_Back != null)
                g.drawImage(i_Back, 0, 0, this);
            g.clipRect(leftMarg, topMarg, sWide, sHigh);
            g.translate(0, scrollPosY);
            g.setColor(textColor);
            g.setFont(theFont);
            String s = "";
            int i = topMarg;
            doDwell = false;
            boolean flag = false;
            v_current = -1;
            Color color = g.getColor();
            while(i + scrollPosY < topMarg + sHigh + fontHigh + fontHigh)
            {
                for(int j = 0; j < v_lineCount; j++)
                {
                    if(m_Good)
                    {
                        String s1 = (String)v_Line.elementAt(j);
                        int k = 0;
                        for(int i1 = 0; s1.indexOf('|', i1) > 0; i1 = s1.indexOf('|', i1) + 1)
                            k += fontHigh;

                        if(m_Y + fontHigh > i + scrollPosY && m_Y < i + k + scrollPosY)
                        {
                            v_current = j;
                            g.setColor(linkColor);
                        }
                    }
                    StringTokenizer stringtokenizer = new StringTokenizer((String)v_Line.elementAt(j), "|");
                    if(i + scrollPosY == topMarg + fontHigh)
                    {
                        doDwell = true;
                        if(A != null && oldScrollY != scrollPosY && !m_Good)
                            A.play();
                        oldScrollY = scrollPosY;
                    }
                    while(stringtokenizer.hasMoreTokens())
                    {
                        String s2 = stringtokenizer.nextToken();
                        if(s2.indexOf("^^^^^") > -1)
                        {
                            s2 = s2.substring(0, s2.indexOf("^^^^^"));
                            s2 = s2 + "     ";
                        }
                        if(HeadLines)
                            if(s2.substring(0, 3).equalsIgnoreCase("<b>"))
                            {
                                s2 = s2.substring(3);
                                color = g.getColor();
                                g.setColor(headColor);
                                if(m_Good && v_current == j)
                                    g.setColor(hotColor);
                            } else
                            {
                                g.setColor(color);
                            }
                        int l = FM.stringWidth(s2);
                        int j1 = (leftMarg + (aWide - rightMarg - leftMarg) / 2) - l / 2;
                        if(howAlign == "left")
                            g.drawString(s2, leftMarg, i);
                        if(howAlign == "right")
                            g.drawString(s2, aWide - rightMarg - l, i);
                        if(howAlign == "center")
                            g.drawString(s2, j1, i);
                        i += fontHigh;
                    }
                    i += fontHigh;
                    g.setColor(textColor);
                }

                if(i + scrollPosY < topMarg)
                    flag = true;
            }
            if(flag)
                scrollPosY = 0;
        }
    }

    public void run()
    {
        do
            try
            {
                if(first)
                {
                    render();
                    repaint();
                }
                if((doDwell || first) && !m_Good)
                {

                    for(int i = 0; !m_Good && i < Dwell && doDwell; i += 10)
                        Thread.sleep(10L);


                    doDwell = false;
                } else
                {
                    Thread.sleep(Speed);
                }
                first = false;
                doDwell = false;
                if(!m_Good)
                    scrollPosY--;
                render();
                repaint();
            }
            catch(InterruptedException _ex)
            {
                return;
            }
        while(true);
    }

    public void update(Graphics g)
    {
        render();
        paint(g);
    }

    public void paint(Graphics g)
    {
        if(i_Buff == null)
            i_Buff = createImage(aWide, aHigh);
        g.drawImage(i_Buff, 0, 0, null);
    }

    public void start()
    {
        if(News == null)
            News = new Thread(this);
        News.start();
    }

    public void stop()
    {
        if(A != null)
            A.stop();
        if(News != null)
        {
            News.stop();
            News = null;
        }
    }

    public void destroy()
    {
    }

    public boolean mouseDown(Event event, int i, int j)
    {
        if(v_current > -1)
        {
            URLcheck = -1;
            URL url = null;
            String s = null;
            if(v_URL.elementAt(v_current) != null)
                url = getURL(v_URL.elementAt(v_current).toString());
            if(v_Targ.elementAt(v_current) != null)
                s = v_Targ.elementAt(v_current).toString();
            if(URLcheck < 0)
                if(v_Targ.elementAt(v_current) == null)
                {
                    if(v_URL.elementAt(v_current) != null)
                        getAppletContext().showDocument(url);
                } else
                if(v_URL.elementAt(v_current) != null)
                    getAppletContext().showDocument(url, s);
        }
        return true;
    }

    public boolean mouseUp(Event event, int i, int j)
    {
        return true;
    }

    public boolean mouseEnter(Event event, int i, int j)
    {
        m_Good = true;
        m_Y = j;
        repaint();
        return true;
    }

    public boolean mouseExit(Event event, int i, int j)
    {
        m_Good = false;
        doDwell = false;
        if(first)
            first = false;
        m_Y = j;
        repaint();
        return true;
    }

    public boolean mouseMove(Event event, int i, int j)
    {
        m_Y = j;
        repaint();
        return true;
    }

    private void DoC()
    {
        pfN = false;
        pfK = false;
        URL url = null;
        String s2 = "none";
        String s3 = null;
        boolean flag = false;
        int j1 = Integer.parseInt("452617".substring(0, 1));
        int k1 = Integer.parseInt("452617".substring(1, 2));
        int l1 = Integer.parseInt("452617".substring(2, 3));
        int i2 = Integer.parseInt("452617".substring(3, 4));
        int j2 = Integer.parseInt("452617".substring(4, 5));
        int k2 = Integer.parseInt("452617".substring(5, 6));
        url = getDocumentBase();
        hostName = url.getHost();
        s2 = url.getProtocol();
        s2 = s2.toUpperCase();
        if(s2.indexOf("FILE") > -1)
        {
            pfK = true;
            fromDisk = true;
        }
        s3 = null;
        s3 = getParameter("Notice");
        if(s3 != null)
            if(s3.equalsIgnoreCase("Applet by www.MicroTicker.com"))
                pfN = true;
            else
                pfN = false;
        if(s3 == null)
            pfN = false;
        String s = "CodeBrainRocks";
        s3 = null;
        s3 = getParameter("KeyCode");
        if(s3 == null)
            pfK = false;
        if(s3 != null)
        {
            if(s3.startsWith("FREE"))
                FreeVersion = true;
            if(s3.length() > 10)
                s = s3;
        }
        int l2 = 0;
        s3 = "";
        for(int i = 0; i < s.length(); i++)
        {
            String s4 = s.substring(i, i + 1);
            if(s4.indexOf("|") > -1)
                l2++;
        }

        if(l2 == 0)
        {
            s = s + "|CodeBrainRocks";
            l2 = 1;
        }
        String as[] = new String[13];
        StringTokenizer stringtokenizer = new StringTokenizer(s, "|");
        l2++;
        for(int j = 0; j < l2; j++)
            as[j] = stringtokenizer.nextToken();

        String as1[] = new String[13];
        for(int i3 = 0; i3 < l2; i3++)
        {
            String s1 = as[i3];
            s1 = s1.substring(3);
            s1 = s1.substring(0, s1.length() - 3);
            String s5 = "";
            int j3 = 0;
            int l3 = 0;
            for(int k = 0; k < s1.length(); k++)
            {
                j3 = s1.charAt(k);
                if(l3 == 0)
                    j3 += j1;
                if(l3 == 1)
                    j3 += k1;
                if(l3 == 2)
                    j3 += l1;
                if(l3 == 3)
                    j3 += i2;
                if(l3 == 4)
                    j3 += j2;
                if(l3 == 5)
                    j3 += k2;
                s5 = s5 + String.valueOf((char)j3);
                if(++l3 > 5)
                    l3 = 0;
            }

            String s7 = "";
            l3 = 0;
            j3 = 0;
            for(int l = 0; l < s5.length(); l++)
            {
                int k3 = s5.charAt(l);
                k3 -= 3;
                if(k3 == 118)
                    k3 = 126;
                s7 = s7 + String.valueOf((char)k3);
                if(++l3 > 5)
                    l3 = 0;
            }

            as1[i3] = s7;
        }

        String s6 = "";
        s6 = String.valueOf(getDocumentBase());
        s6 = s6.toUpperCase();
        for(int i1 = 0; i1 < l2; i1++)
            if(s6.indexOf(as1[i1]) > -1)
                pfK = true;

        if(FreeVersion)
            pfK = true;
    }

    public NewsMaker()
    {
        pfK = false;
        pfN = false;
        FreeVersion = false;
        hostName = "This URL";
        fromDisk = false;
        FirstPass = true;
        Dwell = 3000;
        Speed = 10;
        doDwell = true;
        first = true;
        v_Line = new Vector();
        v_current = -1;
        v_URL = new Vector();
        v_Targ = new Vector();
        i_File = "";
        m_Good = false;
        leftMarg = 5;
        rightMarg = 5;
        topMarg = 5;
        botMarg = 5;
        sWide = 100;
        sHigh = 100;
        scrollPosY = 14;
        textColor = Color.black;
        linkColor = new Color(0, 128, 0);
        headColor = Color.blue;
        hotColor = Color.red;
        backColor = Color.white;
        fontHigh = 14;
        fontSize = 12;
        fontName = "Dialog";
        howAlign = "left";
        HeadLines = false;
        URLcheck = -1;
        Script = new Vector();
        oldScrollY = 12345;
    }

    Thread News;
    boolean pfK;
    boolean pfN;
    boolean FreeVersion;
    String hostName;
    boolean fromDisk;
    boolean FirstPass;
    int Dwell;
    int Speed;
    boolean doDwell;
    boolean first;
    AudioClip A;
    String sFile;
    Vector v_Line;
    int v_current;
    int v_lineCount;
    Vector v_URL;
    Vector v_Targ;
    Image i_Buff;
    Image i_Back;
    String i_File;
    boolean m_Good;
    int m_Y;
    int leftMarg;
    int rightMarg;
    int topMarg;
    int botMarg;
    int sWide;
    int sHigh;
    int scrollPosY;
    Color textColor;
    Color linkColor;
    Color headColor;
    Color hotColor;
    Color backColor;
    Font theFont;
    int fontHigh;
    int fontSize;
    String fontName;
    FontMetrics FM;
    String howAlign;
    boolean HeadLines;
    int URLcheck;
    int aWide;
    int aHigh;
    Vector Script;
    String file;
    int oldScrollY;
}