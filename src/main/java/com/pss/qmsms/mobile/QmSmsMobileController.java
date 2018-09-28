package com.pss.qmsms.mobile;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.RegisterService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by ws on 2018/4/2.
 */

@Controller
@RequestMapping(value = "mobile/qmSms")
public class QmSmsMobileController extends BaseController{

    @Autowired
    private RegisterService registerService;
    @Autowired
    private SystemService systemService;


    /**
     * 发送短信验证码
     * @param mobile
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "doShot",method = RequestMethod.POST)
    public String  doShot(String mobile, HttpServletRequest request, HttpServletResponse response){
        Map map=new HashMap();
        String dynamicCode=this.getVerifyCode();
        if(!isMobile(mobile)){
            map.put("message","手机号码格式不正确");
            map.put("code",400);
//            return renderString(response,map);
            return JsonMapper.toJsonString(map);
        }
        User user=new User();
        user.setMobile(mobile);
        if(systemService.findUserByMobile(user).size()>0){
            map.put("message","该手机号码已注册，请更换手机号码");
            map.put("code",400);
            return JsonMapper.toJsonString(map);
        }
        String smsContent="验证码：" + dynamicCode + ",用于在七米科技进销存平台注册账号 。如非本人操作，请忽略本短信，有效期10分钟。";
//        Integer result=qmSmsService.doShot(smsContent,smsMobile);
//        if(result==1){
//            map.put("message","短信发送失败,请重试");
//            map.put("code",500);
//        }else {
//            map.put("message","短信发送成功");
//            map.put("code",400);
//            map.put("data",dynamicCode);
//        }
        this.registerService.doShot(smsContent,mobile);
        map.put("message","短信验证码已经发送，请注意查收！");
        map.put("data",dynamicCode);
        map.put("code",200);
//        return renderString(response,map);
        return JsonMapper.toJsonString(map);

    }

    /**
     * 获取4位随机生成的短信验证码
     * @return
     */
    public static String getVerifyCode() {
        String[] verifyString = new String[] { "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9" };
        Random random = new Random(System.currentTimeMillis());
        StringBuilder verifyBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int rd = random.nextInt(10);
            verifyBuilder.append(verifyString[rd]);
        }
        String verifyCode = verifyBuilder.toString();
        return verifyCode;
    }


    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {

        // 非空校验
        if (mobile == null) {
            return false;
        }

        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 正则表达式：验证手机号
     */
    public static final String REGEX_MOBILE = "^1[3|4|5|7|8|9]\\d{9}$";
}
