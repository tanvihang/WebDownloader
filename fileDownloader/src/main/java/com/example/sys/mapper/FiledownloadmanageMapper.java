package com.example.sys.mapper;

import com.example.sys.entity.File;
import com.example.sys.entity.Filedownloadmanage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vh
 * @since 2023-07-02
 */
public interface FiledownloadmanageMapper extends BaseMapper<Filedownloadmanage> {
    public Filedownloadmanage get(String fileUrl);
    public void addFile(Filedownloadmanage filedownloadmanage);
    public void deleteFile(String fileUrl);
    public void selectFile(String fileUrl, String selected);
    public List<Filedownloadmanage> getSelectedFileList();
    public void setDownloading(String fileUrl);
    public void setDownloadingNo(String fileUrl);
    public File getFile(String fileUrl);
    public void setDownloaded(String fileUrl);
    public List<Filedownloadmanage> getAllDownloadedFile(String fileDownloaded);
    public List<File> findZipAndDownloaded();
}
