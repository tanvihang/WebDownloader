var langBtn = document.getElementById("btn-lang");
const languageUrl = "/json/language/index.json";

export function changeLanguage(){
    if(langBtn.innerHTML === "CN"){
        langBtn.innerHTML = "EN"
        loadPageLanguage(languageUrl,"en")
    }else{
        langBtn.innerHTML = "CN"
        loadPageLanguage(languageUrl,"cn")

    }
}

function loadPageLanguage(languageUrl, language){
    $.getJSON(languageUrl, function(data){
        let languageObj = data[language][0];

        $("#headerTitle").text(languageObj.title);
        $("#box-title").text(languageObj.box_title);
        $("#addFile").text(languageObj.add_file_btn);
        $("#btn-download").text(languageObj.start_download_btn);
        $("#btn-stop").text(languageObj.stop_download_btn);
        $("#btn-resume").text(languageObj.resume_download_btn);
        $("#btn-choose-file").text(languageObj.file_location);
        $("#download-progress-text").text(languageObj.download_progress);
        $("#download-speed-text").text(languageObj.download_speed);
        $("#download-list").text(languageObj.download_list);
        $("#fin-download-list").text(languageObj.fin_download_list);

    });
}