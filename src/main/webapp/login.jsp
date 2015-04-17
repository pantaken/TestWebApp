<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" %>
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
<center>
${sessionScope['SPRING_SECURITY_LAST_EXCEPTION'].message} 
<div class="panel panel-default" style="width: 400px;">
  <div class="panel-heading">【Spring Security Study】登录</div>
<div class="panel-body form-horizontal">
	<form action="${pageContext.request.contextPath}/security_login" method="post">
			<div class="form-group" id="namediv">
			<label for="name" class="col-xs-3 control-label">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名</label>
			<div class="col-xs-9"><input id="name" name="name" type="text" class="form-control"></div>
			</div>
			
			<div class="form-group" id="passworddiv">
			<label for="password" class="col-xs-3 control-label">密&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;码</label>
			<div class="col-xs-9"><input id="identifyID" name="password" type="password" class="form-control"></div>
			</div>	
			
			<div class="form-group">
				<input type="checkbox" name="security_rememberme" checked="checked" value="true"> 记住密码(2周)
			</div>
			<div class="form-group">
				<label class="col-xs-1 control-label"></label>
				<div class="col-xs-11" align="center">
				<input id="searchbtn" type="submit" value="登录" class="btn btn-default">
				</div>
			</div>			
	</form>    
  </div>
</div>	
</center>
</body>
</html>