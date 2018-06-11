package com.digiarty.phoneassistant.model.dataparse;

import com.digiarty.phoneassistant.utils.Base64Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/***
 *
 * Created on：2018/5/29
 *
 * Created by：henmory
 *
 * Description:
 *
 *
 **/
class FileHandler implements IHandler{
    private static Logger logger = LoggerFactory.getLogger(FileHandler.class);
    private IAction action;

    public FileHandler() {
        logger.debug("FileHandler 开始处理");
    }

    @Override
    public int parse(byte[] datas) {
        String str = new String(datas, Charset.defaultCharset());
        if (str.contains(CommandKey.KEY_ADD_FILES)) {
            action = new PictureAction();
        }else{
            logger.debug("解析数据失败,无效command");
            return 0;
        }
        logger.debug("收到的PC端数据为 " + str);
        return action.parseCommand(str);
    }

    @Override
    public int doAction() {
        return 1;
    }

    @Override
    public byte[] reply() {
        return new byte[0];
    }

    @Override
    public void setNextReceivedDataType() {

    }



    public static byte[] readPicture(String path){
        BufferedInputStream in = null;
        byte[] content = null;
        try {
            in = new BufferedInputStream(new FileInputStream(path));
            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

            byte[] temp = new byte[1024];
            int size = 0;
            while ((size = in.read(temp)) != -1) {
                out.write(temp, 0, size);
            }
            in.close();
            content = out.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;

    }

    public static void  main(String[] art){
        byte[] image = readPicture("/Users/henmory/Downloads/2.jpg");
        String str = Base64Util.encode(image);
        byte[] imagenew = Base64Util.decode(str.getBytes());
        try {
            FileOutputStream fileOutputStream = new FileOutputStream("/Users/henmory/Downloads/3.jpg");
            fileOutputStream.write(imagenew);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
