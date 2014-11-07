<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.UploadOptions" %>

<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>
<% UploadOptions uploadOptions = UploadOptions.Builder.withGoogleStorageBucketName("cs553-data-bucket");  %>

<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Distributed Storage App</title>
</head>

<body>
	<h1>CS553 Distributed Storage App running on Google App Engine!</h1>

	<h2> Upload files </h2>
	<form action="/fileUpload" method="post" enctype="multipart/form-data"> 
		<input type="file" name="files" multiple> 
		<input type="submit" value="Upload Files">
	</form>
	
	<!-- <form action="<%= blobstoreService.createUploadUrl("/blobUpload", uploadOptions) %>" method="post" enctype="multipart/form-data"> 
		<input type="file" name="files" multiple> 
		<input type="submit" value="Upload Files">
	</form> -->
	
	<br/>
	
	<h2> List all files </h2>
	<form action="/fileList" method="get">
		<input type="submit" value="List Files">
	</form>
	
	<br />
	
	<h2> MemCache </h2>
	<form action="/memCacheStats" method="get">
		<input type="submit" value="View Stats">		
	</form>
	<form action="/memCacheRemoveAll" method="get">
		<input type="submit" value="Remove All MemCache">
	</form>
	
	<br />
	
	<h2> Check File </h2>
	<form action="/checkFile" method="get">
		<input type="text" name="fileName">
		<input type="submit" value="Check File">
	</form>
	
	<br />
	
	<h2> Find File </h2>
	<form action="/findFile" method="get">
		<input type="text" name="fileName">
		<input type="submit" value="Find File">
	</form>
	
	<br />
	
	<h2> Delete File </h2>
	<form action="/removeFile" method="get">
		<input type="text" name="fileName">
		<input type="submit" value="Remove File">
	</form>
	
	<form action="/removeAll" method="get">
		<input type="submit" value="Remove All Files">
	</form>
	
</body>

</html>
