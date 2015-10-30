package com.quickship.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.quickship.entity.Shipping;
import com.quickship.entity.Shipping.ReconcileType;
import com.quickship.service.MemberService;
import com.quickship.service.ReconcileShippingService;
import com.quickship.service.ShippingService;
import com.quickship.service.base.BaseDaoImpl;
import com.quickship.utils.HttpClientUtil;

@Service("reconcileShippingServiceImpl")
public class ReconcileShippingServiceImpl extends BaseDaoImpl<Shipping, Long> implements ReconcileShippingService {

	private final String TRACKING_ID_NMAE = "Express or Ground Tracking ID";

	private final String CHARGE_NAME = "Transportation Charge Amount";

	private final String WEIGHT_NAME = "Actual Weight Amount";

	private final String WEIGHT_UNIT_NAME = "Actual Weight Units";

	@Value("${fedex.userName}")
	private String userName;
	@Value("${fedex.userpwd}")
	private String password;
	@Value("${fedex.url.login}")
	private String loginURL;
	@Resource(name = "shippingServiceImpl")
	private ShippingService shippingService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	public void loginFedex() {
		Map<String, String> mapForm = new HashMap<String, String>();
		mapForm.put("appName", "fclfsm");
		mapForm.put("locale", "us_en");
		mapForm.put("step3URL", "https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doRegistration%26locale=en_us%26urlparams=us%26sType=F%26action=fsmregister");
		mapForm.put("afterwardsURL", "https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doEntry%26locale=en_us%26urlparams=us%26sType=F");
		mapForm.put("returnurl", "https%3A%2F%2Fwww.fedex.com%2Fship%2FshipEntryAction.do?method=doEntry%26locale=en_us%26urlparams=us%26sType=F");
		mapForm.put("fromLoginPage", "yes");
		mapForm.put("cc_lang", "us");
		mapForm.put("registrationType", "logon");
		mapForm.put("ssoguest", "n");
		mapForm.put("steps", "2");
		mapForm.put("username", userName);
		mapForm.put("password", password);
		mapForm.put("startpage", "FSM");
		String content = HttpClientUtil.doPost(loginURL, mapForm, "login");
		String cookie = "";
		cookie = HttpClientUtil.checkCookie(cookie, true);
		String aa = "https://www.fedex.com/fedexbillingonline/application.jsp?";
		content = HttpClientUtil.doGet(aa, cookie, false, "login");
		cookie = HttpClientUtil.checkCookie(cookie, true);

		String a2 = "https://www.fedex.com/fedexbillingonline/block/send-receive-updates";
		mapForm.put("ice.submit.partial", "true");
		mapForm.put("ice.event.target", "ice.event.target:mainContentId:advancedSearchwldDataNewSrch");
		mapForm.put("ice.event.captured", "advancedSearchwldDataNewSrch");
		mapForm.put("ice.event.type", "onclick");
		mapForm.put("ice.event.alt", "false");
		mapForm.put("ice.event.ctrl:", "false");
		mapForm.put("ice.event.shift", "false");
		mapForm.put("ice.event.meta", "false");
		mapForm.put("ice.event.x", "840");
		mapForm.put("ice.event.y", "833");
		mapForm.put("ice.event.left", "false");
		mapForm.put("ice.event.right", "false");
		mapForm.put("mainContentId:quickTempl", "-1");
		mapForm.put("mainContentId:j_id204", "All");
		mapForm.put("mainContentId:j_id198", "11/26/2014");
		mapForm.put("mainContentId:j_id196", "10/01/2014");
		mapForm.put("mainContentId:j_id189", "485313443");
		mapForm.put("mainContentId:j_id187", "on");
		mapForm.put("mainContentId:advTempl", "Invoices");
		mapForm.put("mainContentId:j_id176", "-1");
		mapForm.put("javax.faces.ViewState", "1");
		mapForm.put("javax.faces.RenderKitId", "ICEfacesRenderKit");
		mapForm.put("mainContentId", "mainContentId");
		mapForm.put("mainContentId:advancedSearchwldDataNewSrch", "Search");
		mapForm.put("ice.session", "Vm_mjta1fOb5n125DuUq7w");
		mapForm.put("ice.view", "1");
		mapForm.put("ice.focus", "mainContentId:advancedSearchwldDataNewSrch");
		mapForm.put("rand", "0.8442357731983066");
		HttpClientUtil.closeClient();
		content = HttpClientUtil.doPost(a2, mapForm, cookie, "login");
		writeFile(content);
	}

