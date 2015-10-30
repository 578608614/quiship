<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-login</title>
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
	   		<form action="${basePath}member/login.html" method="POST">
	   			<table>
	   				<tr>
	   					<th><s:message code="quickship.member.email" text="Email"/>:</th>
	   					<td><input type="text" name="email" /></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.member.password" text="Password"/>:</th>
	   					<td><input type="password" name="password" id="password"/></td>
	   				</tr>
	   				<tr>
	   					<th></th>
						<td>
	   						<input class="button" type="submit" value="<s:message code="quickship.button.login" text="Login" />"/>
	   					</td>
	   				</tr>
	   			</table>
		    </form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
</html>

