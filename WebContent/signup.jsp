<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>ユーザー新規登録</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="main-contents">
			<div class="header">
				<a href="management">ユーザー管理</a>
			</div><br>

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
				<form action="signup" method="post">
					アカウント　<input name="account" value="${user.account}" />
					（半角英数字6文字以上20文字以下）<br>
					名前　<input name="name" value="${user.name}" />
					（10文字以下）<br>
					パスワード　<input name="password" type="password" />
					（記号を含む全ての半角文字6文字以上20文字以下）<br>
					確認パスワード用　<input name="confirmPassword" type="password" />
					（確認のため、もう一度パスワードを入力してください）<br>
					支社　
					<select name="branchId">
						<c:forEach items="${branches}" var="branch">
							<c:if test="${branch.id == user.branchId}">
								<option value="${branch.id}" selected><c:out value="${branch.name}"></c:out></option>
							</c:if>
							<c:if test="${branch.id != user.branchId}">
								<option value="${branch.id}" ><c:out value="${branch.name}"></c:out></option>
							</c:if>
						</c:forEach>
					</select><br>
					部署　
					<select name="departmentId">
						<c:forEach items="${departments}" var="department">
							<c:if test="${department.id == user.departmentId}">
								<option value="${department.id}" selected><c:out value="${department.name}"></c:out></option>
							</c:if>
							<c:if test="${department.id != user.departmentId}">
								<option value="${department.id}" ><c:out value="${department.name}"></c:out></option>
							</c:if>
						</c:forEach>
					</select><br>
					<input type="submit" value="登録" />
				</form>
			</div><br>
			<div class="copyright">Copyright(c)Your Name</div>
		</div>
	</body>
</html>