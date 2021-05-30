package com.yun.test.test;

import java.util.ArrayList;
import java.util.List;
public class CollectionRemoveIfExample {
    private static List<Employee> employeeList = new ArrayList<>();
    public static void main(String[] args) {
        employeeList.add(new Employee("关羽", 45, 7000.00));
        employeeList.add(new Employee("张飞", 25, 10000.00));
        employeeList.add(new Employee("赵云", 65, 8000.00));
        employeeList.add(new Employee("吕布", 22, 12000.00));
        employeeList.add(new Employee("马超", 29, 9000.00));

        employeeList.removeIf(emp -> emp.getAge() >= 30);

        System.out.println("年龄小于30岁的员工：");
        employeeList.forEach(System.out::println);
    }
}