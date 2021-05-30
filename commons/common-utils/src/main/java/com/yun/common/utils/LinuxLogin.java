package com.yun.common.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.SFTPv3DirectoryEntry;
import ch.ethz.ssh2.Session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wxf
 */
public class LinuxLogin {
    public static Connection conn = null;
    private static String ip = "10.110.18.214";
    private static String user = "clear";
    private static String password = "3clear";

    public boolean login() {
        //创建远程连接，默认连接端口为22，如果不使用默认，可以使用方法
        //new Connection(ip, port)创建对象
        conn = new Connection(ip);
        try {
            //连接远程服务器
            conn.connect();
            //使用用户名和密码登录
            return conn.authenticateWithPassword(user, password);
        } catch (IOException e) {
            System.err.printf("用户%s密码%s登录服务器%s失败！", user, password, ip);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 在远程LINUX服务器上，在指定目录下，获取文件各个属性
     *
     * @param[in] conn Conncetion对象
     * @param[in] remotePath 远程主机的指定目录
     */
    public static void getFileProperties(Connection conn, String remotePath) {
        long l = System.currentTimeMillis();
        SFTPv3Client sft = null;
        try {
            sft = new SFTPv3Client(conn);
            long end = (System.currentTimeMillis()) - l;
            System.out.println("连接时间：" + end);
            List<SFTPv3DirectoryEntry> v = sft.ls(remotePath);
            end = System.currentTimeMillis() - l - end;
            System.out.println("读取时间：" + end);
            for (SFTPv3DirectoryEntry s : v) {
                //文件名
                String filename = s.filename;
                System.out.println(filename);
                //文件的大小
                Long fileSize = s.attributes.size;
                if (filename.endsWith(".BIN")) {
                    System.out.println(filename);
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            sft.close();
        }
    }


    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                files.add(tempList[i].toString());
                //文件名，不包含路径
                String fileName = tempList[i].getName();
                System.out.println(fileName);
            }
            if (tempList[i].isDirectory()) {
                //这里就不递归了，
            }
        }
        return files;
    }

    public static void main(String[] args) throws IOException {
        getFiles("/Users/hmy/idea");

        LinuxLogin log = new LinuxLogin();
        System.out.println(log.login());
        Session session = conn.openSession();
        session.execCommand("mkdir /home/clear/fileTx");
        String path = "/mnt/data1/ftp/ftpdata/OBS_DOM_AWS/CMA";
        getFileProperties(conn, path);
    }
}