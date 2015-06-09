package com.maximus.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

public class IOHttp {


	/**
	 * 文件上传成功则返回文件名，否则返回null
	 * 该方存在一个同名文件上传的问题（待解决）
	 * @param request
	 * @return
	 */
	public static String upload(HttpServletRequest request, String saveDirectory) {
		
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
									new File(ConstantUtil.getInstance().getRealPath() + saveDirectory + fileName));						
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
	/**
	 * 下载文件
	 * @param response
	 * @param filePath
	 */
	public static void download(HttpServletResponse response, String filePath) {
		try {
			File f = new File(filePath);
			//读到流中
			InputStream in = new FileInputStream(f);
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename="+f.getName());
			response.addHeader("Content-Length", "" + f.length());
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				response.getOutputStream().write(buffer, 0, len);
			}
			in.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
