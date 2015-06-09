package com.maximus.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class TestTextOmathImage {
	private static String s = "设<?xml version=\"1.0\" encoding=\"GB2312\"?><math xmlns=\"http://www.w3.org/1998/Math/MathML\"><mi>f</mi><mfenced separators=\"|\"><mrow><mi>x</mi></mrow></mfenced><mo>=</mo><mi mathvariant=\"normal\">&#160;</mi><mrow><mrow><mi mathvariant=\"normal\">cos</mi></mrow><mo>&#8289;</mo><mrow><mi>x</mi></mrow></mrow><mo>(</mo><mi>x</mi><mo>+</mo><mo>|</mo><mrow><mrow><mi mathvariant=\"normal\">sin</mi></mrow><mo>&#8289;</mo><mrow><mi>x</mi></mrow></mrow><mo>|</mo><mo>)</mo></math>,则在<?xml version=\"1.0\" encoding=\"GB2312\"?><math xmlns=\"http://www.w3.org/1998/Math/MathML\"><mi>x</mi><mo>=</mo><mn>0</mn></math>处有(  ) 设不等式<img alt=\"\" src=\"/TestWebApp/resources/0137b8b356a2468cb3c64ea6b9acb248.png\" align=\"middle\" />确定的平面区域为<img alt=\"\" src=\"/TestWebApp/resources/2e9c4c373e8c42e791870dec4e260ccd.png\" align=\"middle\" />证明：<img alt=\"\" src=\"/TestWebApp/resources/4b4a0c64d56f41e298a91d0be66db824.png\" align=\"middle\" />平面<img alt=\"\" src=\"/TestWebApp/resources/0263134026ef4a49b918a3cf2b0b4e60.png\" align=\"middle\" />哈哈哈";
	
	public static void main(String[] args) {
		s = "平面向量a，b满足|a＋2b|＝<img alt=\"\" src=\"/TestWebApp/resources/dfe60a87a0fba87a6c71df0079bf64bb.png\" align=\"middle\" /> ， 且a＋2b平行于直线y＝2x＋1，若b＝(2，－1)，则a＝________.";
		parse_mml_img_string(s, "<\\?xml(.*?)</math>");
	}

	private final static void parse_mml_img_string(String s, String regexString) {
		Pattern ptn = Pattern.compile(regexString);
		Matcher matcher = ptn.matcher(s);
		List<String> list = new ArrayList<String>();
		while (matcher.find()) {
			list.add(matcher.group());
		}
		if (list.size() == 0) {
			if (regexString.equals("<\\?xml(.*?)</math>")) {
				parse_mml_img_string(s, "<img(.*?) />");
			} else {
				System.out.println(s);
			}
		}
		for (int i=0; i<list.size(); i++) {
			String searchStr = list.get(i);
			/**
			 * subStr 为公式加前面的文字（可能是文字和图片[<img ... />]）
			 */
			String subStr = StringUtils.substring(s, 0, StringUtils.indexOf(s, searchStr) + searchStr.length());
			/**
			 * preStr 为subStr中的文字（可能是文字和图片[<img ... />]）
			 */
			String preStr = subStr.substring(0, subStr.indexOf(searchStr));
			/**
			 * nexStr 为subStr中的公式(等于searchStr)
			 */
			String nexStr = subStr.substring(subStr.indexOf(searchStr), subStr.length());
			
//			System.out.println(preStr);
			parse_mml_img_string(preStr, "<img(.*?) />");
			System.out.println(nexStr);
			
			s = StringUtils.removeStart(s, subStr);
			/**
			 * 最后可能存在的文本
			 */
			if (i + 1 == list.size()) {
				parse_mml_img_string(s, "<img(.*?) />");
			}
		}		
	}
}
