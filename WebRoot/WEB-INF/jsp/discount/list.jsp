<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shippingList</title>
    <meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
    		<a class="button" style="float:right;" href="${basePath}discount/add.html"><s:message code="quickship.discount.add" text="Add"/></a>
    		<form id="pageForm" action="" method="post">
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.discount.name" /></th>
	   					<th><s:message code="quickship.shipping.serviceType"/></th>
	   					<th><s:message code="quickship.discount.firstWeight"/></th>
	   					<th><s:message code="quickship.discount.firstPrice"/></th>
<%--  					<th><s:message code="quickship.discount.continueWeight"/></th> --%> 
	   					<th><s:message code="quickship.discount.continuePrice"/></th>
	   					<th><s:message code="quickship.discount.usedAll"/></th>
	   					<th><s:message code="quickship.operator" text="Operator"/></th>
	   				</tr>
	   				<c:if test="${f:length(discounts)==0}">
	   					<tr>
		   					<td colspan="7" class="norecords"><span class="norecords"><s:message code="quickship.norecords" text="No Records"/></span></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${discounts}" var="discount" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${discount.name}</td>
		   					<td>${discount.serverType}</td>
		   					<td>${discount.firstWeight}</td>
		   					<td>${discount.firstDiscount }</td>
		   			<%-- 	<td>${discount.continueWeight }</td>--%>	
		   					<td>${discount.continueDiscount }</td>
		   					<td>${discount.usedAll }</td>
		   					<td class="operator">
		   						<a href="${basePath}discount/edit.html?id=${discount.id}">[<s:message code="quickship.edit" text="edit"/>]</a>
		   						<a href="${basePath}discount/delete.html?id=${discount.id}">[<s:message code="quickship.delete" text="delete"/>]</a>
		   					</td>
		   				</tr>
	   				</c:forEach>
	   			</table>
   			</form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
</html>
