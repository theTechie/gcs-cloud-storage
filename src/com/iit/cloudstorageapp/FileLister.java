package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletException;
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
			res.getOutputStream().print("<ol>");

			ArrayList<String> fileList = GoogleCloudStorageHelper.listFiles();

			for (String file : fileList) {
				res.getOutputStream().print("<li>" + file + "</li>");

				log.info("File: " + file);
			}

			res.getOutputStream().print("</ol>");

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
