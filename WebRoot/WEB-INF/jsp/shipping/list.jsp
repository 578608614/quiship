<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shippingList</title>
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
    		<form id="pageForm" action="" method="post">
				<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}"/>
				<input type="hidden" name="searchProperty" value="labelTrackNo">
				<s:message code="quickship.shipping.trackNo" text="TrackNo"/>:<input type="text" name="searchValue" value="${page.searchValue}"/>
				<input type="submit" value="Search" class="button"/>
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.shipping.createDate" text="Date"/></th>
	   					<th><s:message code="quickship.shipping.trackNum" text="TrackNumber"/></th>
	   					<th><s:message code="quickship.shipping.recepient" text="Recepient"/></th>
	   					<th><s:message code="quickship.shipping.destination" text="Destination"/></th>
	   					<th><s:message code="quickship.shipping.serviceType" text="ServiceType"/></th>
	   					<th><s:message code="quickship.shipping.netCharge" text="Charge"/></th>
	   					<th><s:message code="quickship.shipping.adjustment" text="Adjustment"/></th>
	   					<th><s:message code="quickship.shipping.label" text="Label"/></th>
	   				</tr>
	   				<c:if test="${f:length(page.content)==0}">
	   					<tr>
		   					<td colspan="8" class="norecords"><span class="norecords"><s:message code="quickship.norecords" text="No Records"/></span></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${page.content}" var="shipping" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${shipping.createDate}</td>
		   					<td>${shipping.labelTrackNo }</td>
		   					<td>${shipping.desAddress.person }</td>
		   					<td>${shipping.desAddress.city }</td>
		   					<td>${shipping.serverType }</td>
		   					<td>${shipping.netCharge }</td>
		   					<td>${shipping.netCharge }</td>
		   					<td class="operator">
		   						<a href="${basePath}shipping/downLoad.html?id=${shipping.id}">[<s:message code="quickship.shipping.lable" text="Lable"/>]</a>
		   					</td>
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
