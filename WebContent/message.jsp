<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>新規投稿</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="main-contents">
			<div class="header">
				<a href="./">ホーム</a>
			</div><br />

			<c:if test="${not empty errorMessages}">
				<div class="errorMessages">
					<ul>
						<c:forEach items="${errorMessages}" var="errorMessage">
							<li><c:out value="${errorMessage}" />
						</c:forEach>
					</ul>
				</div><br>
			</c:if>

			<div>
				<form action="message" method="post">
					件名　<input type="text" name="title" value="${message.title}" /><br>
					本文　<textarea name="text" cols="70" rows="5" class="message-box"><c:out value="${message.text}"></c:out></textarea><br />
					カテゴリー　<input type="text" name="category" value="${message.category}" /><br />
					<input type="submit" value="投稿">
				</form>
			</div><br>
			<div class="copyright"> Copyright(c)Your Name</div>
		</div>
	</body>
</html>