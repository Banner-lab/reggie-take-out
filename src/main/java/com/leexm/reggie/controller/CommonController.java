package com.leexm.reggie.controller;

import com.leexm.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * @ClassName CommonController
 * @Description TODO
 * @Author leexm
 * @Version 1.0
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Value("${reggie.path}")
    private String filePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        log.info("文件内容: {}",file.toString());

        //随机生成文件名
        String fileName = UUID.randomUUID().toString();
        //文件后缀名
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        File basePath = new File(filePath);

        if(!basePath.exists()){
            basePath.mkdirs();
        }

        try {
            file.transferTo(new File(filePath+fileName+suffix));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName+suffix);
    }

    /**
     * 文件下载
     * @param name
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fis = new FileInputStream(new File(filePath + name));

            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];

            response.setContentType("image/jpeg");

            while((len = fis.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
