package com.thinkgem.jeesite.common.servlet;

import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
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
 * 验证公司名称是否已注册
 *
 * @author HL
 */
@SuppressWarnings("serial")
public class ValidateCompanyServlet extends HttpServlet {
    @Autowired
    private OfficeService officeService;

    public static final String COMPANY_NAME = "company_name";

    public ValidateCompanyServlet() {
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

    public boolean validate(HttpServletRequest request, Office company) {
        if (StringUtils.isNotBlank(company.getName())) {
            company.setDelFlag("0");
            List<Office> companyList = officeService.findOfficeByName(company);
            if (null != companyList && companyList.size() > 0) {
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
        String company_name = request.getParameter(COMPANY_NAME); // AJAX验证，成功返回true
        if (StringUtils.isNotBlank(company_name)) {
            Office companyModel = new Office();
            companyModel.setName(company_name);
            companyModel.setDelFlag("0");
            response.getOutputStream().print(validate(request, companyModel) ? "false" : "true");
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
