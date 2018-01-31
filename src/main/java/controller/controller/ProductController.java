package controller.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import model.ProductBean;
import model.ProductService;

@Controller
public class ProductController {
	@Autowired
	private ProductService productService;
	private SimpleDateFormat simpleDateFormat;

	@RequestMapping(path = "/product.controller")
	public String method(
			Model model, 
			ProductBean bean, 
			BindingResult bindingResult, 
			String name, 
			String prodaction,
			@RequestParam("id") String temp1, 
			@RequestParam("price") String temp2,
			@RequestParam("make") String temp3,
			@RequestParam("expire") String temp4) {

		// 接收資料
		Locale locale = LocaleContextHolder.getLocale();
		// 轉換資料
		Map<String, String> errors = new HashMap<>();
		model.addAttribute("errors", errors);
		int id = 0;
		if (temp1 != null && temp1.length() != 0) {
			try {
				id = Integer.parseInt(temp1);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("xxx1", "Id必須是整數");
			}
		}
		double price = 0;
		if (temp2 != null && temp2.length() != 0) {
			try {
				price = Double.parseDouble(temp2);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("xxx2", "Price必須是數字");
			}
		}
		java.util.Date make = null;
		if (temp3 != null && temp3.length() != 0) {
			try {
				make = simpleDateFormat.parse(temp3);
			} catch (ParseException e) {
				e.printStackTrace();
				errors.put("xxx3", "Make必須是日期，並且符合YYYY-MM-DD的格式");
			}
		}
		int expire = 0;
		if (temp4 != null && temp4.length() != 0) {
			try {
				expire = Integer.parseInt(temp4);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				errors.put("xxx4", "Expire必須是整數");
			}
		}
		// 驗證資料
		if ("Insert".equals(prodaction) || "Update".equals(prodaction) || "Delete".equals(prodaction)) {
			if (temp1 == null || temp1.length() == 0) {
				errors.put("xxx1", "請輸入Id以便執行" + prodaction);
			}
		}
		if (errors != null && !errors.isEmpty()) {
			return "product.fail";
		}

		// 呼叫model

		// 根據model執行結果呼叫view元件
		if ("Select".equals(prodaction)) {
			List<ProductBean> result = productService.select(bean);
			model.addAttribute("select", result);
			return "product.success";
		} else if ("Insert".equals(prodaction)) {
			ProductBean result = productService.insert(bean);
			if (result == null) {
				errors.put("action", "Insert fail");
			} else {
				model.addAttribute("insert", result);
			}
			return "product.fail";
		} else if ("Update".equals(prodaction)) {
			ProductBean result = productService.update(bean);
			if (result == null) {
				errors.put("action", "Update fail");
			} else {
				model.addAttribute("update", result);
			}
			return "product.fail";
		} else if ("Delete".equals(prodaction)) {
			boolean result = productService.delete(bean);
			if (!result) {
				model.addAttribute("delete", 0);
			} else {
				model.addAttribute("delete", 1);
			}
			return "product.fail";
		} else {
			errors.put("action", "Unknown Action:" + prodaction);
			return "product.fail";

		}
	}
}
