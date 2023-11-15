package com.example.sys.service;

import com.example.sys.entity.File;
import com.example.sys.entity.Filedownloadmanage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sys.service.impl.FiledownloadmanageServiceImpl;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vh
 * @since 2023-07-02
 */
public interface IFiledownloadmanageService extends IService<Filedownloadmanage> {
    public Filedownloadmanage get(String url);
    public Boolean addFile(Integer fileId, String url);
    public Boolean select(String url, String selected);
    public List<Filedownloadmanage> getSelectedFileList();
    public File getFile(String url);
    public Filedownloadmanage setDownloading(String url);
    public Filedownloadmanage setDownloadingNo(String url);
    public Filedownloadmanage setDownloaded(String url);
    public List<Filedownloadmanage> getAllDownloadedFile();
    public List<File> findZipAndDownloaded();

}
