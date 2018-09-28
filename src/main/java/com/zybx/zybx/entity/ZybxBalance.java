/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.zybx.zybx.entity;

import com.thinkgem.jeesite.common.utils.excel.annotation.ExcelField;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 中银保险Entity
 *
 * @author 中银保险
 * @version 2018-07-30
 */
public class ZybxBalance extends DataEntity<ZybxBalance> {

    private static final long serialVersionUID = 1L;
    private String identityCard;        // 身份证号码
    private String name;        // 姓名
    private String mobile;        // 手机号码
    private String formerYearsBalance;        // 往年余额
    private String year2018Balance;        // 2018年余额
    private Date endDate;        // 截止日期

    public ZybxBalance() {
        super();
    }

    public ZybxBalance(String id) {
        super(id);
    }

    public ZybxBalance(String id, String identityCard) {
        super(id);
        this.identityCard = identityCard;
    }

    @Length(min = 1, max = 64, message = "身份证号码长度必须介于 1 和 64 之间")
    @ExcelField(title = "身份证号码", type = 0, align = 2, sort = 10)
    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
    }

    @Length(min = 1, max = 64, message = "姓名长度必须介于 1 和 64 之间")
    @ExcelField(title = "姓名", type = 0, align = 2, sort = 20)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0, max = 64, message = "手机号码长度必须介于 0 和 64 之间")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(min = 0, max = 255, message = "账户余额长度必须介于 0 和 255 之间")
    @ExcelField(title = "账户余额", type = 0, align = 2, sort = 30)
    public String getFormerYearsBalance() {
        return formerYearsBalance;
    }

    public void setFormerYearsBalance(String formerYearsBalance) {
        this.formerYearsBalance = formerYearsBalance;
    }

    @Length(min = 0, max = 255, message = "2018年余额长度必须介于 0 和 255 之间")
    public String getYear2018Balance() {
        return year2018Balance;
    }

    public void setYear2018Balance(String year2018Balance) {
        this.year2018Balance = year2018Balance;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "截止日期不能为空")
    @ExcelField(title = "截止日期", type = 0, align = 2, sort = 40)
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}