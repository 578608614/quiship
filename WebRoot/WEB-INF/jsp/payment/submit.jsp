<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>

  <head>
  	<title>quiship-payment-submit</title>
  	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
  </head>
  
  <body onload="javascript:document.forms[0].submit()">
	<form action="${requestUrl}" <c:if test="${requestMethod!=null}">method="${requestMethod}"</c:if> <c:if test="${reqeustCharest!=null}"> accept-charset="${reqeustCharest}"</c:if>>
		<c:forEach items="${paramMap}" var="item">
		 	<input type="hidden" name="${item.key}" value="${item.value}" />
		</c:forEach>
	</form>
  </body>
</html>
