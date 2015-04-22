<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css">

<!-- 可选的Bootstrap主题文件（一般不用引入） -->
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://cdn.bootcss.com/jquery/1.11.2/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://cdn.bootcss.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
</head>
<body>

Welcome Maven project！<h3><sec:authentication property="name"/></h3>
<!-- 
ifAllGranted——是一个由逗号分隔的权限列表，用户必须拥有所有列出的权限时显示； 

ifAnyGranted——是一个由逗号分隔的权限列表，用户必须至少拥有其中的一个权限时才能显示； 

ifNotGranted——是一个由逗号分隔的权限列表，用户未拥有所有列出的权限时才能显示。
 -->
<sec:authorize ifNotGranted="ROLE_MANAGER">
<li><a href="#">学生管理</a></li>
</sec:authorize>
<sec:authorize ifAnyGranted="ROLE_USER">
<li><a href="#">系统管理</a></li>
</sec:authorize>
<sec:authorize ifAllGranted="ROLE_USER,ROLE_ADMIN,ROLE_MANAGER">
<li><a href="#">密码修改</a></li>
</sec:authorize>
<a href="${pageContext.request.contextPath}/security_logout"><button class="btn btn-default">退出</button></a>
</body>
</html>