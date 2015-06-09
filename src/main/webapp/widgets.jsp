<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Base on Bootstrap custom widgets</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
	<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/octicons/octicons.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/wxl.css">
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/wxl.js"></script>
</head>
<body>

<div class="container">
	<div class="col-xs-12" style="margin-top: 200px;">
		<a class="btn btn-default btn-sm wxl-menu-btn" onclick="javascript:pop(event,'popmenu');"><span class="octicon octicon-eye"></span> 查看代码 </a>
		<a class="wxl-count" href="#">0</a>
		<div class="list-group wxl-pop" id="popmenu" style="display: none;">
		  <a href="#" class="list-group-item wxl-pop-header">
		    <span>属性选择器</span>
		    <span class="octicon octicon-x wxl-pop-close" role="button" aria-label="popmenu"></span>
		  </a>
		  <a href="#" class="list-group-item">Dapibus ac facilisis in</a>
		  <a href="#" class="list-group-item">Morbi leo risus</a>
		  <a href="#" class="list-group-item">Porta ac consectetur ac</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		</div>			
	
	
		<a class="btn btn-default btn-sm wxl-btn"><span class="octicon octicon-star"></span> 收藏代码 </a>
		<a class="wxl-count" href="#">12,123</a>		
	</div>	
<div class="zh-backtotop" style="opacity: 1;"><a data-action="backtotop" data-tip="s$r$回到顶部" href="javascript:;" class="btn-backtotop btn-action"><div class="arrow"></div><div class="stick"></div></a></div>	
</div>
</body>
</html>