package com.quickship.controller;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickship.Message;
import com.quickship.service.ReconcileShippingService;

@Controller
@RequestMapping("/reconcileShipping")
public class ReconcileShippingController extends BaseController{
	
	private static final String PATH = "/WEB-INF/file";
	
	@Resource(name="reconcileShippingServiceImpl")
	private ReconcileShippingService reconcileService;
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String add(){
		return "/reconcile/reconcile";
	}
	
	@RequestMapping(value="/test")
	public String test(){
		reconcileService.loginFedex();
		return null;
	}
	@RequestMapping(value="/upload")
	public String upload(MultipartFile multipartFile,HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
		
		//1.验证格式
		String[] types = new String[]{"csv","xls","xlsx"};
		if(!FilenameUtils.isExtension(multipartFile.getOriginalFilename(),types)){
			addFlashMessage(redirectAttributes, Message.error("type must csv,xls,xlsx "));
			return "redirect:/reconcileShipping/add.html";
		}
		//2.上传
		String path = request.getSession().getServletContext().getRealPath(PATH);
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		String fileName = new Date().getTime()+"_"+multipartFile.getOriginalFilename();
		try {
			File file = new File(path,fileName);
			multipartFile.transferTo(file);
			File report =reconcileService.updateShipping(file);
			downLoadLable(report, response);
		} catch (Exception e) {
			addFlashMessage(redirectAttributes, Message.error("upload error"));
			return "redirect:/reconcileShipping/add.html";
		} 
		return null;
	}
	
//	@RequestMapping("/reconcile")
//	public void reconcile(HttpServletRequest request,HttpServletResponse response,RedirectAttributes redirectAttributes){
//		String path = request.getSession().getServletContext().getRealPath(PATH);
//		
//		File dir = new File(path);
//		if(dir.exists()){
//			List<File> files = Arrays.asList(dir.listFiles());
//			Collections.sort(files, new TimeComparator());
//			if(files.size()>0){
//				File file = files.get(0);
//				File report =reconcileService.updateShipping(file);
//				downLoadLable(report, response);
//			}
//		}
//	}
	
	@RequestMapping("/reconcile")
	public String updateMemeber(){
		reconcileService.updateMember();
		return "redirect:/shipping/income.html";
	}
	
//	private class TimeComparator implements Comparator<File>{
//		public int compare(File o1, File o2) {
//			if(o1.lastModified()>o2.lastModified()) return -1;
//			return 1;
//		}
//	}
}
