package com.bitcademy.mysite.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuestbookController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String actionName = request.getParameter("a");

		if("delete".equals(actionName)) {
			String sno = request.getParameter("no");
			Long no = Long.parseLong(sno);
			String password = request.getParameter("password");
			
			new GuestbookDao().deleteByNoAndPassword(no, password);
			MvcUtil.redirect(request.getContextPath() + "/guestbook", request, response);	
		} else if("deleteform".equals(actionName)) {
			MvcUtil.forward("guestbook/deleteform", request, response);
		} else if("add".equals(actionName)) {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String message = request.getParameter("message");
			
			GuestbookVo vo = new GuestbookVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setMessage(message);
			
			new GuestbookDao().insert(vo);
			response.sendRedirect(request.getContextPath() + "/guestbook");		
		} else {
			List<GuestbookVo> list = new GuestbookDao().findAll();
		
			request.setAttribute("list", list);
			MvcUtil.forward("guestbook/list", request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
