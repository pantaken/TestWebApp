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

public class IOHttp {


	/**
	 * 文件上传成功则返回文件名，否则返回null
	 * @param request
	 * @return
	 */
	public static String upload(HttpServletRequest request) {
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		String fileName = "";
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
							fileName = item.getName();
							FileUtils.copyInputStreamToFile(item.getInputStream(), 
									new File(ConstantUtil.getInstance().getRealPath() + ConstantUtil.UPLOAD_DIR + fileName));						
						}
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (FileUploadException e) {
			e.printStackTrace();
			return null;
		}
		return fileName;
	}
}
