package com.yun.common.algorithm;

import java.util.Arrays;

/**
 * @author wxf
 * @ClassName Greedy
 * @Description 贪心算法
 * @date 2021/1/14 10:58
 * @Version 1.0
 */
public class GreedyUtil {

    public static void main(String[] args) {
        greedy();
    }

    /**
     * 贪心算法1：钱币找零问题
     */
    public static void greedy() {
        //面额
        int[] values = {1, 2, 5, 10, 20, 50, 100};
        //数量
        int[] counts = {3, 3, 2, 1, 1, 3, 3};
        //获取需要各种面值多少张
        int[] result = getNumber1(446, values, counts);
        System.out.println("各币值的数量：" + Arrays.toString(result));
    }

    /**
     * 贪心算法1：钱币找零问题
     *
     * @param sum
     * @param values
     * @param counts
     * @return
     */
    public static int[] getNumber1(int sum, int[] values, int[] counts) {
        int[] result = new int[7];
        //当前凑的金额
        int add = 0;
        for (int i = values.length - 1; i >= 0; i--) {
            int num = (sum - add) / values[i];
            if (num > counts[i]) {
                num = counts[i];
            }
            add = add + num * values[i];
            result[i] = num;
        }
        return result;
    }
}
