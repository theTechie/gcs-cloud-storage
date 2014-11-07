package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MemCacheStats extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");
			
			ServletOutputStream outputStream = res.getOutputStream();
			outputStream.println("<b> MemCache Stats </b>");
			outputStream.print("<ul>");
			outputStream.print("<li> Cache Size (elements) : " + MemCacheHelper.getCacheSizeElem() + "</li>");
			outputStream.println("<li> MemCache Size (MB) : " + MemCacheHelper.getCacheSizeMB() + "</li>");
			outputStream.print("</ul>");
			
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}
}
