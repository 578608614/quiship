<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>quiship-login</title>
	<meta http-equiv="keywords" content="fedex,dhl,usps,quiship,Discount">
	<meta http-equiv="description" content="QuiShip Discount Shipping Label">
</head>

<body>
	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
	<div class="contentWarp">
		<hr style="margin-top:0">
		<div class="content">
			<form id="updateForm" action="${basePath}member/adminUpdate.html" method="POST">
				<table>
					<tr><input type="hidden" name="id" value="${member.id }">
						<th><s:message code="quickship.member.firstName" text="firstName" />:</th>
						<td>${member.firstName}</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.lastName" text="lastName" />:</th>
						<td>${member.lastName} </td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.email" text="Email" />:</th>
						<td>${member.email}</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.city" text="City" />:</th>
						<td>${member.city }</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.zipCode" text="ZipCode" />:</th>
						<td>${member.zip}</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.phone" text="Phone" />:</th>
						<td><input type="text" name="phone" value="${member.phone}"></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.country" text="Country" />:</th>
						<td>${member.area.fullName}</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.balance" text="Balance" />:</th>
						<td>${member.balance }</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.amount" text="Amount" />:</th>
						<td>${member.amount }</td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.adjustmentbalance" text="AdjustmentBalance" />:</th>
						<td><input type="text" name="adbalance"/></td>
					</tr>
<!-- 					<tr> -->
<!-- 						<th><s:message code="quickship.member.isNurse" text="IsNurse" />:</th> -->
<!-- 						<td><input type="checkbox" name="isNurse" <c:if test="${member.isNurse==true }">checked</c:if>></td> -->
<!-- 					</tr> -->
					<tr>
						<th><s:message code="quickship.member.isAdmin" text="IsAdmin" />:</th>
						<td><input type="checkbox" name="isAdmin" <c:if test="${member.isValidate==true }">checked</c:if>></td>
					</tr>
					<tr align="center">
						<th></th>
						<td>
							<input type="submit" class="button" value="<s:message code="quickship.member.update" text="Update" />" />
						</td>
					</tr>

				</table>

			</form>
		</div>
	</div>
	<%@include file="/WEB-INF/jsp/public/footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#updateForm").validate({
				rules : {
					adbalance : "number"
				}
			});
		});
	</script>
</body>

</html>
