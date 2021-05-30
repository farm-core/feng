package com.yun.admin.context;

import com.yun.bean.admin.YunUser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 用户信息上下文
 */
@Component
public class UserContext {
//    private static final ThreadLocal<YunUser> userThreadLocal = new ThreadLocal<>();

    private ThreadLocal<YunUser> userThreadLocal;

    private UserContext() {
        this.userThreadLocal = new ThreadLocal<>();
    }

    /**
     * 创建实例
     *
     * @return
     */
    public static UserContext getInstance() {
        return SingletonHolder.USER_CONTEXT_HOLDER;
    }

    /**
     * 静态内部类单例模式
     * 单例初使化
     */
    private static class SingletonHolder {
        private static final UserContext USER_CONTEXT_HOLDER = new UserContext();
    }

    public void set(YunUser yunUser) {
        userThreadLocal.set(yunUser);
    }

    public YunUser get() {
        return userThreadLocal.get();
    }

    /**
     * 获取上下文中的用户名
     *
     * @return
     */
    public String getUserName() {
        return Optional.ofNullable(userThreadLocal.get()).orElse(new YunUser()).getUserName();
    }

    /**
     * 清空上下文
     */
    public void remove() {
        userThreadLocal.remove();
    }
}
