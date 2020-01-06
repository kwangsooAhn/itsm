/**
 * Constants for validation check.
 * */
const blankReg = /\s/;
const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const upperCaseReg = /^[A-Z]*$/;
const lowerCaseReg = /^[a-z]*$/;
const numberReg = /^[0-9]*$/;
const specialCharReg = /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/;


// 패스워드 validation check
function passwordValidation(password) {
    
    var userId =  document.getElementById("userId").value;
    var emailId = document.getElementById("email").value.split('@');
    var extensionNumber = document.getElementById("extensionNumber").value; 
    var searchUpperCase = password.search(upperCaseReg);
    var searchLowerCase = password.search(lowerCaseReg);
    var searchNumber =  password.search(numberReg);
    var searchSpecialChar = password.search(specialCharReg);

   
    /*
     * Blank Validation Check
     * [ 설명 ]
     * 비밀번호에 공백을 포함하지 않는다.
     */
    if (blankReg.test(password)) {
        alert("비밀번호에 공백을 포함할 수 없습니다.");
        return false;    
    }

    /*
     * Password Validation Check
     * [ 설명 ]
     * 1가지의 문자 구성인 경우 10자 이상, 20자 미만의 비밀번호를 설정한다.
     * 문자 구성 : 대문자, 소문자, 특수문자 , 숫자
     */
    if (lowerCaseReg.test(password) || upperCaseReg.test(password) || specialCharReg.test(password) || numberReg.test(password)) {
        if (password.length < 10 || password.length > 20) {
            alert("1가지 문자 구성의 경우 10자~20자의 비밀번호를 설정해야 합니다.");
            return false;
        }
    }
    
    /*
     * Password Validation Check
     * [ 설명 ] 
     * 2가지의 문자 구성인 경우 8자 이상, 20자 미만의 비밀번호를 설정한다.
     * 문자 구성 : 대문자, 소문자, 특수문자, 숫자
     */
    if ((searchUpperCase < 0 && searchLowerCase < 0) || (searchLowerCase < 0 && searchSpecialChar < 0) || (searchSpecialChar < 0 && searchNumber < 0) || (searchNumber < 0 && searchUpperCase < 0)) {
        if (password.length < 8 || password.length > 20) {
            alert("2가지 문자 구성의 경우 8자~20자의 비밀번호를 설정해야 합니다.");
            return false;
        }
    }
  
    /*
     * Id Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 ID를 포함하지 않는다. 
     */
    if (password.search(userId) > -1) {
        alert("비밀번호에 아이디가 포함되어 있습니다.");
        return false;
    }
    
    /*
     * Email Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 이메일 ID를 포함하지 않는다.
     */
    if (password.search(emailId[0]) > -1) {
        alert("비밀번호에 이메일 아이디가 포함되어 있습니다.");
        return false;
    }
    
    /*
     * Extension Number Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 사내 번호를 포함하지 않는다.
     */
    if (extensionNumber.length >= 10) {
        var middleNum = "";
        var lastNum = "";
        
        if (extensionNumber.length === 10) {
            middleNum = extensionNumber.substring(3,6);
            lastNum = extensionNumber.substring(6,10);
            
            if (password.search(middleNum) > -1 || password.search(lastNum) > -1) {
                alert("비밀번호에 사용자의 사내 번호가 포함되어 있습니다.");
                return false;
            }
        } else {
            middleNum = extensionNumber.substring(3,7);
            lastNum = extensionNumber.substring(7,11);
        
            if (password.search(middleNum) > -1 || password.search(lastNum) > -1) {
                alert("비밀번호에 사용자의 사내 번호가 포함되어 있습니다.");
                return false;
            }
        }
    } else {
        if (password.search(extensionNumber) > -1) {
            alert("비밀번호에 사용자의 사내 번호가 포함되어 있습니다.");
            return false;
        }
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