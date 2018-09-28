/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.zybx.zybx.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.beanvalidator.BeanValidators;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.zybx.zybx.entity.ZybxBalance;
import com.zybx.zybx.service.ZybxBalanceService;

import java.util.Date;
import java.util.List;

/**
 * 中银保险Controller
 *
 * @author 中银保险
 * @version 2018-07-30
 */
@Controller
@RequestMapping(value = "${adminPath}/zybx/zybxBalance")
public class ZybxBalanceController extends BaseController {

    @Autowired
    private ZybxBalanceService zybxBalanceService;

    @ModelAttribute
    public ZybxBalance get(@RequestParam(required = false) String id) {
        ZybxBalance entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = zybxBalanceService.get(id);
        }
        if (entity == null) {
            entity = new ZybxBalance();
        }
        return entity;
    }

    @RequiresPermissions("zybx:zybxBalance:view")
    @RequestMapping(value = {"list", ""})
    public String list(ZybxBalance zybxBalance, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<ZybxBalance> page = zybxBalanceService.findPage(new Page<ZybxBalance>(request, response), zybxBalance);
        model.addAttribute("page", page);
        return "zybx/zybx/zybxBalanceList";
    }

    @RequiresPermissions("zybx:zybxBalance:view")
    @RequestMapping(value = "form")
    public String form(ZybxBalance zybxBalance, Model model) {
        model.addAttribute("zybxBalance", zybxBalance);
        return "zybx/zybx/zybxBalanceForm";
    }

    @RequiresPermissions("zybx:zybxBalance:edit")
    @RequestMapping(value = "save")
    public String save(ZybxBalance zybxBalance, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, zybxBalance)) {
            return form(zybxBalance, model);
        }
        zybxBalanceService.save(zybxBalance);
        addMessage(redirectAttributes, "保存中银保险客户余额成功");
        return "redirect:" + Global.getAdminPath() + "/zybx/zybxBalance/?repage";
    }

    @RequiresPermissions("zybx:zybxBalance:edit")
    @RequestMapping(value = "delete")
    public String delete(ZybxBalance zybxBalance, RedirectAttributes redirectAttributes) {
        zybxBalanceService.delete(zybxBalance);
        addMessage(redirectAttributes, "删除中银保险客户余额成功");
        return "redirect:" + Global.getAdminPath() + "/zybx/zybxBalance/?repage";
    }

    /**
     * 导入客户余额数据
     *
     * @param file
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("zybx:zybxBalance:edit")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        if (Global.isDemoMode()) {
            addMessage(redirectAttributes, "演示模式，不允许操作！");
            return "redirect:" + adminPath + "/zybx/zybxBalance/list?repage";
        }
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<ZybxBalance> list = ei.getDataList(ZybxBalance.class);
            for (ZybxBalance balance : list) {
                try {
                    if ("true".equals(checkIdentityCard("", balance.getIdentityCard()))) {
                        // 如果是新数据（该身份证号码未填写过信息），add by HL
                        BeanValidators.validateWithException(validator, balance);
                        zybxBalanceService.save(balance);
                        successNum++;
                    } else {
                        // 如果已存在该身份证号码的信息，覆盖掉原纪录（软删除旧纪录），add by HL
                        List<ZybxBalance> oldZybxBalanceList = zybxBalanceService.getByIdentityCard(balance.getIdentityCard());
                        for (int i = 0; i < oldZybxBalanceList.size(); i++) {
                            zybxBalanceService.delete(oldZybxBalanceList.get(i));
                        }

                        // 验证数据
                        BeanValidators.validateWithException(validator, balance);
                        // 保存数据
                        zybxBalanceService.save(balance);
//                        failureMsg.append("<br/>身份证号码： " + balance.getIdentityCard() + " 已存在，将覆盖原数据; ");
                        successNum++;
                    }
                } catch (ConstraintViolationException ex) {
                    failureMsg.append("<br/>身份证号码： " + balance.getIdentityCard() + " 导入失败：");
                    List<String> messageList = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
                    for (String message : messageList) {
                        failureMsg.append(message + "; ");
                        failureNum++;
                    }
                } catch (Exception ex) {
                    failureMsg.append("<br/>身份证号码： " + balance.getIdentityCard() + " 导入失败：" + ex.getMessage());
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条客户余额数据，导入信息如下：");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条客户余额数据" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入客户余额数据失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/zybx/zybxBalance/list?repage";
    }

    /**
     * 下载导入客户余额数据模板
     *
     * @param response
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("zybx:zybxBalance:view")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "客户余额数据导入模板.xlsx";
            List<ZybxBalance> list = Lists.newArrayList();
            // 生成一条测试数据, add by HL
            ZybxBalance balance = new ZybxBalance();
            balance.setIdentityCard("123456789123456789");
            balance.setName("测试客户");
            balance.setFormerYearsBalance("1200.55");
            balance.setEndDate(new Date());
            list.add(balance);
            new ExportExcel("客户余额数据", ZybxBalance.class, 2).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + adminPath + "/zybx/zybxBalance/list?repage";
    }

    /**
     * 验证身份证号码是否是新的号码
     *
     * @param oldIdentityCard
     * @param identityCard
     * @return
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkIdentityCard")
    public String checkIdentityCard(String oldIdentityCard, String identityCard) {
        if (null != identityCard && !"".equals(identityCard)) {
            List<ZybxBalance> zybxBalanceList = zybxBalanceService.getByIdentityCard(identityCard);
            // 如果新卡号和旧卡号相同
            if (oldIdentityCard.equals(identityCard)) {
                return "false";
            } else if (null != zybxBalanceList && zybxBalanceList.size() > 0) {
                return "false";
            } else {
                return "true";
            }
        } else {
            return "false";
        }
    }

}