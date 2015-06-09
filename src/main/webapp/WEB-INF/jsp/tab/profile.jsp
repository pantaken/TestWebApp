<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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