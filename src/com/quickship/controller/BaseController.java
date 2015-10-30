package com.quickship.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.quickship.DateEditor;
import com.quickship.Message;
import com.quickship.utils.SpringUtils;

public class BaseController {
	/** ������ͼ */
	protected static final String ERROR_VIEW ="/error";
	
	/** ������Ϣ */
	protected static final Message ERROR_MESSAGE = Message.error("error");

	/** �ɹ���Ϣ */
	protected static final Message SUCCESS_MESSAGE = Message.success("success");
	
	/** �ɹ���Ϣ */
	protected static final String FLASH_MESSAGE = "flashMessage";

	/** "��֤���"�������� */
	private static final String CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME = "constraintViolations";

	@Resource(name = "validator")
	private Validator validator;
	
	/**
	 * ���ݰ�
	 * 
	 * @param binder
	 *            WebDataBinder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Date.class, new DateEditor(true));
	}
	
	/**
	 * ������֤
	 * 
	 * @param target
	 *            ��֤����
	 * @param groups
	 *            ��֤��
	 * @return ��֤���
	 */
	protected boolean isValid(Object target, Class<?>... groups) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}
	
	/**
	 * ������֤
	 * 
	 * @param type
	 *            ����
	 * @param property
	 *            ����
	 * @param value
	 *            ֵ
	 * @param groups
	 *            ��֤��
	 * @return ��֤���
	 */
	protected boolean isValid(Class<?> type, String property, Object value, Class<?>... groups) {
		Set<?> constraintViolations = validator.validateValue(type, property, value, groups);
		if (constraintViolations.isEmpty()) {
			return true;
		} else {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			requestAttributes.setAttribute(CONSTRAINT_VIOLATIONS_ATTRIBUTE_NAME, constraintViolations, RequestAttributes.SCOPE_REQUEST);
			return false;
		}
	}
	/**
	 * ��ȡ���ʻ���Ϣ
	 * 
	 * @param code
	 *            ����
	 * @param args
	 *            ����
	 * @return ���ʻ���Ϣ
	 */
	protected String message(String code, Object... args) {
		return SpringUtils.getMessage(code, args);
	}

	/**
	 * ���˲ʱ��Ϣ
	 * 
	 * @param redirectAttributes
	 *            RedirectAttributes
	 * @param message
	 *            ��Ϣ
	 */
	protected void addFlashMessage(RedirectAttributes redirectAttributes, Message message) {
		if (redirectAttributes != null && message != null) {
			redirectAttributes.addFlashAttribute(FLASH_MESSAGE, message);
		}
	}
	/**
	 * ����
	 * @param file
	 * @param response
	 */
	protected  void downLoadLable(File file,HttpServletResponse response){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("multipart/form-data");
			response.setHeader("Content-Disposition", "attachment;fileName=" + file.getName());

			bis = new BufferedInputStream(new FileInputStream(file));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048 * 10];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
