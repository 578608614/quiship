<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quick-reconcile</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="Discount Shipping Label">

  </head>
  
  <body>
 	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
 	<script language="javascript" type="text/javascript" src="${basePath}script/My97DatePicker/WdatePicker.js"></script>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
		   	<form id="myform" action="${basePath }reconcileShipping/upload.html" method="post" enctype="multipart/form-data">
			  	<input type="file" name="multipartFile"/>
			  	<input type="button" id="upload" value="Upload" class="button"/>
			  	<input type="button" id="reconcile" value="Reconcile" class="button"/>
		   	</form>
		   	<form action="${basePath }reconcileShipping/reconcile.html" method="post" enctype="application/x-www-form-urlencoded" >
	   </div>
	 </div>
	 <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
	 <script>
	 	$(function(){
	 		$myForm = $("#myform");
	 		$("#upload").click(function(){
	 			$myForm.attr("action","${basePath }reconcileShipping/upload.html").attr("enctype","multipart/form-data");
	 			$myForm.submit();
	 		});
	 		$("#reconcile").click(function(){
	 			$myForm.attr("action","${basePath }reconcileShipping/reconcile.html").attr("enctype","application/x-www-form-urlencoded");
	 			$myForm.submit();
	 		});
	 		
	 	});
	 </script>
  </body>
</html>
