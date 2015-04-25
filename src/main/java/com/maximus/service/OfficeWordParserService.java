package com.maximus.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.docx4j.Docx4J;
import org.docx4j.math.CTOMath;
import org.docx4j.math.CTOMathPara;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.P;
import org.docx4j.wml.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maximus.bean.BeautyQuestion;
import com.maximus.dao.IOfficeWordParserDao;
import com.maximus.db.QueryHelper;
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
	/**
	 * 解析并持久化word试题
	 * @param filePath
	 * @throws Docx4JException
	 */
	public void process(String filePath) throws Docx4JException {
		this.wordprocessingMLPackage = Docx4J.load(new File(filePath));
		MainDocumentPart mainDocumentPart = this.wordprocessingMLPackage.getMainDocumentPart();
		List<Object> content = mainDocumentPart.getContent();
		this.processParagraph(content);
	}

	/**
	 * 遍历段落
	 * @param content
	 */
	private final void processParagraph(List<Object> content) {
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
				if (!"".equals(mml) && !mml.startsWith("#img#")) {
					if (contents == null) contents = new ArrayList<String>();
					contents.add(mml);
				} else if (mml.startsWith("#img#")) {
					img = mml.split("=")[1];
				} else if ("".equals(mml)) {
					String[] mmls = contents.toArray(new String[contents.size()]);
					BeautyQuestion bean = new BeautyQuestion();
					bean.setContent(mmls[0]);
					bean.setA(mmls[1]);
					bean.setB(mmls[2]);
					bean.setC(mmls[3]);
					bean.setD(mmls[4]);
					bean.setType(2);
					bean.setImg(img);
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
}
