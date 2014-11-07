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

			if (MemCacheHelper.containsKey(file)) {
				res.setHeader("Content-disposition", "attachment; filename=" + file);
				res.setCharacterEncoding("UTF-8");
				res.getOutputStream().write((byte[]) MemCacheHelper.get(file));
			} else if (GoogleCloudStorageHelper.checkFile(file)){
				GoogleCloudStorageHelper.findFile(file, res);
			} else {
				log.warning("File Not Found in MemCache nor GCS : " + file);
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}
