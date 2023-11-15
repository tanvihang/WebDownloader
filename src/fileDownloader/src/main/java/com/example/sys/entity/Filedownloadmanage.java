package com.example.sys.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author vh
 * @since 2023-07-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filedownloadmanage implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "fileId")
    private Integer fileId;

    private String fileUrl;

    private String fileDownloaded;

    private String fileDownloading;

    private Object downloadedByte;

    private String fileSelected;

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileDownloaded() {
        return fileDownloaded;
    }

    public void setFileDownloaded(String fileDownloaded) {
        this.fileDownloaded = fileDownloaded;
    }

    public String getFileDownloading() {
        return fileDownloading;
    }

    public void setFileDownloading(String fileDownloading) {
        this.fileDownloading = fileDownloading;
    }

    public Object getDownloadedByte() {
        return downloadedByte;
    }

    public void setDownloadedByte(Object downloadedByte) {
        this.downloadedByte = downloadedByte;
    }

    public String getFileSelected() {
        return fileSelected;
    }

    public void setFileSelected(String fileSelected) {
        this.fileSelected = fileSelected;
    }

    @Override
    public String toString() {
        return "Filedownloadmanage{" +
            "fileId = " + fileId +
            ", fileUrl = " + fileUrl +
            ", fileDownloaded = " + fileDownloaded +
            ", fileDownloading = " + fileDownloading +
            ", downloadedByte = " + downloadedByte +
            ", fileSelected = " + fileSelected +
        "}";
    }
}
