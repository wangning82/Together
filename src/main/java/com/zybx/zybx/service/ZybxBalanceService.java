/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.zybx.zybx.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.zybx.zybx.entity.ZybxBalance;
import com.zybx.zybx.dao.ZybxBalanceDao;

/**
 * 中银保险Service
 *
 * @author 中银保险
 * @version 2018-07-30
 */
@Service
@Transactional(readOnly = true)
public class ZybxBalanceService extends CrudService<ZybxBalanceDao, ZybxBalance> {

    @Autowired
    private ZybxBalanceDao zybxBalanceDao;

    public ZybxBalance get(String id) {
        return super.get(id);
    }

    public List<ZybxBalance> findList(ZybxBalance zybxBalance) {
        return super.findList(zybxBalance);
    }

    public Page<ZybxBalance> findPage(Page<ZybxBalance> page, ZybxBalance zybxBalance) {
        return super.findPage(page, zybxBalance);
    }

    @Transactional(readOnly = false)
    public void save(ZybxBalance zybxBalance) {
        super.save(zybxBalance);
    }

    @Transactional(readOnly = false)
    public void delete(ZybxBalance zybxBalance) {
        super.delete(zybxBalance);
    }

    public List<ZybxBalance> getByIdentityCard(String identityCard) {
        return zybxBalanceDao.getByIdentityCard(new ZybxBalance(null, identityCard));
    }

}