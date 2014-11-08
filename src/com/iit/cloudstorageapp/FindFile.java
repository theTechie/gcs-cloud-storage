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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@SuppressWarnings("serial")
public class FindFile extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());
	private DatastoreService datastoreService = DatastoreServiceFactory
			.getDatastoreService();

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {

			String file = req.getParameter("fileName");

			/*
			 * BlobstoreService blobstoreService = BlobstoreServiceFactory
			 * .getBlobstoreService(); file =
			 * blobstoreService.createGsBlobKey("/gs/" +
			 * GoogleCloudStorageHelper.bucketName + "/" + file).getKeyString();
			 */

			res.setCharacterEncoding("UTF-8");

			Query query = new Query("FileMapper");
			Key fileKey = KeyFactory.createKey("FileMapper", file);
			FilterPredicate filter = new FilterPredicate(
					Entity.KEY_RESERVED_PROPERTY, FilterOperator.EQUAL, fileKey);
			query.setFilter(filter);

			List<Entity> entities = datastoreService.prepare(query).asList(
					FetchOptions.Builder.withDefaults());

			for (Entity entity : entities) {
				file = entity.getProperty("name").toString();
				log.info("File: " + entity.getProperty("name").toString());
			}

			if (MemCacheHelper.containsKey(file)) {
				res.setHeader("Content-disposition", "attachment; filename="
						+ req.getParameter("fileName"));
				res.setCharacterEncoding("UTF-8");
				res.getOutputStream().write((byte[]) MemCacheHelper.get(file));
			} else if (GoogleCloudStorageHelper.checkFile(file)) {
				byte[] output = GoogleCloudStorageHelper.findFile(file, res);

				// store in MemCache if file size <= 100 KB
				if (output.length <= MemCacheHelper.CACHE_FILE_MAX_SIZE) {
					MemCacheHelper.put(file, output);
				}

				res.getOutputStream().write(output);
			} else {
				log.warning("File Not Found : " + req.getParameter("fileName").toString());
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
			throw new ServletException(ex);
		}
	}
}