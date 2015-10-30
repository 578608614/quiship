<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shippingSelect</title>
    <style type="text/css">
    	.tableInfo{float:left; width:800px;text-align: center}
    	.tableInfo tr td{text-align: center;padding:0 15px;}
    	.tableInfo .title{background:#488BD1;}
    	.tableInfo .title th{text-align: center}
    </style>
   
  </head>
  
  <body>
 	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
	   		<form id="shippingForm" action="${basePath}shipping/shipping.html" method="POST" >
	   			<table class="listtable">
	   				<tr class="title">   
<!-- 	   					<th><s:message code="quickship.shipping.select" text="Select"/></th> -->
	   					<th><s:message code="quickship.shipping.deliveryDate" text="DeliveryDate"/></th>
	   					<th><s:message code="quickship.shipping.reviceAddress" text="ReviceAddress"/></th>
	   					<th><s:message code="quickship.shipping.revicePhone" text="RevicePhone"/></th>
	   					<th><s:message code="quickship.shipping.serviceType" text="ServiceType"/></th>
	   					<th><s:message code="quickship.shipping.baseCharge" text="BaseCharge"/></th>
	   					<th><s:message code="quickship.shipping.surCharge" text="SurCharge"/></th>
	   					<th><s:message code="quickship.shipping.discount" text="Discount"/></th>
	   					<th><s:message code="quickship.shipping.netCharge" text="NetCharge"/></th>
	   				</tr>
		   				<tr>   
		   					<td>${shipping.deliveryDate}</td>
		   					<td>${shipping.desAddress.address1}</td>
		   					<td>${shipping.desAddress.phone}</td>
		   					<td>${shipping.fedexServerType}</td>
		   					<td>${shipping.baseCharge}</td>
		   					<td>${shipping.surCharge}</td>
		   					<td>${shipping.memberDiscount}</td>
		   					<td>${shipping.netCharge}</td>
		   					
		   				<%-- <td>${reply.completedShipmentDetail.serviceTypeDescription}</td>
		   					<c:forEach items="${reply.completedShipmentDetail.shipmentRating.shipmentRateDetails}" var="rating">
		   						<td>${rating.totalBaseCharge.amount}</td>
		   						<td>${rating.totalSurcharges.amount}</td>
		   						<td>${rating.totalRebates.amount}</td>
		   						<td>${rating.totalNetCharge.amount}</td>
		   					</c:forEach>--%>	
		   				</tr>
	   			</table>
	   			<input type="submit" class="button" value="<s:message code="quickship.shipping.quote" text="Quote"/>">
		    </form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
   <script type="text/javascript">
    	function check(){
//     		if($("input:checked").length>0){
//     			return true;
//     		}
//     		alert("<s:message code='quickship.shipping.selectLable' text='please selected'/>");
//     		return false;
    		
    	}
	</script>
</html>

