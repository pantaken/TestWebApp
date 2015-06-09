<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
 <div class="panel-heading">
   <h3 class="panel-title">课程章节</h3>
 </div>
 <div class="panel-body">
 	<!-- left -->
<div class="col-xs-7">
	<form class="form-horizontal" action="">
  	<div>
  		第一步：<label>选择您的课程名称</label>
  	</div>
  	<select class="form-control" id="subjectname">
  		<c:forEach items="${subjects}" var="subject">
  			<option value="${subject.serialcode}">${subject.subjectname}</option>
  		</c:forEach>
  	</select>
    <div>
    	第二步：<label>创建课程章节</label>
    </div>
    <input type="text" id="nodename" class="form-control">
    <a id="btn_addsection" type="button" class="btn btn-default">确定</a>
    </form>	
</div>
<!-- right tree -->
<div class="col-xs-5">
	<div class="test-tree-container">
	<div id="sidetreecontrol" style="float: right;"><a href="?#">收起</a> | <a href="?#">展开</a></div>
	<input type="hidden" name="pnode" id="pnode" value="0">
	<ul id="tree" style="font-family: 'Microsoft YaHei',arial,sans-serif;font-size: 12px;color: #000;">
	</ul>		
	</div>
</div>
 </div>
