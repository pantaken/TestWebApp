package com.maximus.util;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XSLTransformer {

	private static final String UTF8 = "utf-8";
	private static final String GBK = "gb2312";
	private static final String OMML_NAMESPACE = " xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"";

	
	private static InputStream xmlString2InputStream(String xmlString) throws UnsupportedEncodingException {
		return new ByteArrayInputStream(xmlString.getBytes(UTF8));
	}
	/**
	 * 
	 * @param ommlString
	 * @return mmlString
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	public static String processOMML2MML(String ommlString) throws TransformerException, UnsupportedEncodingException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(XSLTransformer.class.getResourceAsStream("MyOMML2MML.XSL")));
		transformer.setOutputProperty("encoding", GBK);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);		
		
		transformer.transform(new StreamSource(xmlString2InputStream(ommlString)), result);
		String mmlString = sw.toString();
		return mmlString.replace(OMML_NAMESPACE, "");
	}
	/**
	 * 
	 * @param mmlString
	 * @return ommlString
	 * @throws TransformerException
	 * @throws UnsupportedEncodingException
	 */
	public static String processMML2OMML(String mmlString) throws TransformerException, UnsupportedEncodingException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(XSLTransformer.class.getResourceAsStream("MML2OMML.XSL")));
		transformer.setOutputProperty("encoding", UTF8);
		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		
		transformer.transform(new StreamSource(xmlString2InputStream(mmlString)), result);
		String ommlString = sw.toString();
		return ommlString;
	}
	public static void main(String[] args) throws IOException, TransformerException {
		/**
		 * omml 2 mml
		 */
//		InputStream is = XSLTransformer.class.getResourceAsStream("omml.xml");
//		byte[] bytes = new byte[is.available()];
//		is.read(bytes);
//		is.close();
//		String ommlString = new String(bytes,UTF8);
//		System.out.println(ommlString);
//		System.out.println(processOMML2MML(ommlString));
		/**
		 * mml 2 omml
		 */
		InputStream is = XSLTransformer.class.getResourceAsStream("mml.xml");
		byte[] bytes = new byte[is.available()];
		is.read(bytes);
		is.close();
		String mmlString = new String(bytes,UTF8);
		System.out.println(mmlString);
		System.out.println(processMML2OMML(mmlString));
	}
}
