package com.thinkgem.jeesite.modules.sys.mobile;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mobile.BaseMobileController;
import com.thinkgem.jeesite.modules.sys.dao.UserDao;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by ws on 2018/4/26.
 */
@Controller
@RequestMapping(value = "mobile/user")
public class MobileUserController extends BaseMobileController {

    @Autowired
    private SystemService systemService;

    @Autowired
    private UserDao userDao;

    @ResponseBody
    @RequestMapping(value = "list",method = RequestMethod.POST)
    public String userList(HttpServletResponse response, String token, Model model) {
        User user = findUserByToken(token);
        if (user == null) {
            model.addAttribute("message", "令牌失效，请重新登录");
            model.addAttribute("code", 400);
        } else {
//            System.out.println("=======当前用户的公司名称======="+user.getCompany());
            List<User> list = Lists.newArrayList();
            if (user.getRoleNames().contains("系统管理员")) {
                list = this.userDao.findAllList(user);
            } else {
                if (user.getRoleNames().contains("公司管理员")) {
                    // 这个方法不太好,没有通过数据范围筛选
//                    list=this.userDao.findUserByOfficeId(user);
                    user.setLoginName(null);
                    user.setName(null);
                    list = this.userDao.findList(user);
                } else {
                    list = this.userDao.findList(user);
                }

            }

//            if("系统管理员".equals(user.getName())){
//                list=this.userDao.findAllList(user);
//            }else if(user.getName().contains("管理员")){
//                list=this.userDao.findUserByOfficeId(user);
//            }else {
//
//                list=this.userDao.findList(user);
//            }
            List<User> userList = Lists.newArrayList();
            for (User u : list) {
                User u2 = systemService.getUserByLoginName(u.getLoginName());
                userList.add(u2);
            }
//            System.out.println(userList.get(0).getCompany());
            model.addAttribute("message", "查询到" + userList.size() + "条用户信息");
            model.addAttribute("code", 200);
            model.addAttribute("data", userList);
        }
        return JsonMapper.toJsonString(model);
    }
}
