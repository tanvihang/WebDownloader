var progressarea = document.getElementById("progress-area");

const localURL = "http://127.0.0.1:8080/api"

export function addProgressCircle(count){

    var threadCount = count;

    // clear previous nodes
    $("#progress-area").empty();

    //create main progress
    var div = document.createElement("div");

    div.innerHTML =
        '<div class="progress-radial progress-radial-main" id="progress-main">\n'+
        '    <div class="overlay">T</div>\n'+
        '</div>\n'


    //create threads progress
    for(var i =1; i<=threadCount;i++){
        div.innerHTML = div.innerHTML +
            '<div class="progress-radial" id="progress-' + i +'">\n' +
            '    <div class="overlay">'+ i +'</div>\n' +
            '</div>\n'

    }

    progressarea.appendChild(div);
}