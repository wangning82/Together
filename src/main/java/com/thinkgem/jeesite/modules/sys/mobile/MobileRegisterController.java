package com.thinkgem.jeesite.modules.sys.mobile;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mobile.BaseMobileController;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.web.RegisterController;
import org.h2.util.New;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 移动端免费注册用户
 * Created by ws on 2018/4/17.
 */
@Controller
@RequestMapping(value = "/mobile/register")
public class MobileRegisterController extends BaseMobileController {
    @Autowired
    private RegisterController registerController;
    @Autowired
    private SystemService systemService;
    @Autowired
    private OfficeDao officeDao;

    /**
     * 移动端免费注册用户
     * @param request
     * @param response
     * @param model
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "register",method = RequestMethod.POST)
    public String register( HttpServletRequest request,HttpServletResponse response,String company_name,String mobile,String password, Model model){
        String companyMsg= this.validateCompany(company_name);
        if(companyMsg!=null){
            model.addAttribute("message",companyMsg);
            model.addAttribute("result",400);
//            return renderString(response,model);
            return JsonMapper.toJsonString(model);
        }
        String mobileMsg=this.validateMobile( mobile );
        if(mobileMsg!=null){
            model.addAttribute("message",mobileMsg);
            model.addAttribute("result",400);
//            return renderString(response,model);
            return JsonMapper.toJsonString(model);
        }
        String pwdMsg=this.validatePwd( password );
        if(pwdMsg!=null){
            model.addAttribute("message",pwdMsg);
            model.addAttribute("result",400);
//            return renderString(response,model);
            return JsonMapper.toJsonString(model);
        }
        return registerController.register(request,response,model);
    }

    /**
     * 验证公司名称是否已注册
     * @param company_name
     * @return
     */

    public String validateCompany( String company_name){
        if(company_name==null || "".equals(company_name)){
            return "注册失败，请输入您的公司名称";
        }
        Office company=new Office();
        company.setName(company_name);
        company.setDelFlag("0");
        List<Office> officeList = officeDao.findOfficeByName(company);
        if(officeList!=null && officeList.size()>0){
            return "注册失败，该公司已注册";
        }
        return null;

    }

    /**
     * 验证手机号码是否已注册
     * @param mobile
     * @return
     */

    public String validateMobile( String mobile){
        if(mobile==null || "".equals(mobile)){
            return "注册失败，请输入您的手机号码";
        }
        String REGEX_MOBILE = "^1[3|4|5|7|8|9]\\d{9}$";
        if(!Pattern.matches(REGEX_MOBILE, mobile)){
            return "注册失败，请正确填写您的手机号码!";
        }
        User user=new User();
        user.setMobile(mobile);
        user.setDelFlag("0");
        List<User> userList = systemService.findUserByMobile(user);
        if(userList!=null && userList.size()>0){
            return "注册失败，该手机号码已注册";
        }
        return null;
    }

    /**
     * 验证密码
     * @param password
     * @return
     */
    public String validatePwd( String password ){
        if(password==null || "".equals(password)){
            return "注册失败，请设置登录密码";
        }
        System.out.println(""+password.length());
        if(password.length()<6 || password.length()>20){
            return "注册失败，密码长度应在6-20位之间";
        }
        return null;
    }

}
