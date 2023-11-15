package com.example.sys.service.impl;

import com.example.sys.entity.File;
import com.example.sys.entity.Filedownloadmanage;
import com.example.sys.mapper.FiledownloadmanageMapper;
import com.example.sys.service.IFileService;
import com.example.sys.service.IFiledownloadmanageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.utils.fileUtils;
import com.example.utils.httpUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author vh
 * @since 2023-07-02
 */
@Service
public class FiledownloadmanageServiceImpl extends ServiceImpl<FiledownloadmanageMapper, Filedownloadmanage> implements IFiledownloadmanageService {

    private com.example.utils.fileUtils fileUtils = new fileUtils();
    private com.example.utils.httpUtils httpUtils = new httpUtils();


    @Override
    public Filedownloadmanage get(String fileUrl) {
        return this.baseMapper.get(fileUrl);
    }

    @Override
    public Boolean select(String fileUrl, String selected) {
        this.baseMapper.selectFile(fileUrl,selected);

        return true;
    }

    @Override
    public Boolean addFile(Integer fileId, String fileUrl) {
        Filedownloadmanage filedownloadmanage = new Filedownloadmanage(fileId,fileUrl,"no","no",0,"false");
        System.out.println("added to filedownloadmanage" + filedownloadmanage.toString());
        this.baseMapper.addFile(filedownloadmanage);

        return true;
    }

    @Override
    public List<Filedownloadmanage> getSelectedFileList() {
        return this.baseMapper.getSelectedFileList();
    }

    @Override
    public File getFile(String fileUrl) {
        File file = this.baseMapper.getFile(fileUrl);
        return file;
    }

    @Override
    public Filedownloadmanage setDownloading(String fileUrl) {

        this.baseMapper.setDownloading(fileUrl);

        return this.baseMapper.get(fileUrl);
    }

    @Override
    public Filedownloadmanage setDownloadingNo(String fileUrl){
        this.baseMapper.setDownloadingNo(fileUrl);

        return this.baseMapper.get(fileUrl);
    }

    @Override
    public Filedownloadmanage setDownloaded(String fileUrl) {
        this.baseMapper.setDownloaded(fileUrl);

        return this.baseMapper.get(fileUrl);
    }

    @Override
    public List<Filedownloadmanage> getAllDownloadedFile() {
        return this.baseMapper.getAllDownloadedFile("yes");
    }

    @Override
    public List<File> findZipAndDownloaded() {
        return this.baseMapper.findZipAndDownloaded();
    }
}
