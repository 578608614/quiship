<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <%@include file="/WEB-INF/jsp/public/common.jsp" %>
    <style>
    	.flashMessage{
    		height:50px;
    		width:100%;
    		line-height:50px;
    		position:absolute;
    		top:20px;
    		left:0px;
    		z-index:99;
    		text-align: center;
    		background:#E24E2A;
    		font-size: 20px;
    	}
    </style>
    <title>QuiShip-index</title>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="QuiShip ,Discount,Shipping">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  <body>
  <div id="hredWarp">
	   <div id="header">
	   		<div id="logo"><h1><a href="${basePath}index.jsp">QUISHIP</a></h1></div>
	   		
	   		<div class="memberInfo">
	   			<p>
		   			<c:if test="${sessionScope.member==null}">
			   			<s:message code="quickship.welcome" text="Hello"/>: <font style="font-weight:bold;"><s:message code="quickship.guest" text="Guest"/></font><br/>
			   			<a href="${basePath}member/login.html"><s:message code="quickship.button.login" text="Login"/></a>&nbsp;
			   			<a href="${basePath}member/register.html"><s:message code="quickship.button.register" text="Register"/></a>
			   		</c:if>
			   		<c:if test="${sessionScope.member!=null}">
		   				<font style="font-weight:bold;">${sessionScope.member.lastName}</font>: <s:message code="quickship.Welcome" text="Welcome"/><br/>
		   				<a href="${basePath}member/logout.html"><s:message code="quickship.logout" text="Logout"/></a>&nbsp;
		   			<%-- 	<a href="${basePath}member/edit.html?id=${sessionScope.member.id}"><s:message code="quickship.myprofile" text="MyProfile"/></a>--%>
		   				<span class="balance"><s:message code="quickship.member.balance" text="Balance"/>:${member.balance}</span>
		   			</c:if>
			   	</p>
	   		</div>
		 	<ul class="menu">
		 		<li>
		 			<a href="${basePath}member/edit.html?id=${sessionScope.member.id}"><s:message code="quickship.myprofile" text="MyProfile"/></a>
		 			<%--<a href="#"><s:message code="quickship.menu.myDiscountRate" text="My Discount Rate"/></a>--%>
		 		</li>
		 		<li><a href="${basePath}deposit/recharge.html"><s:message code="quickship.menu.statement" text="Statement/Recharge"/></a></li>
		 		<li><a href="${basePath}shipping/list.html"><s:message code="quickship.menu.labelHistory" text="Label History"/></a></li>
		 		<li><a href="${basePath}shipping/shipping.html"><s:message code="quickship.menu.newShipment" text="New Shipment"/></a></li>
		 	</ul>
		 	<c:if test="${sessionScope.member.isValidate==true}">
			 	<ul class="adminMenu">
			 		<li><a href="${basePath}reconcileShipping/add.html"><s:message code="quickship.menu.admin.reconcileStatements" text="Reconcile Statements"/></a></li>
			 		<li><a href="${basePath}discount/list.html"><s:message code="quickship.menu.admin.discount" text="Discount"/></a></li>
			 		<li><a href="${basePath}shipping/adminlist.html"><s:message code="quickship.menu.admin.labelsReport" text="Labels Report"/></a></li>
<!-- 			 		<li><a href="#"><s:message code="menu.admin.newUsersReport" text="New Users Report"/></a></li> -->
			 		<li><a href="${basePath}member/list.html"><s:message code="quickship.menu.admin.userManagement" text="User Management"/></a></li>
			 		<li><a href="${basePath}shipping/income.html"><s:message code="quickship.menu.admin.incomeReport" text="Income Report"/></a></li>
			 	</ul>
		 	</c:if>
	   </div>
  	<c:if test="${flashMessage!=null}">
	 	<div id="flashMessage" class="flashMessage">
	 		<font color="#ffffff">${flashMessage}</font>
	 	</div>
 	</c:if>
   </div>
  </body>
</html>
