<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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