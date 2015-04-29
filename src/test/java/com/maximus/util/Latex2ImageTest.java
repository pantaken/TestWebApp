package com.maximus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.io.IOUtils;

import com.maximus.util.Latex2Image;

public class Latex2ImageTest extends TestCase {

	public void testconv() throws FileNotFoundException, IOException {
		File f = new File("/Users/wangxianlei/1.txt");
		String s = IOUtils.toString(new FileInputStream(f));
		s = new String(s.getBytes(),"UTF-8");
		System.out.println(s);
		Latex2Image.conv(s, "/Users/wangxianlei/1.png");		
	}
}
