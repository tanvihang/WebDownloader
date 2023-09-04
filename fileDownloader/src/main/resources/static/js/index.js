var addFile = document.getElementById("addFile");
var toDownloadList = document.getElementById("toDownloadList");
var downloadBtn = document.getElementById("btn-download");
var stopBtn = document.getElementById("btn-stop");
var resumeBtn = document.getElementById("btn-resume");
var progressarea = document.getElementById("progress-area");
var langBtn = document.getElementById("btn-lang");
var fileBtn = document.getElementById("btn-choose-file");

const localURL = "http://127.0.0.1:8080/api"

let languageUrl = "/json/language/index.json";

import {addToList, loadDownloadFileList,deleteFromList, downloadFile, fileSelected, stopDownload, resumeDownload,loadDownloadedFileList,chooseFileLocation} from "./utils/file.js"
import {addProgressCircle} from "./utils/progress.js";
import {updateFunc} from "./utils/update.js";
import {changeLanguage} from "./utils/language.js";

export let threadCount = 8;
export let speedLimit = 512;

window.onload = function(){
    loadDownloadFileList()
    loadDownloadedFileList()

    //默认8线程
    addProgressCircle(8)

    // get speed value
    $("#downloadspeedselector").change(function(){
        speedLimit = $(this).val();
        console.log($(this).val())
    })

    // thread value
    $("#threadselector").change(function () {
        threadCount = $(this).val()
        addProgressCircle($(this).val())
    })

    // add file to list
    $("#addFile").click(function(){
        addToList();
    })

    //list item select
    toDownloadList.addEventListener("click",function(e){
        fileSelected(e);
    })

    //list item delete select
    toDownloadList.addEventListener("click", function (e){
        deleteFromList(e);
    })

    //download button click
    //download file that are selected
    downloadBtn.addEventListener("click", function(){
        downloadFile();
        setInterval(updateFunc,100);
    })

    //stop button click, pause download
    stopBtn.addEventListener("click", function(){
        stopDownload();
    })

    //resume button click
    resumeBtn.addEventListener("click", function(){
        resumeDownload();
    })

    fileBtn.addEventListener("click", function(){
        chooseFileLocation();
    })

    //lang button click
    langBtn.addEventListener("click", function(){
        changeLanguage();
    })
}

