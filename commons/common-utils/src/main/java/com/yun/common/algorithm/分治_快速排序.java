package com.yun.common.algorithm;

/**
 * @ClassName 分治_快速排序
 * @Description TODO
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
public class 分治_快速排序 {
    /**
     * 交换函数，i,j为数组索引
     */
    static void swap(int A[], int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    /**
     * 选取一个关键字(key)作为枢轴，一般取整组记录的第一个数/最后一个，这里采用选取序列最后一个数为枢轴。
     * 设置两个变量left = 0;right = N - 1;
     * 从left一直向后走，直到找到一个大于key的值，right从后至前，直至找到一个小于key的值，然后交换这两个数。
     * 重复第三步，一直往后找，直到left和right相遇，这时将key放置left的位置即可。
     *
     * @return
     */
    static int PartSort(int[] array, int left, int right) {
        //定义基准
        int key = array[right];

        //保存rigth值
        int count = right;

        //防止数组越界
        while (left < right) {
            while (left < right && array[left] <= key) {
                ++left;
            }
            while (left < right && array[right] >= key) {
                --right;
            }
            swap(array, left, right);
        }
        swap(array, right, count);
        return right;
    }

    /**
     * 分治思想，递归调用
     */
    static void QuickSort(int array[], int left, int right) {
        //表示已经完成一个组
        if (left >= right) {
            return;
        }

        //枢轴的位置
        int index = PartSort(array, left, right);
        QuickSort(array, left, index - 1);
        QuickSort(array, index + 1, right);
    }

    public static void main(String[] args) {
        int a[] = {1, 5, -5, 54, 15, 67, 16, 23};
        QuickSort(a, 0, 7);
        for (int i = 0; i < a.length; i++) {
            System.out.print(" " + a[i]);
        }
    }
}
