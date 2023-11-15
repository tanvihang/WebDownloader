package com.example.sys.controller;

import com.example.common.vo.Result;
import com.example.sys.entity.File;
import com.example.sys.service.IFileService;
import com.example.sys.service.IFiledownloadmanageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author vh
 * @since 2023-07-01
 */
@RestController
@RequestMapping("/api/file")
public class FileController {
    @Autowired
    private IFileService fileService;

    @RequestMapping("/add")
    public Result<Map<String,Object>> addFile(@RequestParam("url") String fileURL) throws MalformedURLException {

        Map<String,Object> data = fileService.addFile(fileURL);

        if(data != null){
            return Result.success(data);
        }

        return Result.fail("URL invalid or duplicated");
    }

    @RequestMapping("/hasFile")
    public Result<File> hasFile(@RequestParam("url") String fileUrl){
        //找文件，如果存在返回
        File file = fileService.hasFile(fileUrl);

        if(file != null){
            return Result.success(file,"File exists");
        }

        return Result.fail("File not exists");
    }

    @RequestMapping("/allFile")
    public Result<List<File>> allFile(){
        List<File> fileList = fileService.allFile();

        if(fileList != null){
            return Result.success(fileList);
        }

        return Result.fail("No File in database");
    }

    @RequestMapping("/deleteFile")
    public Result<File> deleteFile(@RequestParam("url") String url){
        File file = fileService.hasFile(url);

        if(fileService.deleteFile(url)){
            return Result.success(file,"File delete successfully");
        }

        return Result.fail("File delete failed");
    }

}
