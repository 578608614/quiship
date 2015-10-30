<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>quiship-login</title>
</head>

<body>
	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
	<div class="contentWarp">
		<hr style="margin-top:0">
		<div class="content">
			<form id="registerForm" action="${basePath}member/register.html" method="POST">
				<table>
					<tr>
						<th><s:message code="quickship.member.firstName" text="firstName" />:</th>
						<td><input type="text" name="firstName" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.lastName" text="lastName" />:</th>
						<td><input type="text" name="lastName" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.email" text="Email" />:</th>
						<td><input type="text" name="email" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.password" text="Password" />:</th>
						<td><input type="password" name="password" id="password" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.password2" text="RePassword" />:</th>
						<td><input type="password" name="password2" id="password2" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.address1" text="Address Line 1" />:</th>
						<td><input type="text" name="address1" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.address2" text="Address Line 2" />:</th>
						<td><input type="text" name="address2" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.city" text="City" />:</th>
						<td><input type="text" name="city" /></td>
					</tr>
 					<tr>
		   				<th><s:message code="quickship.member.country" text="Country"/>:</th>
	   					<td>
	   						<input type="hidden" name="areaId" id="areaId" value="${areachilds[0].id}"/>
	   						<select id="area">
	   							<c:forEach items="${areas}" var="area">
	   								<option value="${area.id }">${area.name }</option>
	   							</c:forEach>
	   						</select>
	   					</td>
		   			</tr>
		   				<tr id="areaStatetr">
		   					<th><s:message code="quickship.member.state" text="State/Province"/>:</th>
		   					<td>
		   						<select id="areaState">
			   						<c:forEach items="${areachilds}" var="area">
		   								<option value="${area.id }">${area.name }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
 					
					<tr>
						<th><s:message code="quickship.member.zipCode" text="ZipCode" />:</th>
						<td><input type="text" name="zip" /></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.phone" text="Phone" />:</th>
						<td><input type="text" name="phone" /></td>
					</tr>
					<tr align="center">
						<th></th>
						<td>
							<input type="submit" class="button" value="<s:message code="quickship.button.register" text="Register" />" />
						</td>
					</tr>

				</table>

			</form>
		</div>
	</div>
	<%@include file="/WEB-INF/jsp/public/footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function() {
			var $area = $("#area");
			var $areaStatetr = $("#areaStatetr");
			var $areaState = $("#areaState");
			var $areaId = $("#areaId");
			$("#registerForm").validate({
				rules : {
					firstName : "required",
					lastName : "required",
					email : {
						required : true,
						email : true
					},
					address1:"required",
					password : {
						required : true,
						minlength : 6
					},
					password2 : {
						required : true,
						minlength : 6,
						equalTo : "#password"
					},
					phone:"required"
				}
			});
			$area.change(function(){
    			findChildArea($(this),$areaState,$areaStatetr,$areaId);
    		});
    		
    		$areaState.change(function(){
    			setAreaId($areaId,$(this));
    		});
			function setAreaId(desobj,valobj){
  				desobj.val(valobj.val());
  			}
  			
  			function findChildArea(paramObj,selectObj,showObj,desObj){
  				$.get("${basePath}area/areaList.html",{"areaId":paramObj.val()},function(data){
    				if(data!=null&&data.length>0){
    					selectObj.empty();
    					var html = "";
    					$.each(data, function(i,item){
  							html+="<option value="+item.id+">"+item.name+"</option>";
						});
    					selectObj.append(html);
    					showObj.show();
    				}else{
    					showObj.hide();
    				}
    				setAreaId(desObj,paramObj);
    				
    			});
  			}
		});
	</script>
</body>

</html>
