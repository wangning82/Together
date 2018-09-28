package com.zybx.zybx.mobile;

import com.thinkgem.jeesite.common.mobile.BaseMobileController;
import com.thinkgem.jeesite.common.persistence.Page;
import com.zybx.zybx.dao.ZybxBalanceDao;
import com.zybx.zybx.entity.ZybxBalance;
import com.zybx.zybx.service.ZybxBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 公众号访问的客户账户余额数据查询接口
 *
 * @Author HL
 */
@Controller
@RequestMapping(value = "/mobile/zybx/zybxBalance")
public class MobileZybxBalanceController extends BaseMobileController {

    @Autowired
    private ZybxBalanceService zybxBalanceService;

    @Autowired
    private ZybxBalanceDao zybxBalanceDao;

    /**
     * 移动端登录接口
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "login")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        return "zybx/zybx/mobileLogin";
    }

    /**
     * 移动端查询客户账户余额
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "list")
    public String register(HttpServletRequest request, HttpServletResponse response,
                           ZybxBalance zybxBalance, Model model) {
        // 身份证号码，姓名不能为空
        if (null != zybxBalance && null != zybxBalance.getIdentityCard() && null != zybxBalance.getName()) {
            List<ZybxBalance> zybxBalanceList = zybxBalanceService.findList(zybxBalance);
            Page<ZybxBalance> page = new Page<ZybxBalance>();
            model.addAttribute("zybxBalanceList", zybxBalanceList);
        } else {
            return "redirect:" + "/mobile/zybx/zybxBalance/login?repage";
        }
        return "zybx/zybx/zybxBalanceList";
    }
}
