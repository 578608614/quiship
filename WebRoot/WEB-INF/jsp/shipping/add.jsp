<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>quiship-shipping</title>
    <style type="text/css">
    	.tableInfo{float:left; margin-right:10px;width:365px;}
    	.tableInfo .title{background:#488BD1;color:#FFFFFF}
    	.tableInfo .title th{text-align: center}
    	
    	#itemTable{float:left;margin-top:10px;text-align: center; width:360px;}
    	#itemTitle{background:#488BD1;}
    	#itemTable th{text-align: center;width:80px;}
    	#itemTable td{text-align: center;}
    	#itemTable .tdItemsize{display:none;width:170px;}
    	#itemTable .tdItemsize input{float:right;}
    	
    </style>
   
  </head>
  
  <body>
 	<%@include file="/WEB-INF/jsp/public/header.jsp"%>
    <div class="contentWarp">
    	<hr style="margin-top:0">
    	<div class="content">
	   		<form id="shippingForm" action="${basePath}shipping/buildShipping.html" method="POST" onsubmit="return check()">
	   			<strong>
					<s:message code="quickship.shipping.loadShippingProfile" text="Load Shipping Profile"/>:
				</strong>
				<select id="shippings">
					<option value="0">--Select--</option>
					<c:forEach items="${shippings}" var="shipping">
						<option value="${shipping.id}">${shipping.byName }</option>
					</c:forEach>
				</select>
				
	   			<div style="overflow: hidden;margin-bottom:10px;">
		   			<!-- 发件人信息 -->
		   			<table class="tableInfo">
		   				<tr class="title">   
		   					<th colspan="2" style="text-align: center"><s:message code="quickship.shipping.origination" text="Origination"/></th>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.loadAddress" text="Load oriAddress"/>:</th>
		   					<td>
		   						<select id="oriAddress">
		   							<option value="0">--Select--</option>
		   							<c:forEach items="${addresses}" var="address">
		   								<option value="${address.id}">${address.byName }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.person" text="Person"/>:</th>
		   					<td><input type="text" id="oriPerson" name="oriAddress.person" class="required" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.person}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.company" text="Company"/>:</th>
		   					<td><input type="text" id="oriCompany" name="oriAddress.company" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.company}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.phone" text="Phone"/>:</th>
		   					<td><input type="text" id="oriPhone" name="oriAddress.phone" class="required" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.phone}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.address1" text="Address1"/>:</th>
		   					<td><input type="text" id="oriAddress1" name="oriAddress.address1" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.address1}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.address2" text="Address2"/>:</th>
		   					<td><input type="text" id="oriAdddress2" name="oriAddress.address2" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.address2}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.zipCode" text="ZipCode"/>:</th>
		   					<td><input type="text" id="oriZipCode" name="oriAddress.zipCode" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.zipCode}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.city" text="City"/>:</th>
		   					<td><input type="text" id="oriCity" name="oriAddress.city" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.oriAddress.city}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.country" text="Country"/>:</th>
		   					<td>
		   						<input type="hidden" name="oriAreaId" id="oriAreaId" 
		   							<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.area!=null}"> value="${tmpShipping.oriAddress.area.id}"</c:if>
		   							<c:if test="${tmpShipping==null}"> value="${areaschilds[0].id}"</c:if>
		   						/>
		   						<select id="oriArea">
		   							<c:forEach items="${areas}" var="area">
		   								<option value="${area.id }" 
		   									<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.area!=null&&tmpShipping.oriAddress.area.parent==null&&tmpShipping.oriAddress.area.id==area.id}" > selected</c:if>
		   									<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.area!=null&&tmpShipping.oriAddress.area.parent!=null&&tmpShipping.oriAddress.area.parent.id==area.id}" > selected</c:if>
		   								>${area.name }</option>
		   							</c:forEach>
		   						</select>
		   						${temp.oriAddress.area.parent.id}
		   					</td>
		   				</tr>
		   				
		   				<tr id="oriAreaStatetr" <c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.area!=null&&tmpShipping.oriAddress.area.parent==null}"> style="display:none"</c:if>>
		   					<th><s:message code="quickship.shipping.state" text="State/Province"/>:</th>
		   					<td>
		   						<select id="oriAreaState">
		   							<c:forEach items="${areaschilds}" var="area">
		   								<option value="${area.id }"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.area!=null&&tmpShipping.oriAddress.area.parent!=null&&tmpShipping.oriAddress.area.id==area.id}" > selected</c:if>
		   								>${area.name }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.isSave" text="isSaved"/><input type="checkbox" name="oriIsSaved" id="oriIsSaved"
		   							<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.byName!=null}"> checked</c:if>
		   						/>:
		   					</th>
		   					<td>
		   						<input type="text" name="oriAddress.byName" id="oriByName"
		   							<c:if test="${tmpShipping!=null&&tmpShipping.oriAddress.byName!=null}"> value="${tmpShipping.oriAddress.byName}"</c:if>
			   						<c:if test="${tmpShipping==null||tmpShipping.oriAddress.byName==null}"> disabled="disabled"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				
		   			</table>
	   			
		   			<!-- 收件人信息 -->
		   			<table class="tableInfo">
		   				<tr class="title">   
		   					<th colspan="2"><s:message code="quickship.shipping.Destination" text="Destination"/></th>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.loadaddress" text="Load desAddress"/>:</th>
		   					<td>
		   						<select id="desAddress">
		   							<option value="0">--Select--</option>
		   							<c:forEach items="${addresses}" var="address">
		   								<option value="${address.id}">${address.byName }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.person" text="Person"/>:</th>
		   					<td><input type="text" id="desPerson" name="desAddress.person" class="required" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.person}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.company" text="Company"/>:</th>
		   					<td><input type="text" id="desCompany" name="desAddress.company" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.company}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.phone" text="Phone"/>:</th>
		   					<td><input type="text" id="desPhone" name="desAddress.phone" class="required" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.phone}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.address1" text="Address1"/>:</th>
		   					<td><input type="text" id="desAddress1" name="desAddress.address1" class="required" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.address1}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.address2" text="Address2"/>:</th>
		   					<td><input type="text" id="desAddress2" name="desAddress.address2" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.address2}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.zipCode" text="ZipCode"/>:</th>
		   					<td><input type="text" id="desZipCode" name="desAddress.zipCode" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.zipCode}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.city" text="City"/>:</th>
		   					<td><input type="text" id="desCity" name="desAddress.city" 
		   							<c:if test="${tmpShipping!=null}"> value="${tmpShipping.desAddress.city}"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.area" text="Country"/>:</th>
		   					<td>
		   						<input type="hidden" name="desAreaId" id="desAreaId" 
		   							<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.area!=null}"> value="${tmpShipping.desAddress.area.id}"</c:if>
		   							<c:if test="${tmpShipping==null}"> value="0"</c:if>/>
		   						<select id="desArea">
		   							<c:forEach items="${areas}" var="area">
		   								<option value="${area.id }"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.area!=null&&tmpShipping.desAddress.area.parent==null&&tmpShipping.desAddress.area.id==area.id}" > selected</c:if>
		   									<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.area!=null&&tmpShipping.desAddress.area.parent!=null&&tmpShipping.desAddress.area.parent.id==area.id}" > selected</c:if>
		   								>${area.name }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr id="desAreaStatetr" <c:if test="${tmpShipping!=null&&tmpShipping.desAddress.area!=null&&tmpShipping.desAddress.area.parent==null}"> style="display:none"</c:if>>
		   					<th><s:message code="quickship.shipping.state" text="State/Province"/>:</th>
		   					<td>
		   						<select id="desAreaState">
		   							<option value="0">--Select--</option>
		   							<c:forEach items="${areaschilds}" var="area">
		   								<option value="${area.id }"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.area!=null&&tmpShipping.desAddress.area.parent!=null&&tmpShipping.desAddress.area.id==area.id}" > selected</c:if>
		   								>${area.name }</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.isSave" text="isSaved"/><input type="checkbox" name="desIsSaved" id="desIsSaved"
		   						<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.byName!=null}"> checked</c:if>/>:</th>
		   					<td>
		   						<input type="text" name="desAddress.byName" id="desByName"
			   						<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.byName!=null}"> value="${tmpShipping.desAddress.byName}"</c:if>
			   						<c:if test="${tmpShipping==null||tmpShipping.desAddress.byName==null}"> disabled="disabled"</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.residential" text="isResidential"/>:</th>
		   					<td>
