<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<div class="pagination">
	<span class="pagearea">
		<!-- 首页 -->
		<c:choose>
		   <c:when test="${page.pageNumber==1}">
		   		<span class="firstPage">&nbsp;</span>
		   </c:when>
		   <c:otherwise>
		   		<a class="firstPage" href="javascript: $.pageSkip('1');">&nbsp;</a>
		   </c:otherwise>
		</c:choose>
		<!-- 上一页-->
		<c:choose>
		   <c:when test="${page.pageNumber>1 }">
		   		<a class="previousPage" href="javascript: $.pageSkip(${page.pageNumber-1});">&nbsp;</a>
		   </c:when>
		   <c:otherwise>
		   		<span class="previousPage">&nbsp;</span>
		   </c:otherwise>
		</c:choose>
		<!-- 页码列表 -->
		<c:forEach begin="${page.beginPageIndex}" end="${page.endPageIndex}" var="num" >
			<c:choose>
			   <c:when  test="${num == page.pageNumber}">
			   		<span class="currentPage">${num}</span>
			   </c:when>
			   <c:otherwise>
			   		<a href="javascript: $.pageSkip('${num}');">${num}</a>
			   </c:otherwise>
			</c:choose>
		</c:forEach>
		
		<!-- 下一页 -->
		<c:choose>
		   <c:when test="${page.pageNumber<page.totalPages }">
		   		<a class="nextPage" href="javascript: $.pageSkip(${page.pageNumber+1});">&nbsp;</a>
		   </c:when>
		   <c:otherwise>
		   		<span class="nextPage">&nbsp;</span>
		   </c:otherwise>
		</c:choose>
		<!-- 尾页-->
		<c:choose>
		   <c:when test="${page.pageNumber==page.totalPages||page.totalPages==0}">
		   		<span class="lastPage">&nbsp;</span>
		   </c:when>
		   <c:otherwise>
		   		<a class="lastPage" href="javascript: $.pageSkip(${page.totalPages});">&nbsp;</a>
		   </c:otherwise>
		</c:choose>
		&nbsp;&nbsp;
		${page.total} Records
	</span>
	<span class="pageSkip">
<!-- 		<s:message code="page.view.current" text="Current"/>:${page.pageNumber} <s:message code="page.view.unit" text="Page"/>/<s:message code="page.view.total" text="Total"/>:${page.totalPages} <s:message code="page.view.unit" text="Page"/> -->
		${page.pageNumber}/${page.totalPages}
	</span>
</div>