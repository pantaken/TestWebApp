package com.maximus.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.transform.TransformerException;

import my.spring.util.MSMF2IMG;

import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.dml.Graphic;
import org.docx4j.dml.picture.Pic;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.math.CTOMath;
import org.docx4j.math.CTOMathPara;
import org.docx4j.model.listnumbering.Emulator;
import org.docx4j.model.listnumbering.Emulator.ResultTriple;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart;
import org.docx4j.wml.Drawing;
import org.docx4j.wml.P;
import org.docx4j.wml.PPr;
import org.docx4j.wml.R;
import org.docx4j.wml.Text;
import org.docx4j.wml.PPrBase.NumPr;
import org.docx4j.wml.PPrBase.PStyle;
import org.docx4j.wml.PPrBase.NumPr.Ilvl;
import org.docx4j.wml.PPrBase.NumPr.NumId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maximus.bean.BeautyQuestion;
import com.maximus.dao.IOfficeWordParserDao;
import com.maximus.db.QueryHelper;
import com.maximus.util.Configuration;
import com.maximus.util.ConstantUtil;
import com.maximus.util.MF2IMG;
import com.maximus.util.RandomToolkit;
import com.maximus.util.WordParser;
import com.maximus.util.XSLTransformer;

@Service
public class OfficeWordParserService {
	
	@Autowired
	private QueryHelper queryHelper;
	
	@Autowired
	private IOfficeWordParserDao officeWordParserDao;
	
	private final Logger logger = LoggerFactory.getLogger(OfficeWordParserService.class);
	
	private WordprocessingMLPackage wordprocessingMLPackage;
	private RelationshipsPart rels;
	/**
	 * 解析并持久化beauty word试题
	 * @param filePath
	 * @param categorycode 
	 * @param subjectcode 
	 * @throws Docx4JException
	 */
	public void process(String filePath, String subjectcode, String categorycode) throws Docx4JException {
		this.wordprocessingMLPackage = Docx4J.load(new File(filePath));
		MainDocumentPart mainDocumentPart = this.wordprocessingMLPackage.getMainDocumentPart();
		List<Object> content = mainDocumentPart.getContent();
		this.processSelectParagraph(content, subjectcode, categorycode);
		//this.processBlankParagraph(content, subjectcode, categorycode);
	}

