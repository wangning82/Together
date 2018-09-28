/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.modules.sys.dao.OfficeDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 机构Service
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

    public List<Office> findAll() {
        return UserUtils.getOfficeList();
    }

    public List<Office> findList(Boolean isAll) {
        if (isAll != null && isAll) {
            return UserUtils.getOfficeAllList();
        } else {
            return UserUtils.getOfficeList();
        }
    }

    @Transactional(readOnly = true)
    public List<Office> findList(Office office) {
        if (office != null && !"".equals(office) && null != office.getParentIds() && !"".equals(office.getParentIds())) {
            office.setParentIds(office.getParentIds() + "%");
            List<Office> list = dao.findByParentIdsLike(office);
            return list;
        }
        // 返回当前用户允许访问的机构列表
        return UserUtils.getOfficeList();
    }

    /**
     * 注册的时候公司名称查重用
     *
     * @param office
     * @return
     */
    @Transactional(readOnly = true)
    public List<Office> findOfficeByName(Office office) {
        if (office != null && !"".equals(office) && null != office.getName() && !"".equals(office.getName())) {
            List<Office> list = dao.findOfficeByName(office);
            return list;
        }
        // 返回当前用户允许访问的机构列表
        return new ArrayList<Office>();
    }

    @Transactional(readOnly = false)
    public void save(Office office) {
        super.save(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

    @Transactional(readOnly = false)
    public void delete(Office office) {
        super.delete(office);
        UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
    }

}