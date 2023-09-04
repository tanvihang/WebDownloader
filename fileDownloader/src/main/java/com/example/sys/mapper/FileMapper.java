package com.example.sys.mapper;

import com.example.sys.entity.File;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.sys.entity.Filedownloadmanage;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author vh
 * @since 2023-07-01
 */
public interface FileMapper extends BaseMapper<File> {
    public File findFileByURL(String url);
    public void insertFile(File file);
    public List<File> getAllFile();
    public Integer deleteFile(String url);
    public void deleteFileInfdm(String url);


}
