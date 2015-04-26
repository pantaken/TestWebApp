package com.maximus.util;

import java.awt.Dimension;
import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

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

public class MixOutputWord {

	private ObjectFactory factory = Context.getWmlObjectFactory();
	
	private QueryHelper queryHelper;
	private String outputFileName;
	public void setQueryHelper(QueryHelper queryHelper) {
		this.queryHelper = queryHelper;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = ConstantUtil.getInstance().getRealPath() + "resources/" + outputFileName;
	}
	
	
	public void save() throws Exception {
		WordprocessingMLPackage wordprocessingMLPackage = WordprocessingMLPackage.createPackage();
		MainDocumentPart mainDocumentPart = wordprocessingMLPackage.getMainDocumentPart();
		NumberingDefinitionsPart ndp = this.getNumbering();
		if (ndp != null) {
			mainDocumentPart.addTargetPart(ndp);
		}
		List<Map<String, Object>> lists = this.queryHelper.query("SELECT * FROM pub_question_common pqc");
		for (Map<String, Object> map : lists) {
			String s = (String) map.get("content");
			Document doc = Jsoup.parse(s);
			Elements elements = doc.getElementsByTag("img");
			/**
			 * 需要考究的截取字符串的函数
			 */
			P p = factory.createP();
			this.setPropertyNumberedParagraph(1, 6, p);
			for (int x=0; x<=elements.size(); x++) {
				int index = x==0 ? 0 : s.indexOf(elements.get(x - 1).outerHtml()) + elements.get(x - 1).outerHtml().length();
				int end = x==elements.size() ? s.length() : s.indexOf(elements.get(x).outerHtml());
				if (x > 0 && !"".equals(elements.get(x - 1).toString())) {
					URL fileurl = new URL(elements.get(x - 1).attr("src"));
					BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createLinkedImagePart(wordprocessingMLPackage, fileurl);
					int docPrId = 1;
					int cNvPrId = 2;
					long cx, cy;
					ImageSize size = imagePart.getImageInfo().getSize();
					Dimension dPx = size.getDimensionPx();
					Inline inline = imagePart.createImageInline("Filename hint", "Alternative text", docPrId, cNvPrId, UnitsOfMeasurement.twipToEMU( dPx.getWidth()*3), UnitsOfMeasurement.twipToEMU(dPx.getHeight()*3), false);					
					this.addInlineImageToParagraph(p, inline);					
					
//					mainDocumentPart.addObject(this.createNumberedParagraph(1, 6, elements.get(x - 1).toString()));
				}
				if (!"".equals(s.subSequence(index, end).toString())) {
//					mainDocumentPart.addObject(this.createNumberedParagraph(1, 6, s.subSequence(index, end).toString()));
					this.addText(p, s.subSequence(index, end).toString(), null);
				}
				
			}
			mainDocumentPart.addObject(p);
			
		}
		wordprocessingMLPackage.save(new File(outputFileName));
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
		return getRPr(FONT_YAHEI, "000000", "30", STHint.EAST_ASIA, true);
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
	private static final String FONT_YAHEI = "宋体";
}
