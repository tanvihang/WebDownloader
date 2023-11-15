package com.example.sys.controller;

import ch.qos.logback.core.util.FileUtil;
import com.example.common.vo.Result;
import com.example.sys.entity.File;
import com.example.sys.entity.Filedownloadmanage;
import com.example.sys.service.IFiledownloadmanageService;
import com.example.utils.MultiThreadedFileDownloader;
import com.example.utils.fileUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author vh
 * @since 2023-07-02
 */
@RestController
@RequestMapping("/api/filedownloadmanage")
public class FiledownloadmanageController {

        private MultiThreadedFileDownloader downloader = new MultiThreadedFileDownloader();
    @Autowired
    private IFiledownloadmanageService iFiledownloadmanageService;
    private boolean flag = false;

    fileUtils fileUtils = new fileUtils();

    @RequestMapping("/select")
    public Result<Filedownloadmanage> select(@RequestParam("json")  String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        var jsonn = json;
        var node = mapper.readTree(jsonn);

        String url = String.valueOf(node.get("url"));
        url = url.replace("\"", "");
        String selected = String.valueOf(node.get("selected"));

        iFiledownloadmanageService.select(url, selected);

        String msg = "File selected: " + selected;

        return Result.success(msg);
    }

    @RequestMapping("/download")
    public Result<List<Filedownloadmanage>> downloadFile(@RequestParam("json")  String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        var jsonn = json;
        var node = mapper.readTree(jsonn);

        List<Filedownloadmanage> fileList = iFiledownloadmanageService.getSelectedFileList();

        List<File> fileInfoList = new ArrayList<>();

        String home = System.getProperty("user.home");

        for (Filedownloadmanage f: fileList
             ) {
            File newFile = iFiledownloadmanageService.getFile(f.getFileUrl());
            fileInfoList.add(newFile);

            //创建文件夹for对应的文件类型 10/7/2023 文件归档
            String filePath = home + "\\Downloads\\" + newFile.getFileType();
            fileUtils.createFileDirectory(filePath);

        }

        JsonNode thread = node.get("thread");
        JsonNode speed = node.get("speed");

        if(fileList.size() > 0){

            //更新文件当前状态
            for (Filedownloadmanage f: fileList
            ) {
                iFiledownloadmanageService.setDownloading(f.getFileUrl());
            }

            //23:04试着做暂停继续
            downloader.clearDownloadTask();
            downloader.addDownloadTask(fileList,fileInfoList, thread.asInt(), speed.asInt());
            downloader.startDownload();


            //更新文件下载成功状态
            for(Filedownloadmanage f: fileList){
                iFiledownloadmanageService.setDownloaded(f.getFileUrl());
                iFiledownloadmanageService.setDownloadingNo(f.getFileUrl());
            }

            //extract Zip File
            //check downloaded and is zip file
            List<File> zipFiles = iFiledownloadmanageService.findZipAndDownloaded();

            System.out.println(zipFiles);

            if(zipFiles.size()>0){
                for (File f: zipFiles
                     ) {
                    String destFilePath = f.getFilePath().replace(f.getFileName(),"");
                    fileUtils.extractZipFile(f.getFilePath(),destFilePath+"\\unzip");
                }
            }

            return Result.success("Files download finish");

        }

        return Result.fail("No file selected");
    }

    @RequestMapping("/pauseDownload")
    public Result<File> pauseDownload(){
        System.out.println(downloader.toString() + " paused");
        downloader.pause();
        return Result.success("Paused");
    }

    @RequestMapping("/resumeDownload")
    public Result<File> resumeDownload(){
        System.out.println(downloader.toString() + " resumed");
        downloader.resume();
        return Result.success("Resumed");
    }

    @RequestMapping("/getCurrentDownloading")
    public Result<String> getCurrentDownloading(){
        String currentDownloadUrl = downloader.getCurrentDownload();

        if(currentDownloadUrl == null){
            return Result.fail("No downloading file");
        }

        return Result.success(currentDownloadUrl,"current have download");
    }

    @RequestMapping("/getDownloadedFile")
    public Result<List<File>> getDownloadedFile(){
        List<Filedownloadmanage> fdmList = iFiledownloadmanageService.getAllDownloadedFile();

        List<File> fileList = new ArrayList<>();

        for (Filedownloadmanage f: fdmList
             ) {
            File myfile = iFiledownloadmanageService.getFile(f.getFileUrl());
            fileList.add(myfile);
        }

        return Result.success(fileList,"Downloaded file list loaded");
    }

    @RequestMapping("/getThreadProcess")
    public Result<List<Integer>> getThreadProcess(){

        if(downloader.getCurrentDownload() == null){
            return Result.fail("No downloading file");
        }
        //return 1,2,3,4 goes to 25%, 50%, 75%, 100%;
        List<Integer> list = downloader.getThreadProcess();

        if(list == null){
            return Result.fail("No downloading file");
        }

        for (Integer i: list
             ) {
            if(i != 0){
                flag = true;
            }
        }

        if(flag){
            flag = false;
            return Result.success(list);
        }else{
            return Result.fail("No downloading file");
        }

    }

    @RequestMapping("/getDownloadSpeed")
    public Result<Double> getDownloadSpeed(){
        if(downloader.getCurrentDownload() == null){
            return Result.fail("No downloading file");
        }

        List<Double> downloadSpeed = downloader.getThreadDownloadSpeedList();
        Double totalDownloadSpeed = Double.valueOf(0);

        if(downloadSpeed == null){
            return Result.fail("No downloading file");
        }

        for (Double d: downloadSpeed
             ) {
            totalDownloadSpeed += d;
        }

        if(totalDownloadSpeed == 0){
            return Result.fail("No downloading file");
        }

        totalDownloadSpeed = Math.round(totalDownloadSpeed * 100) / 100.00;

        return Result.success(totalDownloadSpeed);
    }

}
