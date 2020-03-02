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
    var mobileNumber = document.getElementById("mobileNumber").value;
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
        alert(i18n.get('validation.msg.pwNotContainSpace'));
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
            alert(i18n.get('validation.msg.pwOneBetween10And20'));
            return false;
        }
    }
    
    /*
     * Password Validation Check
     * [ 설명 ] 
     * 2가지의 문자 구성인 경우 8자 이상, 20자 미만의 비밀번호를 설정한다.
     * 문자 구성 : 대문자, 소문자, 특수문자, 숫자
     */
    if ((searchUpperCase < 0 && searchLowerCase < 0) || (searchLowerCase < 0 && searchSpecialChar < 0) ||
        (searchSpecialChar < 0 && searchNumber < 0) || (searchNumber < 0 && searchUpperCase < 0)) {
        if (password.length < 8 || password.length > 20) {
            alert(i18n.get('validation.msg.pwTwoBetween8And20'));
            return false;
        }
    }
  
    /*
     * Id Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 ID를 포함하지 않는다. 
     */
    if (password.search(userId) > -1) {
        alert(i18n.get('validation.msg.pwContainsId'));
        return false;
    }
    
    /*
     * Email Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 이메일 ID를 포함하지 않는다.
     */
    if (password.search(emailId[0]) > -1) {
        alert(i18n.get('validation.msg.pwContainsEmail'));
        return false;
    }
    
    /*
     * Mobile Number Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 핸드폰 번호를 포함하지 않는다.
     */
    if (mobileNumber != "") {
        if (mobileNumber.length >= 10) {
            var middleNum = "";
            var lastNum = "";

            if (mobileNumber.length === 10) {
                middleNum = mobileNumber.substring(3, 6);
                lastNum = mobileNumber.substring(6, 10);

                if (password.search(middleNum) > -1 || password.search(lastNum) > -1) {
                    alert(i18n.get('validation.msg.pwContainsInnerMobileNumber'));
                    return false;
                }
            } else {
                middleNum = mobileNumber.substring(3, 7);
                lastNum = mobileNumber.substring(7, 11);

                if (password.search(middleNum) > -1 || password.search(lastNum) > -1) {
                    alert(i18n.get('validation.msg.pwContainsInnerMobileNumber'));
                    return false;
                }
            }
        } else {
            if (password.search(mobileNumber) > -1) {
                alert(i18n.get('validation.msg.pwContainsInnerMobileNumber'));
                return false;
            }
        }
    }
    return true;
}

// 이메일 validation check
function emailValidation(email) {
    if (!emailReg.test(email)) {
        alert(i18n.get('validation.msg.checkEmailFormat'));
        return false;
    }
    return true;
}

/*
 * element 요소의 null validation을 검사한다.
 *
 */
function validNull(id, message) {
    if (document.getElementById(id).value === '') {
        if (message != null) {
            alert(i18n.get(message));
            return true;
        } else {
            alert(i18n.get('validation.msg.checkNull'));
            return true;
        }
    }
    return false;
}
/*
 * element 요소의 value가 min과 max 사이 범위에 있는지 검사한다.
 *
 */
function validRange(id, min, max, message) {
    if (document.getElementById(id).value < min || document.getElementById(id).value > max) {
        alert(i18n.get(message));
        return true;
    }
    return false;
}

/*
 * 두 element 요소의 value가 일치여부 검사.
 *
 */
function validEquals(id, anotherId, message) {
    if (document.getElementById(id).value === document.getElementById(anotherId).value) {
        alert(i18n.get(message));
        return true;
    }
    return false;
}


