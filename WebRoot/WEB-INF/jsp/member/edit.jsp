<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>quiship-edit</title>

<style type="text/css">
	#passwordDiv{
		position:absolute;
		top:30%;
		left:40%;
		border:2px #E24E2A solid;
		background:#ffffff;
		padding:20px;
		z-index: 999;
	}
</style>
</head>

<body>
	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
	<div class="contentWarp">
		<div id="maskDiv"></div>
		<div id="passwordDiv" style="display:none">
			<form id="changePassowrdForm" action="${basePath }member/changePassword.html" action="password">
				<table>
					<tr>
						<input type="hidden" name="id" value="${member.id }"/>
						<th><s:message code="quickship.member.oldpassword" text="Old Password" />:</th>
						<td><input type="password" name="oldPassword"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.password" text="Password" />:</th>
						<td><input type="text" name="password1" id="password1"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.password2" text="Password2" />:</th>
						<td><input type="text" name="password2"/></td>
					</tr>
					<tr>
						<th><input type="submit" class="button" value="<s:message code="quickship.button.submit" text="Change" />"/></th>
						<td><input type="button" class="button" id="cancel" value="<s:message code="quickship.button.cancel" text="Cancel" />"/></td>
					</tr>
				</table>
			</form>
		</div>
		<hr style="margin-top:0">
		<div class="content">
			<form id="updateForm" action="${basePath}member/update.html" method="POST">
				<table>
					<tr>
						<input type="hidden" name="id" value="${member.id }"/>
						<th><s:message code="quickship.member.firstName" text="firstName" />:</th>
						<td><input type="text" name="firstName" value="${member.firstName }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.lastName" text="lastName" />:</th>
						<td><input type="text" name="lastName" value="${member.lastName }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.email" text="Email" />:</th>
						<td><input type="text" name="email" value="${member.email }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.address1" text="Address Line 1" />:</th>
						<td><input type="text" name="address1" value="${member.address1 }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.address2" text="Address Line 2" />:</th>
						<td><input type="text" name="address2" value="${member.address2 }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.city" text="City" />:</th>
						<td><input type="text" name="city" value="${member.city }"/></td>
					</tr>
 					<tr>
		   				<th><s:message code="quickship.member.country" text="Country"/>:</th>
	   					<td>
	   						<input type="hidden" name="areaId" id="areaId" value="${member.area.id }"/>
	   						<select id="area">
	   							<c:forEach items="${areas}" var="area">
	   								<option value="${area.id }" <c:if test="${member.area.id==area.id ||member.area.id==area.parent.id}"> selected</c:if >>${area.name }</option>
	   							</c:forEach>
	   						</select>
	   					</td>
		   			</tr>
		   				<tr id="areaStatetr" <c:if test="${member.area.parent==null}"> style="display:none"</c:if>>
		   					<th><s:message code="quickship.member.state" text="State/Province"/>:</th>
		   					<td>
		   						<select id="areaState">
		   							<c:forEach items="${areachilds}" var="area">
	   									<option value="${area.id }" <c:if test="${member.area.id==area.id }"> selected</c:if >>${area.name }</option>
	   								</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
 					
					<tr>
						<th><s:message code="quickship.member.zipCode" text="ZipCode" />:</th>
						<td><input type="text" name="zip" value="${member.zip }"/></td>
					</tr>
					<tr>
						<th><s:message code="quickship.member.phone" text="Phone" />:</th>
						<td><input type="text" name="phone" value="${member.phone }"/></td>
					</tr>
					<tr align="center">
						<th></th>
						<td>
							<input type="submit" class="button" value="<s:message code="quickship.button.submit" text="Update" />" />
							<input type="button" id="editPassword" class="button" value="<s:message code="quickship.member.changePassword" text="ChangePassword" />"/>
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
			
			var $editPassword = $("#editPassword");
			var $maskDiv = $("#maskDiv");
			var $passwordDiv=$("#passwordDiv");
			var $cancel = $("#cancel");
			var $changePassowrdForm = $("#changePassowrdForm");
			$("#updateForm").validate({
				rules : {
					firstName : "required",
					lastName : "required",
					email : {
						required : true,
						email : true
					},
					address1:"required",
					address2:"required",
					phone:"required",
					zip:"required"
				}
			});
			$changePassowrdForm.validate({
				rules : {
					oldPassword : "required",
					password1 :{
						required:true,
						minlength : 6
					},
					password2 : {
						required : true,
						minlength : 6,
						equalTo : "#password1"
					}
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
  			$editPassword.click(function(){
  				$maskDiv.show();
  				$passwordDiv.show();
  			});
  			$cancel.click(function(){
  				$maskDiv.hide();
  				$passwordDiv.hide();
  			});
  			
		});
	</script>
</body>

</html>
