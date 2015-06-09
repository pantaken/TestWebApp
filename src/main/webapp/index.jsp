<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>高等教育题库网站</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/patchbootstrap.css">
<style type="text/css">
</style>
<!-- 
<link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap-theme.min.css">
 -->
<link href="${pageContext.request.contextPath}/css/patchbootstrap-theme.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/non-responsive.css" rel="stylesheet">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/octicons/octicons.css">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.pjax.js"></script>
<script type="text/javascript">
$(function() {
	$(document).pjax('.pjax', '#pjaxcontainer');
	$('[data-tip="tooltip"]').tooltip({container: 'body', animation : false});
});
function switch_selected(el) {
	$("#tabmenu").children().removeClass('selected');
	$(el).addClass("selected");
}
</script>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
      	<!-- logo -->
        <div class="navbar-header" style="width: 58px; height: 50px;">
          <div class="navbar-brand" style="font-size: 28px;padding: 10px 15px;"><a href="${pageContext.request.contextPath}/"><span class="glyphicon glyphicon-education"></span></a></div>
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
              <a href="#" class="dropdown-toggle" aria-haspopup="true" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
              <ul id="moremenu" class="dropdown-menu" role="menu"  aria-labelledby="dLabel">
                <li><a href="${pageContext.request.contextPath}/mixedequation">混合公式</a></li>
                <li><a href="${pageContext.request.contextPath}/standardequation"">标准公式</a></li>
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
            	<a href="#" data-toggle="dropdown" aria-haspopup="true" data-tip="tooltip" data-placement="bottom" title="创建..." ><span class="glyphicon glyphicon-plus"></span><span class="caret"></span></a>
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
		<div class="list-group" id="tabmenu">
		  <a href="${pageContext.request.contextPath}/index?tab=profile" class="list-group-item selected pjax" onclick="switch_selected(this)">个人简介</a>
		  <a href="${pageContext.request.contextPath}/index?tab=createTestLibrary" class="list-group-item pjax" onclick="switch_selected(this)">创建题库</a>
		  <a href="${pageContext.request.contextPath}/index?tab=createTemplate" class="list-group-item pjax" onclick="switch_selected(this)">创建模板</a>
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">创建试卷</a>
		  <a href="${pageContext.request.contextPath}/directory_manager" class="list-group-item" onclick="switch_selected(this)">目录管理</a>
		  <a href="${pageContext.request.contextPath}/index?tab=testfiling" class="list-group-item pjax" onclick="switch_selected(this)">试题归档</a>
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">答案管理</a>
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">常用工具</a>
		<!-- 
		ifAllGranted——是一个由逗号分隔的权限列表，用户必须拥有所有列出的权限时显示； 
		
		ifAnyGranted——是一个由逗号分隔的权限列表，用户必须至少拥有其中的一个权限时才能显示； 
		
		ifNotGranted——是一个由逗号分隔的权限列表，用户未拥有所有列出的权限时才能显示。
		 -->		  
		  <sec:authorize ifNotGranted="ROLE_MANAGER">
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">无ROLE_MANAGER权限可见</a>
		  </sec:authorize>
		  <sec:authorize ifAnyGranted="ROLE_USER">
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">有ROLE_USER权限可见</a>
		  </sec:authorize>
		  <sec:authorize ifAllGranted="ROLE_USER,ROLE_ADMIN,ROLE_MANAGER">
		  <a href="#" class="list-group-item pjax" onclick="switch_selected(this)">有ROLE_USER,ROLE_ADMIN,ROLE_MANAGER权限可见</a>
		  </sec:authorize>		  
		</div>
</div>
</div><!-- left end -->

<div id="pjaxcontainer">
<!-- right --> 
<div class="col-xs-9" id="privateProfile">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">个人简介</h3>
  </div>
  <div class="panel-body">
    <a class="btn btn-default" name="imageuploadbtn" id="imageuploadbtn" data-loading-text="上传中..." autocomplete="off">上传图片</a>
    <form action="" id="fileform">
		<input type="file" id="imageupload" name="imageupload" style="display: none;">
	</form>
  </div>		
</div>
</div><!-- right end -->
</div>

<!-- separator -->
<div class="row"><div class="col-xs-12"><hr/></div></div>
<!-- separator end -->
<!-- foot -->
<div class="row" style="font-size: 12px">
	<div class="col-xs-3">
		<span class="copy">© 2015 java_wxl@msn.cn</span>
	</div>
	<div class="col-xs-9">
		<div style="float: right">
			<ul style="list-style: none;">
			<li style="float: left;padding: 0 5px;"><a href="#" target="_blank">使用帮助</a></li>
			<li style="float: left;padding: 0 5px;"><a href="#" target="_blank">建议反馈</a></li>
			<li style="float: left;padding: 0 5px;"><a href="#" target="_blank">移动应用</a></li>
			<li style="float: left;padding: 0 5px;"><a href="#" target="_blank">加入我吧</a></li>
			<li style="float: left;padding: 0 5px;"><a href="mailto:542787045@qq.com">商务合作</a></li>
			</ul>				
		</div>
	</div>			
</div><!-- foot end -->
</div><!-- container end -->   

<!-- Modal -->
<div class="modal fade" id="picModel" tabindex="-1" role="dialog" aria-labelledby="picLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="picLabel">编辑您的头像</h4>
      </div>
      <div class="modal-body">
        <center>
        <img style="height: 326px;width: 184px;" alt="" src="${pageContext.request.contextPath}/images/IMG_20150417_161121.jpg">
        </center>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-success" data-dismiss="modal">保存图片</button>
      </div>
    </div>
  </div>
</div><!-- Model end -->

</body>
</html>
