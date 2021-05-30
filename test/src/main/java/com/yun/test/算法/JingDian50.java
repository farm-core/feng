package com.yun.test.算法;

/**
 * @ClassName JingDian50
 * @Description TODO
 * @Author wxf
 * @Date 2021/2/26 14:25
 * @Version 1.0
 */
public class JingDian50 {
    public static void main(String[] args) {
        System.out.println(test1(20));
    }

    // 兔子的规律为数列1,1,2,3,5,8,13,21....
    public static int test1(int x) {
        if (x == 1 || x == 2) {
            return 1;
        } else {
            return test1(x - 1) + test1(x - 2);
        }
    }
}
