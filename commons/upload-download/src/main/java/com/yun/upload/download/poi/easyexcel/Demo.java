package com.yun.upload.download.poi.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class Demo {
    //设置表头的名称
    @ExcelProperty("学生编号")
    private  Integer sno;
    @ExcelProperty("学生姓名")
    private  String name;
}