<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shipList</title>
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
    		<form id="pageForm" action="" method="post">
				<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}"/>
				<input type="hidden" id="searchProperty" name="searchProperty" value="labelTrackNo">
				<s:message code="quickship.shipping.trackNum" text="TrackNumber"/>:<input type="text" id="searchValue" name="searchValue" value="${page.searchValue}"/>
				<s:message code="quickship.shipping.member" text="MemberEmail"/>:<input type="text"  name="email" value="${email }"/>
				<input type="submit" value="Search" class="button"/>
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.shipping.createDate" text="Date"/></th>
	   					<th><s:message code="quickship.shipping.trackNum" text="TrackNumber"/></th>
	   					<th><s:message code="quickship.shipping.recepient" text="Recepient"/></th>
	   					<th><s:message code="quickship.shipping.baseCharge" text="BaseCharge"/></th>
	   					<th><s:message code="quickship.shipping.surCharge" text="SurCharge"/></th>
	   					<th><s:message code="quickship.shipping.discount" text="Discount"/></th>
	   					<th><s:message code="quickship.shipping.cost" text="Cost"/></th>
	   					<th><s:message code="quickship.shipping.netCharge" text="netCharge"/></th>
<!-- 	   					<th><s:message code="quickship.shipping.factCharge" text="FactCharge"/></th> -->
	   					<th><s:message code="quickship.shipping.reconcileType" text="ReconcileSate"/></th>
	   				</tr>
	   				<c:if test="${f:length(page.content)==0}">
	   					<tr>
		   					<td colspan="8" class="norecords"><s:message code="quickship.norecords" text="No Records"/></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${page.content}" var="shipping" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${shipping.createDate}</td>
		   					<td>${shipping.labelTrackNo }</td>
		   					<td>${shipping.desAddress.person }</td>
		   					<td>${shipping.baseCharge }</td>
		   					<td>${shipping.surCharge }</td>
		   					<td>${shipping.fedexDiscount }</td>
		   					<td>${shipping.costCharge }</td>
		   					<td>${shipping.netCharge }</td>
<!-- 		   					<td>${shipping.factCharge }</td> -->
		   					<td>${shipping.reconcileType }</td>
		   				</tr>
	   				</c:forEach>
	   			</table>
	   			<c:if test="${f:length(page.content)!=0}">
	   				<%@include file="/WEB-INF/jsp/public/page.jsp" %>
	   			</c:if>
   			</form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
</html>
