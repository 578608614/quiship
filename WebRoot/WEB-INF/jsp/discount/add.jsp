<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-discount</title>
    <meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  
  <body>
 	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
	   		<form id="discountForm" action="${basePath}discount/save.html" method="POST">
	   			<table class="tableInfo">
	   				<tr>
	   					<th><s:message code="quickship.discount.name" text="Name"/>:</th>
	   					<td><input type="text" name="name" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.shipping.serviceType" text="Server Type"/>:</th>
	   					<td>
	   						<select id="serverType" name="serverType">
	   							<option value="">--Select--</option>
	   							<c:forEach items="${serverTypes}" var="serverType">
	   								<option value="${serverType}">${serverType}</option>
	   							</c:forEach>
	   						</select>
	   					</td>
	   				</tr>
	   				
	   				<tr>
	   					<th><s:message code="quickship.discount.firstWeight" text="First Weight"/>:</th>
	   					<td><input type="text" name="firstWeight" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.firstPrice" text="First Price"/>:</th>
	   					<td><input type="text" name="firstDiscount" class="required"/></td>
	   				</tr>
<%--	   				<tr>--%>
<%--	   					<th><s:message code="quickship.discount.continueWeight" text="Continue Weight"/>:</th>--%>
<%--	   					<td><input type="text" name="continueWeight" class="required"/></td>--%>
<%--	   				</tr>--%>
	   				<tr>
	   					<th><s:message code="quickship.discount.continuePrice" text="Continue Price"/>:</th>
	   					<td><input type="text" name="continueDiscount" class="required"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.description" text="Description"/>:</th>
	   					<td><input type="text" name="description"/></td>
	   				</tr>
	   				<tr>
	   					<th><s:message code="quickship.discount.usedAll" text="Description"/>:</th>
	   					<td><input type="checkbox" name="usedAll"/></td>
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

