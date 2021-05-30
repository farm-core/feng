package com.yun.common.algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SortUtle {
    private static final String url = "D:\\算法排序\\";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入一个整数(0-100000]：");
        int n = sc.nextInt();
        while (true) {
            if (n <= 0 || n > 100000) {
                System.out.println("**************************输入不合法，请重新输入：**************************");
                n = sc.nextInt();
            } else {
                break;
            }
        }
        sortTime(n);
    }

    /**
     * 获取排序的结果
     *
     * @param n
     * @return
     */
    private static void sortTime(int n) {
        int[] arr = getRandoms(n);
        Map<String, Long> map = new HashMap<>();

        long insertSort = insertSort(arr);
        long quickSort = quickSort(arr);
        long sortPlus = sortPlus(arr);
        long getSort = getSort(arr);
        long mergeSort = mergeSort(arr);
        long arraysSort = arraysSort(arr);

        map.put("插入排序时间：", insertSort);
        map.put("快速排序法时间：", quickSort);
        map.put("冒泡排序法时间：", sortPlus);
        map.put("选择排序法时间：", getSort);
        map.put("归并排序法时间：", mergeSort);
        map.put("自带工具排序法时间：", arraysSort);

        Map<String, Long> sortMap = mapSortByValueAsc(map);

        System.out.println("\n按照时间快慢排序依次为：");
        int count = 1;
        for (String key : sortMap.keySet()) {
            System.out.println(count + ".".concat(key) + sortMap.get(key) + " ms");
            count++;
        }
    }

    /**
     * 根据value排序
     *
     * @param map
     * @return
     */
    public static Map<String, Long> mapSortByValueAsc(Map<String, Long> map) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Map<String, Long> result = new LinkedHashMap<>();
        list.forEach(
                o -> {
                    result.put(o.getKey(), o.getValue());
                });
        return result;
    }


    /**
     * 获取一个打乱的数组
     *
     * @param n
     * @return
     */
    private static int[] getRandoms(int n) {
        int[] arr = new int[n];
        StringBuilder stringBuilder = new StringBuilder(n);
        System.out.println("********************当前随机出的" + n + "个数为********************");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Random().nextInt(arr.length);
            stringBuilder.append(arr[i]).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("随机数.txt"));
        return arr;
    }

    /**
     * 1.插入排序
     *
     * @return
     */
    private static long insertSort(int[] arr) {
        long time = System.currentTimeMillis();

        if (arr.length >= 2) {
            for (int i = 1; i < arr.length; i++) {
                //挖出一个要用来插入的值,同时位置上留下一个可以存新的值的坑
                int x = arr[i];
                int j = i - 1;
                //在前面有一个或连续多个值比x大的时候,一直循环往前面找,将x插入到这串值前面
                while (j >= 0 && arr[j] > x) {
                    //当arr[j]比x大的时候,将j向后移一位,正好填到坑中
                    arr[j + 1] = arr[j];
                    j--;
                }
                //将x插入到最前面
                arr[j + 1] = x;
            }
        }
        long result = System.currentTimeMillis() - time;
        System.out.println("*********************插入排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("插入排序法.txt"));
        return result;
    }

    /**
     * 2.分治排序法,快速排序法
     *
     * @param arr
     * @return
     */
    private static long quickSort(int[] arr) {
        long time = System.currentTimeMillis();
        quick_sort(arr, 0, arr.length - 1);
        long result = System.currentTimeMillis() - time;
        System.out.println("*********************快速排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("快速排序法.txt"));
        return result;
    }


    private static void quick_sort(int[] arr, int strat, int end) {
        int loc = 0;
        if (strat < end) {
            //6.得到基准数的下标
            loc = partition(arr, strat, end);
            //7.递归调用
            quick_sort(arr, strat, loc - 1);
            quick_sort(arr, loc + 1, end);
        }
    }

    public static int partition(int[] arr, int strat, int end) {
        //1.确定基准数
        int pivot = arr[strat];
        //2.确定结束条件
        while (strat < end) {
            //3.从右向左找小于pivot的数来填arr[strat]
            while (strat < end && arr[end] > pivot) {
                end--;
            }
            if (strat < end) {
                arr[strat] = arr[end];
                strat++;
            }

            //4.从左向右找大于或等于pivot的数来填arr[end]
            while (strat < end && arr[strat] <= pivot) {
                strat++;
            }
            if (strat < end) {
                arr[end] = arr[strat];
                end--;
            }
        }
        //5.退出时，strat等于end。将pivot填到这个坑中
        arr[strat] = pivot;
        return strat;
    }


    /**
     * 3.冒泡排序
     *
     * @param arr
     * @return
     */
    private static long sortPlus(int[] arr) {
        long time = System.currentTimeMillis();
        if (arr != null && arr.length > 1) {
            for (int i = 0; i < arr.length - 1; i++) {
                // 初始化一个布尔值
                boolean flag = true;
                for (int j = 0; j < arr.length - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        // 调换
                        int temp;
                        temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        // 改变flag
                        flag = false;
                    }
                }
                if (flag) {
                    break;
                }
            }
        }
        long result = System.currentTimeMillis() - time;
        System.out.println("*********************冒泡排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("冒泡排序法.txt"));
        return result;
    }

    /**
     * 4.选择排序
     *
     * @return
     */
    private static long getSort(int[] arr) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < arr.length - 1; i++) {
            // 遍历的区间最小的值
            int min = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    // 找到当前遍历区间最小的值的索引
                    min = j;
                }
            }
            if (min != i) {
                // 发生了调换
                int temp = arr[min];
                arr[min] = arr[i];
                arr[i] = temp;
            }
        }

        long result = System.currentTimeMillis() - time;
        System.out.println("*********************选择排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("选择排序法.txt"));
        return result;
    }

    /**
     * 5.归并排序
     *
     * @param arr
     * @return
     */
    private static long mergeSort(int[] arr) {
        long time = System.currentTimeMillis();
        merge(arr, 0, arr.length - 1);
        long result = System.currentTimeMillis() - time;

        System.out.println("*********************归并排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("归并排序法.txt"));
        return result;
    }

    private static void merge(int[] arr, int s, int e) {
        int m = (s + e) / 2;
        if (s < e) {
            merge(arr, s, m);
            merge(arr, m + 1, e);


            //归并
            //初始化一个从起始s到终止e的一个数组
            int[] temp = new int[(e - s) + 1];
            //左起始指针
            int l = s;
            //右起始指针
            int r = m + 1;
            int i = 0;
            //将s-e这段数据在逻辑上一分为二,l-m为一个左边的数组,r-e为一个右边的数组,两边都是有序的
            //从两边的第一个指针开始遍历,将其中小的那个值放在temp数组中
            while (l <= m && r <= e) {
                if (arr[l] < arr[r]) {
                    temp[i++] = arr[l++];
                } else {
                    temp[i++] = arr[r++];
                }
            }

            //将两个数组剩余的数放到temp中
            while (l <= m) {
                temp[i++] = arr[l++];
            }
            while (r <= e) {
                temp[i++] = arr[r++];
            }

            //将temp数组覆盖原数组
            for (int n = 0; n < temp.length; n++) {
                arr[s + n] = temp[n];
            }
        }
    }


    /**
     * 6.自带工具排序
     *
     * @param arr
     */
    private static long arraysSort(int[] arr) {
        long time = System.currentTimeMillis();

        Arrays.sort(arr);

        long result = System.currentTimeMillis() - time;
        System.out.println("*********************自带工具排序法结果***********************");
        StringBuilder stringBuilder = new StringBuilder(arr.length);
        for (int ar : arr) {
            stringBuilder.append(ar).append(" ");
        }
        getPx(stringBuilder);
        writeLocalStrOne(stringBuilder.toString(), url.concat("自带工具排序法.txt"));
        return result;
    }

    /**
     * 文本写入本地
     *
     * @param str
     * @param path
     */
    public static void writeLocalStrOne(String str, String path) {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            if (str != null && !"".equals(str)) {
                // true为追加
                FileWriter fw = new FileWriter(file, false);
                //写入本地文件中
                fw.write(str);
                fw.flush();
                fw.close();
                System.out.println("执行完毕!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void getPx(StringBuilder stringBuilder) {
        // 站址打印控制台的数量避免溢出
        if (stringBuilder.toString().split(" ").length > 100) {
            System.out.println(stringBuilder.toString().substring(0, stringBuilder.toString().indexOf(" ", 100)).concat("..."));
        } else {
            System.out.println(stringBuilder.toString());
        }
    }
}