	/**
	 * 遍历段落（填空题）
	 * @param content
	 * @param subjectcode
	 * @param categorycode
	 */
	private final void processBlankParagraph(List<Object> content, String subjectcode, String categorycode) {
		List<Object> question = new ArrayList<Object>();
		for (Object o : content) {
			if (o instanceof P) {
				P p = (P) o;
				
				StringBuffer sb = new StringBuffer();
				for (Object child : p.getContent()) {
					if (child instanceof R) {
						R r = (R) child;
						sb.append(WordParser.processJAXBElementTEXT(this.wordprocessingMLPackage, r.getContent()));
					} else if (child instanceof JAXBElement<?>) {
						JAXBElement<?> el = (JAXBElement<?>) child;
						Class<?> elType = el.getDeclaredType();
						if (elType.equals(CTOMath.class) || elType.equals(CTOMathPara.class)) {
							try {
								sb.append(XSLTransformer.processOMML2MML(WordParser.processOMath(el)));
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							} catch (TransformerException e) {
								e.printStackTrace();
							}
						}
					}
				}
				BeautyQuestion bean = new BeautyQuestion();
				bean.setContent(sb.toString());
				bean.setSubjectcode(subjectcode);
				bean.setCategorycode(categorycode);
				question.add(bean);
			}
		}
		officeWordParserDao.insertBatchBlank(question);
	}
	/**
	 * 遍历段落(选择题)
	 * @param content
	 * @param categorycode 
	 * @param subjectcode 
	 */
	private final void processSelectParagraph(List<Object> content, String subjectcode, String categorycode) {
		List<Object> question = new ArrayList<Object>();
		List<String> contents = null;
		String img = "";
		for (Object o : content) {
			if (o instanceof P) {
				P p = (P) o;
				
				StringBuilder sb_mml = new StringBuilder();
				StringBuilder sb_omml = new StringBuilder();
				
				for (Object child : p.getContent()) {
					if (child instanceof R) {
						R r = (R) child;
						sb_mml.append(WordParser.processJAXBElementTEXT(this.wordprocessingMLPackage, r.getContent()));
					} else if (child instanceof JAXBElement<?>) {
						JAXBElement<?> elem = (JAXBElement<?>) child;
						Class<?> elemType = elem.getDeclaredType();
						if (elemType.equals(CTOMath.class) || elemType.equals(CTOMathPara.class)) {
							String ommlString = WordParser.processOMath(elem);
							try {
								sb_mml.append(XSLTransformer.processOMML2MML(ommlString));
								sb_omml.append(ommlString);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}						
					}
				}
				logger.info(">>>parser mml string : " + sb_mml.toString());
				String mml = sb_mml.toString();
				if (!"".equals(mml) && !mml.startsWith("<img")) {
					if (contents == null) contents = new ArrayList<String>();
					contents.add(mml);
				} else if (mml.startsWith("<img")) {
					Document doc = Jsoup.parse(mml);
					Elements elements = doc.getElementsByTag("img");
					img = elements.get(0).attr("src");
				} else if ("".equals(mml)) {
					String[] mmls = contents.toArray(new String[contents.size()]);
					BeautyQuestion bean = new BeautyQuestion();
					bean.setContent(mmls[0]);
					bean.setA(mmls[1]);
					bean.setB(mmls[2]);
					bean.setC(mmls[3]);
					bean.setD(mmls[4]);
					bean.setImg(img);
					bean.setSubjectcode(subjectcode);
					bean.setCategorycode(categorycode);
					question.add(bean);
					contents = null;
				}
			}
		}
		officeWordParserDao.insertBatch(question);
	}
	
	public List<?> query() {
		return officeWordParserDao.select();
	}
	/****************************************************************************************************************/
	private List<Object[]> questionImages = new ArrayList<Object[]>();
	private List<Object[]> questionCommons = new ArrayList<Object[]>();
	/**
	 * 解析并持久化mix word试题
	 * @param filePath
	 */
	public void mixprocess(String filePath) throws Docx4JException {
		this.wordprocessingMLPackage = Docx4J.load(new File(filePath));
		MainDocumentPart mainPart = wordprocessingMLPackage.getMainDocumentPart();
		this.rels = mainPart.getRelationshipsPart();
		List<Object> mainContents = mainPart.getContent();
		mixprocessParagraph(mainContents);
	}

	private final void mixprocessParagraph(List<Object> content) {
    	boolean paragraphBulletFlag = false;
    	StringBuffer sb_test = null;
    	//循环处理段落
        for (Object child : content) {
            if (child instanceof P) {
            	P p = (P) child;
            	PPr pPr = p.getPPr();
            	if (pPr != null) {
              		NumPr numPr = pPr.getNumPr();
              		//NumPr存在的P为列表段落,NumPr不存在的P为普通段落
            		if (numPr != null) {
                		PStyle pStyle = pPr.getPStyle();
                		NumId numId = numPr.getNumId();
                		Ilvl ilvl = numPr.getIlvl();
                		ResultTriple rt = Emulator.getNumber(
                				this.wordprocessingMLPackage, 
                				pStyle == null ? "" : pStyle.getVal(), 
                				String.valueOf(numId.getVal()),
                				String.valueOf(ilvl.getVal())
                		);
                		if (rt != null) {
//                			System.out.println(rt.getBullet());
//                			System.out.println(rt.getNumString());
                			paragraphBulletFlag = true;
                			
                		} 
            		}   
            	}
                if (paragraphBulletFlag) {
                	if (sb_test != null) questionCommons.add(new Object[]{sb_test.toString()});
                	sb_test = new StringBuffer();
                	sb_test.append(this.mixprocessR(p));
                	paragraphBulletFlag = false;
                } else {
                	sb_test.append("&nbsp;<br/>&nbsp;").append(this.mixprocessR(p));
                }
            }
        }
        
        for (Object[] o : questionCommons) {
        	String s = (String) o[0];
        	System.out.println(s);
        }
        queryHelper.batchUpdate("INSERT INTO pub_question_common(content) VALUES (?)",questionCommons);
        queryHelper.batchUpdate("INSERT INTO pub_question_image(imageuuid,filename,filepath,width,height) VALUES (?,?,?,?,?)", questionImages);		
	}
	private String mixprocessR(P p) {
    	StringBuilder sb = new StringBuilder();
    	for (Object o : p.getContent()) {
    		if (o instanceof R) {
				R r = (R) o;
				sb.append(mixprocessJAXBElementTEXT(r.getContent()));
    		}
    		else if (o instanceof JAXBElement<?>) {
    	    	JAXBElement<?> elem = (JAXBElement<?>) o;
    			Class<?> elemType = elem.getDeclaredType();
    			if (elemType.equals(CTOMath.class) || elemType.equals(CTOMathPara.class)) {
    				String uuid = RandomToolkit.getId(false);
    				sb.append("{" + uuid + "}");
    				String ommlString = mixprocessOMath(elem);
    				try {
    					sb.append(XSLTransformer.processOMML2MML(ommlString));
    				} catch (UnsupportedEncodingException e) {
    					e.printStackTrace();
    				} catch (TransformerException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    	}
    	return sb.toString();
    }	
    private String mixprocessJAXBElementTEXT(List<Object> content) {
    	StringBuilder sb = new StringBuilder();
		for (Object child : content) {
			if (child instanceof JAXBElement<?>) {
				JAXBElement<?> elem = (JAXBElement<?>) child;
				Class<?> elemType = elem.getDeclaredType();
				if (elemType.equals(Text.class)) {
					sb.append(mixprocessText((Text) elem.getValue()));
				} else if (elemType.equals(Drawing.class)) {
					Drawing drawing = (Drawing) elem.getValue();				
					sb.append(mixprocessDrawing(drawing));
				}
			}
		}
		return sb.toString();
    }	
    private String mixprocessText(Text text) {
    	return text.getValue();
    } 
	private String mixprocessDrawing(Drawing drawing) {
		Object o = drawing.getAnchorOrInline().get(0);
		if (o instanceof Inline) {
			Inline inline = (Inline) o;
			Graphic graphic = inline.getGraphic();
			Pic pic = graphic.getGraphicData().getPic();
			String embedId = pic.getBlipFill().getBlip().getEmbed();
			String target = rels.getRelationshipByID(embedId).getTarget();
			String picSuffix = target.substring(target.lastIndexOf('.'));

			String imageUUID = RandomToolkit.getId(false);
			String originalImageName = (pic.getNvPicPr().getCNvPr().getName() + picSuffix).replace(" ", "");
			String filepath = ConstantUtil.getInstance().getRealPath() + "resources" + ConstantUtil.SEPARATOR + originalImageName;
			int width = 0,height = 0;
			try {
				
				
				mixsaveImage(this.wordprocessingMLPackage, graphic, filepath);
				if (picSuffix.equals(MF2IMG.METAFILE_WMF)) {
					int[] picSize = MSMF2IMG.getInstance().conv(Integer.parseInt(Configuration.getInstance().getValue("msmf2imgScale")), filepath, filepath + ".png");
//					System.out.println(picSize[0]+ "*" + picSize[1]);
					questionImages.add(new Object[]{
							imageUUID,
							originalImageName + ".png",
							filepath + ".png",
							picSize[0],
							picSize[1]
					});
					width = picSize[0];
					height = picSize[1];
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
//			return imageUUID;
			return "<img alt=\"\" src=\""+ConstantUtil.getInstance().getContextPath()+"/resources/"+originalImageName+".png\" width=\""+width+"\" height=\""+height+"\" align=\"middle\" />";
		}
		return "";
	} 
	private void mixsaveImage(WordprocessingMLPackage wordPackage, Graphic graphic, String saveFilePath) throws IOException {
		byte[] bytes = BinaryPartAbstractImage.getImage(wordPackage, graphic);
		if(bytes == null) return;
		OutputStream os = new FileOutputStream(new File(saveFilePath));
		os.write(bytes);
		os.flush();
		os.close();
		bytes = null;
	}
    private String mixprocessOMath(JAXBElement<?> elem) {
    	/**
    	 * true : 不打印 <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    	 * false : 不格式化xml代码
    	 */
    	return XmlUtils.marshaltoString(elem, true, false);
    }	
}
