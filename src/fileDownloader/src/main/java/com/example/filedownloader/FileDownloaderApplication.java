package com.example.filedownloader;

import com.example.sys.controller.FileController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.*.mapper")
//@MapperScan("com.example.*")
//@ComponentScan(basePackageClasses = FileController.class)
@ComponentScan("com.example.*")
public class FileDownloaderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileDownloaderApplication.class, args);
    }

}
