package com.example.filedownloader;

import com.example.utils.httpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class FileDownloaderApplicationTests {
    httpUtils httpUtils = new httpUtils();
    @Test
    void contextLoads(){
        try {
            String urlStr = "https://dldir1.qq.com/weixin/Windows/WeChatSetup.exe";
            URL url = new URL(urlStr);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // Handle the response
                System.out.println("Response: " + connection.getHeaderFields());
                System.out.println(connection.getHeaderField("Content-Length"));
            } else {
                System.out.println("Error: " + responseCode);
            }

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testgetFileTypeAndNameByURL()throws MalformedURLException {
        String urlString = "https://dldir1.qq.com/weixin/Windows/WeChatSetup.exe";
        URL url = new URL(urlString);

        String file = url.getFile();

        String[] fileSection = file.split("/");

        String fileName = fileSection[fileSection.length-1].split("\\.")[0];
        String fileType = fileSection[fileSection.length-1].split("\\.")[1];
    }

    @Test
    void testHttpRequest(){
        httpUtils.getHttpInfo("https://dldir1.qq.com/weixin/Windows/WeChatSetup.exe");
    }

    @Test
    void createFileTest() throws IOException {
        String filePath = "C:\\Users\\tvh10\\Downloads\\WeChatSetup.exe";
        File file = new File(filePath);

        System.out.println(file.exists());
        System.out.println(file.createNewFile());
    }
}
