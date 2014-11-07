package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RemoveFile extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			String file = req.getParameter("fileName");

			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");

			Boolean isDeleted = false, isExist = false;
			
			if (MemCacheHelper.containsKey(file)) {
				MemCacheHelper.remove(file);
			}
			
			if (GoogleCloudStorageHelper.checkFile(file)) {
				isExist = true;
				isDeleted = GoogleCloudStorageHelper.removeFile(file);
			}
			
			if (!isExist) {
				res.getOutputStream().print(
						"<b> File Not Found : " + file + "</b>");
				log.info("File Not Found: " + file);
			} else if (isDeleted) {
				res.getOutputStream().print(
						"<b> File Deleted Successfully : " + file + " ! </b>");
				log.info("File Deleted Successfully: " + file);
			} else {
				res.getOutputStream().print(
						"<b> File Not Deleted : " + file + "</b>");
				log.info("File Not Deleted: " + file);
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}
