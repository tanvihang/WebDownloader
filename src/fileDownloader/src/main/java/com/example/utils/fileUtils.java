package com.example.utils;

import com.example.sys.entity.Filedownloadmanage;
import com.example.sys.service.IFileService;
import com.example.sys.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class fileUtils {
    public Integer StringToIntegerConverter(String string) {
        int uniqueInteger = 0;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(string.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a positive integer
            uniqueInteger = bytesToPositiveInt(hashBytes);

//            System.out.println("Unique Integer: " + uniqueInteger);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return uniqueInteger;
    }

    private static int bytesToPositiveInt(byte[] bytes) {
        int value = 0;
        for (int i = 0; i < Math.min(bytes.length, 4); i++) {
            value += (bytes[i] & 0xFF) << (8 * i);
        }
        return Math.abs(value);
    }

    public Map<String, Object> getFileTypeAndNameByURL(String urls) throws MalformedURLException {
        URL url = new URL(urls);
        String file = url.getFile();

        ///qqfile/qq/PCQQ9.7.9/QQ9.7.9.29065.exe
        String[] fileSection = file.split("/");
        //QQ9.7.9.29065.exe
        String[] fileSection1 = fileSection[fileSection.length - 1].split("\\.");

        String fileName = fileSection[fileSection.length - 1];
        String fileType = fileSection1[fileSection1.length - 1];

        Map<String, Object> map = new HashMap<>();

        map.put("fileName", fileName);
        map.put("fileType", fileType);

        return map;
    }

    public Map<String, Object> createFile(String filePath) throws IOException {
        File file = new File(filePath);
        System.out.println(filePath);

        Map<String, Object> map = new HashMap<>();

        if (file.exists()) {
            map.put("status", "true");
            map.put("message", "file exists");
            return map;
        } else {
            boolean created = file.createNewFile();
            if (created) {
                map.put("status", "true");
                map.put("message", "file created");
                return map;
            } else {
                map.put("status", "false");
                map.put("message", "file path error");
                return map;
            }
        }
    }

    public boolean createFileDirectory(String filePath) {
        File directory = new File(filePath);

        return directory.mkdirs();
    }


    public boolean extractZipFile(String zipFilePath, String destinationFolder) {

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                String entryName = entry.getName();
                File entryFile = new File(destinationFolder, entryName);

                if (entry.isDirectory()) {
                    // Create directory if it doesn't exist
                    entryFile.mkdirs();
                } else {
                    // Create parent directories if they don't exist
                    entryFile.getParentFile().mkdirs();

                    // Extract the file
                    try (OutputStream os = new FileOutputStream(entryFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    }
                }

                zis.closeEntry();
            }

            System.out.println("Zip file extracted successfully.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}