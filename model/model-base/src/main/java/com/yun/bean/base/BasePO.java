package com.yun.bean.base;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * 基础po
 *
 * @param <T>
 */
@Data
@Slf4j
public class BasePO<T extends BaseVO> implements Serializable {

    public final static String DEFAULT_USERNAME = "system";

    private Integer id;

    private String createdBy = DEFAULT_USERNAME;

    private String updatedBy = DEFAULT_USERNAME;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedTime = Date.from(ZonedDateTime.now().toInstant());

    /**
     * 是否删除 逻辑删除标识
     */
    @TableLogic
    private Integer deleted = 0;

    /**
     * 数据来源
     */
    public T toVo(Class<T> clazz) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Po NewInstance Error");
        }
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
