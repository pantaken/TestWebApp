<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$("#fileuploadbtn").on('click',function(){
	$("[name = 'fileupload']").trigger('click');		
});
$("#fileupload").change(function(){
	var $btn = $("#fileuploadbtn").button('loading');
	var fileObject = {};
	var actionURL = "${pageContext.request.contextPath}/beautyParser?subjectcode="+$("#SubjectName").val()+"&categorycode="+$("#SubjectCategory").val();
	//var actionURL = "${pageContext.request.contextPath}/mixParser";
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
	this.value = null;
});
$("#SubjectName").change(function(){
	$.ajax({
		 url		: "${pageContext.request.contextPath}/category_query",
		 data 		: {subjectcode : $("#SubjectName").val()},
		 type		: 'POST',
		 dataType	: "json", 
		 success	: function(result){
			var html = '<option value="nil" disabled="disabled" selected="selected" style="color: #3e8f3e;font-weight: bold;">请选择课程名称</option>';
		  	$.each(result, function(index, item){
		  	html +=
		    '<option value="'+item.serialcode+'">'+item.category+'</option>';
		  	});
		  	$("#SubjectCategory").html(html);
		 }
	});	
});
</script>
<!-- right -->
<div class="col-xs-9" id="createTestLibrary">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">创建题库</h3>
  </div>
  <div class="panel-body">
  	<label for="SubjectName">课程名称</label>
  	<select id="SubjectName" name="SubjectName" class="form-control">
  			<option value="nil" disabled="disabled" selected="selected" style="color: #3e8f3e;font-weight: bold;">请选择课程名称</option>
  		<c:forEach items="${subjects}" var="subject">
  			<option value="${subject.serialcode}">${subject.subjectname}</option>
  		</c:forEach>  	
  	</select>
  	<label for="SubjectCategory">题目类型</label>
  	<select name="SubjectCategory" id="SubjectCategory" class="form-control">
  		<option value="nil" disabled="disabled" selected="selected" style="color: #3e8f3e;font-weight: bold;">请选择题目类型</option>
  	</select>
    <a class="btn btn-default" name="fileuploadbtn" id="fileuploadbtn" data-loading-text="上传中..." autocomplete="off">上传文件</a>
	<input type="file" id="fileupload" name="fileupload" style="display: none;">
  </div>		
</div>
</div><!-- right end -->