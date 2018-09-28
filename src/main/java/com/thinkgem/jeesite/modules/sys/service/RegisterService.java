package com.thinkgem.jeesite.modules.sys.service;

import com.pss.qmsms.entity.QmSms;
import com.pss.qmsms.service.QmSmsService;
import com.pss.utils.TimeUtil;
import com.pss.utils.ZtMD5;
import com.thinkgem.jeesite.common.service.BaseService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 注册用户用的服务类
 *
 * @Author HL
 */
@Service
@Transactional(readOnly = true)
public class RegisterService extends BaseService {
    @Autowired
    QmSmsService qmSmsService;
    //md5加密插件
    ZtMD5 ztMD5 = new ZtMD5();

    /**
     * 发送短信验证码，注册的时候用
     *
     * @param smsContext
     * @param smsMobiles
     * @return
     */

    @Transactional(readOnly = false)
    public Integer doShot(String smsContext, String smsMobiles) {
        QmSms qmSms = qmSmsService.get("1");
        try {
            // 创建StringBuffer对象用来操作字符串
            StringBuffer sms = new StringBuffer(qmSms.getSmsUrl() + "?");
            //用户名
            sms.append("username=" + qmSms.getSmsUsername());
            //时间码
            String tkey = TimeUtil.getNowTime("yyyyMMddHHmmss");
            //密码(加密)
            String password = ztMD5.getMD5(ztMD5.getMD5(qmSms.getSmsPassword()) + tkey);
            sms.append("&password=" + password);
            //时间码
            sms.append("&tkey=" + tkey);
            //要发送的手机号
            sms.append("&mobile=" + smsMobiles);
            // 向StringBuffer追加消息内容转URL标准码
            sms.append("&content=" + URLEncoder.encode(smsContext + "【中银保险】", "UTF-8"));
            System.out.println("发送验证码内容：" + URLEncoder.encode(smsContext + "【中银保险】", "UTF-8"));
            //产品ID
            sms.append("&productid=" + qmSms.getSmsProductid());
            //xh扩展的小号,没有为空
            if (!StringUtils.isEmpty(qmSms.getSmsXh()) || qmSms.getSmsXh() == "") {
                sms.append("&xh=" + qmSms.getSmsXh());
            }
            // 创建url对象
            URL url = new URL(sms.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 发送
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            // 返回发送结果
            String inputline = in.readLine();
            if (!inputline.startsWith("1,")) {
                throw new RuntimeException(inputline);
            } else {
                logger.info("短信网关返回信息：" + inputline);
                return Integer.valueOf(inputline.substring(0, inputline.indexOf(",")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 99代表通用错误
            return 99;
        }

    }
}
