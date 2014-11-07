package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FileLister extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");
			

			ArrayList<String> fileList = GoogleCloudStorageHelper.listFiles();
			ServletOutputStream outputStream = res.getOutputStream();
			
			if (fileList.isEmpty()) {
				outputStream.print("Data Storage is Empty");
			} else {
				outputStream.print("Total Storage Size (MB) : " + GoogleCloudStorageHelper.getStorageSizeMB());
				
				outputStream.print("<br/><ol>");
				log.info("List of Files => ");
				
				for (String file : fileList) {
					outputStream.print("<li>" + file + "</li>");

					log.info("File: " + file);
				}
				
				outputStream.print("</ol>");
			}			

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
