/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.pss.qmsms.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 短信设置Entity
 * @author whisky
 * @version 2018-03-05
 */
public class QmSms extends DataEntity<QmSms> {
	
	private static final long serialVersionUID = 1L;
	private String smsId;		// id
	private String smsTitle;		// 短信标题
	private String smsUrl;		// 提交url
	private String smsUsername;		// 用户名
	private String smsPassword;		// 密码
	private String smsTkey;		// 当前时间,如20160315130530
	private String smsMobile;		// 手机号码
	private String smsContent;		// 发送内容
	private String smsProductid;		// 产品id
	private String smsXh;		// xh
	
	public QmSms() {
		super();
	}

	public QmSms(String id){
		super(id);
	}

	@Length(min=0, max=10, message="id长度必须介于 0 和 10 之间")
	public String getSmsId() {
		return smsId;
	}

	public void setSmsId(String smsId) {
		this.smsId = smsId;
	}
	
	@Length(min=0, max=50, message="短信标题长度必须介于 0 和 50 之间")
	public String getSmsTitle() {
		return smsTitle;
	}

	public void setSmsTitle(String smsTitle) {
		this.smsTitle = smsTitle;
	}
	
	@Length(min=0, max=100, message="提交url长度必须介于 0 和 100 之间")
	public String getSmsUrl() {
		return smsUrl;
	}

	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}
	
	@Length(min=0, max=50, message="用户名长度必须介于 0 和 50 之间")
	public String getSmsUsername() {
		return smsUsername;
	}

	public void setSmsUsername(String smsUsername) {
		this.smsUsername = smsUsername;
	}
	
	@Length(min=0, max=50, message="密码长度必须介于 0 和 50 之间")
	public String getSmsPassword() {
		return smsPassword;
	}

	public void setSmsPassword(String smsPassword) {
		this.smsPassword = smsPassword;
	}
	
	@Length(min=0, max=50, message="当前时间,如20160315130530长度必须介于 0 和 50 之间")
	public String getSmsTkey() {
		return smsTkey;
	}

	public void setSmsTkey(String smsTkey) {
		this.smsTkey = smsTkey;
	}
	
	@Length(min=0, max=500, message="手机号码长度必须介于 0 和 500 之间")
	public String getSmsMobile() {
		return smsMobile;
	}

	public void setSmsMobile(String smsMobile) {
		this.smsMobile = smsMobile;
	}
	
	@Length(min=0, max=500, message="发送内容长度必须介于 0 和 500 之间")
	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}
	
	@Length(min=0, max=50, message="产品id长度必须介于 0 和 50 之间")
	public String getSmsProductid() {
		return smsProductid;
	}

	public void setSmsProductid(String smsProductid) {
		this.smsProductid = smsProductid;
	}
	
	@Length(min=0, max=50, message="xh长度必须介于 0 和 50 之间")
	public String getSmsXh() {
		return smsXh;
	}

	public void setSmsXh(String smsXh) {
		this.smsXh = smsXh;
	}
	
}