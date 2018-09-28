package com.thinkgem.jeesite.common.servlet;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.RegisterService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.activiti.engine.impl.util.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 注册的时候发送短信验证码用
 *
 * @Author HL
 */
public class SendMsgCodeServlet extends HttpServlet {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private SystemService systemService;

    public SendMsgCodeServlet() {
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

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String mobile = request.getParameter("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            // 先检查手机号是否已经注册过了
//            User user = new User();
//            user.setMobile(mobile);
//            user.setDelFlag("0");
//            List<User> userList = systemService.findUserByMobile(user);
//            if (null != userList && userList.size() > 0) {
//                // 封装JSON
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("code", "1");
//                jsonObject.put("message", "该手机号码已经注册过了，请更换手机号再试！");
//                response.getWriter().print(jsonObject.toString());
//            } else {
            //验证码
            String vcode = "";
            for (int i = 0; i < 6; i++) {
                vcode = vcode + (int) (Math.random() * 9);
            }
            System.out.println("短信验证码：" + vcode);
            // 拼接上下文
            String code = "验证码：" + vcode + ",用于在中银保险微信公众号登录。如非本人操作，请忽略本短信。有效期10分钟。";

            // 发送短信验证码
            int status = registerService.doShot(code, mobile);
            // 封装JSON
            JSONObject jsonObject = new JSONObject();
            switch (status) {
                case 0:
                    jsonObject.put("code", "1");
                    jsonObject.put("message", "验证码发送失败，请稍后再试！");
                    response.getWriter().print(jsonObject.toString());
                    break;
                case 1:
                    // 验证码压入session
                    request.getSession().setAttribute("register" + mobile, vcode);
                    // 验证码发送时间压入session
                    request.getSession().setAttribute("regTime" + mobile, new Date().getTime());
                    jsonObject.put("code", "0");
                    jsonObject.put("message", "短信验证码已经发送，请注意查收！");
                    response.getWriter().print(jsonObject.toString());
                    break;
                default:
                    jsonObject.put("code", "1");
                    jsonObject.put("message", "验证码发送失败，请稍后再试！");
                    response.getWriter().print(jsonObject.toString());
            }


        }

//        }
        else {
            // 封装JSON
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", "2");
            jsonObject.put("message", "该手机号码无效，请检查输入的手机号码！");
            response.getWriter().print(jsonObject);
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        doGet(request, response);
    }
}
