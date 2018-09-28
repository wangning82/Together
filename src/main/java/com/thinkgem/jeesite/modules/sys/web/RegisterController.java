package com.thinkgem.jeesite.modules.sys.web;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.dao.RoleDao;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.RegisterService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户注册接口
 *
 * @Author HL
 */
@Controller
public class RegisterController extends BaseController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OfficeDao officeDao;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private RoleDao roleDao;

    /**
     * pc端免费注册接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public String register(HttpServletRequest request, HttpServletResponse response, Model model) {
        // 进行新用户注册一系列操作
//        if (null != register && !"".equals(register)) {
        // 手机号码非空判断
        String mobile = request.getParameter("mobile");
        if (StringUtils.isBlank(mobile)) {
            logger.error("错误，未获取到手机号码。");
            model.addAttribute("result", "1");
            model.addAttribute("message", "注册失败，未获取到手机号码。");
            return JsonMapper.toJsonString(model);
        }
        // 公司名称非空判断
        String company_name = request.getParameter("company_name");
        if (StringUtils.isBlank(company_name)) {
            logger.error("错误，未获取到公司名称。");
            model.addAttribute("result", "1");
            model.addAttribute("message", "注册失败，未获取到公司名称。");
            return JsonMapper.toJsonString(model);
        }
        // 登录密码非空判断
        String password = request.getParameter("password");
        if (StringUtils.isBlank(password)) {
            logger.error("错误，未获取到密码。");
            model.addAttribute("result", "1");
            model.addAttribute("message", "注册失败，未获取到密码。");
            return JsonMapper.toJsonString(model);
        }
        // 获取顶级公司（默认是七米科技）
        Office rootCompany = null;
        Office companyModel = new Office();
        companyModel.setParentIds("0,");
        companyModel.setType("1");
        companyModel.setUseable("1");
        companyModel.setDelFlag("0");
        List<Office> rootCompanyList = officeDao.findByParentIds(companyModel);
        if (null != rootCompanyList && rootCompanyList.size() > 0) {
            rootCompany = rootCompanyList.get(0);
            // 新公司
            Office company = new Office();
            company.setName(company_name);
            company.setParent(rootCompany);
            company.setParentIds("0," + rootCompany.getId() + ",");
            company.setArea(rootCompany.getArea());
            company.setType("1");
            company.setGrade(String.valueOf(rootCompany.getGrade()));
            company.setUseable(Global.YES);
            company.setCreateBy(UserUtils.get("1"));
            company.setUpdateBy(UserUtils.get("1"));
            company.setCreateDate(new Date());
            company.setUpdateDate(new Date());
            officeService.save(company);
            // 默认下级公司
            Office childOffice = null;
            List<Dict> dictList = DictUtils.getDictList("sys_office_common");
            for (int i = 0; i < dictList.size(); i++) {
                childOffice = new Office();
                childOffice.setName(DictUtils.getDictLabel(dictList.get(i).getValue(), "sys_office_common", "未知"));
                childOffice.setParent(company);
                childOffice.setArea(company.getArea());
                childOffice.setType("2");
                childOffice.setGrade(String.valueOf(Integer.valueOf(company.getGrade()) + 1));
                childOffice.setUseable(Global.YES);
                childOffice.setCreateBy(company.getCreateBy());
                childOffice.setUpdateBy(company.getUpdateBy());
                childOffice.setCreateDate(company.getCreateDate());
                childOffice.setUpdateDate(company.getUpdateDate());
                officeService.save(childOffice);
            }

            // "公司领导"部门
            Office office = new Office();
            Office officeModel = new Office();
            officeModel.setName("公司领导");
            officeModel.setParent(company);
            officeModel.setArea(company.getArea());
            officeModel.setType("2");
            officeModel.setUseable(Global.YES);
            officeModel.setDelFlag("0");
            List<Office> officeList = officeService.findOfficeByName(officeModel);
            if (null != officeList && !"".equals(officeList)) {
                for (int i = 0; i < officeList.size(); i++) {
                    if (officeList.get(i).getParent().getId().equals(company.getId())) {
                        office = officeList.get(i);

                    }
                }
            }


            Role roleModel = new Role();
            roleModel.setDelFlag("0");
            // 公司管理员角色
            roleModel.setName("公司管理员");
            Role company_admin = roleDao.getByName(roleModel);
            // 销售经理
            roleModel.setName("销售经理");
            Role xiaoshou = roleDao.getByName(roleModel);
            // 采购经理
            roleModel.setName("采购经理");
            Role caigou = roleDao.getByName(roleModel);
            // 生产经理
            roleModel.setName("生产经理");
            Role shengchan = roleDao.getByName(roleModel);
            // 财务经理
            roleModel.setName("财务经理");
            Role caiwu = roleDao.getByName(roleModel);
            // 评审副组长
            roleModel.setName("评审副组长");
            Role fuzuzhang = roleDao.getByName(roleModel);
            // 评审组长
            roleModel.setName("评审组长");
            Role zuzhang = roleDao.getByName(roleModel);


            // 注册用户(公司管理员)
            User user = new User();
            user.setName(company_name + "管理员");
            user.setLoginName(mobile);
            user.setMobile(mobile);
            user.setPassword(systemService.entryptPassword(password));
            user.setCompany(company);
            user.setOffice(office);
            user.setRole(company_admin);
            List<Role> roleList = new ArrayList<Role>();
            roleList.add(company_admin);
            user.setRoleList(roleList);
            user.setUserType("1");
            user.setLoginFlag("1");
            user.setCreateBy(UserUtils.get("1"));
            user.setUpdateBy(UserUtils.get("1"));
            user.setCreateDate(new Date());
            user.setUpdateDate(new Date());
            // 添加到期时间，默认试用期一个月，add by HL
            Date expireDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 1);
            expireDate = calendar.getTime();
            user.setExpireDate(expireDate);
            systemService.saveUser(user);

            // 注册用户（销售经理）
            user.setId(null);
            user.setName(company_name + "销售经理");
            user.setLoginName(mobile + "xiaoshou");
            user.setRole(xiaoshou);
            List<Role> roleList1 = new ArrayList<Role>();
            roleList1.add(xiaoshou);
            user.setRoleList(roleList1);
            user.setUserType("2");
            systemService.saveUser(user);

            // 注册用户(采购经理)
            user.setId(null);
            user.setName(company_name + "采购经理");
            user.setLoginName(mobile + "caigou");
            user.setRole(caigou);
            List<Role> roleList2 = new ArrayList<Role>();
            roleList2.add(caigou);
            user.setRoleList(roleList2);
            user.setUserType("2");
            systemService.saveUser(user);

            // 注册用户(生产经理)
            user.setId(null);
            user.setName(company_name + "生产经理");
            user.setLoginName(mobile + "shengchan");
            user.setRole(shengchan);
            List<Role> roleList3 = new ArrayList<Role>();
            roleList3.add(shengchan);
            user.setRoleList(roleList3);
            user.setUserType("2");
            systemService.saveUser(user);

            // 注册用户(财务经理)
            user.setId(null);
            user.setName(company_name + "财务经理");
            user.setLoginName(mobile + "caiwu");
            List<Role> roleList4 = new ArrayList<Role>();
            roleList4.add(caiwu);
            user.setRoleList(roleList4);
            user.setUserType("2");
            systemService.saveUser(user);

            // 注册用户(评审副组长)
            user.setId(null);
            user.setName(company_name + "评审副组长");
            user.setLoginName(mobile + "fuzuzhang");
            user.setRole(fuzuzhang);
            List<Role> roleList5 = new ArrayList<Role>();
            roleList5.add(fuzuzhang);
            user.setRoleList(roleList5);
            user.setUserType("2");
            systemService.saveUser(user);

            // 注册用户(评审组长)
            user.setId(null);
            user.setName(company_name + "评审组长");
            user.setLoginName(mobile + "zuzhang");
            user.setRole(zuzhang);
            List<Role> roleList6 = new ArrayList<Role>();
            roleList6.add(zuzhang);
            user.setRoleList(roleList6);
            user.setUserType("2");
            systemService.saveUser(user);
            logger.info("注册成功");
            model.addAttribute("result", "0");
            model.addAttribute("message", "注册成功,正在跳转请稍后...");
            // 直接用jsonMapper转换，add by HL
            String result = JsonMapper.toJsonString(model);
            return result;

        } else {
            logger.error("未获取到公司信息");
            model.addAttribute("result", "1");
            model.addAttribute("message", "注册失败，未获取到顶级公司信息");
            String result = JsonMapper.toJsonString(model);
            return result;
        }

//        } else {
//            logger.error("注册失败，未获取到参数");
//            model.addAttribute("result", "1");
//            model.addAttribute("message", "注册失败，未获取到参数");
//            return renderString(response, model);
//    }

    }


    /**
     * 检测手机号是否已经注册过，暂时没用这个方法
     *
     * @return
     */
    @RequestMapping(value = "/checkMobile", method = RequestMethod.POST)
    public boolean checkMobile(HttpServletRequest request, HttpServletResponse response) {
        String mobile = (String) request.getSession().getAttribute("mobile");
        if (StringUtils.isNotBlank(mobile)) {
            User userModel = new User();
            userModel.setMobile(mobile);
            userModel.setDelFlag("0");
            List<User> userList = userDao.findList(userModel);
            if (null != userList && userList.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检测公司名称是否已经注册过了,暂时没用这个方法
     *
     * @return
     */
    @RequestMapping(value = "/checkCompany", method = RequestMethod.POST)
    public boolean checkCompany_name(HttpServletRequest request, HttpServletResponse response) {
        String company_name = (String) request.getSession().getAttribute("company_name");
        if (StringUtils.isNotBlank(company_name)) {
            Office officeModel = new Office();
            officeModel.setName(company_name);
            officeModel.setDelFlag("0");
            List<Office> officeList = officeDao.findList(officeModel);
            if (null != officeList && officeList.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }


    }

    /**
     * 注册的时候发送短信验证码，目的是确定手机号的正确性,此方法弃用
     *
     * @param request
     * @param response
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendMsgCode", method = RequestMethod.GET)
    public String sendMsgCode(HttpServletRequest request, HttpServletResponse response) {
        String mobile = "1";
        if (StringUtils.isNotBlank(mobile)) {
            //验证码
            String vcode = "";
            for (int i = 0; i < 6; i++) {
                vcode = vcode + (int) (Math.random() * 9);
            }
            System.out.println("短信验证码：" + vcode);
            // 拼接上下文
            String code = "验证码：" + vcode + ",用于在七米科技进销存注册。如非本人操作，请忽略本短信。有效期10分钟。";

            // 发送短信验证码
            registerService.doShot(code, mobile);

            return renderString(response, "短信验证码已经发送，请注意查收！");
        } else {
            return renderString(response, "手机号码无效，请检查输入的手机号码！");
        }
    }

    /**
     * 验证手机验证码是否正确，该方法弃用
     *
     * @param validCode 验证码
     * @param mobile    手机号
     * @return
     * @throws RuntimeException 手机号格式错误出错
     */
    public static boolean validSmsCode(HttpServletRequest request, String validCode, String mobile, String key) {


        //防止 空值
        if (key == null || "".equals(key)) {

            // 默认为登录
            key = "register";
        }

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

//        //新增优化  auth zjp 2016-12-13
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
}
