<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>掲示板</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
	</head>
	<body>
		<div class="main-contents">
			<div class="header">
				<a href="./">ホーム</a>
				<a href="message">新規投稿</a>
				<c:if test="${loginUser.branchId ==1 && loginUser.departmentId ==1}">
					<a href="management">ユーザー管理</a>
				</c:if>
				<a href="logout">ログアウト</a>
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
				<form action="./" method="get">
					開始日　<input name="startDate" type="date" value="${startDate}" id="startDate"/>
					　～　
					終了日　<input name="endDate" type="date" value="${endDate}" id="endDate"/> <br>
					カテゴリー　<input name="category" value="${category}" id="category"/><br>
					<input type="submit" value="絞り込み" />
				</form>
			</div><br><br>

			<div>
				<c:forEach items="${messages}" var="message">
					<div class="message">
						投稿者：<c:out value="${message.userName}" /><br>
						件名：<c:out value="${message.title}" /><br>
						<c:forEach var="line" items="${fn:split(message.text, '
')}">
							<c:out value="${line}" /><br>
						</c:forEach>
						カテゴリー：<c:out value="${message.category}" /><br>
						<fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /><br>
						<c:if test="${loginUser.id == message.userId}">
							<form action="deleteMessage" method="post" onSubmit="return confirmDelete('投稿');">
								<input type="hidden" name="id" value="${message.id}"/>
								<input type="submit" value="投稿を削除"/>
							</form>
						</c:if>
					</div><br>

					<c:forEach items="${comments}" var="comment">
						<c:if test="${message.id == comment.messageId}">
							<div class="comment">
								投稿者：<c:out value="${comment.userName}" /><br>
								<c:forEach var="line" items="${fn:split(comment.text, '
')}">
									<c:out value="${line}" /><br>
								</c:forEach>
								<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /><br>
								<c:if test="${loginUser.id == comment.userId}">
									<form action="deleteComment" method="post" onSubmit="return confirmDelete('コメント');">
										<input type="hidden" name="id" value="${comment.id}"/>
										<input type="submit" value="コメントを削除"/>
									</form>
								</c:if>
							</div><br>
						</c:if>
					</c:forEach>

					<div>
						<form action="comment" method="post">
							<input name="messageId" type="hidden" value="${message.id}"/>
							<textarea name="text" cols="70" rows="3" class="comment-box"></textarea><br>
							<input type="submit" value="コメント">
						</form>
					</div><br><br>
				</c:forEach>
			</div>
		<div class="copyright">Copyright(c)YourName</div>
		</div>

		<script type="text/javascript">
			function confirmDelete(msg) {
				return window.confirm(msg + 'を削除してもよろしいですか？');
			}
		</script>
	</body>
</html>