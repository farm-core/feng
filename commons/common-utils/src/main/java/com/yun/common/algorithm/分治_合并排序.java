package com.yun.common.algorithm;

/**
 * @ClassName 分治_合并排序
 * @Description TODO
 * @Auther wu_xufeng
 * @Date 2020/11/24
 * @Version 1.0
 */
public class 分治_合并排序 {
    /**
     * 函数说明：在数组被拆分以后进行合并
     */
    static void Merge(int a[], int left, int middle, int right) {
        // 定义左端数组大小
        int n1 = middle - left + 1;
        int n2 = right - middle;

        // 初始化数组，分配内存
        int bejin[] = new int[n1];
        int end[] = new int[n2];

        // 数组赋值
        for (int i = 0; i < n1; i++) {
            bejin[i] = a[left + i];
        }
        for (int i = 0; i < n2; i++) {
            end[i] = a[middle + 1 + i];
        }

        // 用key做原数组索引，每调用一次函数重新给原数组赋一次值
        int i = 0, j = 0, key;
        for (key = left; key <= right; key++) {
            if (n1 > i && n2 > j && i < n1 && bejin[i] <= end[j]) {
                a[key] = bejin[i++];
            } else if (n1 > i && n2 > j && i < n2 && bejin[i] >= end[j]) {
                a[key] = end[j++];
            } else if (i == n1 && j < n2) {
                a[key] = end[j++];
            } else {
                a[key] = bejin[i++];
            }
        }
    }

    /**
     * 差分数组区间，不断分支
     */
    static void MergeSort(int a[], int left, int right) {
        int middle = 0;
        if (left < right) {
            middle = (right + left) / 2;
            MergeSort(a, left, middle);
            MergeSort(a, middle + 1, right);
            Merge(a, left, middle, right);
        }
    }

    public static void main(String[] args) {
        int a[] = {85, 3, 52, 9, 7, 1, 5, 4};
        MergeSort(a, 0, 7);
        for (int i = 0; i < 8; i++) {
            System.out.println(" " + a[i]);
        }
    }
}
