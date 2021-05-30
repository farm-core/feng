package com.yun.auth.service;

import com.yun.bean.admin.YunApi;
import com.yun.bean.admin.YunUser;

import java.util.Set;

public interface YunApiService {
    Set<YunApi> findAll();

    Set<YunApi> queryUserApisByUserId(Integer id);
}
