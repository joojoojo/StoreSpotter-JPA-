const info_modal = document.querySelector('.info-modal');
const modalClose = document.querySelector('.close_btn');

//닫기 버튼을 눌렀을 때 모달팝업이 닫힘
modalClose.addEventListener('click',function(){
    //display 속성을 none으로 변경
    info_modal.style.display = 'none';
});

function setCookie(name, value, expiredays){
    var today = new Date();
    today.setDate(today.getDate() + expiredays);
    document.cookie = name + '=' + escape(value) + '; expires=' + today.toGMTString();
}

function getCookie(name) {
    var cookie = document.cookie;
    if (document.cookie !== "") {
        var cookie_array = cookie.split("; ");
        for ( var index in cookie_array) {
            var cookie_name = cookie_array[index].split("=");
            if (cookie_name[0] === "mycookie") {
                return cookie_name[1];
            }
        }
    }
}
$("#modal-today-close").click(function() {
    $(".info-modal").style.display = "hide";
    setCookie("mycookie", 'popupEnd', 1);
})

var checkCookie = getCookie("mycookie");

if (checkCookie === 'popupEnd') {
    $(".info-modal").css("display", "none");
} else {
    $(".info-modal").css("display", "block");
}