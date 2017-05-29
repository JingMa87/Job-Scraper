<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html> 
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Data</title>
	</head>
	<body>				 
		<h1>Web Scraper</h1>
		
		<!-- Form for web scraping job vacancies. -->
		<form action="web-scrape" method="get">
			<!-- A drop down menu where you can pick a job search engine. -->
			<p>Pick a job search engine to scrape:</p>
			<select name="searchEngine">
				<option value="indeed">Indeed</option>
				<option value="nationaleVacaturebank">Nationale Vacaturebank</option>
				<option value="monsterboard">Monsterboard</option>
			</select>
			<br>
			<!-- Input fields for filling in a job title and location + a submit button. -->
			<p>Fill in a job title and location:</p>
			<input type="text" name="jobTitle" placeholder="Add a job title..." value="${jobTitle}"></input>
			<input type="text" name="location" placeholder="Add a location..." value="${location}"></input>
			<button type="submit">Scrape</button><br>
		</form>	
		<!-- Buttons to generate XML and PDF documents. -->
		<p>Download the data:</p>
		<form action="generate-document" method="get">
			<button name="generate" value="XML" type="submit">XML</button>
			<button name="generate" value="PDF" type="submit">PDF</button><br>
		</form>
		
		<!-- Status and error messages. -->
		<p style="color:red;">${emptyField}</p>
		<p style="color:green;">${successScrape}</p>
		<p style="color:red;">${databaseError}</p>
		<p style="color:green;">${xmlDownloaded}</p>
		<p style="color:green;">${pdfDownloaded}</p>
		<p style="color:red;">${documentError}</p>
		
		<!-- Table with web scraped data on vacancies. -->
		<c:if test="${scraped}">
		<table border="1" style="width:500px">
			<!-- Loops to generate headers. -->
			<tr>
				<c:forEach items="${headers}" var="item">
				<th><c:out value="${item}"/></th>
				</c:forEach>
			</tr> 
			<!-- Loops to generate table rows. -->
			<c:forEach items="${vacancies}" var="vacancy">
			<tr>
				<td><c:out value="${vacancy.vacancyID}"/></td>
			   	<td><c:out value="${vacancy.jobTitle}"/></td>
			   	<td><c:out value="${vacancy.company}"/></td>
			   	<td><c:out value="${vacancy.location}"/></td>
			</tr>
			</c:forEach>
		</table>
		</c:if>
	</body>
</html>