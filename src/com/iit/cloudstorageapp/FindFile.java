package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FindFile extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			String file = req.getParameter("fileName");

			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");

			GoogleCloudStorageHelper.findFile(file, res);

			
			//res.getOutputStream().write(bytes);
			
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}
