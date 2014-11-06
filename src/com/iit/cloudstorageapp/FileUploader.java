package com.iit.cloudstorageapp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
public class FileUploader extends HttpServlet {
	private final Logger log = Logger.getLogger(FileUploader.class.getName());

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			res.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.warning("Got a form field: " + item.getFieldName());
				} else {
					log.warning("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());

					int len;
					ByteArrayOutputStream output = new ByteArrayOutputStream();
					byte[] buffer = new byte[8192];
					while ((len = (stream.read(buffer, 0, buffer.length))) != -1) {
						output.write(buffer, 0, len);
					}
					
					// Upload to GCS using GcsService
					GoogleCloudStorageHelper.insertFile(item.getName(),
							item.getContentType(), output.toByteArray(),
							true);
				}
			}
			res.sendRedirect("/");
		} catch (Exception ex) {
			throw new ServletException(ex);
		}
	}
}
