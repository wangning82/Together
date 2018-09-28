/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.servlet;

import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 验证手机号是否已注册
 *
 * @author HL
 */
@SuppressWarnings("serial")
public class ValidateMobileServlet extends HttpServlet {
    @Autowired
    private SystemService systemService;

    public static final String MOBILE = "mobile";

    public ValidateMobileServlet() {
        super();
    }

    public void destroy() {
        super.destroy();
    }

    /**
     * 可以在servlet的初始化方法中调用spring的方法进行注入
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    public boolean validate(HttpServletRequest request, User user) {
        if (StringUtils.isNotBlank(user.getMobile())) {
            user.setDelFlag("0");
            List<User> userList = systemService.findUserByMobile(user);
            if (null != userList && userList.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String mobile = request.getParameter(MOBILE); // AJAX验证，成功返回true
        if (StringUtils.isNotBlank(mobile)) {
            User user = new User();
            user.setMobile(mobile);
            user.setDelFlag("0");
            response.getOutputStream().print(validate(request, user) ? "false" : "true");
        } else {
            response.getOutputStream().print("true");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 调用doGet方法
        doGet(request, response);
    }

}
