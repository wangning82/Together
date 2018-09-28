package com.thinkgem.jeesite.common.mobile;

import com.thinkgem.jeesite.common.utils.EhCacheUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by HL.
 */
public class BaseMobileController extends BaseController {

    protected int expires = 7200; // 凭证有效时间（半小时），单位：秒

    protected static final String TOKEN_CACHE = "tokenCache"; // 接口令牌缓存

    /**
     * 根据令牌获取用户
     *
     * @param token
     * @return
     */
    protected User findUserByToken(String token) {
        User user = (User) EhCacheUtils.get(TOKEN_CACHE, token);
        if (user != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getExpiresTime());
            calendar.add(Calendar.SECOND, expires);

            Calendar now = Calendar.getInstance();
            now.setTime(new Date());

            if (calendar.before(now)) {
                EhCacheUtils.remove(TOKEN_CACHE, token);
                return null;
            } else {
                return (User) EhCacheUtils.get(TOKEN_CACHE, token);
            }
        } else {
            return null;
        }
    }


}
