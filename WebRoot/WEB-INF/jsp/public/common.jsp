<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("basePath", basePath);
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${basePath}style/css/common.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${basePath}script/jquery.js"></script>
<script type="text/javascript" src="${basePath}script/jquery.validate.js"></script>
<script type="text/javascript" src="${basePath}script/jquery.metadata.js"></script>

<script type="text/javascript">
 $(function(){
 	$pageForm = $("#pageForm");
 	$pageNumber = $("#pageNumber");
 	$flashMessage = $("#flashMessage");
 	// 页码跳转
	$.pageSkip = function(pageNumber) {
		$pageNumber.val(pageNumber);
		$pageForm.submit();
		return false;
	};
	setTimeout(function(){
		$flashMessage.hide();
	},3500);
 });
</script>

