package com.thinkgem.jeesite.modules.sys.mobile;

import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.mobile.BaseMobileController;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.modules.sys.entity.Menu;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 手机端用户登录
 * Created by ws on 2018/2/26.
 */
@Controller
public class MobileLoginController extends BaseMobileController {
    @Autowired
    private SystemService systemService;

    /**
     * @param request
     * @param response
     * @param loginName
     * @param password
     * @param model     (message:提示信息；
     *                  code：错误代码  200：成功；400：信息错误；500：系统错误
     *                  data：返回的信息 )
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/mobile/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, String loginName,
                        String password, Model model) {
        //User user=systemService.getUserByMobileLoginName(loginName);
        // 打印日志， add by HL
        logger.debug("param_front_loginName: " + loginName);
        // 调用手机端专用的服务类,add by HL
        User user = systemService.getUserByMobileLoginName(loginName);
        if (user == null) {
            model.addAttribute("message", "用户名输入不正确,请重新输入");
            model.addAttribute("code", 400);
//            return renderString(response, model);
            return JsonMapper.toJsonString(model);
        }
        //获取当前时间
        if(user.getExpireDate().before(new Date())){
            model.addAttribute("message", "禁止登录，该账号试用期已到，请联系管理员!");
            model.addAttribute("code", 400);
            return JsonMapper.toJsonString(model);
        }
        // 打印日志， add by HL
        logger.debug("param_background_id: " + user.getId() + ", name: " + user.getName()
                + ", loginName: " + user.getLoginName());
        boolean validatePassword = systemService.validatePassword(password, user.getPassword());
        if (!validatePassword) {
            model.addAttribute("message", "密码输入不正确，请重新输入");
            model.addAttribute("code", 400);
//            return renderString(response, model);
            return JsonMapper.toJsonString(model);
        } else {
            // 获取授权菜单
            List<Menu> menuList = this.systemService.findMobileAllMenu(null, user);
            List<String> permissionList = new ArrayList<String>();
            for (Menu menu : menuList) {
                if (menu.getPermission() != null & !"".equals(menu.getPermission())) {
                    permissionList.add(menu.getPermission());
                }
            }
            // 添加权限列表
            if (permissionList == null || "".equals(permissionList)) {
                model.addAttribute("message", "暂无访问权限");
                model.addAttribute("code", 200);
//                return renderString(response, model);
                return JsonMapper.toJsonString(model);
            }
            // 写入user对象
            user.setPermissionList(permissionList);
            // 打印日志，add by HL
            logger.debug("permissionList: " + permissionList.size());

            // 获取授权角色列表
            List<Role> roleList = systemService.findMobileRoleList(null, user);
//            List<String> roleNameList = new ArrayList<String>();
//            List<String> roleIdList = new ArrayList<String>();

            if (null != roleList && roleList.size() > 0) {
                // 写入角色列表
                user.setRoleList(roleList);
                logger.debug("roleList: " + roleList.size());
                // 写入角色名称
                user.setRoleNames(user.getRoleNames());
                logger.debug("roleNames: " + user.getRoleNames());
            }
//            // 打印日志，add by HL
//            logger.debug("roleList: " + user.getRoleList().size() + ", roleNames: " + user.getRoleNames());

            // 生产随机令牌，压入缓存，并发送给客户端
            String token = UUID.randomUUID().toString();
            user.setAccessToken(token);
            user.setExpiresTime(new Date());
            EhCacheUtils.put(TOKEN_CACHE, token, user);

            // 打印日志，add by HL
//            logger.debug(TOKEN_CACHE + ": " + EhCacheUtils.get(TOKEN_CACHE, token));
            model.addAttribute("message", "登录成功");
            model.addAttribute("code", 200);
            model.addAttribute("data", user);

//            HttpSession session=request.getSession();
//            session.setAttribute("userId",user.getId());
            // 打印日志，add by HL
            logger.debug("返回结果: " + "loginName: " + user.getLoginName());
//            String result = renderString(response, model);
//            return result;
            return JsonMapper.toJsonString(model);
        }

    }

}
