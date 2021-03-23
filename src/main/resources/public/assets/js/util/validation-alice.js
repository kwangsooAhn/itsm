/**
 * Constants for validation check.
 * */
const blankReg = /\s/;
const emailReg = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
const upperCaseReg = /^[A-Z]*$/;
const lowerCaseReg = /^[a-z]*$/;
const integerReg = /^[0-9]*$/; // 정수
const numberReg = /^[-+]?[0-9]*\.?[0-9]+$/; // 숫자
const charReg = /^[a-zA-Z가-힣]*$/; // 영문자 , 한글
const specialCharReg = /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/;
const rgbReg = /^\#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/;
const idReg = /^[A-Za-z0-9+][A-Za-z0-9@\-_\.]*$/;
const errorClass = 'error'; // 에러 발생시 추가될 클래스명

/**
 * 해당 엘리먼트가 null 인지 판별한다.
 */
function isNullElement(elementId) {
    const elem = document.getElementById(elementId);
    if (elem === undefined || elem === null) {
        aliceJs.alertWarning(i18n.msg('validation.msg.elementNotExist') + '\n' + 'ElementId : ' + elementId);
        return null;
    }
    return elem;
}

/** @brief 해당 아이디를 가진 엘리먼트의 값이 null인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNull(elementId, messageId, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (elem.value === null) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
            }
            return true;
        }
        return false;
    }
    return true;
}

/** @brief 해당 엘리먼트의 값이 ''인 경우를 판별한다.
 *  @date 2020-03-03
 */
function isEmpty(elementId, messageId, callbackFunc) {
    const elem = isNullElement(elementId);
    const message = i18n.msg(messageId || 'common.msg.emptyValue');
    const callback = (typeof callbackFunc === 'function') ? callbackFunc : function () {
        elem.focus();
    };
    if (elem !== null) {
        if (elem.value.trim() === '') {
            aliceJs.alertWarning(message, callback);
            return true;
        }
        return false;
    }
    return true;
}

/** @brief 해당 엘리먼트 값이 ''이 아닌 경우를 판별한다.
 *  @date 2020-03-03
 */
function isNotEmpty(elementId, messageId) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (elem.value !== '') {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.msg(messageId));
            }
            return true;
        }
        return false;
    }
    return true;
}

/** @brief 서로 다른 엘리먼트의 값이 동일한지 판별한다.
 *  @date 2020-03-03
 */
function isEquals(elementId1, elementId2, messageId, callbackFunc) {
    if (document.getElementById(elementId1) !== null && document.getElementById(elementId2) !== null) {
        if (document.getElementById(elementId1).value === document.getElementById(elementId2).value) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.msg('validation.msg.elementNotExist') + '\n' + 'ElementId1 : ' + elementId1 + 'ElementId2 : ' + elementId2);
    return true;
}

/** @brief 서로 다른 엘리먼트의 값이 상이한지 판별한다.
 *  @date 2020-03-03
 */
function isNotEquals(elementId1, elementId2, messageId, callbackFunc) {
    if (document.getElementById(elementId1) !== null && document.getElementById(elementId2) !== null) {
        if (document.getElementById(elementId1).value !== document.getElementById(elementId2).value) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
            }
            return true;
        }
        return false;
    }
    aliceJs.alertWarning(i18n.msg('validation.msg.elementNotExist') + '\n' + 'ElementId1 : ' + elementId1 + 'ElementId2 : ' + elementId2);
    return true;
}

/** @brief 엘리먼트의 값이 범위 내에 있는지 판별한다.
 *  @date 2020-03-03
 */
function isExistInScope(elementId, minValue, maxValue, messageId, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if ((minValue !== undefined && elem.value < minValue) || (maxValue !== undefined && elem.value > maxValue)) {
            if (messageId !== undefined) {
                aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
            }
            return true;
        }
        return false;
    }
    return true;
}

/** @brief 논리 연산자 (AND) 판별.
 *  @date 2020-03-03
 */
function isAndOperator(condition1, condition2, messageId, callbackFunc) {
    if (condition1 && condition2) {
        if (messageId !== undefined) {
            aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
        }
        return true;
    }
    return false;
}

/** @brief 논리 연산자 (OR) 판별.
 *  @date 2020-03-03
 */
function isOrOperator(condition1, condition2, messageId, callbackFunc) {
    if (condition1 || condition2) {
        if (messageId !== undefined) {
            aliceJs.alertWarning(i18n.msg(messageId), callbackFunc);
        }
        return true;
    }
    return false;
}

