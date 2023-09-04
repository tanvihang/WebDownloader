const localURL = "http://127.0.0.1:8080/api"

var currentDownloadText = document.getElementById("current-downloading")
var downloadSpeedText = document.getElementById("download-speed");

export function updateFunc(){
    //1.get current downloading file name, update on current downloading
    $.ajax({
        url: localURL + "/filedownloadmanage/getCurrentDownloading",
        method: "GET",
        success: function(data){
            if(data.code === 20001){
                currentDownloadText.innerText = "no file downloading"
            }else{
                currentDownloadText.innerText = data.data
            }

        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })

    //2. get thread download process
    $.ajax({
        url: localURL + "/filedownloadmanage/getThreadProcess",
        method: "GET",
        success: function(data){
            if(data.code === 20001){
                return
            }
            else{
                for(let i = 0; i < data.data.length - 1;i++){
                    //更新图片，删除原有的class，增加class
                    let name = "#progress-" + (i+1)
                    $(name).removeClass();
                    $(name).addClass("progress-radial")
                    $(name).addClass("progress-"+ (data.data[i]*10))
                }

                let name = "#progress-main";
                $(name).removeClass();
                $(name).addClass("progress-radial-main");
                $(name).addClass("progress-radial");
                $(name).addClass("progress-radial-main-"+(data.data[data.data.length-1]*10))
            }

        },
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }

    })

    //3. get download speed
    $.ajax({
        url: localURL + "/filedownloadmanage/getDownloadSpeed",
        method: "GET",
        success: function(data){
            if(data.code === 20001){
                downloadSpeedText.innerText="No file downloading"
            }
            else{
                    downloadSpeedText.innerText = data.data + "KB/s";
                }
            }
        ,
        error: function(jqXHR, textStatus, errorThrown){
            console.error("Error:",  errorThrown);
        }
    })

}