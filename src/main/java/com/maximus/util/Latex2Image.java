package com.maximus.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

public class Latex2Image {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		File f = new File("F:/1.txt");
		String s = IOUtils.toString(new FileInputStream(f));
		s = new String(s.getBytes(),"UTF-8");
		System.out.println(s);
		conv(s, "F:/Example1.png");
	}
	public static void conv(String latexString, String outputFilePath) {

        TeXFormula formula = new TeXFormula(latexString);
        TeXIcon icon = formula.new TeXIconBuilder().setStyle(TeXConstants.STYLE_DISPLAY).setSize(20).build();

        icon.setInsets(new Insets(5, 5, 5, 5));

        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.white);
        g2.fillRect(0,0,icon.getIconWidth(),icon.getIconHeight());
        JLabel jl = new JLabel();
        jl.setForeground(new Color(0, 0, 0));
        icon.paintIcon(jl, g2, 0, 0);
        File file = new File(outputFilePath);
        try {
            ImageIO.write(image, "png", file.getAbsoluteFile());
        } catch (IOException ex) { }		
	}
	
}
