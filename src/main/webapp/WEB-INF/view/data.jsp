<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Data</title>
	</head>
	<body>				 
		<h1>Good day!</h1>
		<form action="add-words" method="get">
			<input type="text" name="jobTitle" placeholder="Add a job title..."></input>
			<input type="text" name="location" placeholder="Add a location..."></input><br>
			<button type="submit">Add</button><br>
		</form>
		<form action="generate-document" method="post">
			<button name="generateButton" value="XML" type="submit">XML</button>
			<button name="generateButton" value="PDF" type="submit">PDF</button><br>
		</form>
		<p style="color:green;">${wordAdded}</p>
		<p style="color:red;">${wordAlreadyInList}</p>
		<p style="color:green;">${xmlDownloaded}</p>
		<p style="color:green;">${pdfDownloaded}</p>
		<p style="color:red;">${error}</p>
		<p style="color:red;">${emptyField}</p>		
		<table border="1" style="width:500px">
			<tr>
				<th>Vacancy ID</th>
				<th>Job title</th>
			   	<th>Company</th>
			   	<th>Location</th>
			</tr> 
			<c:forEach items="${vacancies}" var="vacancy">
			<tr>
				<td><c:out value="${vacancy.vacancyID}"/></td>
			   	<td><c:out value="${vacancy.jobTitle}"/></td>
			   	<td><c:out value="${vacancy.company}"/></td>
			   	<td><c:out value="${vacancy.location}"/></td>
			</tr>
			</c:forEach>
		</table>
	</body>
</html>