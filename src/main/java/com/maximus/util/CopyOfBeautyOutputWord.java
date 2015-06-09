package com.maximus.util;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.docx4j.UnitsOfMeasurement;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.Color;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.HpsMeasure;
import org.docx4j.wml.Numbering;
import org.docx4j.wml.ObjectFactory;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.PPrBase.NumPr;
import org.docx4j.wml.PPrBase.NumPr.Ilvl;
import org.docx4j.wml.PPrBase.NumPr.NumId;
import org.docx4j.wml.R;
import org.docx4j.wml.RFonts;
import org.docx4j.wml.RPr;
import org.docx4j.wml.STHint;
import org.docx4j.wml.Text;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.maximus.db.QueryHelper;

public class CopyOfBeautyOutputWord {

	private ObjectFactory factory = Context.getWmlObjectFactory();
	
	private QueryHelper queryHelper;
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}
	private String outputFileName;
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = ConstantUtil.getInstance().getRealPath() + "resources/" + outputFileName;
	}
	/**
	 * 创建word文档
	 * @throws Exception
	 */
	public void save() throws Exception {
		WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();
		MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
		NumberingDefinitionsPart ndp = this.getNumbering();
		if (ndp != null) {
			mainDocumentPart.addTargetPart(ndp);
		}
		List<Map<String, Object>> lists = this.queryHelper.query("SELECT * FROM pub_question_content pqc");
		for (Map<String, Object> map : lists) {
			String s = (String) map.get("content");
			String sA = (String) map.get("selectA");
			String sB = (String) map.get("selectB");
			String sC = (String) map.get("selectC");
			String sD = (String) map.get("selectD");
			/**
			 * 添加选择题的题干
			 */
			P p = factory.createP();
			this.add_fix_text_omath(p, true, s, mainDocumentPart, wordprocessingMLPackage);
			/**
			 * 添加被选答案A、B、C、D
			 * 单独一行的OMath公式默认为两端对齐
			 * 参考omath对齐方式.txt设置对齐方式
			 */
			if (sA != null) {
				P paragraph_indicateAnswer_A = factory.createP();
				this.add_fix_text_omath(paragraph_indicateAnswer_A, false, "(A) " + sA, mainDocumentPart, wordprocessingMLPackage);				
			}
			if (sB != null) {
				P paragraph_indicateAnswer_B = factory.createP();
				this.add_fix_text_omath(paragraph_indicateAnswer_B, false, "(B) " + sB, mainDocumentPart, wordprocessingMLPackage);				
			}
			if (sC != null) {
				P paragraph_indicateAnswer_C = factory.createP();
				this.add_fix_text_omath(paragraph_indicateAnswer_C, false, "(C) " + sC, mainDocumentPart, wordprocessingMLPackage);				
			}
			if (sD != null) {
				P paragraph_indicateAnswer_D = factory.createP();
				this.add_fix_text_omath(paragraph_indicateAnswer_D, false, "(D) " + sD, mainDocumentPart, wordprocessingMLPackage);				
			}
		}
		wordprocessingMLPackage.save(new File(outputFileName));
	}
	/**
	 * 添加混淆文字和OpenXmlFormat数学公式的字符串到指定段落，并指定段落是否列表显示
	 * @param p
	 * @param isNumbered
	 * @param fix_text_omath_string
	 * @param docPart
	 * @throws Exception 
	 */
	private void add_fix_text_omath(P p, boolean isNumbered, String fix_text_omath_string, MainDocumentPart docPart,WordprocessingMLPackage wordprocessingMLPackage) throws Exception {
		if (isNumbered) 
			this.setPropertyNumberedParagraph(1, 6, p);
		Pattern ptn = Pattern.compile("<\\?xml(.*?)</math>");
		Matcher matcher = ptn.matcher(fix_text_omath_string);
		List<String> mmlList = new ArrayList<String>();
		while (matcher.find()) {
			mmlList.add(matcher.group());
		}
		//字符串中没有oMath公式
		if (mmlList.size() == 0) {
			this.addText(p, fix_text_omath_string, null);
		}
		else {
		/**
		 * 需要考究的截取字符串的函数
		 */
			for (int i=0; i<mmlList.size(); i++) {
				String searchStr = mmlList.get(i);
				String subStr = StringUtils.substring(fix_text_omath_string, 0, StringUtils.indexOf(fix_text_omath_string, searchStr) + searchStr.length());
				String preStr = subStr.substring(0, subStr.indexOf(searchStr));
				String nexStr = subStr.substring(subStr.indexOf(searchStr), subStr.length());
				//添加文本
				this.addText(p, preStr, null);
	//			System.out.println(preStr);
				//添加omml数学公式
				InputStream ommlInputStream = new ByteArrayInputStream(XSLTransformer.processMML2OMML(nexStr).getBytes());
				this.addOMath(p, ommlInputStream);
				System.out.println(nexStr);
				fix_text_omath_string = StringUtils.removeStart(fix_text_omath_string, subStr);
				//最后可能存在的文本
				if (i + 1 == mmlList.size()) {
					//添加文本
					this.addText(p, fix_text_omath_string, null);
	//				System.out.println(fix_text_omath_string);
				}
			}
		}
//		System.out.println("----------------------------------------------------------------------------------------");
		docPart.addObject(p);		
	}
	/**
	 * 创建 定义项目列表、序号的对象
	 * @return 创建numbering.xml
	 */
	private NumberingDefinitionsPart getNumbering() {
		try {
			NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
			ndp.setJaxbElement((Numbering) XmlUtils.unmarshalString(initialNumbering));
			return ndp;
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 创建带有项目列表、编号的段落
	 * @param numId
	 * @param ilvl
	 * @param paragraphText
	 * @return
	 */
	private P createNumberedParagraph(long numId, long ilvl, String paragraphText) {
		P p = factory.createP();
		Text t = factory.createText();
		t.setValue(paragraphText);
		
		R r = factory.createR();
		r.getContent().add(t);
		
		p.getContent().add(r);
		
		PPr pPr = factory.createPPr();
		p.setPPr(pPr);
		
		NumPr numPr = factory.createPPrBaseNumPr();
		pPr.setNumPr(numPr);
		
		Ilvl ilvlElement = factory.createPPrBaseNumPrIlvl();
		NumId numIdElement = factory.createPPrBaseNumPrNumId();
		numPr.setIlvl(ilvlElement);
		numPr.setNumId(numIdElement);
		
		ilvlElement.setVal(BigInteger.valueOf(ilvl));
		numIdElement.setVal(BigInteger.valueOf(numId));
		
		return p;
	}
	/**
	 * 设置段落项目列表、序号属性
	 * @param numId
	 * @param ilvl
	 * @param p
	 * @return
	 */
	private P setPropertyNumberedParagraph(long numId, long ilvl, P p) {
				
		PPr pPr = factory.createPPr();
		p.setPPr(pPr);
		
		NumPr numPr = factory.createPPrBaseNumPr();
		pPr.setNumPr(numPr);
		
		Ilvl ilvlElement = factory.createPPrBaseNumPrIlvl();
		NumId numIdElement = factory.createPPrBaseNumPrNumId();
		numPr.setIlvl(ilvlElement);
		numPr.setNumId(numIdElement);
		
		ilvlElement.setVal(BigInteger.valueOf(ilvl));
		numIdElement.setVal(BigInteger.valueOf(numId));
		
		return p;		
	}
	/**
	 * 增加内联图片到指定段落
	 * @param inline
	 * @return
	 */
	private P addInlineImageToParagraph(P p, Inline inline) {
		R run =  factory.createR();
		p.getContent().add(run);
		Drawing drawing = factory.createDrawing();
		run.getContent().add(drawing);
		drawing.getAnchorOrInline().add(inline);
		return p;
	}	
	/**
	 * 向指定的段落添加文本信息
	 * @param wmlP
	 * @param text
	 * @param rPr
	 */
	public void addText(P wmlP, String text, RPr rPr) {
		R wmlR = factory.createR();
		if (rPr == null) wmlR.setRPr(getRPr());
		else wmlR.setRPr(rPr);
		wmlP.getContent().add(wmlR);
		Text wmlText = factory.createText();
		wmlText.setValue(text);
		wmlR.getContent().add(wmlText);
	}	
	/**
	 * 获取默认是：宋体、黑色、22、eastAisa格式、不加粗的字体样式
	 * @return RPr
	 */
	private RPr getRPr() {
		return getRPr(FONT_SONGTI, "000000", "20", STHint.EAST_ASIA, false);
	}
	/**
	 * 获取设置的字体样式
	 * @param fontFamily 字体
	 * @param colorValue 颜色
	 * @param fontSize 字号
	 * @param stHint 字体格式
	 * @param isBold 是否加粗
	 * @return RPr 字体样式对象
	 */
	private RPr getRPr(String fontFamily, String colorValue, String fontSize, STHint stHint, boolean isBold) {
		RPr rPr = factory.createRPr();
		RFonts rFonts = new RFonts();
		rFonts.setHint(stHint);
		rFonts.setAscii(fontFamily);
		rFonts.setHAnsi(fontFamily);
		rFonts.setEastAsia(fontFamily);
		rPr.setRFonts(rFonts);
		
		BooleanDefaultTrue bdt = factory.createBooleanDefaultTrue();
		rPr.setBCs(bdt);
		if (isBold) rPr.setB(bdt);
		
		Color color = new Color();
		color.setVal(colorValue);
		rPr.setColor(color);
		
		HpsMeasure sz = new HpsMeasure();
		sz.setVal(new BigInteger(fontSize));
		
		rPr.setSz(sz);
		rPr.setSzCs(sz);
		
		return rPr;
	}
	/**
	 * 向指定的段落添加数学公式
	 * @param wmlP
	 * @param ommlStream 数学公式的输入流
	 * @throws JAXBException
	 */
	private void addOMath(P wmlP, InputStream inputStream) throws JAXBException {
		/**
		 * 将OMML输入流转换为JAXBElement对象
		 */
		wmlP.getContent().add(XmlUtils.unmarshal(inputStream));
	}
	private static final String initialNumbering = "<w:numbering xmlns:ve=\"http://schemas.openxmlformats.org/markup-compatibility/2006\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" xmlns:w10=\"urn:schemas-microsoft-com:office:word\" xmlns:w=\"http://schemas.openxmlformats.org/wordprocessingml/2006/main\" xmlns:wne=\"http://schemas.microsoft.com/office/word/2006/wordml\">"
		    + "<w:abstractNum w:abstractNumId=\"0\">"
		    + "<w:nsid w:val=\"2DD860C0\"/>"
		    + "<w:multiLevelType w:val=\"multilevel\"/>"
		    + "<w:tmpl w:val=\"0409001D\"/>"
		    + "<w:lvl w:ilvl=\"0\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"%1)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"1\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"%2)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"2\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"%3)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"3\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"(%4)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"4\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"(%5)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"5\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"(%6)\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"6\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"decimal\"/>"
		        + "<w:lvlText w:val=\"%7.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"7\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerLetter\"/>"
		        + "<w:lvlText w:val=\"%8.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		    + "<w:lvl w:ilvl=\"8\">"
		        + "<w:start w:val=\"1\"/>"
		        + "<w:numFmt w:val=\"lowerRoman\"/>"
		        + "<w:lvlText w:val=\"%9.\"/>"
		        + "<w:lvlJc w:val=\"left\"/>"
		        + "<w:pPr>"
		            + "<w:ind w:left=\"360\" w:hanging=\"360\"/>"
		        + "</w:pPr>"
		    + "</w:lvl>"
		+ "</w:abstractNum>"
		+ "<w:num w:numId=\"1\">"
		    + "<w:abstractNumId w:val=\"0\"/>"
		 + "</w:num>"
		+ "</w:numbering>";	
	private static final String FONT_SONGTI = "宋体";
}
