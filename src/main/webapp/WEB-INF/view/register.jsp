<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Register</title>
	</head>
	<body>		 
		<h1>Hi User!</h1>
		<p>You should register first.</p>
		<form action="register" method="post">
			<input type="text" name="username" placeholder="Username"><br>
			<input type="password" name="password" placeholder="Password"><br>
			<button type="submit" name="submit">Login</button><br>
		</form>
		<p style="color:red;">${uniqueConstraint}</p>
		<p style="color:red;">${incorrectUsernamePassword}</p>
	</body>
</html>