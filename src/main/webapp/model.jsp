<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css">
<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>


</head>
<body>
<!-- Button trigger modal -->
<button type="button" class="btn btn-default" data-toggle="modal" data-target="#picModel" style="display: none;">
弹出
</button>
<input type="button" value="弹出" onclick="javascript:$('#picModel').modal('toggle');">
<!-- Modal -->
<div class="modal fade" id="picModel" tabindex="-1" role="dialog" aria-labelledby="picLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="picLabel">剪切你的头像</h4>
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
</div>
</body>
</html>