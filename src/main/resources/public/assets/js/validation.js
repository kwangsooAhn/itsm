/**
 * validation.
 * */
const numberReg = /[0-9]/;
const blankReg = /\s/;

// 패스워드 validation check
// 일단 숫자 포함, 공백 미포함만 체크
function passwordValidation(password) {
    if (!numberReg.test(password) || blankReg.test(password)) {
        alert("비밀번호는 숫자를 포함해야 합니다. (단, 띄어쓰기는 포함되면 안됨.)");
        return false;
    }
    return true;
}