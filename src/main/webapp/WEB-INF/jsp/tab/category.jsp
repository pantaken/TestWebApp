<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
#categorydiv > div {
	margin: 5px 7.5px; 
	display: inline-block;
}
</style>
<script type="text/javascript">
$("#btn_addcategory").on('click',function(){
	$.ajax({
		 url		: "${pageContext.request.contextPath}/addcategory",
		 data 		: {category : $("#category").val(), subjectname : $("#subjectname").val()},
		 type		: 'POST',
		 async		: false,
		 dataType	: "json", 
		 success	: function(result){
		  	console.log(result);
		 }
	});	
});
$("#subjectname").on('change',function(){
	$.ajax({
		 url		: "${pageContext.request.contextPath}/category_query",
		 data 		: {subjectcode : $("#subjectname").val()},
		 type		: 'POST',
		 async		: false,
		 dataType	: "json", 
		 success	: function(result){
			
			var html = '';
		  	$.each(result, function(index, item){
		  	html +=
		    '<div class="label label-success">' + item.category +
	    		'<button type="button" style="float: none;font-size: inherit;padding-left:5px;" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">×</span></button>' +
	    	'</div>';
		  	});
		  	$("#categorydiv").html(html);
		 }
	});
});
</script>
 <div class="panel-heading">
   <h3 class="panel-title">题目类型</h3>
 </div>
 <div class="panel-body">
	<!-- left -->
	<form class="form-horizontal col-xs-6" action="${pageContext.request.contextPath}/addcategory">
  	<div>
  		第一步：<label>选择您的课程名称</label>
  	</div>
  	<select class="form-control" id="subjectname">
  		<option value="nil" disabled="disabled" selected="selected">---------------------------------选择课程----------------------------------</option>
  		<c:forEach items="${subjects}" var="subject">
  			<option value="${subject.serialcode}">${subject.subjectname}</option>
  		</c:forEach>
  	</select>
    <div>
    	第二步：<label>添加题目类型</label>
    </div>
    <input type="text" id="category" name="category" class="form-control">
    <a id="btn_addcategory" type="button" class="btn btn-default">确定</a>
    </form>
    <!-- right -->
    <div class="col-xs-6" id="categorydiv">
    </div>
 </div>