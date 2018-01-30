package controller.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import model.CustomerBean;
import model.CustomerService;

@Controller
public class LoginController {
	@Autowired
	private CustomerService customerService = null;

	@RequestMapping()
	protected String method(String username, String password, HttpSession session) {

		// 接收資料

		// 驗證資料
		Map<String, String> errors = new HashMap<>();
		session.setAttribute("errors", errors);

		if (username == null || username.trim().length() == 0) {
			errors.put("username", "請輸入ID");
		}
		if (password == null || password.trim().length() == 0) {
			errors.put("password", "請輸入PWD");
		}

		if (errors != null && !errors.isEmpty()) {
			// .getRequestDispatcher("/secure/login.jsp").forward(request, response);
			return "secure.login";
		}

		// 呼叫model
		CustomerBean bean = customerService.login(username, password);

		// 根據model執行結果呼叫view元件
		if (bean == null) {
			errors.put("password", "登入失敗");
			// request.getRequestDispatcher("/secure/login.jsp").forward(request, response);
			return "secure.login";

		} else {
			session.setAttribute("user", bean);

			// response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
			// response.setHeader("Location", "/LabWebServletJdbc/index.jsp");

			// response.sendRedirect(path + "/index.jsp");
			// String path =session.getServletContext().getContextPath();
			return "index";
		}
	}
}
