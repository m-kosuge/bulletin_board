<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>ユーザー管理</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="main-contents">
			<div class="header">
				<a href="./">ホーム</a>
				<a href="signup">ユーザー新規登録</a>
			</div><br>

			<c:if test="${not empty errorMessages}">
				<div class="errorMessages">
					<ul>
						<c:forEach items="${errorMessages}" var="errorMessage">
							<li><c:out value="${errorMessage}" />
						</c:forEach>
					</ul>
				</div><br>
				<c:remove var="errorMessages" scope="session" />
			</c:if>

			<div>
				<c:forEach items="${users}" var="user">
					アカウント名　<c:out value="${user.account}"/>
					<c:if test="${user.id == loginUser.id}">
					　※このアカウントでログインしています
					</c:if><br>
					名前　<c:out value="${user.name}"/><br>
					支社　<c:out value="${user.branchName}"/><br>
					部署　<c:out value="${user.departmentName}"/><br>
					<c:if test="${user.isStopped == 0}">
						ステータス　稼働中
					</c:if>
					<c:if test="${user.isStopped == 1}">
						ステータス　停止中
					</c:if><br>
					<form action="setting" method="get">
						<input type="hidden" name="id" value="${user.id}"/>
						<input class="button" type="submit" value="編集"/>
					</form>
					<c:if test="${user.id != loginUser.id}">
						<c:if test="${user.isStopped == 0}">
							<form action="stop" method="post" onSubmit="return confirmStop('停止');">
								<input type="hidden" name="id" value="${user.id}"/>
								<input type="hidden" name="isStopped" value="1"/>
								<input type="submit" value="停止" />
							</form>
						</c:if>
						<c:if test="${user.isStopped == 1}">
							<form action="stop" method="post" onSubmit="return confirmStop('復活');">
								<input type="hidden" name="id" value="${user.id}"/>
								<input type="hidden" name="isStopped" value="0"/>
								<input type="submit" value="復活" />
							</form>
						</c:if>
					</c:if><br>
				</c:forEach>
			</div><br>
		<div class="copyright"> Copyright(c)Your Name</div>
		</div>

		<script type="text/javascript">
			function confirmStop(msg) {
				return window.confirm('このアカウントを' + msg + 'してもよろしいですか？');
			}
		</script>
	</body>
</html>