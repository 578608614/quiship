<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-notify</title>
  </head>
  
  <body>
  	<%@include file="/WEB-INF/jsp/public/header.jsp" %>
  	
  	<div class="contentWarp">
  		<hr style="margin-top:0">
  		<div class="content">
			${sessionScope.member.lastName}: 充值 ${payment.status}
  		</div>
	</div>
  	<%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  	</body>
</html>
