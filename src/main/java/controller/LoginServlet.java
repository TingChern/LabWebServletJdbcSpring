package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import model.CustomerBean;
import model.CustomerService;

public class LoginServlet extends HttpServlet {
	private CustomerService customerService = null;
	@Override
	public void init() throws ServletException {
		ApplicationContext context = (ApplicationContext) this.getServletContext().getAttribute(
				WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		customerService = (CustomerService) context.getBean("customerService");
	}
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

//接收資料
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
//驗證資料
		Map<String, String> errors = new HashMap<>();
		request.setAttribute("errors", errors);

		if(username==null || username.trim().length()==0) {
			errors.put("username", "請輸入ID");
		}
		if(password==null || password.trim().length()==0) {
			errors.put("password", "請輸入PWD");
		}
		
		if(errors!=null && !errors.isEmpty()) {
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
			return;
		}
		
//呼叫model
		CustomerBean bean = customerService.login(username, password);
		
//根據model執行結果呼叫view元件
		if(bean==null) {
			errors.put("password", "登入失敗");
			request.getRequestDispatcher(
					"/secure/login.jsp").forward(request, response);
		} else {
			HttpSession session = request.getSession();
			session.setAttribute("user", bean);

//			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
//			response.setHeader("Location", "/LabWebServletJdbc/index.jsp");
			
			String path = request.getContextPath();
			response.sendRedirect(path+"/index.jsp");
		}
	}
	@Override
	protected void doPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		this.doGet(req, resp);
	}
}
