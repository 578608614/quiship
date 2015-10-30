<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shipList</title>
    <meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <script language="javascript" type="text/javascript" src="${basePath}script/My97DatePicker/WdatePicker.js"></script>
  </script>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
    		<form id="pageForm" action="" method="post">
    			<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}"/>
    			<s:message code="quickship.income.begindate" text="BeginDate"/>:<input type="text" id="beginDate" name="beginDate" value="${beginDate }" onClick="WdatePicker({lang:'en'})"/>
				<s:message code="quickship.income.enddate" text="EndDate"/>:<input type="text" id="endDate" name="endDate" value="${endDate }" onClick="WdatePicker({lang:'en'})"/>
				<s:message code="quickship.member.email" text="Email"/>:<input type="text" id="email" name="email" value="${email }"/>
				<input type="submit" value="Search" class="button"/>
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.shipping.createDate" text="Date"/></th>
	   					<th><s:message code="quickship.member.email" text="Email"/></th>
	   					<th><s:message code="quickship.shipping.baseCharge" text="BaseCharge"/></th>
	   					<th><s:message code="quickship.shipping.surCharge" text="SurCharge"/></th>
	   					<th><s:message code="quickship.shipping.discount" text="Discount"/></th>
	   					<th><s:message code="quickship.income.cost" text="Cost"/></th>
	   					<th><s:message code="quickship.income.charge" text="Charge"/></th>
	   					<th><s:message code="quickship.shipping.serviceType" text="ServiceType"/></th>
	   					<th><s:message code="quickship.shipping.itemWeight" text="Weight"/></th>
	   					<th><s:message code="quickship.shipping.factWeight" text="FactWeight"/></th>
	   					<th><s:message code="quickship.shipping.factCharge" text="FactCharge"/></th>
	   				</tr>
	   				<c:if test="${f:length(page.content)==0}">
	   					<tr>
		   					<td colspan="9" class="norecords"><s:message code="quickship.norecords" text="No Records"/></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${page.content}" var="shipping" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${shipping.createDate}</td>
		   					<td>${shipping.member.email}</td>
		   					<td>${shipping.baseCharge}</td>
		   					<td>${shipping.surCharge }</td>
		   					<td>${shipping.fedexDiscount }</td>
		   					<td>${shipping.costCharge }</td>
		   					<td>${shipping.netCharge }</td>
		   					<td>${shipping.fedexServerType }</td>
		   					<td>${shipping.weight }</td>
		   					<td>${shipping.factWeight }</td>
		   					<td>${shipping.factCharge }</td>
		   				</tr>
	   				</c:forEach>
	   			</table>
	   			<c:if test="${f:length(page.content)!=0}">
	   				<%@include file="/WEB-INF/jsp/public/page.jsp" %>
	   			</c:if>
   			</form>
   			<table class="income">
   				<tr>
  					<th colspan="3" class="title1"><s:message code="quickship.income.total" text="Total Income"/></th>
  				</tr>
  				<tr>
  					<th><s:message code="quickship.income.charge" text="Charge"/></th>
  					<th><s:message code="quickship.income.cost" text="Cost"/></th>
  					<th><s:message code="quickship.income.profit" text="Profit"/></th>
  				</tr>
  				<tr>
   					<td>${totalIncome.charge}</td>
   					<td>${totalIncome.cost }</td>
   					<td>${totalIncome.profit }</td>
		   		</tr>
   			</table>
   			<c:if test="${pageIncome!=null }">
   			<table class="income">
   				<tr>
  					<th colspan="3" class="title1"><s:message code="quickship.income.constain" text="Page Income"/></th>
  				</tr>
  				<tr>
  					<th><s:message code="quickship.income.charge" text="Charge"/></th>
  					<th><s:message code="quickship.income.cost" text="Cost"/></th>
  					<th><s:message code="quickship.income.profit" text="Profit"/></th>
  				</tr>
  				<tr>
   					<td>${pageIncome.charge}</td>
   					<td>${pageIncome.cost }</td>
   					<td>${pageIncome.profit}</td>
		   		</tr>
   			</table>
   			</c:if>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
  
</html>
