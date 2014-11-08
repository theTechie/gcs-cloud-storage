package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class CheckFile extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());
	private DatastoreService datastoreService =
			DatastoreServiceFactory.getDatastoreService();
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			String file = req.getParameter("fileName");
			
			/*BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();			
			file = blobstoreService.createGsBlobKey("/gs/" + file).getKeyString();*/
			
			Query query = new Query("FileMapper");
			Key fileKey = KeyFactory.createKey("FileMapper", file);			
			FilterPredicate filter = 
				    new FilterPredicate(Entity.KEY_RESERVED_PROPERTY,FilterOperator.EQUAL,fileKey);
			query.setFilter(filter);
			
			List<Entity> entities = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());
			
			for (Entity entity : entities) {
				file = entity.getProperty("name").toString();
				log.info("File: " + entity.getProperty("name").toString());
			}

			res.setContentType("text/html");
			res.setCharacterEncoding("UTF-8");

			if (MemCacheHelper.containsKey(file)) {
				res.getOutputStream().print(
						"<b> File Found in MemCache : " + req.getParameter("fileName").toString() + " ! </b>");
				log.info("File Found in MemCache: " + req.getParameter("fileName").toString());
			} else if (GoogleCloudStorageHelper.checkFile(file)) {
					res.getOutputStream().print(
							"<b> File Found in GCS : " + req.getParameter("fileName").toString() + " ! </b>");
					log.info("File Found in GCS: " + req.getParameter("fileName").toString());
			} else {
					res.getOutputStream().print(
							"<b> File Not Found : " + req.getParameter("fileName").toString() + "</b>");
					log.info("File Not Found in MemCache nor GCS: " + req.getParameter("fileName").toString());
			}						
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}