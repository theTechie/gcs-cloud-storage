package com.iit.cloudstorageapp;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class BlobUploader extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		try {
			BlobstoreService blobstoreService = BlobstoreServiceFactory
					.getBlobstoreService();

			Map<String, List<BlobInfo>> uploads = blobstoreService
					.getBlobInfos(req);
			List<BlobInfo> blobInfos = uploads.get("files");

			for (BlobInfo blob : blobInfos) {
				byte[] buffer = new byte[(int) blob.getSize()];
				BlobstoreInputStream inputStream = new BlobstoreInputStream(
						blob.getBlobKey());

				inputStream.read(buffer);
				GoogleCloudStorageHelper.insertFile(blob.getFilename(),
						blob.getContentType(), buffer, true);
				inputStream.close();
			}
			
			log.info("Successfully Uploaded all files !");
			res.sendRedirect("/");
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}
}