/** @brief 비밀번호 관련 유효성 검사(공백, 문자 구성, 개인정보 포함 여부)
 *  @date 2020-03-03
 */
function isValidPassword(elementId, callbackFunc) {

    let userId = document.getElementById('userId').value;
    let emailId = document.getElementById('email').value.split('@');
    let password = document.getElementById(elementId).value;
    let searchUpperCase = password.search(upperCaseReg);
    let searchLowerCase = password.search(lowerCaseReg);
    let searchNumber = password.search(integerReg);
    let searchSpecialChar = password.search(specialCharReg);

    /*
     * Blank Validation Check
     * [ 설명 ]
     * 비밀번호에 공백을 포함하지 않는다.
     */
    if (blankReg.test(password)) {
        aliceJs.alertWarning(i18n.msg('validation.msg.pwNotContainSpace'), callbackFunc);
        return false;
    }

    /*
     * Password Validation Check
     * [ 설명 ]
     * 1가지의 문자 구성인 경우 10자 이상, 20자 미만의 비밀번호를 설정한다.
     * 문자 구성 : 대문자, 소문자, 특수문자 , 숫자
     */
    if (lowerCaseReg.test(password) || upperCaseReg.test(password) || specialCharReg.test(password) || integerReg.test(password)) {
        if (password.length < 10 || password.length > 20) {
            aliceJs.alertWarning(i18n.msg('validation.msg.pwOneBetween10And20'), callbackFunc);
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
            aliceJs.alertWarning(i18n.msg('validation.msg.pwTwoBetween8And20'), callbackFunc);
            return false;
        }
    }

    /*
     * ElementId Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 ID를 포함하지 않는다.
     */
    if (password.search(userId) > -1) {
        aliceJs.alertWarning(i18n.msg('validation.msg.pwContainsId'), callbackFunc);
        return false;
    }

    /*
     * Email Inclusion Validation Check
     * [ 설명 ]
     * 비밀번호에 사용자의 이메일 ID를 포함하지 않는다.
     */
    if (password.search(emailId[0]) > -1) {
        aliceJs.alertWarning(i18n.msg('validation.msg.pwContainsEmail'), callbackFunc);
        return false;
    }

    return true;
}

/**
 *  이메일 관련 유효성 검사(이메일 형식)
 *
 * @param elementId 엘리먼트ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidEmail(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (!emailReg.test(elem.value)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('validation.msg.checkEmailFormat'), function () {
                    elem.value = '';
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = '';
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 *  아이디 관련 유효성 검사(아이디 형식)
 *
 * @param elementId 엘리먼트ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidUserId(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (!idReg.test(elem.value)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('validation.msg.checkUserIdFormat'), function () {
                    elem.value = '';
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = '';
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * RGB 유효성 검사.
 *
 * @param elementId element ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback function
 * @return {boolean} 유효성 검사 통과 여부
 */
function isValidRgb(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (!rgbReg.test(elem.value)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('validation.msg.checkRgbFormat'), function () {
                    elem.value = '';
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = '';
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            return false;
        }
        return true;
    }
    return true;
}

