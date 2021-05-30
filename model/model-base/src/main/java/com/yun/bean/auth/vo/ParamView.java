package com.yun.bean.auth.vo;

import com.yun.bean.auth.base.CompareMode;
import com.yun.bean.auth.base.ParameterType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * 参数视图类
 * @author wxf
 * @date 2019/5/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ParamView extends BaseVo {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 接口ID
     */
    private Long apiId;
    /**
     * 接口方法URL
     */
    private String apiUrl;
    /**
     *  参数名
     */
    private String paramName;
    /**
     *  参数序号
     */
    private int index;
    /**
     *  参数限制类型
     *  @see CompareMode
     */
    private String compareMode = CompareMode.BETWEEN;
    /**
     *  参数数值类型 (Time) (数值)（字符串）
     *  @see ParameterType
     */
    private int paramType = ParameterType.NUMBER;
    /**
     * 参数是否允许为空
     */
    private Boolean enableBlank = false;
    /**
     * 参数大值
     */
    private Double maxValue;
    /**
     * 参数小值
     */
    private Double minValue;
    /**
     * 参数范围
     */
    private String allowValues;

    /**
     * 参数是否生效
     */
    private Boolean enabled = true;

    public boolean compare(String value)  {
        switch (paramType) {
            case ParameterType.NUMBER:
                switch (compareMode) {
                    case CompareMode.GE:
                        return Double.parseDouble(value) >= minValue;
                    case CompareMode.LE:
                        return Double.parseDouble(value) <= maxValue;
                    case CompareMode.BETWEEN:
                        return Double.parseDouble(value) >= minValue && Double.parseDouble(value) <= maxValue;
                    default:
                        return false;
                }
            case ParameterType.STRING:
                String[] values = value.split(",");
                String[] allowv = allowValues.split(",");
                return Arrays.asList(allowv).containsAll(Arrays.asList(values));
            case ParameterType.TIME:
                SimpleDateFormat fm = new SimpleDateFormat("yyyyMM");
                SimpleDateFormat fd = new SimpleDateFormat("yyyyMMdd");
                SimpleDateFormat fh = new SimpleDateFormat("yyyyMMddhh");
                SimpleDateFormat ff = new SimpleDateFormat("yyyyMMddhhmm");
                SimpleDateFormat fs = new SimpleDateFormat("yyyyMMddhhmmss");
                long time;
                try {
                    if (value.length() == 6) {
                        time = fm.parse(value).getTime();
                    } else if (value.length() == 8) {
                        time = fd.parse(value).getTime();
                    }else if (value.length() == 10) {
                        time = fh.parse(value).getTime();
                    }else if (value.length() == 12) {
                        time = ff.parse(value).getTime();
                    } else if (value.length() == 14) {
                        time = fs.parse(value).getTime();
                    } else {
                        time = fs.parse(value).getTime();
                    }

                } catch (ParseException e) {
                    return false;
                }
                switch (compareMode) {
                    case CompareMode.GE:
                        return time >= minValue;
                    case CompareMode.LE:
                        return time <= maxValue;
                    case CompareMode.BETWEEN:
                        return time >= minValue && time <= maxValue;
                    default:
                        return false;
                }
            default:
                return false;
        }

    }
}
