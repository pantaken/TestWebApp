<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
<script type="text/javascript">
$(function() {
	$('[data-tip="tooltip"]').tooltip({container: 'body', animation : false});
	$("#fileuploadbtn").on('click',function(){
		$("[name = 'fileupload']").trigger('click');		
	});	
	$("#fileupload").change(function(){
		var $btn = $("#fileuploadbtn").button('loading');
		var fileObject = {};
		var actionURL = "${pageContext.request.contextPath}/beautyParser";
		try {
			fileObject = document.getElementById("fileupload").files[0];	
		} catch (e) {
			console.log(e);
			return;
		}
		//FormData对象
		var form = new FormData();
		form.append("author","github");
		form.append("file", fileObject);
		
		//XMLHttpRequest对象
		var xhr = new XMLHttpRequest();
		xhr.open('post', actionURL, true);
		xhr.onload = function(event) {
			$btn.button('reset');
			if (xhr.status == 200) {
				var jsonMsg = eval("(" + xhr.responseText + ")");
				if (jsonMsg.success) {
					//$('#picModel').modal('toggle');
				}
			}
		};
		xhr.send(form);
	});
	
	$("#createTestLibraryBtn").on('click',function(){
		$("#createTestLibrary").addClass('selected');
		$("#privateProfile").removeClass('selected');
		$("#privateProfile").hide();
		$("#createTestLibrary").show();
	});
	
	$("#privateProfileBtn").on('click', function(){
		$("#privateProfile").addClass('selected');
		$("#createTestLibrary").removeClass('selected');
		$("#privateProfile").show();
		$("#createTestLibrary").hide();
	});
});
</script>
</head>
<body>
<nav class="navbar navbar-default navbar-fixed-top">
      <div class="container">
      	<!-- logo -->
        <div class="navbar-header" style="width: 58px; height: 50px;">
          <div class="navbar-brand" style="font-size: 28px;padding: 10px 15px;"><span class="glyphicon glyphicon-education" aria-hidden="true"></span></div>
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
            <li><a href="${pageContext.request.contextPath}/assembletestpaper">组题</a></li>
            <li><a href="#about">训练</a></li>
            <li><a href="#contact">博客</a></li>
            <li class="dropdown">
              <a id="dLabel" href="#" class="dropdown-toggle" aria-haspopup="true" data-toggle="dropdown" role="button" aria-expanded="false">更多<span class="caret"></span></a>
              <ul id="moremenu" class="dropdown-menu" role="menu"  aria-labelledby="dLabel">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="#">Separated link</a></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
          </ul>

          <ul class="nav navbar-nav navbar-right">
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="退出"><span class="glyphicon glyphicon-share-alt"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="标签"><span class="glyphicon glyphicon-pushpin"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="设置"><span class="glyphicon glyphicon-cog"></span></a></li>
            <li><a href="#" data-tip="tooltip" data-placement="bottom" title="我的收藏"><span class="glyphicon glyphicon-star"></span></a></li>
            <li>
            	<a id="drop123" href="#" data-toggle="dropdown" aria-haspopup="true" data-tip="tooltip" data-placement="bottom" title="创建..." ><span class="glyphicon glyphicon-plus"></span><span class="caret"></span></a>
           		<ul id="createmenu" class="dropdown-menu" role="menu" aria-labelledby="drop123">
                	<li role="presentation"><a href="#">Action</a></li>
                	<li role="presentation"><a href="#">Another action</a></li>
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
    <h3 class="panel-title">个人设置</h3>
  </div>
		<div class="list-group">
		  <a href="#" class="list-group-item selected" id="privateProfileBtn">个人简介</a>
		  <a href="#" class="list-group-item" id="createTestLibraryBtn">创建题库</a>
		  <a href="#" class="list-group-item">Porta ac consectetur ac</a>
		  <a href="#" class="list-group-item">Vestibulum at eros</a>
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
    <h3 class="panel-title">个人简介</h3>
  </div>
  <div class="panel-body">
    <button class="btn btn-default" name="imageuploadbtn" id="imageuploadbtn" data-loading-text="上传中..." autocomplete="off">上传图片</button>
	<input type="file" id="imageupload" name="imageupload" style="display: none;">
  </div>		
</div>
</div><!-- right end -->

<!-- right -->
<div class="col-xs-9" id="createTestLibrary" style="display: none;">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">创建题库</h3>
  </div>
  <div class="panel-body">
    <button class="btn btn-default" name="fileuploadbtn" id="fileuploadbtn" data-loading-text="上传中..." autocomplete="off">上传文件</button>
	<input type="file" id="fileupload" name="fileupload" style="display: none;">
  </div>		
</div>
</div><!-- right end -->

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
