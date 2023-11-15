var addFile = document.getElementById("addFile");
var toDownloadList = document.getElementById("toDownloadList");
var downloadedList = document.getElementById("downloadedList");

let isPaused = false;
let isDownloading = false;

import {threadCount, speedLimit} from "../index.js"

const localURL = "http://127.0.0.1:8080/api"


export function addToList(){
    var newFile = document.getElementById("newFile").value;

    if(newFile === ""){
        alert("please fill out the form");
        return false;
    }

    if(isValidURL(newFile)){
        //检查文件是否存在于数据库
        $.ajax({
            url: localURL + "/file/hasFile/" + "?url=" + newFile,
            method: "GET",
            success: function(data){
                var returnCode = data.code;
                console.log(data)
                if(returnCode === 20000){
                    alert(data.message);
                    // clear
                    newFile = document.getElementById("newFile").value = "";
                    return false;
                }else{
                    // create new element
                    var newItem = document.createElement("li");
                    var text = document.createTextNode(newFile);
                    var deleteFileBtn = document.createElement("BUTTON");

                    deleteFileBtn.setAttribute('class','deleteTask');
                    deleteFileBtn.innerHTML = "X";

                    newItem.appendChild(text);
                    newItem.appendChild(deleteFileBtn);

                    $.ajax({
                        url: localURL + "/file/add/"+"?url="+newFile,
                        method: 'GET',
                        success: function(data){
                            console.log(data);
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            console.error("Error:",  errorThrown);
                        }
                    });

                    toDownloadList.appendChild(newItem);

                    // clear
                    newFile = document.getElementById("newFile").value = "";
                }
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.error("Error:",  errorThrown);
            }
        });

    }else{
        newFile = document.getElementById("newFile").value = "";
        alert("invalid URL");
        return false;
    }

}

function isValidURL(url){
    var pattern = new RegExp('^https?:\\/\\/([^:\\/?#\\s]+)(?:[\\/|\\/?#]\\S*)?$');

    return pattern.test(url);
}

export function loadDownloadedFileList(){
    $("#downloadedList").empty();

    $.ajax({
        url: localURL + "/filedownloadmanage/getDownloadedFile",
        method: "GET",
        success: function (data){
            console.log(data)
            for (var i = 0; i < data.data.length; i++){
                //获取每一个data的fileUrl
                var filePath = data.data[i].filePath;
                var newItem = document.createElement("li");
                var text = document.createTextNode(filePath);
                var deleteFileBtn = document.createElement("BUTTON");

                deleteFileBtn.setAttribute('class','deleteTask');
                deleteFileBtn.innerHTML = "X";

                newItem.appendChild(text);
                newItem.appendChild(deleteFileBtn);

                downloadedList.appendChild(newItem);
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })
}

export function loadDownloadFileList(){
    $.ajax({
        url: localURL + "/file/allFile",
        method: 'GET',
        success: function (data) {
            //加载到fileDownloadList
            for (var i = 0; i < data.data.length; i++){
                //获取每一个data的fileUrl
                var fileUrl = data.data[i].fileUrl;
                var newItem = document.createElement("li");
                var text = document.createTextNode(fileUrl);
                var deleteFileBtn = document.createElement("BUTTON");

                deleteFileBtn.setAttribute('class','deleteTask');
                deleteFileBtn.innerHTML = "X";

                newItem.appendChild(text);
                newItem.appendChild(deleteFileBtn);

                toDownloadList.appendChild(newItem);
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })
}

export function deleteFromList(e){
    if(e.target && e.target.className == "deleteTask"){
        if(!confirm("Do you want to delete this file?")){
            e.preventDefault();
        }else{
            //获取当前点击的文件url
            var fileUrl = e.target.parentNode.innerText.split("\n")[0]
            //再数据库里面删除
            $.ajax({
                url: localURL + "/file/deleteFile/" + "?url=" + fileUrl,
                method: "GET",
                success: function(data){
                    console.log(data)
                    window.location.reload();
                },
                error: function(jqXHR, textStatus, errorThrown){
                    console.error("Error:",  errorThrown);
                }
            })
        }
    }
}

export function downloadFile(){

    var obj = {
        thread: threadCount,
        speed: speedLimit
    }


    if(isDownloading === false){
        alert("Download started!");
        isDownloading = true
        $.ajax({
            url: localURL + "/filedownloadmanage/download",
            method: "POST",
            data: {
                json: JSON.stringify(obj)
            },
            success: function(data){
                if(data.code === 20000){
                    //开始下载
                    alert("All File Download finished!")
                    isDownloading = false
                    //将下载成功的文件移动到另外一个列表
                    //1. 移除在Download List的位置

                    //2. 加入到Finished Download List
                    loadDownloadedFileList();

                }else{
                    //没有文件选择
                    alert("Please select file to download")
                }
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.error("Error:",  errorThrown);
            }
        })
    }else{
        alert("There is file currently downloading! please wait for file to finish");
        return false
    }

}

//用来更新数据库的fileSelected，用来确定是否进行下载。
export function fileSelected(e){
    if(e.target.nodeName == "LI"){
        e.target.classList.toggle("completeTask")
        var fileUrl = e.target.innerText.split("\n")[0]
        //when selected, true, when deselect, false
        var hasclass = e.target.classList.contains("completeTask");

        var obj = {
            url: fileUrl,
            selected: hasclass
        };

        $.ajax({
            url: localURL + "/filedownloadmanage/select",
            method: "POST",
            data: {
                json: JSON.stringify(obj)
            },
            success: function(data){
                console.log(data)
            },
            error: function(jqXHR, textStatus, errorThrown){
                console.error("Error:",  errorThrown);
            }

        })
    }
}

export function stopDownload(){
    //获取当前正在下载的文件。
    let currentDownload = null;

    $.ajax({
        url: localURL + "/filedownloadmanage/getCurrentDownloading",
        method: "GET",
        success: function(data){
            console.log(data.data)
            currentDownload = data.data

            if(currentDownload !== null){
                if(!confirm("Do you want to pause download: " + currentDownload +"?")){
                    return false;}
                else{
                    $.ajax({
                        url: localURL + "/filedownloadmanage/pauseDownload",
                        method: "GET",
                        success: function(data){
                            //更新当前有文件已停止
                            isPaused = true;
                            console.log(data)
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            console.error("Error:",  errorThrown);
                        }
                    })
                }
            }
            else{
                alert("No downloading file");
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            alert("No downloading file")
            console.error("Error:",  errorThrown);
        }
    })

}

export function resumeDownload(){

    //获取当前正在下载的文件。
    let currentDownload = null;

    $.ajax({
        url: localURL + "/filedownloadmanage/getCurrentDownloading",
        method: "GET",
        success: function(data){
            currentDownload = data.data

            //如果当前有文件在下载队列，并且已经被停止
            if(currentDownload !== null && isPaused === true){
                if(!confirm("Resume download file: " + currentDownload +"?")){
                    return false;
                }else{
                    $.ajax({
                        url: localURL + "/filedownloadmanage/resumeDownload",
                        method: "GET",
                        success: function(data){
                            console.log(data)
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            console.error("Error:",  errorThrown);
                        }
                    })
                }
            }else{
                alert("No paused or downloading file");
            }
        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })
}

export function chooseFileLocation(){
    $.ajax({
        url: localURL + "/file/chooseFileLocation",
        method: "GET",
        success: function(data){

        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })
}