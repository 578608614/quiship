package com.quickship;

import com.quickship.utils.SpringUtils;



public class Message {
	
	public enum Type{
		success,
		warn,
		error
	}
	private Type type;
	private String content;
	
	public Message(){}
	
	public Message(Type type,String content){
		this.type = type;
		this.content = content;
	}
	
	public Message(Type type,String content,Object...args){
		this.type = type;
		this.content = SpringUtils.getMessage(content,args);
	}
	
	public static Message success(String content,Object... args){
		return new Message(Type.success,content,args);
	}
	/**
	 * ���ؾ�����Ϣ
	 * 
	 * @param content
	 *            ����
	 * @param args
	 *            ����
	 * @return ������Ϣ
	 */
	public static Message warn(String content, Object... args) {
		return new Message(Type.warn, content, args);
	}
	/**
	 * ���ش�����Ϣ
	 * 
	 * @param content
	 *            ����
	 * @param args
	 *            ����
	 * @return ������Ϣ
	 */
	public static Message error(String content, Object... args) {
		return new Message(Type.error, content, args);
	}
	
	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public Type getType() {
		return type;
	}

	/**
	 * ��������
	 * 
	 * @param type
	 *            ����
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * ��ȡ����
	 * 
	 * @return ����
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ��������
	 * 
	 * @param content
	 *            ����
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return SpringUtils.getMessage(content);
	}
	
}
