/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.sys.dao;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.sys.entity.Office;

import java.util.List;

/**
 * 机构DAO接口
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {

    /**
     * 根据名称查找公司列表，注册的时候查重用
     *
     * @param office
     * @return
     */
    public List<Office> findOfficeByName(Office office);

    /**
     * 根据上级ID查找，id要完全匹配而不是like
     * @param company
     * @return
     */
    public List<Office> findByParentIds(Office company);

    /**
     * 根据名称和上级ID查找
     * @param officeModel
     * @return
     */
    public List<Office> findByNameAndParent(Office officeModel);
}
