package com.yun.bean.base.context;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.bean.base.BasePO;
import com.yun.bean.base.Result;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 通用请求controller,强制全部数据集controller继承此类
 *
 * @param <T>
 * @param <M>
 * @param <N>
 */
@Data
@SuppressWarnings("unchecked")
public abstract class AbstractBaseController<T extends BasePO, M extends BaseMapper<T>, N extends ServiceImpl<M, T>> {

    /**
     * 获取serviceImpl实例
     *
     * @return ServiceImpl
     * @author wxf
     */
    protected abstract N getBaseService();

    /**
     * 新增
     */
    @PostMapping("add")
    public Result add(@RequestBody T t) {
        getBaseService().save(t);
        return Result.success();
    }

    /**
     * 批量新增
     */
    @PostMapping("addBatch")
    public Result add(@RequestBody List<T> list) {
        getBaseService().saveBatch(list, list.size());
        return Result.success();
    }

    /**
     * @param row 数据集主键
     * @return Result
     * @author wxf
     */
    @DeleteMapping("remove")
    public Result delete(@RequestParam(value = "row") String row) {
        getBaseService().removeById(row);
        return Result.success();
    }

    /**
     * 通过主键id批量删除 参数必须为数组，名为ids
     *
     * @param rows 主键集合
     * @return Result
     * @author wxf
     */
    @DeleteMapping("deletes")
    public Result batchDelete(@RequestParam("rows[]") Integer[] rows) {
        boolean removed = getBaseService().removeByIds(Arrays.asList(rows));
        if (removed) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 根据主键更新
     *
     * @param t 包含主键实例
     * @param t
     * @return
     */
    @PutMapping("updateById")
    public Result updateById(@RequestBody T t) {
        boolean updated = getBaseService().updateById(t);
        if (updated) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 查询一个
     *
     * @param map 查询条件
     * @return Result
     * @author zhanggk
     * @date 2019/11/24 15:26
     */
    @GetMapping("get")
    public Result getOne(@RequestBody Map map) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(map);
        T t = getBaseService().getOne(wrapper);
        if (null == t) {
            return Result.fail();
        }
        return Result.success(t);
    }

    /**
     * 附带检索条件的分页查询
     *
     * @param map     查询条件
     * @param current 当前页码
     * @param size    页面条数
     * @return Result
     */
    @GetMapping("page")
    public Result getListByPage(@RequestBody Map map, @RequestParam("current") int current, @RequestParam("size") int size) {
        IPage<T> page = new Page<>(current, size);
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        wrapper.allEq(map);
        IPage<T> iPage = getBaseService().page(page, wrapper);
        return Result.success(iPage);
    }

    /**
     * 从thread local获取网络上下文
     */
    public HttpServletRequest getServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes;
        if (requestAttributes instanceof ServletRequestAttributes) {
            servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获取当前客户端session对象
     */
    public HttpSession getSession() {
        return getServletRequest().getSession();
    }
}