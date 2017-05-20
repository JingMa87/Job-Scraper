<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Login</title>
	</head>
	<body>		 
		<h1>Hi User!</h1>
		<p>Login here.</p>
		<form action="login" method="post">
			<input type="text" name="username" placeholder="Username" value="${username}"><br>
			<input type="password" name="password" placeholder="Password"><br>
			<button type="submit" name="submit">Login</button><br>
		</form>
	</body>
</html>