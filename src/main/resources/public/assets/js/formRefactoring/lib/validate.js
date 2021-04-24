/**
 * 유효성 검증 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

export default class Validate {
    constructor(options = { alert: true }) {
        this.alert = options.alert; // 알림창 사용여부
        this.regex = Object.assign({}, {
            number: /^[-+]?[0-9]*\.?[0-9]+$/, // 숫자
            integer: /^[0-9]*$/,              // 정수
            char: /^[a-zA-Z가-힣]*$/,          // 영문자, 한글
            specialChar: /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/, // 특수문자
            email: /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i,
            blank: /\s/,
            rgb: /^\#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/,
        }, options.regex);
        this.errorClassName = 'error';
        this.errorMsgClassName = 'error-msg';
        this.events = {};

        // 이벤트 등록
        this.on('number', this.isNumber);
        this.on('integer', this.isInteger);
        this.on('char', this.isChar);
        this.on('specialChar', this.isSpecialChar);
        this.on('email', this.isEmail);
        this.on('rgb', this.isRgb);
        this.on('min', this.isMin);
        this.on('max', this.isMax);
        this.on('required', this.isRequired);
        this.on('minLength', this.isMinLength);
        this.on('maxLength', this.isMaxLength);
    }
    /**
     * 이벤트 추가
     * @param type 이벤트 타입
     * @param callback 콜백함수
     */
    on(type, callback, ...args) {
        if (typeof this.events[type] === 'undefined') {
            this.events[type] = [];
        }
        this.events[type].push({ callback, args });
    }
    /**
     * 이벤트 삭제
     * @param type 이벤트 타입
     * @param callback 콜백함수
     */
    off(type, callback){
        if (typeof this.events[type] === 'undefined') {
            return false;
        }
        // 전달 된 이벤트와 동일하지 않은 모든 이벤트는 유지된다.
        const filterFn = event => event.callback !== callback;
        this.events[type] = this.events[type].filter(filterFn);
    }
    /**
     * 모든 이벤트 삭제
     * @param type 이벤트 타입
     * @param callback 콜백함수
     * @param scope 이벤트 실행 컨텍스트
     */
    clear() {
        for (const type of Object.getOwnPropertyNames(this.events)) {
            delete this.events[type];
        }
    }
    /**
     * 이벤트 실행 (trigger)
     * @param type 이벤트 타입
     * @param target 호출 대상
     */
    emit(type, ...args) {
        if (typeof this.events[type] === 'undefined') {
            return false;
        }

        const events = this.events[type].slice();

        for (const event of events) {
            if (event && event.callback) {
                return event.callback.apply(this, [...args, ...event.args]);
            }
        }
    }
    /**
     * DOM 엘리먼트 값 초기화
     */
    setDOMElementValue(element, value) {
        if (element instanceof HTMLInputElement) {
            if (element.type === 'text') {
                element.value = value;
            } else if (element.type === 'checkbox' || element.type === 'radio') {
                // 동일한 이름을 가진 엘리먼트
                const valueArray = value.split('|');
                const name = element.getAttribute('name');
                const elements = document.getElementsByName(name);
                elements.forEach((item) => {
                    if (valueArray.includes(item.value)) {
                        item.checked = true;
                    } else {
                        item.checked = false;
                    }
                });
            }
        } else if (element instanceof HTMLSelectElement) {
            element.value = value;
        }
    }
    /**
     * DOM 엘리먼트 값 조회
     */
    getDOMElementValue(element) {
        if (element instanceof HTMLInputElement) {
            if (element.type === 'text') {
                return element.value;
            } else if (element.type === 'checkbox' || element.type === 'radio') {
                // 동일한 이름을 가진 엘리먼트 중 체크된 항목의 값을 반환
                const name = element.getAttribute('name');
                const elements = document.getElementsByName(name);
                return elements.filter((item) => item.checked).map((item) => item.value).join('|');
            }
        } else if (element instanceof HTMLSelectElement) {
            return element.options[element.selectedIndex].value;
        }
    }
    /**
     * DOM 에러 메시지 엘리먼트 조회
     */
    getDOMErrorMsg(element) {
        let el = element.parentNode;
        while (el = el.nextElementSibling) {
            if (el.classList && el.classList.contains(this.errorMsgClassName)) {
                return el;
            }
        }
        if (element.parentNode && el === element.parentNode) {
            this.getDOMErrorMsg(el);
        }
        return null;
    }
    /**
     * DOM 엘리먼트인지 체크
     */
    isDOMElement(object) {
        if (!object) { return false; }
        if (!object.querySelectorAll || !object.querySelector) { return false; }
        if (document === Object(document) && object === document) { return true; }

        if (typeof HTMLElement === 'object') {
            return object instanceof HTMLElement;
        } else {
            return object && typeof object === 'object' &&
                object !== null && object.nodeType === 1 &&
                typeof object.nodeName === 'string';
        }
    }

    /**
     * DOM 엘리먼트 에러 표시
     * @param isValid 유효성 통과 여부
     * @param element 엘리먼트
     * @param message 에러 메시지
     * @param callback 콜백
     */
    setDOMElementError(isValid, element, message, callback) {
        element.classList.remove(this.errorClassName);

        const errorMsgElement = this.getDOMErrorMsg(element);
        console.log(errorMsgElement);
        errorMsgElement.classList.remove('on');

        if (isValid) { return true; }
        if (this.alert) { // 알림창 사용시
            aliceJs.alertWarning(message, () => {
                element.classList.add(this.errorClassName);
                element.focus();
                if (typeof callback === 'function') {
                    callback();
                }
            });
        } else { // 알림창 미사용시 error-msg 엘리먼트에 에러 표기
            errorMsgElement.classList.add('on');
            errorMsgElement.textContent = message;
            element.classList.add(this.errorClassName);
            element.focus();

            if (typeof callback === 'function') { callback(); }
        }
    }
    /**
     * 숫자인지 체크
     */
    isNumber(target, callback) {
        const isDOMElement = this.isDOMElement(target);
        let rtn = true;
        // 유효성 검증
        if (isDOMElement) {
            rtn = this.regex.number.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('common.msg.number'), callback);
        } else {
            rtn = (typeof target === 'number' && !isNaN(target));

            if (rtn) { return true; }
            if (this.alert) {
                aliceJs.alertWarning(i18n.msg('common.msg.number'), () => {
                    if (typeof callback === 'function') { callback(); }
                });
            }
        }
        return rtn;
    }
    /**
     * 정수인지 체크
     */
    isInteger(target, callback) {}
    /**
     * 문자인지 체크
     */
    isChar(target, callback) {}
    /**
     * 특수문자인지 체크
     */
    isSpecialChar(target, callback) {}
    /**
     * 이메일 체크
     */
    isEmail(target, callback) {}
    /**
     * rgb 체크
     */
    isRgb(target, callback) {}
    /**
     * 최소값 체크
     */
    isMin(target, callback) {}
    /**
     * 최대값 체크
     */
    isMax(target, callback) {}
    /**
     * 필수값 체크
     */
    isRequired(target, callback) {}
    /**
     * 최소길이 체크
     */
    isMinLength(target, callback) {}
    /**
     * 최대길이 체크
     */
    isMaxLength(target, callback) {}
}