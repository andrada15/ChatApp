<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link href="${pageContext.request.contextPath}/style.css" rel="stylesheet">
</head>
<body>
<div class="login-container">
    <h1 class="login-title">Welcome to the Chat App</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error-message">
                ${errorMessage}
        </div>
    </c:if>

    <form method="post" action="/login">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="btn-group">
            <button type="submit" class="btn btn-primary">Login</button>
            <a href="/register" class="btn btn-secondary">Register</a>
        </div>
    </form>
</div>
</body>
</html>