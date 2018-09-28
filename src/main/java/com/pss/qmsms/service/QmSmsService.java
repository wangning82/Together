/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.pss.qmsms.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import com.pss.utils.TimeUtil;
import com.pss.utils.ZtMD5;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import org.apache.batik.bridge.TextUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.pss.qmsms.entity.QmSms;
import com.pss.qmsms.dao.QmSmsDao;

/**
 * 短信设置Service
 * @author whisky
 * @version 2018-03-05
 */
@Service
@Transactional(readOnly = true)
public class QmSmsService extends CrudService<QmSmsDao, QmSms> {

	@Autowired
		private QmSmsDao qmSmsDao;
	@Autowired
	private SystemService systemService;
	//md5加密插件
	ZtMD5 ztMD5 = new ZtMD5();

	public QmSms get() {
		return super.get("1");
	}

	//更新短信配置信息
	@Transactional(readOnly = false)
	public void update(QmSms qmSms) {
		//选择默认数据
		qmSms.setId("1");
		//更新数据
		super.save(qmSms);
	}

	//发送短信
	@Transactional(readOnly = false)
	public Integer doShot(String smsContext , String smsMobiles){
		QmSms qmSms = super.get("1");
		try {
			// 创建StringBuffer对象用来操作字符串
			StringBuffer sms = new StringBuffer(qmSms.getSmsUrl() + "?" );
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
			sms.append("&content=" + URLEncoder.encode(smsContext + "【七米科技】" ,"UTF-8") );
			//产品ID
			sms.append("&productid=" + qmSms.getSmsProductid());
			//xh扩展的小号,没有为空
			if(!StringUtils.isEmpty(qmSms.getSmsXh()) || qmSms.getSmsXh() == ""){
				sms.append("&xh=" + qmSms.getSmsXh());
			}
			// 创建url对象
			URL url = new URL(sms.toString());
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			url.openStream();
			return 0;
		}catch (Exception e){
			return 1;
		}

	}

	//根据公司id获取需要发送短信的号码
	public String getMobileById(User user) {
//		User userMoble = new User();
//		userMoble.setOffice(user.getOffice());
//		userMoble.setCompany(user.getCompany());
		user.setLoginName(null);
		user.setName(null);
		List<User> mobileList = systemService.findUser(user);
		StringBuffer sb = new StringBuffer("");
		if (mobileList.size() != 0) {
			for (int i = 0; i < mobileList.size(); i++) {
				if (mobileList.get(i).getMobile() != null && mobileList.get(i).getMobile().length() == 11){
					sb.append(mobileList.get(i).getMobile() + ",");
				}
			}
			if(sb.length() != 0){
				sb.deleteCharAt(sb.length() - 1);
			}
		}
		return sb.toString();
	}

	
}