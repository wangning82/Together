package com.thinkgem.jeesite.common.servlet;

import com.thinkgem.jeesite.common.config.Global;
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
import java.util.Date;

/**
 * 验证短信验证码是否正确
 *
 * @Author HL
 */
public class ValidateMsgCodeServlet extends HttpServlet {
    @Autowired
    private SystemService systemService;

    public static final String MSGCODE = "msgCode";
    public static final String MOBILE = "mobile";

    public ValidateMsgCodeServlet() {
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

    public boolean validate(HttpServletRequest request, String validCode, String mobile) {
        //防止 空值

        // 默认为登录
        String key = "register";

        // 如果验证码为空
        if (validCode == null || "".equals(validCode)) {
            return false;
        }
        String code = (String) request.getSession().getAttribute(key + mobile);

        // 验证码为空
        if (code == null) {
            return false;
        } else {

            // 忽略大小写 判断  不正确
            if (!code.equalsIgnoreCase(validCode)) {
                return false;
            }
        }

//        //验证短信是否超时
        Long sendtime = (Long) request.getSession().getAttribute("regTime" + mobile);
        Long checktime = (new Date()).getTime();
        //验证session当中是否存在当前注册用户的验证码
        if (sendtime == null) {
            return false;
        }
        if ((checktime - sendtime >= Long.parseLong(Global.getSmsCodeTimeout()))) {
            throw new RuntimeException("验证码超时");
        }
        //验证通过后  去除session信息
        request.getSession().removeAttribute(key + mobile);
        return true;
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String msgCode = request.getParameter(MSGCODE); // AJAX验证，成功返回true
        String mobile = request.getParameter(MOBILE);
        if (StringUtils.isNotBlank(msgCode)) {

            response.getOutputStream().print(validate(request, msgCode, mobile) ? "true" : "false");
        } else {
            response.getOutputStream().print("false");
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 调用doGet方法
        doGet(request, response);
    }
}
