package com.yun.common;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName PictureToByte
 * @Description 图片转base64
 * @Auther wu_xufeng
 * @Date 2020/11/9
 * @Version 1.0
 */
@Slf4j
public class PictureToByte {
    /**
     * 图片转base64
     *
     * @param pictureFilePathAndName 图片位置
     * @return base64
     */
    public static String pictureToByte(String pictureFilePathAndName) {
        log.info("---------------------------------------开始图片转base64-------------------------------------------");
        long start = System.currentTimeMillis();
        try (InputStream in = new FileInputStream(pictureFilePathAndName)) {
            byte[] data = new byte[in.available()];
            in.read(data);
            BASE64Encoder encoder = new BASE64Encoder();
            String encode = encoder.encode(data);
            long end = System.currentTimeMillis();
            log.info("------------------------图片转base64完成，用时：" + (end - start) + "ms--------------------------");
            return encode;
        } catch (Exception e) {
            log.error("----------------------图片转base64失败,异常信息:" + e.getMessage() + "--------------------------");
        }
        return "";
    }
}
