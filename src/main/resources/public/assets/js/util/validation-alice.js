/**
 * Constants for validation check.
 * */
const blankReg = /\s/;
const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const upperCaseReg = /^[A-Z]*$/;
const lowerCaseReg = /^[a-z]*$/;
const numberReg = /^[0-9]*$/;
const specialCharReg = /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/;

/** @brief 해당 엘리먼트 아이디의 값이 null인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNull(elementId, message) {
    if (document.getElementById(elementId).value === null) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 해당 엘리먼트 아이디의 값이 null이 아닌 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNotNull(elementId, message) {
    if (document.getElementById(elementId).value !== null) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 해당 엘리먼트 아이디의 값이 ''인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isEmpty(elementId, message) {
    if (document.getElementById(elementId).value === '') {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 해당 엘리먼트 아이디의 값이 ''이 아닌 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNotEmpty(elementId, message) {
    if (document.getElementById(elementId).value !== '') {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 해당 엘리먼트 아이디의 값이 ''이면서 null 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNullAndEmpty(elementId, message) {
    if (document.getElementById(elementId).value === null && document.getElementById(elementId).value === '') {
        if(message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 서로 다른 엘리먼트의 값이 동일한지 판별한다.
 *  @date 2020-03-03
 */
function isEquals(elementId, elementId1, message) {
    if (document.getElementById(elementId).value === document.getElementById(elementId1).value) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 서로 다른 엘리먼트의 값이 상이한지 판별한다.
 *  @date 2020-03-03
 */
function isNotEquals(elementId, elementId1, message) {
    if (document.getElementById(elementId).value !== document.getElementById(elementId1).value) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 엘리먼트의 값이 범위 내에 있는지 판별한다.
 *  @date 2020-03-03
 */
function isWithinScope(elementId, minValue, maxValue, message) {
    if (document.getElementById(elementId).value < minValue || document.getElementById(elementId).value > maxValue) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 논리 연산자 (AND) 판별.
 *  @date 2020-03-03
 */
function isAndOperator(condition, condition1, message) {
    if (condition && condition1) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 논리 연산자 (OR) 판별.
 *  @date 2020-03-03
 */
function isOrOperator(condition, condition1, message) {
    if (condition || condition1) {
        if (message != null) {
            alert(i18n.get(message));
        }
        return true;
    }
    return false;
}

/** @brief 패스워드 관련 유효성 검사(공백, 문자 구성, 개인정보 포함 여부)
 *  @date 2020-03-03
 */
function validPassword(id) {

    var userId =  document.getElementById("userId").value;
    var emailId = document.getElementById("email").value.split('@');
    var password = document.getElementById(id).value;
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

/** @brief 이메일 관련 유효성 검사(이메일 형식)
 *  @date 2020-03-03
 */
// 이메일 validation check
function validEmail(id) {
    var email = document.getElementById(id).value;

    if (!emailReg.test(email)) {
        alert(i18n.get('validation.msg.checkEmailFormat'));
        return false;
    }
    return true;
}