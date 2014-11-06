package com.iit.cloudstorageapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.tools.cloudstorage.GcsFileMetadata;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.ListItem;
import com.google.appengine.tools.cloudstorage.ListOptions;
import com.google.appengine.tools.cloudstorage.ListResult;

public class GoogleCloudStorageHelper {
	private final static Logger log = Logger.getLogger(FileUploader.class
			.getName());
	private static GcsService gcsService = GcsServiceFactory.createGcsService();
	private final static String bucketName = "cs553-data-bucket";

	// Insert new one or many files on GCS
	public static void insertFile(String fileName, String contentType,
			byte[] bytes, boolean stream) {
		GcsFilename gcsFileName = new GcsFilename(bucketName, fileName);
		ByteBuffer src = ByteBuffer.wrap(bytes);
		GcsFileOptions options = new GcsFileOptions.Builder()
				.mimeType(contentType) // .addUserMetadata("cs553", "cs553")
				.contentEncoding("UTF-8").build();

		if (stream) {
			try (GcsOutputChannel outputChannel = gcsService.createOrReplace(
					gcsFileName, options)) {
				outputChannel.write(src);
			} catch (IOException ex) {
				log.warning(ex.getMessage());
			}
		} else {
			try {
				gcsService.createOrReplace(gcsFileName, options, src);
			} catch (IOException ex) {
				log.warning(ex.getMessage());
			}
		}

		log.warning("Successfully uploaded : " + gcsFileName.getObjectName());
	}

	// Check if there exists a file named 'fileName'
	public static Boolean checkFile(String fileName) {
		Boolean found = false;

		try {
			ListResult listResult = gcsService.list(bucketName,
					ListOptions.DEFAULT);

			while (listResult.hasNext()) {
				ListItem item = listResult.next();
				String file = item.getName();

				if (file.equals(fileName)) {
					found = true;
				}
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
		return found;
	}

	// Delete a file named 'fileName'
	public static Boolean removeFile(String fileName) {
		Boolean isDeleted = false;

		try {
			GcsFilename gcsFileName = new GcsFilename(bucketName, fileName);
			isDeleted = gcsService.delete(gcsFileName);

		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
		return isDeleted;
	}

	// List all files in bucket
	public static ArrayList<String> listFiles() {
		ArrayList<String> fileList = new ArrayList<String>();

		try {
			ListResult listResult = gcsService.list(bucketName,
					ListOptions.DEFAULT);

			while (listResult.hasNext()) {
				ListItem item = listResult.next();
				String fileName = item.getName();
				fileList.add(fileName);

				log.info("File: " + fileName);
			}
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
		return fileList;
	}
	
	// Find a file
	public static void findFile(String fileName, HttpServletResponse res){
		byte[] buffer = new byte[8192];
		ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
		
		try {
			GcsFilename gcsFileName = new GcsFilename(bucketName, fileName);
			GcsFileMetadata gcsFileMetadata = gcsService.getMetadata(gcsFileName);
			GcsInputChannel inputChannel = gcsService.openReadChannel(gcsFileName, 0);
			GcsFileOptions fileOptions = gcsFileMetadata.getOptions();
			
			res.setContentType(fileOptions.getMimeType());
			res.setCharacterEncoding(fileOptions.getContentEncoding());
			res.setHeader("Content-disposition", "attachment; filename=" + fileName);
			
			int len;
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			while ((len = inputChannel.read(byteBuffer)) != -1) {
				output.write(byteBuffer.array(), 0, len);
				byteBuffer.clear();
			}
			
			res.getOutputStream().write(output.toByteArray());
			
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}
	
	/*public static void findFile_Blob(String fileName, HttpServletResponse resp) {
		try {
			GcsFilename gcsFileName = new GcsFilename(bucketName, fileName);
			GcsFileMetadata fileMetadata = gcsService.getMetadata(gcsFileName);
			FileInfo info = 
			BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
			BlobKey blobKey = blobstoreService.createGsBlobKey(
			    "/gs/" + bucketName + "/" + fileName);
			blobstoreService.serve(blobKey, resp);
		} catch (Exception ex) {
			log.warning(ex.getMessage());
		}
	}*/
}
