/**
 * validation.
 * */
const numberReg = /[0-9]/;
const blankReg = /\s/;
const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;

// 패스워드 validation check
function passwordValidation(password) {
    if (!numberReg.test(password) || blankReg.test(password)) {
        alert("비밀번호는 숫자를 포함해야 합니다. (단, 띄어쓰기는 포함되면 안됨.)");
        return false;
    }
    return true;
}

// 이메일 validation check
function emailValidation(email) {
    if (!emailReg.test(email)) {
        alert("이메일 형식에 맞게 입력해주세요.");
        return false;
    }
    return true;
}