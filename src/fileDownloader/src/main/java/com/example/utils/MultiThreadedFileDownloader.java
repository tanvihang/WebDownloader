package com.example.utils;

import com.example.sys.entity.File;
import com.example.sys.entity.Filedownloadmanage;


import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MultiThreadedFileDownloader{
    private static final int BUFFER_SIZE = 1024;

    private final List<Filedownloadmanage> fdmList;
    private final List<File> fileInfoList;
    private final List<DownloadTask> downloadTasks;
    private volatile boolean paused;
    private int threads;
    private int speed;
    private String currentDownload;
    private Integer currentDownloadFileSize;
    private List<Integer> threadDownloadProgressList;
    private List<Double> threadDownloadSpeedList;


    public MultiThreadedFileDownloader() {
        this.fdmList = new ArrayList<>();
        this.fileInfoList = new ArrayList<>();
        this.downloadTasks = new ArrayList<>();
        this.paused = false;
        this.threads = 0;
        this.currentDownload = null;
        this.currentDownloadFileSize = null;
    }

    public void clearDownloadTask(){
        downloadTasks.clear();
    }
    public void addDownloadTask(List<Filedownloadmanage> fdmList, List<File> fileInfoList, int numThreads, int speed) throws MalformedURLException {
        System.out.println("fdmList size: " + fdmList.size());
        System.out.println("FileList size: " + fileInfoList.size());

        this.speed = speed;

        threadDownloadProgressList = new ArrayList<>();
        threadDownloadSpeedList = new ArrayList<>();
        //初始化列表
        for(int i =0 ; i < numThreads+1; i++){
            this.threadDownloadProgressList.add(0);
            this.threadDownloadSpeedList.add(0D);
        }

        System.out.println("Download progress list size " + threadDownloadProgressList.size());

        for (File f: fileInfoList) {
            String fileUrl = f.getFileUrl();

            if(fileUrl != null){
                URL url = new URL(fileUrl);
                DownloadTask downloadTask = new DownloadTask(url,f.getFilePath(),numThreads);
                downloadTasks.add(downloadTask);
            }else{
                System.out.println("Error getting fileUrl");
            }

        }
        this.threads = numThreads;
    }

    public void startDownload() {

        for (DownloadTask task : downloadTasks) {
            System.out.println("One file downloading: " + task.url);

            //将该文件改为正在下载中
            this.currentDownload = task.url.toString();

            task.start();

            System.out.println("One file downloaded: " + task.url);

        }

        this.currentDownload = null;
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public String getCurrentDownload(){
        return this.currentDownload;
    }

    public List<Integer> getThreadProcess(){
        Double count = 0D;

        //计算总的进度
        for (int i = 0; i < this.threadDownloadProgressList.size()-1;i++){
            count += this.threadDownloadProgressList.get(i);
        }

        count = (count / ((this.threadDownloadProgressList.size()-1) * 10))*10;

        this.threadDownloadProgressList.set(this.threadDownloadProgressList.size()-1,count.intValue());

        return this.threadDownloadProgressList;
    }

    public List<Double> getThreadDownloadSpeedList() {
        return this.threadDownloadSpeedList;
    }

    private class DownloadTask {
        private final URL url;
        private final String savePath;
        private final int numThreads;


        private DownloadTask(URL url, String savePath, int numThreads) {
            this.url = url;
            this.savePath = savePath;
            this.numThreads = numThreads;
        }

        public void start() {
            try {
                long contentLength = getContentLength();
                long bytesPerThread = contentLength / numThreads; //通过线程数量平分下载数量

                ExecutorService executorService = Executors.newFixedThreadPool(numThreads); //创建线程数

                //分别对更小的线程进行开始下载，传送开始和结束范围
                for (int i = 0; i < numThreads; i++) {
                    long startRange = i * bytesPerThread;
                    long endRange = (i == numThreads - 1) ? contentLength - 1 : startRange + bytesPerThread - 1;

                    Runnable downloader = new DownloaderThread(url, savePath, startRange, endRange,i);
                    executorService.execute(downloader);
                }

                executorService.shutdown();

                try {
                    // Wait for all threads to complete their tasks
                    executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                    System.out.println("killed threads");

                    //重置所有线程下载进度为0，用以可视化下载进度
                    for(int i =0 ; i < threadDownloadProgressList.size(); i++){
                        threadDownloadProgressList.set(i,0);
                        threadDownloadSpeedList.set(i, (double) 0);
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private long getContentLength() throws IOException {
            return url.openConnection().getContentLengthLong();
        }

    }

    private class DownloaderThread implements Runnable {
        private final URL url;
        private final String savePath;
        private final long startRange;
        private final long endRange;
        private final int threadNo;
        private long downloadedByte = 0;

        private DownloaderThread(URL url, String savePath, long startRange, long endRange, int threadNo) {
            this.url = url;
            this.savePath = savePath;
            this.startRange = startRange;
            this.endRange = endRange;
            this.threadNo = threadNo;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //建立连接
                String range = "bytes=" + startRange + "-" + endRange;
                connection.setRequestProperty("Range", range); //请求特定范围

                try (InputStream in = connection.getInputStream();
                     RandomAccessFile file = new RandomAccessFile(savePath, "rw")) {

                    file.seek(startRange); //索引到文件特定位置

                    byte[] buffer = new byte[BUFFER_SIZE]; //单次写入的大小
                    int bytesRead; //单次读取大小数
                    long startTime = System.nanoTime(); //下载开始时间

                    //开始写入文件
                    while ((bytesRead = in.read(buffer)) != -1) {
                        synchronized (MultiThreadedFileDownloader.this) {
                            while (paused) {
                                try {
                                    MultiThreadedFileDownloader.this.wait();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    return;
                                }
                            }
                        }

                        downloadedByte += bytesRead; //downloadedByte用来记录线程下载进度

                        file.write(buffer, 0, bytesRead); //写入文件

                        //开始做计算（下载速度，下载进度）
                        long endTime = System.nanoTime();
                        long duration = endTime-startTime;
                        double seconds = (double) duration/1_000_000_000.0;
                        double speedy = (downloadedByte/seconds)/1_000_000_000.0;

//                        System.out.println(speedy);

                        long elapsedTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                        double downloadSpeed = (double) downloadedByte / elapsedTime / 1024;
                        long expectedTime = (bytesRead * 1000) / (speed * 1024);



                        threadDownloadSpeedList.set(threadNo, speedy);
                        threadDownloadProgressList.set(threadNo, getPercentage(downloadedByte, endRange - startRange, threadNo)); //加入到一个队列
                        if(elapsedTime < expectedTime){
                            Thread.sleep(expectedTime-elapsedTime);
                        }
                        startTime = System.nanoTime();
                        //结束计算

                    }

                    System.out.println("Thread completed: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        public int getPercentage(Long downloadedBytes, Long totalBytes, Integer threadNo){
            float per = downloadedBytes.floatValue()/totalBytes.floatValue();
            int state = 0;

            if(per < 0.1){
                state = 1;
            } else if (0.1 < per && per < 0.2 ) {
                state = 2;
            } else if (0.2 < per && per < 0.3) {
                state = 3;
            } else if (0.3 < per && per < 0.4) {
                state = 4;
            } else if (0.4 < per && per < 0.5) {
                state = 5;
            } else if (0.5 < per && per < 0.6) {
                state = 6;
            } else if (0.6 < per && per < 0.7) {
                state = 7;
            } else if (0.7 < per && per < 0.8) {
                state = 8;
            } else if (0.8 < per && per < 0.9) {
                state = 9;
            }else{
                state = 10;
            }


            return state;
        }

    }
}
