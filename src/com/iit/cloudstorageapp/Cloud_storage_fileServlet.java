package com.iit.cloudstorageapp;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.appengine.api.blobstore.*;
import com.google.appengine.tools.cloudstorage.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Cloud_storage_fileServlet extends HttpServlet {

	private GcsService gcsService = GcsServiceFactory.createGcsService();
	private GcsFileOptions options = new GcsFileOptions.Builder()
			.mimeType("text/plain").addUserMetadata("cs553", "cs553").build();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		// GcsFilename fileName = getFileName(req);

		// Depending on the request-param, retrieve the file

		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobstoreService.serve(blobKey, res);

		// testWrite10mb();
	}

	public void testWrite10mb() throws IOException {
		int length = 1 * 1024 * 1024 + 1;
		GcsFilename filename = new GcsFilename("cs553-data-bucket",
				"testWrite1mbFile");
		createFile(filename, length, true);
		log(getServletInfo());
	}

	private byte[] createFile(GcsFilename filename, int length, boolean stream)
			throws IOException {
		byte[] content = new byte[length];
		Random r = new Random();
		r.nextBytes(content);
		createFile(filename, content, stream);
		return content;
	}

	private void createFile(GcsFilename filename, String content, boolean stream)
			throws IOException {
		createFile(filename, content.getBytes(UTF_8), stream);
	}

	private void createFile(GcsFilename filename, byte[] bytes, boolean stream)
			throws IOException {
		ByteBuffer src = ByteBuffer.wrap(bytes);
		if (stream) {
			try (GcsOutputChannel outputChannel = gcsService.createOrReplace(
					filename, options)) {
				outputChannel.write(src);
			}
		} else {
			gcsService.createOrReplace(filename, options, src);
		}
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		/*
		 * log(getServletInfo());
		 * 
		 * createFile(getFileName(req), req.getInputStream().toString(), true);
		 */

		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKey = blobs.get("myFile");

		if (blobKey == null) {
			res.sendRedirect("/");
		} else {
			res.sendRedirect("/cloud_storage_file?blob-key="
					+ blobKey.get(0).getKeyString());
		}

		for (BlobKey bk : blobKey) {
			GcsFilename filename = new GcsFilename("cs553-data-bucket",
					bk.getKeyString());
			createFile(filename, bk.toString(), false);
		}
		log("Successfully uploaded !");

		/*
		 * GcsOutputChannel outputChannel = gcsService.createOrReplace(
		 * getFileName(req), GcsFileOptions.getDefaultInstance());
		 * copy(req.getInputStream(), Channels.newOutputStream(outputChannel));
		 */
	}

	private GcsFilename getFileName(HttpServletRequest req) {
		String[] splits = req.getRequestURI().split("/", 4);
		if (!splits[0].equals("") || !splits[1].equals("gcs")) {
			throw new IllegalArgumentException(
					"The URL is not formed as expected. "
							+ "Expecting /gcs/<bucket>/<object>");
		}
		return new GcsFilename(splits[2], splits[3]);
	}

	/*
	 * private void copy(InputStream input, OutputStream output) throws
	 * IOException { try { byte[] buffer = new byte[BUFFER_SIZE]; int bytesRead
	 * = input.read(buffer); while (bytesRead != -1) { output.write(buffer, 0,
	 * bytesRead); bytesRead = input.read(buffer); } } finally { input.close();
	 * output.close(); } }
	 */
}
