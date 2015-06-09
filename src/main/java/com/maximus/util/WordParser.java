package com.maximus.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.StringUtils;
import org.docx4j.XmlUtils;
import org.docx4j.dml.Graphic;
import org.docx4j.dml.picture.Pic;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.Text;

public class WordParser {

	/**
	 * 处理文本
	 * @param wordprocessingMLPackage
	 * @param content
	 * @return
	 */
	public static String processJAXBElementTEXT(WordprocessingMLPackage wordprocessingMLPackage, List<Object> content) {
		StringBuilder sb = new StringBuilder();
		for (Object child : content) {
			if (child instanceof JAXBElement<?>) {
				JAXBElement<?> elem = (JAXBElement<?>) child;
				Class<?> elemType = elem.getDeclaredType();
				if (elemType.equals(Text.class)) {
					sb.append(processText((Text) elem.getValue()));
				} else if (elemType.equals(Drawing.class)) {
					Drawing drawing = (Drawing) elem.getValue();				
					sb.append("<img alt=\"\" src=\""+ConstantUtil.getInstance().getContextPath()+"/resources/"+processDrawing(wordprocessingMLPackage, drawing)+"\" align=\"middle\" />");
				}
			}
		}
		return sb.toString();
	}
	/**
	 * 提取文本
	 * @param text
	 * @return
	 */
	private final static String processText(Text text) {
		return text.getValue();
	}
	/**
	 * save image and return image name
	 * @param wordprocessingMLPackage
	 * @param drawing
	 * @return
	 */
	private final static String processDrawing(WordprocessingMLPackage wordprocessingMLPackage, Drawing drawing) {
		Object o = drawing.getAnchorOrInline().get(0);
		if (o instanceof Inline) {
			Inline inline = (Inline) o;
			Graphic graphic = inline.getGraphic();
			Pic pic = graphic.getGraphicData().getPic();
			String fileName = pic.getNvPicPr().getCNvPr().getName();
			String newFileName = RandomToolkit.getId(false);
			try {
				if (fileName.endsWith(".png") || fileName.endsWith(".jpg")) {
					saveImage(wordprocessingMLPackage, graphic, ConstantUtil.getInstance().getRealPath() + ConstantUtil.UPLOAD_DIR + newFileName);
					return newFileName;
				} else {
					/*StringUtils.deleteWhitespace(fileName)*/
					saveImage(wordprocessingMLPackage, graphic, ConstantUtil.getInstance().getRealPath() + ConstantUtil.UPLOAD_DIR + newFileName + ".png");
					return newFileName + ".png";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}	
	/**
	 * save image
	 * @param wordPackage
	 * @param graphic
	 * @param saveFilePath
	 * @throws IOException
	 */
	private static void saveImage(WordprocessingMLPackage wordPackage, Graphic graphic, String saveFilePath) throws IOException {
		byte[] bytes = BinaryPartAbstractImage.getImage(wordPackage, graphic);
		if(bytes == null) return;
		OutputStream os = new FileOutputStream(new File(saveFilePath));
		os.write(bytes);
		os.flush();
		os.close();
	}
	/**
	 * 
	 * @param elem
	 * @return
	 */
	public static String processOMath(JAXBElement<?> elem) {
		/**
		 * true : 不打印 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
		 * false : 不格式化xml代码
		 */
		return XmlUtils.marshaltoString(elem, false, false);
	}	
}