	public File updateShipping(File file) {
		InputStream is = null;
		OutputStream out = null;
		File report = null;
		try {
			is = new FileInputStream(file);
			HSSFWorkbook workbook = new HSSFWorkbook(is);
			// 1.获取第一个Sheet
			HSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet == null) {
				return null;
			}
			Integer idIndex = null, weightIndex = null, weightUnitIndex = null, chargeIndex = null;
			for (int rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
				HSSFRow row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				if (rowNum == 0) {
					for (int i = 0; i <= row.getLastCellNum(); i++) {
						String temp = row.getCell(i).getStringCellValue();
						if (TRACKING_ID_NMAE.equals(temp)) {
							idIndex = i;
						} else if (WEIGHT_NAME.equals(temp)) {
							weightIndex = i;
						} else if (CHARGE_NAME.equals(temp)) {
							chargeIndex = i;
						} else if (WEIGHT_UNIT_NAME.equals(temp)) {
							weightUnitIndex = i;
						}
						if (idIndex != null && weightIndex != null && chargeIndex != null && weightUnitIndex != null) {
							break;
						}
					}
					if (idIndex == null || weightIndex == null || chargeIndex == null || weightUnitIndex == null) {
						return null;
					}
				} else {
					// 3.根据表头索引获取值
					String trackNo = row.getCell(idIndex).getStringCellValue();
					Shipping shipping = shippingService.findByTrackNo(trackNo);
					if (shipping != null && shipping.getReconcileType() == ReconcileType.NOREADY) {
						String shipWeight = row.getCell(weightIndex).getStringCellValue();
						String weightUnit = row.getCell(weightUnitIndex).getStringCellValue();
						String charge = row.getCell(chargeIndex).getStringCellValue();
						if (!StringUtils.isBlank(shipWeight) && !StringUtils.isBlank(weightUnit)) {
							if ("L".equals(weightUnit)) {
								shipping.setFactWeight(new BigDecimal(shipWeight).divide(new BigDecimal(12), 6));
							} else {
								shipping.setFactWeight(new BigDecimal(shipWeight));
							}
						}
						if (!StringUtils.isBlank(charge)) {
							shipping.setFactCharge(new BigDecimal(charge));
						}
						shipping.setReconcileType(ReconcileType.READY);
						shippingService.update(shipping);
					}else{
						HSSFCellStyle style = workbook.createCellStyle();
						style.setFillBackgroundColor(HSSFColor.SKY_BLUE.index);// 设置背景色
						style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);// 设置背景色
						style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
						row.getCell(0).setCellStyle(style);
					}

				}
			}
			report =new File(file.getParent(),"report_"+file.getName());
			out=new FileOutputStream(report);
			workbook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return report;
	}

	public void updateMember() {
		List<Shipping> list = shippingService.findList(ReconcileType.READY);
		if (list != null) {
			for (Shipping shipping : list) {
				if (shipping.getDifferenceCharge().compareTo(new BigDecimal(0)) > 0) {
					memberService.update(shipping.getMember(), -shipping.getDifferenceCharge().longValue());
				}
				shipping.setReconcileType(ReconcileType.COMPLETED);
				shippingService.update(shipping);
			}
		}

	}

	public void writeFile(String content) {
		if (content == null || "".equals(content)) {
			System.out.println("内容为空");
			return;
		}
		Writer r = null;
		try {
			File file = new File("C:/Users/it-1/Desktop/test.html");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			r = new FileWriter(file);
			r.write(content);
			r.flush();
			System.out.println("输出文件完成");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (r != null) {
				try {
					r.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
