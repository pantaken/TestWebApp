<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
#template {
	margin-left: 60px;
}
#template td {
	padding-right: 20px;
}
#template a {
	display: block;
  	border: 1px solid #ddd;
  	border-radius:3px;
  	padding: 10px;
  	text-align: center;
  	background-color: #fff;
  	text-decoration: none;
}
</style>
<script type="text/javascript">
	$("[name = template]").on('click',function(){
		$("#templateSelected").val($(this).val());
		if ($(this).val() == 'A4custom') {
			$("#customTemplateDiv").show();
		} else {
			$("#customTemplateDiv").hide();
		}
	});
	$("#fileuploadbtnForTemplate").on('click',function(){
		$("[name = 'fileuploadForTemplate']").trigger('click');		
	});	
	$("[name = 'fileuploadForTemplate']").change(function(){
		var $btn = $("#fileuploadbtnForTemplate").button('loading');
		var fileObject = {};
		var actionURL = "${pageContext.request.contextPath}/templateUpload";
		try {
			fileObject = document.getElementById("fileuploadForTemplate").files[0];	
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
					alert('模板上传成功');
				} else {
					alert("模板上传失败");
				}
			}
		};
		xhr.send(form);
		this.value = null;
	});	
</script>
<!-- right --> 
<div class="col-xs-9" id="privateProfile">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">创建模板</h3>
  </div>
  <div class="panel-body">
  	<input type="hidden" id="templateSelected" value="">
    <table id="template">
    	<tr>
    		<td>
    			<a>
    			<img src="${pageContext.request.contextPath}/images/a1.jpg"/><br/>
    			<label class="radio-inline"><input type="radio" name="template" value="A4vertical">A4竖板</label>
    			</a>
    		</td>
    		<td>
    			<a>
   				<img src="${pageContext.request.contextPath}/images/a2.jpg"/><br/>
   				<label class="radio-inline"><input type="radio" name="template" value="A4horizontal">A4横版</label>
    			</a>
    		</td>
    		<td>
    			<a>
    			<img src="${pageContext.request.contextPath}/images/a3.jpg"/><br/>
    			<label class="radio-inline"><input type="radio" name="template" value="A4custom">自定义模板</label>
    			</a>
    		</td>
    	</tr>
    </table>
    <div style="display:none;margin-left: 260px;" id="customTemplateDiv" class="panel-body">
    	<input type="file" style="display:none;" name="fileuploadForTemplate" id="fileuploadForTemplate">
    	<a class="btn btn-success" type="button" id="fileuploadbtnForTemplate">上传模板</a>
    </div>
  </div>		
</div>
</div><!-- right end -->