<%--		   						<s:message code="quickship.shipping.residential" text="isResidential"/>--%>
		   						<input type="checkbox" name="desAddress.isResidential" 
		   							<c:if test="${tmpShipping!=null&&tmpShipping.desAddress.isResidential==true}"> checked</c:if>
		   						/>
		   					</td>
		   				</tr>
		   				
		   			</table>
	   				<!-- 包裹信息 -->
	   				<table class="tableInfo">
		   				<tr class="title">   
		   					<th colspan="2"><s:message code="quickship.shipping.package" text="Package"/></th>
		   				</tr>
		   				<tr>   
		   					<th><s:message code="quickship.shipping.serviceType" text="Service Type"/>:</th>
		   					<td>
		   						<select id="servicType" name="serverType">
		   							<c:forEach items="${serverTypes}" var="serverType">
		   								<option value="${serverType}"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.serverType==serverType}"> selected</c:if>
		   								>${serverType}</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   					
		   				</tr>
		   				<tr>
		   					<th><s:message code="quickship.shipping.dropoffType" text="Dropoff Type"/>:</th>
		   					<td>
		   						<select id="dropoffType" name="dropoffType">
		   							<c:forEach items="${dropoffTypes}" var="dropoffType">
		   								<option value="${dropoffType}"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.dropoffType==dropoffType}"> selected</c:if>
		   								>${dropoffType}</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>   
		   					<th><s:message code="quickship.shipping.packageType" text="Package Type"/>:</th>
		   					<td>
		   						<select id="pakageType" name="pakageType">
		   							<c:forEach items="${packageTypes}" var="packageType">
		   								<option value="${packageType}"
		   									<c:if test="${tmpShipping!=null&&tmpShipping.pakageType==packageType}"> selected</c:if>
		   								>${packageType}</option>
		   							</c:forEach>
		   						</select>
		   					</td>
		   				</tr>
		   				<tr>
		   				 	<th style="width:166px"><s:message code="quickship.shipping.totalPackages" text="Total Packages"/>:</th>
		   					<td>
		   						<select id="packageNum">
		   							<option value="1">1</option>
		   							<option value="2">2</option>
		   							<option value="3">3</option>
		   							<option value="4">4</option>
		   							<option value="5">5</option>
		   							<option value="6">6</option>
		   							<option value="7">7</option>
		   							<option value="8">8</option>
		   							<option value="9">9</option>
		   						</select>
		   					</td>
		   				</tr>
		   			</table>
