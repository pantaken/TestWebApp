package com.maximus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import com.adobe.dp.office.conv.FileResourceWriter;
import com.adobe.dp.office.conv.GDISVGSurface;
import com.adobe.dp.office.metafile.EMFParser;
import com.adobe.dp.office.metafile.WMFParser;


public class MF2IMG {

	public final static String METAFILE_WMF = ".wmf";
	public final static String METAFILE_EMF = ".emf";

	public static void main(String[] args) {
		File metaFile = new File("F:\\webapps\\docx4j\\resource\\image1.wmf");
		conv(metaFile);
	}
	public static void conv(File metaFile) {
        String outPNGFile=metaFile.getAbsolutePath()+".png";
        if(metaFile.getAbsolutePath().endsWith(".emf")) {
            doConvertEMF2IMG(metaFile.getAbsolutePath(),outPNGFile);
        } else {
            doConvertWMF2IMG(metaFile.getAbsolutePath(),outPNGFile);
        }		
	}
    /**
     * EMF to img
     * @param emfFileStr
     * @param imgName
     */
    private static void doConvertEMF2IMG(String emfFileStr, String imgName) {
        try {
            String svgStr=doConvertEMF2SVG(emfFileStr);
            Writer out = new OutputStreamWriter(new FileOutputStream(emfFileStr+".svg"), "UTF-8");
            out.write(svgStr);
            out.close(); 

            SVG2IMG svg2img= new SVG2IMG();
            svg2img.doSVG2PNGConvert(emfFileStr+".svg",imgName);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }    
    /**
     * WMF to img
     * @param wmfFileStr
     * @param imgName
     */
    private static void doConvertWMF2IMG(String wmfFileStr, String imgName) {
    	System.out.println(wmfFileStr);
        try {
            String svgStr=doConvertWMF2SVG(wmfFileStr);
            Writer out = new OutputStreamWriter(new FileOutputStream(wmfFileStr+".svg"), "UTF-8");
            out.write(svgStr);
            out.close(); 

            SVG2IMG svg2img= new SVG2IMG();
            svg2img.doSVG2PNGConvert(wmfFileStr+".svg",imgName);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    /**
     * wmfԪ�ļ�ת��svg�ļ� 
     * @param wmfFileStr
     * @return SVG��xml����
     */
    private static String doConvertWMF2SVG(String wmfFileStr) {
        try{
            if(wmfFileStr != null) {
                FileInputStream in = new FileInputStream(wmfFileStr);
                FileResourceWriter rw = new FileResourceWriter(new File("."));
                
                GDISVGSurface svg = new GDISVGSurface(rw);
                WMFParser r = new WMFParser(in, svg);
                //EMFParser r = new EMFParser(fin, svg);

                while (r.readNext()) {
                	
                }
                return svg.getSVG();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * emfԪ�ļ�ת��ΪSVG�ļ�
     * @param wmfFileStr
     * @return svg��xml����
     */
    private static String doConvertEMF2SVG(String wmfFileStr) {
        try {
            if(wmfFileStr != null) {

                FileInputStream in = new FileInputStream(wmfFileStr);
                FileResourceWriter rw = new FileResourceWriter(new File("."));

                GDISVGSurface svg = new GDISVGSurface(rw);
                EMFParser r = new EMFParser(in, svg);

                while (r.readNext()) {
                }
                return svg.getSVG();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}


