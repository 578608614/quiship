<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
  <head>
    <title>quiship-recharge</title>
    <meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  
  <body>
  	<%@include file="/WEB-INF/jsp/public/header.jsp" %>
  	
  	<div class="contentWarp">
  		<hr style="margin-top:0">
  		<div class="content">
  			<form id="pageForm" action="" method="post">
				<input type="hidden" id="pageNumber" name="pageNumber"/>
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.deposit.date" text="Date"/></th>
	   					<th><s:message code="quickship.deposit.amount" text="Amount"/></th>
	   					<th><s:message code="quickship.deposit.credit" text="Credit"/></th>
	   					<th><s:message code="quickship.deposit.debit" text="Debit"/></th>
	   					<th><s:message code="quickship.deposit.type" text="Memo"/></th>
	   				</tr>
	   				<c:if test="${f:length(page.content)==0}">
	   					<tr>
		   					<td colspan="5" class="norecords"><s:message code="quickship.norecords" text="No Records"/></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${page.content}" var="deposit" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${deposit.createDate}</td>
		   					<td>${deposit.balance}</td>
		   					<td>${deposit.credit }</td>
		   					<td>${deposit.debit}</td>
		   					<td>${deposit.type}</td>
		   				</tr>
	   				</c:forEach>
	   			</table>
	   			<c:if test="${f:length(page.content)!=0}">
	   				<%@include file="/WEB-INF/jsp/public/page.jsp" %>
	   			</c:if>
	   			
   			</form>
			<form action="${basePath }payment/submit.html" method="POST" target="_blank">
				<table>
					<input type="hidden" name="type" value="recharge"/>
					<tr> 
						<th><s:message code="quickship.deposit.recharge" text="Recharge"/>:</th>
						<td><select name="amount">
								<option value="0.1">0.1$</option>
								<option value="50">50$</option>
								<option value="100">100$</option>
								<option value="150">150$</option>
								<option value="200">200$</option>
								<option value="500">500$</option>
							</select> 
						</td>	
					</tr>
					<tr> 
						<th><s:message code="quickship.deposit.accept" text="Accept"/>:</th>
						<td>
							<input type="hidden" name="paymentPluginId" value="paypalPlugin">
							<img src="${basePath}style/image/paypal.png" alt="${paymentPlugin.name }"/>
						<!--  	<c:forEach items="${paymentPlugins}" var="paymentPlugin">
								<input type="radio" name="paymentPluginId"	value="${paymentPlugin.id}" checked/>
								<img src="${basePath}style/image/paypal.png" alt="${paymentPlugin.name }"/>
							</c:forEach>
							-->
						</td>	
							
					</tr>
					<tr> 
						<th></th>
						<td>
							<input type="submit" class="button" value="<s:message code="submit" text="submit"/>" />
						</td>	
					</tr>
				</table>
			</form>
  		</div>
	</div>
  	
  	<%@include file="/WEB-INF/jsp/public/footer.jsp" %>
 </body>
</html>
