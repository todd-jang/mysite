package com.bitcademy.mysite.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bitcademy.mysite.dao.UserDao;
import com.bitcademy.mysite.vo.UserVo;

public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		
		String actionName = request.getParameter("a");
		if("joinform".equals(actionName)) {
			request
				.getRequestDispatcher("/WEB-INF/views/user/joinform.jsp")
				.forward(request, response);
		} else if("joinsuccess".equals(actionName)) {
			request
				.getRequestDispatcher("/WEB-INF/views/user/joinsuccess.jsp")
				.forward(request, response);
		} else if("join".equals(actionName)) {
			String name = request.getParameter("name");
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setEmail(email);
			vo.setPassword(password);
			vo.setGender(gender);
			
			new UserDao().insert(vo);
			
			response.sendRedirect(request.getContextPath() + "/user?a=joinsuccess");
		} else if("loginform".equals(actionName)) {
			request
				.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp")
				.forward(request, response);
		} else if("login".equals(actionName)) {
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			
			UserVo authUser = new UserDao().findByEmailAndPassword(email, password);
			if(authUser == null) {
				// 인증 실패
				// response.sendRedirect(request.getContextPath() + "/user?a=loginform&result=fail");
				request.setAttribute("result", "fail");
				request
					.getRequestDispatcher("/WEB-INF/views/user/loginform.jsp")
					.forward(request, response);				
				return;
			}
			
			// 로그인 처리
			HttpSession session = request.getSession(true);
			session.setAttribute("authUser", authUser);
			
			response.sendRedirect(request.getContextPath());
		} else if("logout".equals(actionName)) {
			HttpSession session = request.getSession();
			
			session.removeAttribute("authUser");
			session.invalidate();
			
			response.sendRedirect(request.getContextPath());
		} else if("updateform".equals(actionName)) {
			// Access Control
			///////////////////////////////////////////////////////////
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			///////////////////////////////////////////////////////////
			
			UserVo userVo = new UserDao().findByNo(authUser.getNo());
			request.setAttribute("userVo", userVo);
			
			request
				.getRequestDispatcher("/WEB-INF/views/user/updateform.jsp")
				.forward(request, response);				
		} else if("update".equals(actionName)) {
			// Access Control(보안, 인증체크)
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo)session.getAttribute("authUser");
			if(authUser == null) {
				response.sendRedirect(request.getContextPath());
				return;
			}
			//////////////////////////////////////////////////////
			
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String gender = request.getParameter("gender");
			
			UserVo vo = new UserVo();
			vo.setName(name);
			vo.setPassword(password);
			vo.setGender(gender);
			vo.setNo(authUser.getNo());
	
			new UserDao().update(vo);
			authUser.setName(name);
			
			response.sendRedirect(request.getContextPath() + "/user?a=updateform");
		} 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
