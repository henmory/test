package com.example.log.file;

import java.io.File;

/***
 *
 * Created on：26/04/2018
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
public class FileHelper {


    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        boolean b = dir.mkdirs();
        System.out.println(b);
        if (b) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }



}
