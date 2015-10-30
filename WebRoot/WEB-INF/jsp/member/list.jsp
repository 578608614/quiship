<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-userMamager</title>
   
  </head>
  
  <body>
    <%@include file="/WEB-INF/jsp/public/header.jsp" %>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
    		<form id="pageForm" action="" method="post">
				<input type="hidden" id="pageNumber" name="pageNumber" value="${page.pageNumber}"/>
				<input type="hidden" name="searchProperty" value="email">
				<s:message code="quickship.member.email" text="Email"/>:<input type="text" name="searchValue" value="${page.searchValue}"/>
				<input type="submit" value="<s:message code="quickship.button.search" text="Search"/>" class="button"/>
	   			<table class="listtable">
	   				<tr class="title">
	   					<th><s:message code="quickship.member.userId" text="UserID"/></th>
	   					<th><s:message code="quickship.member.createDate" text="CreateDate"/></th>
	   					<th><s:message code="quickship.member.email" text="Email"/></th>
	   					<th><s:message code="quickship.member.balance" text="Balance"/></th>
	   					<th><s:message code="quickship.member.isNurse" text="IsNurse"/></th>
	   					<th><s:message code="quickship.operator" text="Operator"/></th>
	   				</tr>
	   				<c:if test="${f:length(page.content)==0}">
	   					<tr>
		   					<td colspan="6" class="norecords"><s:message code="quickship.norecords" text="No Records"/></td>
		   				</tr>
	   				</c:if>
	   				<c:forEach items="${page.content}" var="member" varStatus="sta">
		   				<tr <c:if test="${ sta.index%2==1}"> class="backcolor"</c:if>>
		   					<td>${member.id}</td>
		   					<td>${member.createDate}</td>
		   					<td>${member.email }</td>
		   					<td>${member.balance }</td>
		   					<td>${member.isNurse}</td>
		   					<td class="operator">
		   						<a href="javascript:login('${member.id}')">[<s:message code="quickship.admin.login" text="Login"/>]</a>
	   							<a href="${basePath }member/edit.html?id=${member.id}&flag=1">[<s:message code="quickship.admin.edit" text="Edit"/>]</a>
		   					</td>
		   				</tr>
	   				</c:forEach>
	   			</table>
	   			<c:if test="${f:length(page.content)!=0}">
	   				<%@include file="/WEB-INF/jsp/public/page.jsp" %>
	   			</c:if>
   			</form>
   			
   			<form id="loginForm" action="${basePath}member/adminLogin.html" method="post">
   				<input type="hidden" name="id" id="id"/>
   			</form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
    <script type="text/javascript">
    	function login(id){
    		$("#id").val(id);
    		$("#loginForm").submit();
    	}
    </script>
  </body>
</html>
