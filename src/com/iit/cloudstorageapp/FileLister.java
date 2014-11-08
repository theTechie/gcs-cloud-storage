package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class FileLister extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());
	private DatastoreService datastoreService =
			DatastoreServiceFactory.getDatastoreService();

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
				
				Query query = new Query("FileMapper");
				List<Entity> entities = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
				
				for (Entity entity : entities) {
					outputStream.print("<li>" + entity.getKey().getName() + "</li>");

					log.info("File: " + entity.getKey().getName());
				}
				
				outputStream.print("</ol>");
			}			

		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}