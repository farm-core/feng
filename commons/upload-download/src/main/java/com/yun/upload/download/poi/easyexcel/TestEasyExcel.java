package com.yun.upload.download.poi.easyexcel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现excel写的操作,生成excel的内容
        //1.设置写入文件夹地址和excel文件名称
        String filename = "E:\\write.xlsx";
        //2.调用easyEXcel里面的方法实现写操作
        //第一个参数是文件路径名称,第二个是参数实体类
        EasyExcel.write(filename,Demo.class).sheet("学生列表").doWrite(getData());
    }
    
    
    //创建方法使其返回list集合
    private static List<Demo> getData(){
        ArrayList<Demo> list = new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Demo demo = new Demo();
            demo.setSno(i);
            demo.setName("qiuzhikang"+i);
            list.add(demo);
        }
        return list;
    }
}