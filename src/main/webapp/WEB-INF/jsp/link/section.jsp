<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery.treeview.css">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.cookie.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.treeview.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.treeview.async.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.treeview.edit.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.pjax.js"></script>
<script type="text/javascript">
function config_pid(pid) {
	$("#pnode").val(pid);
	console.log("config_pid");
}
function switch_selected(el) {
	console.log(el);
	$("#submenu").children().removeClass();
	$(el).addClass("active");
}

$(function() {
	$(document).pjax('.pjax', '#pjaxcontainer');
	$('[data-tip="tooltip"]').tooltip({container: 'body', animation : false});
	$("#tree").treeview({
		url	: '${pageContext.request.contextPath}/section_query',
		collapsed: true,
		animated: "medium",
		control:"#sidetreecontrol",
		persist: "location"
	});
	$(document).on('pjax:complete', function() {
		$("#tree").treeview({
			url	: '${pageContext.request.contextPath}/section_query',
			collapsed: true,
			animated: "medium",
			control:"#sidetreecontrol",
			persist: "location"
		});
	});
	$("#btn_addsection").on('click', function(){
		   $.ajax({
			   url		: "${pageContext.request.contextPath}/section_add",
			   data 	: {node : $("#nodename").val(),pnode : $("#pnode").val()},
			   type		: 'POST',
			   async	: false,
			   dataType	: "json", 
			   success	: function(result){
				   console.log(result);
			   }
		   });		
	});
});
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
              <a id="dLabel" href="#" class="dropdown-toggle" aria-haspopup="true" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
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
<!-- info div -->
<div class="alert alert-info alert-dismissible" role="alert">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  <strong>提示信息：</strong> ${message}
</div><!-- info div end -->
<!-- right --> 
<div class="col-xs-12" id="privateProfile">
	<ul class="nav nav-tabs" id="submenu"><!-- 两端对齐 nav-justified -->
		<li role="presentation" class="active" onclick="switch_selected(this)"><a href="${pageContext.request.contextPath}/directory_manager?tab=section" class="pjax">课程章节</a></li>
		<li role="presentation" class="" onclick="switch_selected(this)"><a href="${pageContext.request.contextPath}/directory_manager?tab=category" class="pjax">题目类型</a></li>
		<li role="presentation" class="" onclick="switch_selected(this)"><a href="${pageContext.request.contextPath}/directory_manager?tab=subjectinfo" class="pjax">课程信息</a></li>
	</ul>
<div class="panel panel-default" style="margin-top: 20px;" id="pjaxcontainer">
  <div class="panel-heading">
    <h3 class="panel-title">课程章节</h3>
  </div>
  <div class="panel-body">
  	left
	<div class="col-xs-7">
		<form class="form-horizontal" action="">
	  	<div>
	  		第一步：<label>选择您的课程名称</label>
	  	</div>
	  	<select class="form-control" id="subjectname">
	  		<option>1</option>
	  		<option>2</option>
	  		<option>3</option>
	  	</select>
	    <div>
	    	第二步：<label>创建课程章节</label>
	    </div>
	    <input type="text" id="nodename" class="form-control">
	    <button id="btn_addsection" type="button" class="btn btn-default">确定</button>
	    </form>	
	</div>
	right tree
	<div class="col-xs-5">
		<div class="test-tree-container">
		<div id="sidetreecontrol" style="float: right;"><a href="?#">收起</a> | <a href="?#">展开</a></div>
		<input type="hidden" name="pnode" id="pnode" value="0">
		<ul id="tree">
		</ul>		
		</div>
	</div>
  </div>		
</div>
</div><!-- right end -->

</div><!-- container end -->   
</body>
</html>
