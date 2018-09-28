/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.pss.qmsms.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.pss.qmsms.entity.QmSms;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.util.List;

/**
 * 短信设置DAO接口
 * @author whisky
 * @version 2018-03-05
 */
@MyBatisDao
public interface QmSmsDao extends CrudDao<QmSms> {

    //根据id获取需要发送短信的号码
    public List<String> getMobileById(User user);
	
}