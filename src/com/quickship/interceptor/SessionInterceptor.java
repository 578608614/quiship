package com.quickship.interceptor;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.quickship.entity.Member;

public class SessionInterceptor implements HandlerInterceptor{
	
	public static String[] urls = {"/index","/login","/register","/register","/success","/areaList"};
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object obj, Exception arg3) throws Exception {

	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object obj, ModelAndView arg3) throws Exception {
		
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object obj) throws Exception {
		Member member = (Member)request.getSession().getAttribute("member");
		String uri = request.getRequestURI();
		String temp=null;
		if(uri.endsWith("html")){
			temp = uri.substring(uri.lastIndexOf("/"),uri.length()-5);
		}
		//Ã»ÓÐµÇÂ¼
		if(member==null){
			if(!Arrays.asList(urls).contains(temp)){
				 response.sendRedirect(request.getContextPath()+"/member/login.html?redirect="+uri);
//				 request.getRequestDispatcher("/WEB-INF/jsp/member/login.jsp?redirect="+uri).forward(request, response);
				 return false;
			}
		}else{
			String redirect = request.getParameter("redirect");
			if(redirect!=null){
				String path = request.getContextPath()+redirect;
				request.getRequestDispatcher(path).forward(request, response);
				return false;
			}
		}
		return true;
	}

}
