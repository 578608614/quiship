package com.quickship.service;

import java.util.Map;

public interface MailService {
	/**
	 * �����ʼ�
	 * 
	 * @param smtpFromMail
	 *            ����������
	 * @param smtpHost
	 *            SMTP��������ַ
	 * @param smtpPort
	 *            SMTP�������˿�
	 * @param smtpUsername
	 *            SMTP�û���
	 * @param smtpPassword
	 *            SMTP����
	 * @param toMail
	 *            �ռ�������
	 * @param subject
	 *            ����
	 * @param templatePath
	 *            ģ��·��
	 * @param model
	 *            ����
	 * @param async
	 *            �Ƿ��첽
	 */
	void send(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);
	
	/**
	 * �����ʼ�
	 * 
	 * @param toMail
	 *            �ռ�������
	 * @param subject
	 *            ����
	 * @param templatePath
	 *            ģ��·��
	 * @param model
	 *            ����
	 * @param async
	 *            �Ƿ��첽
	 */
	void send(String toMail, String subject, String templatePath, Map<String, Object> model, boolean async);

	/**
	 * �����ʼ�(�첽)
	 * 
	 * @param toMail
	 *            �ռ�������
	 * @param subject
	 *            ����
	 * @param templatePath
	 *            ģ��·��
	 * @param model
	 *            ����
	 */
	void send(String toMail, String subject, String templatePath, Map<String, Object> model);

	/**
	 * �����ʼ�(�첽)
	 * 
	 * @param toMail
	 *            �ռ�������
	 * @param subject
	 *            ����
	 * @param templatePath
	 *            ģ��·��
	 */
	void send(String toMail, String subject, String templatePath);

	
	/**
	 * ���Ͳ����ʼ�
	 * 
	 * @param smtpFromMail
	 *            ����������
	 * @param smtpHost
	 *            SMTP��������ַ
	 * @param smtpPort
	 *            SMTP�������˿�
	 * @param smtpUsername
	 *            SMTP�û���
	 * @param smtpPassword
	 *            SMTP����
	 * @param toMail
	 *            �ռ�������
	 */
	void sendTestMail(String smtpFromMail, String smtpHost, Integer smtpPort, String smtpUsername, String smtpPassword, String toMail);
}
