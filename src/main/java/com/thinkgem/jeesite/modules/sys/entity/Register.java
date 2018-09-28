package com.thinkgem.jeesite.modules.sys.entity;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 免费注册实体类
 */
public class Register extends DataEntity<Register> {
    private static final long serialVersionUID = 1L;
    private String mobile;    // 手机
    private String company_name;    // 公司名称
    private String password;// 密码

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
