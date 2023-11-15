package com.example.sys.service.impl;

import com.example.sys.entity.File;
import com.example.sys.mapper.FileMapper;
import com.example.sys.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sys.service.IFiledownloadmanageService;
import com.example.utils.fileUtils;
import com.example.utils.httpUtils;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vh
 * @since 2023-07-01
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {

    private com.example.utils.fileUtils fileUtils = new fileUtils();
    private com.example.utils.httpUtils httpUtils = new httpUtils();

    private IFiledownloadmanageService iFiledownloadmanageService;


    public FileServiceImpl(IFiledownloadmanageService iFiledownloadmanageService) {
        this.iFiledownloadmanageService = iFiledownloadmanageService;
    }

    @Override
    public boolean deleteFile(String url) {

        Integer count = this.baseMapper.deleteFile(url);

        if(count != 0){
            this.baseMapper.deleteFileInfdm(url);
            return true;
        }

        return false;
    }

    @Override
    public List<File> allFile() {
        List<File> fileList = this.baseMapper.getAllFile();

        if(fileList != null){
            return fileList;
        }

        return null;
    }

    @Override
    public File hasFile(String url) {
        String fileUrl = url;
        File file = this.baseMapper.findFileByURL(fileUrl);

        if(file != null){
            return file;
        }

        return null;
    }

    @Override
    public Map<String, Object> addFile(String url) throws MalformedURLException {

        //初始化文件信息
        Integer fileId = fileUtils.StringToIntegerConverter(url);
        Map<String, Object> objectMap = fileUtils.getFileTypeAndNameByURL(url);
        String fileName = (String) objectMap.get("fileName");
        String fileType = (String) objectMap.get("fileType");
        System.out.println(fileType);

        Integer fileSize = Integer.valueOf(httpUtils.getContentLength(url));
        String home = System.getProperty("user.home");
        String filePath = home + "\\Downloads\\" + fileType;

        filePath = filePath + "\\" + fileName;
        File newFile = new File(fileId, url, fileName,fileType, fileSize,filePath);

        //添加文件到文件数据库
        this.baseMapper.insertFile(newFile);

        //添加该文件进入，文件下载管理
        iFiledownloadmanageService.addFile(fileId,url);

        Map<String, Object> objectMap1 = new HashMap<>();
        objectMap1.put("file",newFile);

        return objectMap1;

    }

}
