<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
function uuid() {
    var s = [];
    var hexDigits = "0123456789abcdef";
    for (var i = 0; i < 36; i++) {
        s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
    }
    s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
    s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
    s[8] = s[13] = s[18] = s[23] = "-";
 
    var uuid = s.join("");
    return uuid;
}
</script>
 <div class="panel-heading">
   <h3 class="panel-title">课程信息</h3>
 </div>
 <div class="panel-body">
	<form data-pjax action="${pageContext.request.contextPath}/savesubjectinfo" method="post" class="form-horizontal" enctype="application/x-www-form-urlencoded">
		<div class="form-group" id="subjectnamediv">
		<label for="subjectname" class="col-xs-2 control-label">课程名称</label>
		<div class="col-xs-6">
			<input type="text" name="subjectname" class="form-control">
		</div>
		</div>
		
		<div class="form-group" id="subjectcodediv">
		<label for="subjectcode" class="col-xs-2 control-label">课程编码</label>
		<div class="col-xs-6">
			<div class="input-group">
				<input type="text" name="subjectcode" class="form-control" id="subjectcode">
				<span class="input-group-btn">
		        	<button class="btn btn-success" type="button" onclick="javascript:document.getElementById('subjectcode').setAttribute('value', uuid());">系统生成</button>
		      	</span>
	      	</div>	
		</div>
		</div>		
		
		<div class="form-group">
			<label class="col-xs-2 control-label"></label>
			<div class="col-xs-6" align="center">
			<input type="submit" value="保存" class="btn btn-default">
			</div>
		</div>
	</form>
  </div>