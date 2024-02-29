$(document).ready(function (){
    $("#name_btn").on("click", function () {
        updateNickname();
    })
    $("#phone_btn").on("click", function () {
        updatePhone();
    });
    $("#pwd_btn").on("click", function () {
        updatePwd();
    });

})

// ---------- 이름 수정 ----------
function updateNickname() {
    var name_btn = document.getElementById('name_btn');
        const nickname = document.getElementById('change_name').value;
        $.ajax({
            type: 'POST',
            url: "/mypage/info/modify/nickname",
            data: {
                nickname: nickname
            },
            success: function (response) {
                location.reload();
            }, error: function (error) {
                console.log(error)
            }
        });
}


// ---------- 번호 수정 ----------
function updatePhone() {
    var phone_btn = document.getElementById('phone_btn');
        var phone = document.getElementById('change_phone').value;
        $.ajax({
            type: 'POST',
            url: "/mypage/info/modify/phone",
            data: {
                phone: phone
            },
            success: function (response) {
                location.reload();
            }
        });
}


// ---------- 비밀번호 수정 ----------
function updatePwd() {
    var pwd_btn = document.getElementById('pwd_btn');
        const current_pwd = document.getElementById('current_pwd').value;
        const change_pwd = document.getElementById('change_pwd').value;
        const change_chk_pwd = document.getElementById('change_chk_pwd').value;
        const pwRegExp = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

        if (!current_pwd || !change_pwd || !change_chk_pwd) {
            alert("모든 항목을 작성해주세요.");
            return;
        }
        if (change_pwd !== change_chk_pwd) {
            alert("새 비밀번호를 확인해주세요.");
            return;
        }
        if (!pwRegExp.test(change_pwd)) {
            alert("최소 8자, 영문, 숫자, 특수문자를 사용하여 비밀번호를 생성하세요.");
            return;
        }

        $.ajax({
            type: 'POST',
            url: "/mypage/info/modify/password",
            data: {
                currentPassword: current_pwd,
                password: change_pwd
            },
            success: function (response) {
                location.reload();
            }, error: function (error) {
                if (error.responseText === "incorrect password") {
                    alert("비밀번호가 일치하지 않습니다.")
                }
            }
        });
}