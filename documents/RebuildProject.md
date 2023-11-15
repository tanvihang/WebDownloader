# Rebuild Project
| Description                                                                                         | Date       | Time    |
| --------------------------------------------------------------------------------------------------- | ---------- | ------- |
| Choose file location（Actuallt it is legal, so i think i should just set it to the usual location） | 2023/11/15 | 10:40am |
| Click the finished download list to go to location                                                  | 2023/11/15 | 11:46am |
| Detect if the Corresponding website allow Multithreaded download                                    |            |         |
| Make it look nicer lah                                                                              |            |         |
| Accept different protocol: FTP, SFTP, P2P                                                           |            |         |
| Speed limit                                                                                         |            |         |


# Choose file location
- Unfortunately choosing file location is not ok, File download destination is controlled by the browser.
- it's about security purpose

# Click finished downloaded to execute program
- in java simply get the downloaded location and run shell command

```java
ProcessBuilder processBuilder = new ProcessBuilder();
processBuilder.command("cmd.exe","/c","explorer " +filePath);

//get the output log if wanted
try{
    Process process = processBuilder.start();

    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
}
catch (IOException e){
    e.printStackTrace();
}
catch (InterruptedException e){
    e.printStackTrace();
}
```

```shell
explorer C:/Users/User/Downloads/file
```