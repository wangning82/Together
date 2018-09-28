package com.thinkgem.jeesite.modules.sys.service;

import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 系统退出
 *
 * @Author HL
 */
@Service
public class SystemLogoutFilter extends LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(SystemLogoutFilter.class);

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        // 在这里清除缓存
        UserUtils.clearCache();
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        //try/catch added for SHIRO-298:
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        issueRedirect(request, response, redirectUrl);
        return false;
    }
}