/**
 * 해당 엘리먼트 아이디의 값이 숫자인지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidNumber(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (!numberReg.test(elem.value)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.number'), function () {
                    elem.value = '';
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = '';
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 해당 엘리먼트 아이디의 값이 문자(영문자, 한글)인지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidChar(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (!charReg.test(elem.value)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.char'), function () {
                    elem.value = '';
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = '';
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 엘리먼트의 값이 최대값에 만족하는지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param maxValue 최대 값
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidMax(elementId, maxValue, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null && maxValue !== undefined) {
        if (maxValue !== '' && elem.value > Number(maxValue)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.max', maxValue), function () {
                    elem.value = maxValue;
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = maxValue;
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 엘리먼트의 값이 최소값에 만족하는지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param minValue 최소 값
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidMin(elementId, minValue, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null && minValue !== undefined) {
        if (minValue !== '' && elem.value < Number(minValue)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.min', minValue), function () {
                    elem.value = minValue;
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = minValue;
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add('error');
            return false;
        }
        elem.classList.remove('error');
        return true;
    }
    return true;
}

/**
 * 엘리먼트의 값의 길이가 최대값에 만족하는지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param maxLength 최대 길이 값
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidMaxLength(elementId, maxLength, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null && maxLength !== undefined) {
        if (maxLength !== '' && elem.value.length > Number(maxLength)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.maxLength', maxLength), function () {
                    elem.value = elem.value.substring(0, maxLength);
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.value = elem.value.substring(0, maxLength);
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 엘리먼트의 값의 길이가 최소값에 만족하는지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param minLength 최소 길이 값
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidMinLength(elementId, minLength, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null && minLength !== undefined) {
        if (minLength !== '' && elem.value.length < Number(minLength)) {
            if (isMessage) {
                aliceJs.alertWarning(i18n.msg('common.msg.minLength', minLength), function () {
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 엘리먼트가 필수값인데 비워져 있는지 판별한다.
 *
 * @param elementId 엘리먼트ID
 * @param isMessage 메시지 표시여부
 * @param callbackFunc callback 함수
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidRequired(elementId, isMessage, callbackFunc) {
    const elem = isNullElement(elementId);
    if (elem !== null) {
        if (elem.required && elem.value.trim() === '') {
            if (isMessage) {
                const requiredElemName = elem.getAttribute('data-required-name');
                let requiredMsg = (requiredElemName !== null) ? i18n.msg('common.msg.required', requiredElemName) : i18n.msg('common.msg.requiredEnter');
                aliceJs.alertWarning(requiredMsg, function() {
                    elem.focus();
                    if (typeof callbackFunc === 'function') {
                        callbackFunc();
                    }
                });
            } else {
                elem.focus();
                if (typeof callbackFunc === 'function') {
                    callbackFunc();
                }
            }
            elem.classList.add(errorClass);
            return false;
        }
        elem.classList.remove(errorClass);
        return true;
    }
    return true;
}

/**
 * 화면에서 required 속성을 가진 element 들을 모두 검색한 후 하나라도 일지하지 않으면, 경고창과 함께 해당 element에 포커스가 간다.
 *
 * @param messageId
 * @returns {boolean} 유효성 검사 통과 여부
 */
function isValidRequiredAll(modal) {
    let requiredElems = '';
    if (modal === undefined) {
        requiredElems = document.querySelectorAll('[required]');
    } else {
        let modalContent = document.querySelector('.modal-content');
        requiredElems = modalContent.querySelectorAll('[required]');
    }
    for (let i = 0, len = requiredElems.length; i < len; i++) {
        const requiredElem = requiredElems[i];
        const requiredElemName = requiredElem.getAttribute('data-required-name');
        if (requiredElem.value.trim() === '') {
            requiredElem.classList.add(errorClass);
            let requiredMsg = (requiredElemName !== null) ? i18n.msg('common.msg.required', requiredElemName) : i18n.msg('common.msg.requiredEnter');
            aliceJs.alertWarning(requiredMsg, function() {
                requiredElem.focus();
            });
            return false;
        }
        requiredElem.classList.remove(errorClass);
    }
    return true;
}

/**
 * 데이터 저장전, 에러를 가진 엘리먼트가 존재하는치 체크한다. 에러를 가진 엘리먼트가 존재할 경우 포커싱한다.
 * @returns {boolean}
 */
function hasErrorClass() {
    const errorElem = document.querySelector('.error');
    if (errorElem !== null) {
        errorElem.focus();
        return false;
    }
    return true;
}

/**
 * 필드에 숫자만 있는지 확인한다.
 * @returns {boolean}
 */
function onlyNumber(event) {
    event = event || window.event;
    const keyID = (event.which) ? event.which : event.keyCode;
    if (!((keyID >= 48 && keyID <= 57) || (keyID >= 96 && keyID <= 105)
        || keyID === 8 || keyID === 9 || keyID === 46 || keyID === 37 || keyID === 39)) {
        return false;
    }
}

/**
 * 허용된 IP인지 확인한다.
 * @returns {boolean}
 */
function ipAccessCheck(ipList, clientIp, Separator) {
    let isIpClassBand = false;
    let isSameIpClassBand = false;
    let count = ipList.length;
    if (ipList !== null && clientIp !== null) {
        for (let i = 0; i < count; i++) {
            let comparativeIndex = ipList[i].ipAddr.indexOf(Separator);
            isIpClassBand = comparativeIndex != -1;
            isSameIpClassBand = clientIp.substr(0, comparativeIndex) === ipList[i].ipAddr.substr(0, comparativeIndex);
            if (clientIp === ipList[i].ipAddr) {
                return true;
            } else if (isIpClassBand && isSameIpClassBand) {
                return true;
            }
        }
        return false;
    }
    return true;
}

/**
 * 스크롤바가 바닥에 닿았는지를 확인한다.
 * @returns {boolean}
 */
function isScrollbarBottom(scrollHeight, scrollTop, clientHeight) {
    return scrollHeight - scrollTop === clientHeight
}
