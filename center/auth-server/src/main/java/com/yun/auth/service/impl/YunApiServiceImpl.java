package com.yun.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.auth.service.YunApiService;
import com.yun.bean.admin.YunApi;
import com.yun.bean.admin.YunUserApi;
import com.yun.bean.base.BasePO;
import com.yun.idb.mapper.YunApiMapper;
import com.yun.idb.mapper.YunUserApiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName ApiServiceImpl
 * @Description TODO
 * @Auther wu_xufeng
 * @Date 2020/12/20
 * @Version 1.0
 */
@Service
public class YunApiServiceImpl implements YunApiService {

    @Autowired
    private YunApiMapper yunApiMapper;
    @Autowired
    private YunUserApiMapper yunUserApiMapper;

    @Override
    public Set<YunApi> findAll() {
        return new HashSet<>(yunApiMapper.selectList(new QueryWrapper<>()));
    }

    @Override
    public Set<YunApi> queryUserApisByUserId(Integer id) {
        QueryWrapper<YunUserApi> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        List<YunUserApi> yunUserApis = yunUserApiMapper.selectList(queryWrapper);
        List<YunApi> yunApis = yunApiMapper.selectList(new QueryWrapper<>());
        Map<Integer, YunApi> apiMap = yunApis.stream().collect(Collectors.toMap(BasePO::getId, Function.identity()));

        Set<YunApi> collect = yunUserApis.stream().map(u -> {
            YunApi apiEntity = apiMap.get(u.getApiId());
            if (apiEntity != null) {
                apiEntity.setParameters(u.getParamIds());
            }
            return apiEntity;
        }).collect(Collectors.toSet());

        Iterator<YunApi> it = collect.iterator();
        for (int i = 0; i < collect.size(); i++) {
            if (it.next() == null) {
                it.remove();
                i--;
            }
        }
        return collect;
    }
}
