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

	<!-- <form action="<%= blobstoreService.createUploadUrl("/cloud_storage_file", uploadOptions) %>" method="post" enctype="multipart/form-data"> 
		<input type="text" name="foo"> 
		<input type="file" name="myFile" multiple> 
		<input type="submit" value="Submit">
	</form> -->

	<h2> Upload files </h2>
	<form action="/fileUpload" method="post" enctype="multipart/form-data"> 
		<input type="file" name="files" multiple> 
		<input type="submit" value="Upload Files">
	</form>
	
	<br/>
	
	<h2> List all files </h2>
	<form action="/fileList" method="get">
		<input type="submit" value="List Files">
	</form>
	
	<br />
	
	<h2> Check File </h2>
	<form action="/checkFile" method="get">
		<input type="text" name="fileName">
		<input type="submit" value="Check File">
	</form>
	
	<br />
	
	<h2> Delete File </h2>
	<form action="/deleteFile" method="get">
		<input type="text" name="fileName">
		<input type="submit" value="Delete File">
	</form>

	<!--    <table>
      <tr>
        <td colspan="2" style="font-weight:bold;">Available Servlets:</td>        
      </tr>
      <tr>
			<td><a href="cloud_storage_app">Cloud_storage_app</a></td>
			<td><a href="cloud_storage_file">Cloud_storage_file</a></td>			
		</tr>
    </table> -->
</body>

</html>