<%-- 		   			<table id="packSize" class="tableInfo" style="display: none">
		   				<tr class="title">   
		   					<th colspan="2"><s:message code="quickship.shipping.size" text="Package Size "/></th>
		   				</tr>
		   				<tr>   
		   					<th><s:message code="quickship.shipping.length" text="Length" />:</th>
		   					<td><input type="text" id="length" name="length" class="required"></td>
		   				</tr>
		   				<tr> 
		   					<th><s:message code="quickship.shipping.width" text="Width" />:</th>  
		   					<td><input type="text" id="width" name="width" class="required"></td>
		   				</tr>
		   				<tr>   
		   					<th><s:message code="quickship.shipping.height" text="Height" />:</th>
		   					<td><input type="text" id="height" name="height" class="required"></td>
		   				</tr>
		   			</table>
		   			--%>
		   			<table id="itemTable" > 
 		   				<tr id="itemTitle">   
 		   					<th><s:message code="quickship.shipping.itemWight" text="Item Wight"/></th> 
 		   					<th><s:message code="quickship.shipping.itemInsured" text="Item Insured"/></th> 
 		   					<th class="tdItemsize"><s:message code="quickship.shipping.size" text="Package Size "/></th> 
 		   				</tr> 
 		   				<tr>    
		   					<td><input type="text" name="shipItems[0].weight" value="1" size="5"/></td> 
 		   					<td><input type="text" name="shipItems[0].insured" value="0" size="5"/></td> 
 		   					<td class="tdItemsize">
 		   						<input type="text" name="shipItems[0].length" value="L" size="3"/>
 		   						<input type="text" name="shipItems[0].width" value="W" size="3"/>
 		   						<input type="text" name="shipItems[0].height" value="H" size="3"/>
 		   					</td> 
 		   				</tr> 
 		   			</table> 
	   			</div>
	   			<strong>
	   				<s:message code="quickship.shipping.savedShipping" text="Save This Shipping Profile"/>
	   				<input type="checkbox" name="isSeaved" id="shippingSaved" <c:if test="${tmpShipping!=null&&tmpShipping.isSeaved==true}"> checked</c:if>>
	   				<input type="text" id="shippingByName" disabled="disabled" name="byName" 
	   					<c:if test="${tmpShipping!=null&&tmpShipping.byName!=null}"> value="${tmpShipping.byName }"</c:if>
	   					<c:if test="${tmpShipping!=null&&(tmpShipping.isSeaved==false||tmpShipping.isSeaved==null)}"> disabled="disabled" </c:if>
	   				/>
	   			</strong>
	   			<br/>	   			
	   			<input type="submit"  class="button" value="<s:message code="quickship.shipping.quote" text="Quote"/>">
		    </form>
    	</div>
    </div >
    <%@include file="/WEB-INF/jsp/public/footer.jsp" %>
  </body>
   <script type="text/javascript">
    	$().ready(function() {
    		var $shippings = $("#shippings");
    		
    		var $oriAddress = $("#oriAddress");
    		var $oriPerson=$("#oriPerson");
    		var $oriCompany=$("#oriCompany");
    		var $oriPhone=$("#oriPhone");
    		var $oriAddress1=$("#oriAddress1");
    		var $oriAddress2=$("#oriAddress2");
    		var $oriZipCode = $("#oriZipCode");
    		var $oriCity=$("#oriCity");
    		var $oriAreaId=$("#oriAreaId");
    		var $oriArea=$("#oriArea");
    		var $oriAreaStatetr=$("#oriAreaStatetr");
    		var $oriAreaState=$("#oriAreaState");
    		var $oriIsSaved=$("#oriIsSaved");
    		var $oriByName=$("#oriByName");
    		
    		
    		var $desAddress = $("#desAddress");
    		
    		var $desPerson=$("#desPerson");
    		var $desCompany=$("#desCompany");
    		var $desPhone=$("#desPhone");
    		var $desAddress1=$("#desAddress1");
    		var $desAddress2=$("#desAddress2");
    		var $desZipCode = $("#desZipCode");
    		var $desCity=$("#desCity");
    		var $desAreaId=$("#desAreaId");
    		var $desArea=$("#desArea");
    		var $desAreaStatetr=$("#desAreaStatetr");
    		var $desAreaState=$("#desAreaState");
    		var $desIsSaved=$("#desIsSaved");
    		var $desByName=$("#desByName");
    		
    		var $serviceType=$("#servicType");
    		var $pakageType=$("#pakageType");
    		var $packageNum=$("#packageNum");
    		var $itemTable=$("#itemTable");
    		var $packSize=$("#packSize");
    		var $shippingSaved=$("#shippingSaved");
    		var $shippingByName=$("#shippingByName");
    		
    		var $length=$("#length");
    		var $height=$("#height");
    		var $width=$("#width");
    		
    		
    		var $shippingForm=$("#shippingForm");
    		$shippingForm.validate();
    		//加载预存发货单
    		$shippings.change(function(){
    			var val = $(this).val();
    			if(val==0){
    				 location.reload();
    				return;
    			}
    			$.get("${basePath}shipping/getShipping.html",{id:val},function(data){
    				setOriAddress(data.oriAddress);
    				setDesAddress(data.desAddress);
    				$serviceType.val(data.serverType);
    				$pakageType.val(data.pakageType);
    				$packageNum.val(data.shipItems.length);    	
    				$("#itemTitle").siblings().remove();
    				var html="";
    				$.each(data.shipItems, function(i,item){
    					html+="<tr><td><input type='text' name='shipItems["+i+"].weight' value='"+item.weight+"' size='5'></td><td><input type='text' name='shipItems["+i+"].insured' value='"+item.insured+"' size='5'></td>"+
    					"<td class='tdItemsize'><input type='text' name='shipItems["+i+"].length' value='"+item.length+"' size='3'/><input type='text' name='shipItems["+i+"].width' value='"+item.width+"' size='3'/><input type='text' name='shipItems["+i+"].height' value='"+item.height+"' size='3'/></td></tr>";
    				});
	    			$itemTable.append(html);
	    			
	    			var $itemTableSizeTr=$(".tdItemsize");
	    			if(data.pakageType=="YOUR_PACKAGING"){
	    				$itemTableSizeTr.show();
	    			//	$length.val(data.length);
	    			//	$height.val(data.height);
	    			//	$width.val(data.width);
	    			//	$packSize.show();
	    			}else{
	    				$itemTableSizeTr.hide();
	    				//$packSize.hide();
	    			}
	    		});
    		});
    		//设置发货人信息
    		function setOriAddress(oriAddress){
    			$oriPerson.val(oriAddress.person);
   				$oriCompany.val(oriAddress.company);
   				$oriPhone.val(oriAddress.phone);
   				$oriAddress1.val(oriAddress.address1);
   				$oriAddress2.val(oriAddress.address2);
   				$oriZipCode.val(oriAddress.zipCode);
   				$oriCity.val(oriAddress.city);
   				var area = oriAddress.area;
   				if(area!=null&&area!=undefined){
   					$oriAreaId.val(area.id);
   					if(area.parent!=null){
   						$oriAreaState.find("option[value='"+area.id+"']").attr("selected",true);
   						$oriArea.find("option[value='"+area.parent.id+"']").attr("selected",true);
   						$oriAreaStatetr.show();
   					}else{
   						$oriArea.find("option[value='"+area.id+"']").attr("selected",true);
   					}
   				}
    		}
    		//设置收货人信息
    		function setDesAddress(desAddress){
    			$desPerson.val(desAddress.person);
   				$desCompany.val(desAddress.company);
   				$desPhone.val(desAddress.phone);
   				$desAddress1.val(desAddress.address1);
   				$desAddress2.val(desAddress.address2);
   				$desZipCode.val(desAddress.zipCode);
   				$desCity.val(desAddress.city);
   				var area = desAddress.area;
   				if(area!=null&&area!=undefined){
   					$desAreaId.val(area.id);
   					if(area.parent!=null){
   						$desAreaState.find("option[value='"+area.id+"']").attr("selected",true);
   						$desArea.find("option[value='"+area.parent.id+"']").attr("selected",true);
   						$desAreaStatetr.show();
   					}else{
   						$desArea.find("option[value='"+area.id+"']").attr("selected",true);
   					}
   				}
    		}
    		
    		//加载发货人信息
    		$oriAddress.change(function(){
    			var val = $(this).val();
    			if(val==0){
    				$shippingForm[0].reset();
    				return;
    			}
    			$.get("${basePath}/shipping/getAddress.html",{id:val},function(data){
    				setOriAddress(data);
    			});
    		});
    		//加载收货人信息
    		$desAddress.change(function(){
    			var val = $(this).val();
    			if(val==0){
    				return;
    			}
    			$.get("${basePath}/shipping/getAddress.html",{id:val},function(data){
    				setDesAddress(data);
    			});
    		});
    		
    		//显示隐藏发货信息别名
    		$oriIsSaved.click(function(){
    			if($(this).attr("checked")=="checked"){
    				$oriByName.attr("disabled",false);
    			}else{
    				$oriByName.attr("disabled",true);
    			}
    		});
    		//显示隐藏收货信息别名
    		$desIsSaved.click(function(){
    			if($(this).attr("checked")=="checked"){
    				$desByName.attr("disabled",false);
    			}else{
    				$desByName.attr("disabled",true);
    			}
    		});
    		//显示隐藏运单别名
    		$shippingSaved.click(function(){
    			if($(this).attr("checked")=="checked"){
    				$shippingByName.attr("disabled",false);
    			}else{
    				$shippingByName.attr("disabled",true);
    			}
    		});
    		
    		$serviceType.change(function(){
    			var serType = $(this).val();
    			if(serType!="FIRST_OVERNIGHT"&&serType!="PRIORITY_OVERNIGHT"&&serType!="STANDARD_OVERNIGHT"&&serType!="FEDEX_2_DAY"&&serType!="FEDEX_2_DAY_AM"&&serType!="FEDEX_EXPRESS_SAVER"){
    				$pakageType.val("YOUR_PACKAGING");
    			}
    			var $itemTableSizeTr=$(".tdItemsize");
    			if($pakageType.val()=="YOUR_PACKAGING"){
    				$itemTableSizeTr.show();
    				$packSize.show();
    			}else{
    				$itemTableSizeTr.hide();
    				$packSize.hide();
    			}
    		});
    		$pakageType.change(function(){
    			var serType = $serviceType.val();
    			if(serType!="FIRST_OVERNIGHT"&&serType!="PRIORITY_OVERNIGHT"&&serType!="STANDARD_OVERNIGHT"&&serType!="FEDEX_2_DAY"&&serType!="FEDEX_2_DAY_AM"&&serType!="FEDEX_EXPRESS_SAVER"){
    				$(this).val("YOUR_PACKAGING");
    			}
    			var $itemTableSizeTr=$(".tdItemsize");
    			if($(this).val()=="YOUR_PACKAGING"){
    				$itemTableSizeTr.show();
    			//	$packSize.show();
    			}else{
    				$itemTableSizeTr.hide();
    			//	$packSize.hide();
    			}
    		});
    		
    		$packageNum.change(function(){
    			$("#itemTitle").siblings().remove();
    			var num=$(this).val();
    			var html="";
    /*			if($pakageType.val()!="YOUR_PACKAGING"){
    				for(var i=0;i<num;i++){
    					html+="<tr><td><input type='text' name='shipItems["+i+"].weight' value='1'></td><td><input type='text' name='shipItems["+i+"].insured' value='0'></td></tr>";
    				}
    			}else{*/
    				for(var i=0;i<num;i++){
    					html+="<tr><td><input type='text' name='shipItems["+i+"].weight' value='1' size='5'></td><td><input type='text' name='shipItems["+i+"].insured' value='0' size='5'></td>"+
    					"<td class='tdItemsize'><input type='text' name='shipItems["+i+"].length' value='L' size='3'/><input type='text' name='shipItems["+i+"].width' value='W' size='3'/><input type='text' name='shipItems["+i+"].height' value='H' size='3'/></td></tr>";
    				}
    //			}
    			$itemTable.append(html);
    			var $itemTableSizeTr=$(".tdItemsize");
    			if($pakageType.val()=="YOUR_PACKAGING"){
    				$itemTableSizeTr.show();
    			}else{
    				$itemTableSizeTr.hide();
    			}
    		});
    		
    		$oriArea.change(function(){
    			findChildArea($(this),$oriAreaState,$oriAreaStatetr,$oriAreaId);
    		});
    		
    		$oriAreaState.change(function(){
    			setAreaId($oriAreaId,$(this).val());
    		});
    		
    		$desArea.change(function(){
    			findChildArea($(this),$desAreaState,$desAreaStatetr,$desAreaId);
    		});
    		
    		$desAreaState.change(function(){
    			setAreaId($desAreaId,$(this).val());
    		});
    			
  			function setAreaId(desobj,val){
  				desobj.val(val);
  			}
  			
  			function findChildArea(paramObj,selectObj,showObj,desObj){
  				if(paramObj.val()==0){
  					setAreaId(desObj,paramObj.val());
  					showObj.hide();
  					return ;
  				}
  				$.get("${basePath}area/areaList.html",{"areaId":paramObj.val()},function(data){
    				if(data!=null&&data.length>0){
    					selectObj.empty();
    					var html = "";
    					$.each(data, function(i,item){
  							html+="<option value="+item.id+">"+item.name+"</option>";
						});
    					selectObj.append(html);
    					setAreaId(desObj,data[0].id);
    					showObj.show();
    				}else{
    					showObj.hide();
    					setAreaId(desObj,paramObj.val());
    				}
    			});
  			}
    		
    	});
    	function check(){
    		var inputs=$(".tdItemsize :input");
    		var type= $("#servicType").val();
    		$.each(inputs, function(i, input){
    			  var val = input.value;
    			  if(val=="H"||val=="L"||val=="W"||val==null||val=="null"){
    				  if(type=="FEDEX_FIRST_FREIGHT"||type=="FEDEX_1_DAY_FREIGHT"||type=="FEDEX_2_DAY_FREIGHT"||type=="FEDEX_3_DAY_FREIGHT"){
    					  alert("This Service Type Package Size Required");
    					  return false;
    				  }else{
    					  input.value="";
    				  }
    				  
    			  }
    		});
    	}
	</script>
</html>

