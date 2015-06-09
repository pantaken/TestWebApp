<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/patchbootstrap.css">
<!-- 
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
 -->
<link href="${pageContext.request.contextPath}/css/patchbootstrap-theme.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/non-responsive.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/octicons/octicons.css">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(function() {
	$('[data-tip="tooltip"]').tooltip({container: 'body', animation : false});
	/*
	$("#filedownloadbtn").on('click',function() {
		$.ajax({
		  url:"${pageContext.request.contextPath}/downloadassembletest",
		  type:'POST',
		  async:false,
		  dataType: "json", 
		  success:function(result){
		  }
		});		
	});
	*/
	var selected_ids = $.cookie('TEST_COLLECTIONS');
	$("#selected_ids").html(selected_ids);
});
</script>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
      	<!-- logo -->
        <div class="navbar-header" style="width: 58px; height: 50px;">
          <div class="navbar-brand" style="font-size: 28px;padding: 10px 15px;"><a href="${pageContext.request.contextPath}/"><span class="glyphicon glyphicon-education" aria-hidden="true"></span></a></div>
        </div>
        <!-- search -->
		<form class="navbar-form navbar-left" role="search" style="padding-top: 3px;">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search" style="width: 360px; height: 28px;">
			</div>
		</form>
		<!-- menu -->
        <div id="navbar">
          <ul class="nav navbar-nav">
            <li class="dropdown">
            	<a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" data-placement="bottom" >组题<span class="caret"></span></a>
           		<ul id="assemblemenu" class="dropdown-menu" role="menu" aria-labelledby="drop123">
                	<li role="presentation"><a href="${pageContext.request.contextPath}/assembletestpaperbystep"> 手动组题</a></li>
                	<li role="presentation"><a href="${pageContext.request.contextPath}/assembletestpaperbyauto"> 系统组题</a></li>
				</ul>
			</li>
            <li><a href="#about">训练</a></li>
            <li><a href="#contact">博客</a></li>
            <li class="dropdown">
              <a id="dLabel" href="#" class="dropdown-toggle" aria-haspopup="true" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
              <ul id="moremenu" class="dropdown-menu" role="menu"  aria-labelledby="dLabel">
                <li><a href="${pageContext.request.contextPath}/mixedequation">混合公式</a></li>
                <li><a href="#">标准公式</a></li>
                <li class="divider"></li>
                <li><a href="#">共享资源</a></li>
                <li><a href="#">论坛</a></li>
              </ul>
            </li>
          </ul>

          <ul class="nav navbar-nav navbar-right">
            <li><a href="${pageContext.request.contextPath}/security_logout" data-tip="tooltip" data-placement="bottom" title="退出"><span class="glyphicon glyphicon-share-alt"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="标签"><span class="glyphicon glyphicon-pushpin"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="设置"><span class="glyphicon glyphicon-cog"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="我的收藏"><span class="glyphicon glyphicon-star"></span></a></li>
            <li>
            	<a id="drop123" href="#" data-toggle="dropdown" aria-haspopup="true" data-tip="tooltip" data-placement="bottom" title="创建..." ><span class="glyphicon glyphicon-plus"></span><span class="caret"></span></a>
           		<ul id="createmenu" class="dropdown-menu" role="menu" aria-labelledby="drop123">
                	<li role="presentation"><a href="#"><span style="opacity:0.5;" class="octicon octicon-file-text"></span>      试卷</a></li>
                	<li role="presentation"><a href="#"><span style="opacity:0.5;" class="octicon octicon-book"></span>      模板</a></li>
				</ul>
            </li>
          </ul>
        </div>
      </div>
</nav>
    
<!-- container -->    
<div class="container">
<!-- left -->
<div class="col-xs-3">
<div class="panel panel-default">
  <div class="panel-heading">
    <h6 class="panel-title"><sec:authentication property="principal.username"/></h6>
  </div>
		<div class="list-group">
		  <a href="#" class="list-group-item" id="privateProfileBtn">个人简介</a>
		  <a href="#" class="list-group-item" id="createTestLibraryBtn">创建题库</a>
		  <a href="#" class="list-group-item">创建模板</a>
		  <a href="#" class="list-group-item selected">创建试卷</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
		</div>
</div>
</div><!-- left end -->
<!-- right --> 
<div class="col-xs-9" id="privateProfile">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">收藏列表</h3>
  </div>
  <div class="panel-body">
  	<li id="selected_ids"></li>
	<!-- action="downloadassembletest/downloadmixassembletest" -->
  	<form action="${pageContext.request.contextPath}/downloadassembletest" method="get">
		<button type="submit" class="btn btn-default" id="filedownloadbtn" data-loading-text="下载中..." autocomplete="off">下载试卷</button>
	</form>
  </div>		
</div>
</div><!-- right end -->

</div><!-- container end -->   
</body>
</html>
