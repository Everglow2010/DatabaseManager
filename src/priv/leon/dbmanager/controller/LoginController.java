/**
 * 
 */
package priv.leon.dbmanager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import priv.leon.dbmanager.util.DBUtil;
import priv.leon.dbmanager.util.StringUtils;

/**
 * @author Everglow
 *
 */

@Controller
@RequestMapping({"everglow"})
public class LoginController {
	//登录跳转
	@RequestMapping(value = { "login" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String login() {
		return "system/login";
	}
	
	//登录验证
	@RequestMapping(value = { "loginVaildate" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	public String loginVaildate(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String captcha = request.getParameter("captcha").toLowerCase();

		username = StringEscapeUtils.escapeHtml4(username.trim());

		HttpSession session = request.getSession(true);

		String cap = (String) session.getAttribute("KAPTCHA_SESSION_KEY");

		String message = "";
		HashMap map = new HashMap();

		if ((username == "") || (username == null)) {
			message = "请输入帐号！";
			map.put("error", message);
			request.setAttribute("message", message);
			return "system/login";
		}

		if (!(captcha.equals(cap))) {
			message = "验证码错误！";
			map.put("error", message);
			request.setAttribute("message", message);
			return "system/login";
		}

		List list = new ArrayList();

		String sql = " select * from everglow_users where  username='" + username + "'";

		DBUtil db = new DBUtil();
		
		try {
			list = db.executeQuery(sql);
		} catch (Exception e) {
			list = null;
			e.printStackTrace();
		}

		if (list.size() <= 0) {
			message = "您输入的帐号或密码有误！";
			map.put("error", message);
			request.setAttribute("message", message);
			return "system/login";
		}

		String pas = (String) ((HashMap<String, Object>)list.get(0)).get("password");

		if (!(pas.equals(StringUtils.MD5(password)))) {
			message = "您输入的帐号或密码有误！";
			map.put("error", message);
			request.setAttribute("message", message);
			return "system/login";
		}

		message = "登录成功！";

		session.setAttribute("LOGIN_USER_NAME", username);
		request.setAttribute("username", username);
		//db.initDbConfig();
		return "redirect:/connectpara_input";
	}

}

