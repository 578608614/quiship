<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-discount_update</title>
    <meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  
  <body>
 	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
	   		<form id="discountForm" action="${basePath}discount/update.html" method="POST">
	   			<table class="tableInfo">
	   				<input type="hidden" name="id" value="${discount.id }">
	   				<tr>
	   					<th><s:message code="quickship.discount.name" text="Name"/>:</th>
	   					<td><input type="text" name="name"  value="${discount.name}" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.shipping.serviceType" text="Server Type"/>:</th>
	   					<td>
	   						<select id="serverType" name="servierTYpe">
	   							<option value="0">--Select--</option>
	   							<c:forEach items="${serverTypes}" var="serverType">
	   								<option value="${serverType}" <c:if test="${discount.serverType==serverType}"> selected </c:if>>${serverType}</option>
	   							</c:forEach>
	   						</select>
	   					</td>
	   				</tr>
	   				
	   				<tr>
	   					<th><s:message code="quickship.discount.firstWeight" text="First Weight"/>:</th>
	   					<td><input type="text" name="firstWeight" value="${discount.firstWeight}" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.firstPrice" text="First Price"/>:</th>
	   					<td><input type="text" name="firstDiscount" value="${discount.firstDiscount}" class="required"/></td>
	   				</tr>
	<%-- 				<tr>
	   					<th><s:message code="quickship.discount.continueWeight" text="Continue Weight"/>:</th>
	   					<td><input type="text" name="continueWeight" value="${discount.continueWeight}" class="required"/></td>
	   				</tr>--%>   
	   				<tr>
	   					<th><s:message code="quickship.discount.continuePrice" text="Continue Price"/>:</th>
	   					<td><input type="text" name="continueDiscount" value="${discount.continueDiscount}" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.description" text="Description"/>:</th>
	   					<td><input type="text" name="description" value="${discount.description}"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.usedAll" text="Description"/>:</th>
	   					<td><input type="checkbox" name="usedAll" <c:if test="${discount.usedAll==true}"> checked </c:if>/></td>
	   				</tr>
	   				<tr>
	   					<th>&nbsp;</th>
	   					<td>
	   						 <input type="submit"  class="button" value="<s:message code="quickship.shipping.quote" text="Quote"/>">
	   					</td>
	   				</tr>
	   				
	   			</table>
	   			
		    </form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
   <script type="text/javascript">
    	$().ready(function() {
    		$("#discountForm").validate();
    	});
	</script>
</html>

