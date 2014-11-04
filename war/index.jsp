<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>


<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>Distributed Storage App</title>
</head>

<body>
	<h1>CS553 Distributed Storage App running on Google App Engine!</h1>

	<form action="<%= blobstoreService.createUploadUrl("/cloud_storage_file") %>" method="post" enctype="multipart/form-data"> <input type="text"
			name="foo"> <input type="file" name="myFile"> <input
			type="submit" value="Submit">
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
