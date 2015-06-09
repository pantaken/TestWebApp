<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/wxl.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/js/wxl.js">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/mj/MathJax.js?config=default"></script>
<script type="text/x-mathjax-config">
$('[data-tip="tooltip"]').tooltip({container: 'body', animation : false});
MathJax.Hub.Config({
	showProcessingMessages: false,
	jax: ["input/MathML","output/CommonHTML"],
	extensions: ["mml2jax.js"],
	showMathMenu: false,
	showMathMenuMSIE: false
});
</script>
<!-- right --> 
<div class="col-xs-9" id="privateProfile">
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">题目列表</h3>
  </div>
	<ul class="list-group" style="text-align: left;font-style: normal;">
		<c:forEach var="BeautyQuestion" items="${lists}" varStatus="status">
			<li class="list-group-item test-list-group-item">
				${status.index + 1}.  <c:out value="${BeautyQuestion.content}" escapeXml="false"/><br/>
				<c:if test="${not empty BeautyQuestion.img}">
					<img alt="" src="${pageContext.request.contextPath}/resources/${BeautyQuestion.img}" /><br/>
				</c:if>
				<c:if test="${BeautyQuestion.categorycode eq '1d3805b9-fcf2-4c73-b602-2acb49080963'}">				
				（A）   <c:out value="${BeautyQuestion.a}" escapeXml="false"/><br/>
				（B）   <c:out value="${BeautyQuestion.b}" escapeXml="false"/><br/>
				（C）   <c:out value="${BeautyQuestion.c}" escapeXml="false"/><br/>
				（D）   <c:out value="${BeautyQuestion.d}" escapeXml="false"/><br/>
				</c:if>
			</li>
			
			<span class="fieldtip">
				<div>
					<a href="javascript:void(0)" onclick="" class="tool" data-tip="tooltip" data-placement="right" title="设置知识点"><span class="octicon octicon-repo"></span></a>
					<a href="javascript:void(0)" onclick="" class="tool" data-tip="tooltip" data-placement="right" title="设置难易程度"><span class="octicon octicon-law"></span></a>
					<a href='javascript:void(0)' onclick='javascript:Obj.switchfavorite(<c:out value="${BeautyQuestion.id}" />,this);' class="tool" data-tip="tooltip" data-placement="right" title="移除(已设置属性？)"><span class="octicon octicon-trashcan"></span></a>
				</div>
			</span>
		</c:forEach>	
	</ul>
</div>
</div><!-- right end -->