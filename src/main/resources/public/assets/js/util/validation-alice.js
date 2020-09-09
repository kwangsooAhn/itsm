/**
 * Constants for validation check.
 * */
const blankReg = /\s/;
const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const upperCaseReg = /^[A-Z]*$/;
const lowerCaseReg = /^[a-z]*$/;
const numberReg = /^[0-9]*$/;
const specialCharReg = /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/;
const rgbReg = /^\#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/;
const idReg = /^[A-Za-z0-9+][A-Za-z0-9@\-_\.]*$/;

/** @brief 해당 엘리먼트 아이디의 값이 null인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNull(elementId, messageId) {
    if (document.getElementById(elementId) !== null) {
        if (document.getElementById(elementId).value === null) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
    return true;
}

/** @brief 해당 엘리먼트 아이디의 값이 null이 아닌 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNotNull(elementId, messageId) {
    if (document.getElementById(elementId) !== null) {
        if (document.getElementById(elementId).value !== null) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
    return true;
}

/** @brief 해당 엘리먼트 아이디의 값이 ''인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isEmpty(elementId, messageId) {
    if (document.getElementById(elementId) !== null) {
        if (document.getElementById(elementId).value.trim() === '') {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
    return true;
}

/** @brief 해당 엘리먼트 아이디의 값이 ''이 아닌 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNotEmpty(elementId, messageId) {
    if (document.getElementById(elementId) !== null) {
        if (document.getElementById(elementId).value !== '') {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
    return true;
}

/** @brief 서로 다른 엘리먼트의 값이 동일한지 판별한다.
 *  @date 2020-03-03
 */
function isEquals(elementId1, elementId2, messageId) {
    if (document.getElementById(elementId1) !== null && document.getElementById(elementId2) !== null) {
        if (document.getElementById(elementId1).value === document.getElementById(elementId2).value) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId1 : ' + elementId1 + 'ElementId2 : ' + elementId2);
    return true;
}

/** @brief 서로 다른 엘리먼트의 값이 상이한지 판별한다.
 *  @date 2020-03-03
 */
function isNotEquals(elementId1, elementId2, messageId) {
    if (document.getElementById(elementId1) !== null && document.getElementById(elementId2) !== null) {
        if (document.getElementById(elementId1).value !== document.getElementById(elementId2).value) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId1 : ' + elementId1 + 'ElementId2 : ' + elementId2);
    return true;
}

/** @brief 엘리먼트의 값이 범위 내에 있는지 판별한다.
 *  @date 2020-03-03
 */
function isExistInScope(elementId, minValue, maxValue, messageId) {
    if (document.getElementById(elementId) !== null) {
        if (document.getElementById(elementId).value < minValue || document.getElementById(elementId).value > maxValue) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.get(messageId));
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.get('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
    return true;
}

/** @brief 논리 연산자 (AND) 판별.
 *  @date 2020-03-03
 */
function isAndOperator(condition1, condition2, messageId) {
    if (condition1 && condition2) {
        if (messageId !== undefined) {
            aliceJs.alertWarning(i18n.get(messageId));
        }
        return true;
    }
    return false;
}

/** @brief 논리 연산자 (OR) 판별.
 *  @date 2020-03-03
 */
function isOrOperator(condition1, condition2, messageId) {
    if (condition1 || condition2) {
        if (messageId !== undefined) {
            aliceJs.alertWarning(i18n.get(messageId));
        }
        return true;
    }
    return false;
}

/** @brief 패스워드 관련 유효성 검사(공백, 문자 구성, 개인정보 포함 여부)
 *  @date 2020-03-03
 */
function isValidPassword(elementId) {

    let userId = document.getElementById("userId").value;
    let emailId = document.getElementById("email").value.split('@');
    let password = document.getElementById(elementId).value;
    let searchUpperCase = password.search(upperCaseReg);
    let searchLowerCase = password.search(lowerCaseReg);
    let searchNumber = password.search(numberReg);
    let searchSpecialChar = password.search(specialCharReg);

    /*
     * Blank Validation Check
     * [ 설명 ]
     * 비밀번호에 공백을 포함하지 않는다.
     */
    if (blankReg.test(password)) {
        aliceJs.alertWarning(i18n.get('validation.msg.pwNotContainSpace'));
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
            aliceJs.alertWarning(i18n.get('validation.msg.pwOneBetween10And20'));
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
            aliceJs.alertWarning(i18n.get('validation.msg.pwTwoBetween8And20'));
            return false;
        }
    }

    /*
     * ElementId Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 ID를 포함하지 않는다.
     */
    if (password.search(userId) > -1) {
        aliceJs.alertWarning(i18n.get('validation.msg.pwContainsId'));
        return false;
    }

    /*
     * Email Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 이메일 ID를 포함하지 않는다.
     */
    if (password.search(emailId[0]) > -1) {
        aliceJs.alertWarning(i18n.get('validation.msg.pwContainsEmail'));
        return false;
    }

    return true;
}

/** @brief 이메일 관련 유효성 검사(이메일 형식)
 *  @date 2020-03-03
 */
function isValidEmail(elementId) {
    let email = document.getElementById(elementId).value;

    if (!emailReg.test(email)) {
        aliceJs.alertWarning(i18n.get('validation.msg.checkEmailFormat'));
        return false;
    }
    return true;
}

/** @brief 아이디 관련 유효성 검사(아이디 형식)
 *  @date 2020-07-08
 */
function isValidUserId(elementId) {
    let userId = document.getElementById(elementId).value;

    if (!idReg.test(userId)) {
        aliceJs.alertWarning(i18n.get('validation.msg.checkUserIdFormat'));
        return false;
    }
    return true;
}

/**
 * RGB 유효성 검사.
 *
 * @param elementId element ID
 * @param callbackFunc callback function
 * @return {boolean} 유효성 검사 통과 여부
 */
function isValidRgb(elementId, callbackFunc) {
    let rgb = document.getElementById(elementId).value;

    if (!rgbReg.test(rgb)) {
        aliceJs.alertWarning(i18n.get('validation.msg.checkRgbFormat'), callbackFunc);
        return false;
    }
    return true;
}
