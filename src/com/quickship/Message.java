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
	 * 返回警告消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 警告消息
	 */
	public static Message warn(String content, Object... args) {
		return new Message(Type.warn, content, args);
	}
	/**
	 * 返回错误消息
	 * 
	 * @param content
	 *            内容
	 * @param args
	 *            参数
	 * @return 错误消息
	 */
	public static Message error(String content, Object... args) {
		return new Message(Type.error, content, args);
	}
	
	/**
	 * 获取类型
	 * 
	 * @return 类型
	 */
	public Type getType() {
		return type;
	}

	/**
	 * 设置类型
	 * 
	 * @param type
	 *            类型
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * 获取内容
	 * 
	 * @return 内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置内容
	 * 
	 * @param content
	 *            内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return SpringUtils.getMessage(content);
	}
	
}
