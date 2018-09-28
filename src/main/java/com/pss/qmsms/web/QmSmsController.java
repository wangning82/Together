/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.pss.qmsms.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.modules.sys.entity.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.pss.qmsms.entity.QmSms;
import com.pss.qmsms.service.QmSmsService;

/**
 * 短信设置Controller
 * @author whisky
 * @version 2018-03-05
 */
@Controller
@RequestMapping(value = "${adminPath}/qmsms/qmSms")
public class QmSmsController extends BaseController {

	@Autowired
	private QmSmsService qmSmsService;
	
	@ModelAttribute
	public QmSms get(@RequestParam(required=false) String id) {
		QmSms entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = qmSmsService.get(id);
		}
		if (entity == null){
			entity = new QmSms();
		}
		return entity;
	}

	//跳转短信设置界面
	@RequiresPermissions("qmsms:qmSms:view")
	@RequestMapping(value = "form")
	public String form( Model model) {
		QmSms qmSms = qmSmsService.get("1");
		model.addAttribute("qmSms", qmSms);
		return "pss/qmsms/qmSmsForm";
	}


	//跟新短信配置信息
	@RequestMapping(value = "update")
	public String update(QmSms qmSms , RedirectAttributes redirectAttributes){
		qmSmsService.update(qmSms);
		addMessage(redirectAttributes, "保存短信配置成功");
		return "redirect:"+Global.getAdminPath()+"/qmsms/qmSms/form";
	}

	//发送短信
	//返回0成功   返回1失败
	@RequestMapping(value = "doShot")
	public Integer doShot(String smsContext,String smsMobiles){
		return qmSmsService.doShot(smsContext,smsMobiles);

	}

	//创建合同时获取要发送短信的号码
	@RequestMapping(value = "getMobilesById")
	public String getMobileById(User user){
		return qmSmsService.getMobileById(user);
	}


}