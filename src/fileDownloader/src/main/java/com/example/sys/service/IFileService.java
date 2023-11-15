package com.example.sys.service;

import com.example.sys.entity.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author vh
 * @since 2023-07-01
 */
public interface IFileService extends IService<File> {
    public Map<String, Object> addFile(String url) throws MalformedURLException;  //添加文件到数据库
    public File hasFile(String url); //检查数据库是否有该文件
    public List<File> allFile(); //获取数据库内的所有文件
    public boolean deleteFile(String url); //删除数据库内的特定文件
}
