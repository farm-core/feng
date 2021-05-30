package com.yun.upload.download.file;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

/**
 * @ClassName UploadAndDownload
 * @Description 上传下载
 * @Author wxf
 * @Date 2021/2/2 14:11
 * @Version 1.0
 */
@Slf4j
@Component
public class FileUploadAndDownload {


    /**
     * 文件下载
     *
     * @param map
     * @param response
     */
    @SneakyThrows
    private void fileUpload(Map map, HttpServletResponse response) {
        OutputStream out = null;
        BufferedInputStream br = null;
        try {
            // 需要下载文件的地址
            File file = new File(map.get("filePath").toString());
            String fileName = file.getName();
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");

            br = new BufferedInputStream(new FileInputStream(file));
            byte[] buf = new byte[1024];
            int len = 0;
            response.reset(); // 非常重要
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            out = response.getOutputStream();
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            if (br != null) {
                br.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
}
