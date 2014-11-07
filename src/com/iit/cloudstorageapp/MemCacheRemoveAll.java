package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class MemCacheRemoveAll extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");
			
			ArrayList<String> keys = GoogleCloudStorageHelper.listFiles();
			MemCacheHelper.removeAll(keys);
			
			log.info("Removed all MemCache data !");
			res.getOutputStream().print("Successfully Removed all MemCache data !");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}
}
