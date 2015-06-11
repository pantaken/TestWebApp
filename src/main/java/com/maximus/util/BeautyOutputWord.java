package com.maximus.util;

import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Tag;
import javax.xml.bind.JAXBException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlgraphics.image.loader.ImageSize;
import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;
import org.docx4j.wml.BooleanDefaultTrue;
import org.docx4j.wml.CTSignedHpsMeasure;
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
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.maximus.db.QueryHelper;

public class BeautyOutputWord {

	private final static String mml_regex = "<\\?xml(.*?)</math>";
	private final static String img_regex = "<img(.*?) />";
	
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
//		WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();
		WordprocessingMLPackage wordprocessingMLPackage = Docx4J.load(new File("F:\\workspace\\TestWebApp\\src\\main\\webapp\\template\\Docx4j.docx"));
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
//			this.add_fix_text_omath(p, true, s, mainDocumentPart, wordprocessingMLPackage);
			P contentP = this.parse_mml_img_string(wordprocessingMLPackage,p, true, s, mml_regex);
			mainDocumentPart.addObject(contentP);
			/**
			 * 添加被选答案A、B、C、D
			 * 单独一行的OMath公式默认为两端对齐
			 * 参考omath对齐方式.txt设置对齐方式
			 */
			if (sA != null) {
				P paragraph_indicateAnswer_A = factory.createP();
				this.parse_mml_img_string(wordprocessingMLPackage,paragraph_indicateAnswer_A, false, "(A) " + sA, mml_regex);
				mainDocumentPart.addObject(paragraph_indicateAnswer_A);
			}
			if (sB != null) {
				P paragraph_indicateAnswer_B = factory.createP();
				this.parse_mml_img_string(wordprocessingMLPackage,paragraph_indicateAnswer_B, false, "(B) " + sB, mml_regex);
				mainDocumentPart.addObject(paragraph_indicateAnswer_B);
			}
			if (sC != null) {
				P paragraph_indicateAnswer_C = factory.createP();
				this.parse_mml_img_string(wordprocessingMLPackage,paragraph_indicateAnswer_C, false, "(C) " + sC, mml_regex);
				mainDocumentPart.addObject(paragraph_indicateAnswer_C);
			}
			if (sD != null) {
				P paragraph_indicateAnswer_D = factory.createP();
				this.parse_mml_img_string(wordprocessingMLPackage,paragraph_indicateAnswer_D, false, "(D) " + sD, mml_regex);
				mainDocumentPart.addObject(paragraph_indicateAnswer_D);
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
	private final P parse_mml_img_string(WordprocessingMLPackage wordprocessingMLPackage,P p,boolean isNumbered,String s, String regexString) throws Exception {
		if (isNumbered)
			this.setPropertyNumberedParagraph(1, 6, p);
		Pattern ptn = Pattern.compile(regexString);
		Matcher matcher = ptn.matcher(s);
		List<String> mmlList = new ArrayList<String>();
		while (matcher.find()) {
			mmlList.add(matcher.group());
		}
		if (mmlList.size() == 0) {
//			System.out.println(s);
			if (regexString.equals("<\\?xml(.*?)</math>")) {
				parse_mml_img_string(wordprocessingMLPackage,p,isNumbered,s, "<img(.*?) />");
			} else {
//				System.out.println(s);
				this.addText(p, s, null);
			}
			
		}
		for (int i=0; i<mmlList.size(); i++) {
			String searchStr = mmlList.get(i);
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
			parse_mml_img_string(wordprocessingMLPackage,p, isNumbered, preStr, img_regex);
//			System.out.println(nexStr);
			if (nexStr.startsWith("<?xml")) {
				InputStream ommlInputStream = new ByteArrayInputStream(XSLTransformer.processMML2OMML(nexStr).getBytes());
				this.addOMath(p, ommlInputStream);				
			} else if (nexStr.startsWith("<img")) {
				Document doc = Jsoup.parse(nexStr);
				Elements elements = doc.getElementsByTag("img");
//				URL fileurl = new URL(ConstantUtil.getInstance().getBasePath() + elements.get(0).attr("src"));
//				BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createLinkedImagePart(wordprocessingMLPackage, fileurl);
				String path = "F:/webapps" + elements.get(0).attr("src");
				BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(wordprocessingMLPackage, convertImageToByteArray(new File(path)));
				int docPrId = 1;
				int cNvPrId = 2;
				ImageSize size = imagePart.getImageInfo().getSize();
				Dimension dPx = size.getDimensionPx();
				Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, false);					
				this.addInlineImageToParagraph(p, inline);				
			}
			
			s = StringUtils.removeStart(s, subStr);
			/**
			 * 最后可能存在的文本
			 */
			if (i + 1 == mmlList.size()) {
				parse_mml_img_string(wordprocessingMLPackage,p, isNumbered, s, img_regex);
			}
		}	
		return p;
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
		/**
		 * 使图片下降5磅
		 */
		RPr rPr = factory.createRPr();
		CTSignedHpsMeasure value = new CTSignedHpsMeasure();
		value.setVal(BigInteger.valueOf(-10));
		rPr.setPosition(value);
		
		R run =  factory.createR();
		run.setRPr(rPr);
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
		return getRPr(FONT_YAHEI, "000000", "20", STHint.EAST_ASIA, false);
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
	/**
	 * 文件转字节数组
	 * @param file
	 * @return
	 * @throws IOException
	 */	
	private static byte[] convertImageToByteArray(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("文件大小超过限制");
		}
		byte[] bytes = new byte[is.available()];
		int offset = 0;
		int len = 0;
		//从文件输入流is中读取bytes.leng - offset个字节到起始偏移量为offset的缓冲区bytes中,并返读入缓冲区中的字节总数
		while (offset < bytes.length && (len = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += len;
		}
		if (offset < bytes.length) {
			System.out.println("不能完全读取文件内容");
		}
		is.close();
		return bytes;
	}
	/**
	 * 文件转字节数组
	 * @param file
	 * @return
	 */
	private static byte[] convertToByteArray(File file) {
		byte[] byteArray = null;
		if (file == null) {
			return null;
		}
		try {
			InputStream is = new FileInputStream(file);
			ByteArrayOutputStream out = new ByteArrayOutputStream(4094);
			byte[] b = new byte[1024];
			int len = 0;
			while((len = is.read(b)) != -1) {
				out.write(b, 0, len);
			}
			is.close();
			out.close();
			byteArray = out.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return byteArray;
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
	private static final String FONT_YAHEI = "微软雅黑";
}
