package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CheckFile extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			String file = req.getParameter("fileName");

			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");

			Boolean isExist = GoogleCloudStorageHelper.checkFile(file);

			if (isExist) {
				res.getOutputStream().print(
						"<b> File Found : " + file + " ! </b>");
				log.info("File Found: " + file);
			} else {
				res.getOutputStream().print(
						"<b> File Not Found : " + file + "</b>");
				log.info("File Not Found: " + file);
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}
