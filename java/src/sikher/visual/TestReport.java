package sikher.visual;

import net.sf.jasperreports.engine.*;

import java.util.HashMap;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import com.lowagie.text.pdf.BaseFont;
import sun.java2d.SunGraphicsEnvironment;
import sun.font.PhysicalFont;
import sun.font.TrueTypeFont;
import sun.font.FontManager;
import sun.font.FileFont;
import sun.awt.FontConfiguration;

/**
 * Created by IntelliJ IDEA.
 * Author: Lilu
 * Date: 18.03.2006
 * Time: 21:03:07
 * $Header: /cvsroot/sikher/sikher_java/src/sikher/visual/TestReport.java,v 1.1 2006/04/21 13:04:12 mamonts Exp $
 * $Log: TestReport.java,v $
 * Revision 1.1  2006/04/21 13:04:12  mamonts
 * First commtin
 *
 * Description:
 */
public class TestReport {
    public static void main(String[] args){
        /*
        JasperReport jasperReport;
        JasperPrint jasperPrint;
        try
        {
          jasperReport = JasperCompileManager.compileReport(
              "reports/demo.jrxml");
          jasperPrint = JasperFillManager.fillReport(
              jasperReport, new HashMap(), new JREmptyDataSource());
          JasperExportManager.exportReportToPdfFile(
              jasperPrint, "reports/simple_report.pdf");
        }
        catch ( JRException e)
        {
          e.printStackTrace();
        } */

        try {
            Font _font = Font.createFont( Font.TRUETYPE_FONT, new File( "font/CODE2000.ttf" ) );
        } catch (FontFormatException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String _fontDir = FontManager.getFontPath( true );
        File[] _fontFiles = new File( _fontDir ).listFiles();
        if( _fontFiles != null ){
            for( File _fontFile : _fontFiles ){
                if( _fontFile.getName().endsWith( ".ttf" ) ){
                    try {
                        Font _font = Font.createFont( Font.TRUETYPE_FONT, _fontFile );
                        System.out.println( _font.getFontName() );
                    } catch (FontFormatException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        }

        System.out.println( "--------------------------------------------------------" );

        Font[] _fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for( Font _font: _fonts ){
            System.out.println(
                    String.format(
                            "Name=%s, supports=%s, bold=%s",
                            _font.getFontName(), _font.canDisplay( '\u0A06' ), _font.isBold() ) );
        }
        /*
        try{
        BaseFont.createFont( "AmnolUni", BaseFont.IDENTITY_H, BaseFont.EMBEDDED );
        }catch( Exception e ){
            e.printStackTrace();
        }*/
    }

}
