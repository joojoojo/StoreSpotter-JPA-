$(document).ready(function() {
    // "닫기" 버튼 클릭 시 모달 닫기
    $(".close_btn").click(function() {
        $(".info-modal").css("display", "none");
    });

    // "24시간 보지 않기" 버튼 클릭 시 모달 닫기 및 쿠키 설정
    $("#modal-today-close").click(function() {
        $(".info-modal").css("display", "none");
        setCookie("mycookie", 'popupEnd', 1); // 1일 후 만료되는 쿠키 설정
    });

    // 페이지 로드 시 쿠키 확인하여 모달 표시/숨김
    var checkCookie = getCookie("mycookie");
    if (checkCookie === 'popupEnd') {
        $(".info-modal").css("display", "none");
    } else {
        $(".info-modal").css("display", "block");
    }
});

// 쿠키 설정 함수
function setCookie(name, value, expiredays) {
    var today = new Date();
    today.setDate(today.getDate() + expiredays);
    document.cookie = name + '=' + escape(value) + '; expires=' + today.toGMTString() + '; path=/';
}

// 쿠키 가져오는 함수
function getCookie(name) {
    var cookie = document.cookie;
    if (cookie !== "") {
        var cookie_array = cookie.split("; ");
        for (var index in cookie_array) {
            var cookie_name = cookie_array[index].split("=");
            if (cookie_name[0] === name) {
                return cookie_name[1];
            }
        }
    }
    return null;
}
