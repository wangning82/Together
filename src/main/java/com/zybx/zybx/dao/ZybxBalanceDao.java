/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.zybx.zybx.dao;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.zybx.zybx.entity.ZybxBalance;

import java.util.List;

/**
 * 中银保险DAO接口
 *
 * @author 中银保险
 * @version 2018-07-30
 */
@MyBatisDao
public interface ZybxBalanceDao extends CrudDao<ZybxBalance> {

    /**
     * 根据登录名称查询用户
     *
     * @param zybxBalance
     * @return
     */
    public List<ZybxBalance> getByIdentityCard(ZybxBalance zybxBalance);

}