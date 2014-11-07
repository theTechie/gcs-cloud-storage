package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RemoveAll extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");

			ArrayList<String> files = GoogleCloudStorageHelper.listFiles();
			for (String file : files) {
				Boolean isDeleted = GoogleCloudStorageHelper.removeFile(file);
				if (isDeleted) {
					log.info("Successfully Deleted : " + file);
				} else {
					log.info("Failed to Delete : " + file);
				}
			}
			MemCacheHelper.removeAll(files);
			
			res.getOutputStream().print("Successfully Deleted all files from Storage !");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}	
	}
}
