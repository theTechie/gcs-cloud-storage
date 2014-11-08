package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class BlobUploader extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());
	private final DatastoreService datastoreService =
			DatastoreServiceFactory.getDatastoreService();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();

			Map<String, List<FileInfo>> uploads = blobstoreService
					.getFileInfos(req);
			List<FileInfo> blobInfos = uploads.get("files");
			List<Entity> entities = new ArrayList<Entity>(blobInfos.size());
			 
			for (FileInfo blob : blobInfos) {
				String[] url = blob.getGsObjectName().split("/");
				
				if (blob.getSize() <= MemCacheHelper.CACHE_FILE_MAX_SIZE) {
					// Upload to MemCache for faster retrieval
					MemCacheHelper.put(url[url.length-1], GoogleCloudStorageHelper.findFile(url[url.length-1]));
				}
				
				// store file info in entity
				Entity entity = new Entity("FileMapper", blob.getFilename());
				entity.setProperty("name", url[url.length-1]);
				entities.add(entity);
				
				/*GoogleCloudStorageHelper.insertFile(blob.getFilename(),
						blob.getContentType(), buffer, true);*/
			}
			datastoreService.put(entities);
			
			log.info("Successfully Uploaded all files !");
			res.sendRedirect("/");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}
	
	/*@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();

			Map<String, List<BlobInfo>> uploads = blobstoreService
					.getBlobInfos(req);
			List<BlobInfo> blobInfos = uploads.get("files");
			List<Entity> entities = new ArrayList<Entity>(blobInfos.size());
			 
			for (BlobInfo blob : blobInfos) {
				byte[] buffer = new byte[(int) blob.getSize()];
				
				BlobstoreInputStream inputStream = new BlobstoreInputStream(
						blob.getBlobKey());

				inputStream.read(buffer);
				
				if (blob.getSize() <= MemCacheHelper.CACHE_FILE_MAX_SIZE) {
					// Upload to MemCache for faster retrieval
					MemCacheHelper.put(blob.getBlobKey(), buffer);
				}
				
				// store file info in entity
				Entity entity = new Entity("FileMapper", blob.getBlobKey().getKeyString());
				entity.setProperty(blob.getBlobKey().getKeyString(), blob.getFilename());
				entities.add(entity);
				
				inputStream.close();
				
				GoogleCloudStorageHelper.insertFile(blob.getFilename(),
						blob.getContentType(), buffer, true);
			}
			datastoreService.put(entities);
			
			log.info("Successfully Uploaded all files !");
			res.sendRedirect("/");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}*/
}