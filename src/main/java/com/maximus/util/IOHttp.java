package com.maximus.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.web.util.ServletContextPropertyUtils;
import org.springframework.web.util.WebUtils;

public class IOHttp {


	public static boolean upload(HttpServletRequest request) {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		try {
			if (isMultipart) {
				FileItemFactory factory = new DiskFileItemFactory();
				
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> items = upload.parseRequest(request);
				Iterator<FileItem> itors = items.iterator();
				while (itors.hasNext()) {
					FileItem item = (FileItem) itors.next();
					
					if (!item.isFormField()) {
						/**
						 * fileName : html control type
						 */
						String fieldName = item.getFieldName();
						if ("file".equals(fieldName)) {
							String fileName = item.getName();
							FileUtils.copyInputStreamToFile(item.getInputStream(), new File(request.getRealPath("/") +"resources/" + fileName));						
						}
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (FileUploadException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
