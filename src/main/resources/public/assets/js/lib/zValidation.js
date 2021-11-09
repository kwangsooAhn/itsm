/**
 * 유효성 검증 모듈 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

const KEY_UP_VALID_TYPE = ['char', 'number', 'phone'];
const CHANGE_VALID_TYPE = ['email'];
class ZValidation {
    constructor(options = { alert: true }) {
        // 알림창 사용 여부가 false일 경우 DOM을 검색하여 'error-msg' class를 찾아서 에러 메시지를 표기한다.
        this.alert = options.alert; // 알림창 사용여부

        this.regex = Object.assign({}, {
            number: /^[-+]?[0-9]*\.?[0-9]+$/, // 숫자
            integer: /^[0-9]*$/,              // 정수
            char: /^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]*$/,          // 영문자, 한글
            specialChar: /^[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]*$/, // 특수문자
            email: /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i,
            blank: /^\s*$/,
            rgb: /^\#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$/,
        }, options.regex);

        this.errorClassName = 'error';
        this.events = {};

        // 이벤트 등록
        this.on('number', this.isNumber);
        this.on('phone', this.isNumber);
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
        if (typeof this.events[type] !== 'undefined') {
            const events = this.events[type].slice();

            for (const event of events) {
                if (event && event.callback) {
                    return event.callback.apply(this, [...args, ...event.args]);
                }
            }
        } else {
            return true;
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
        } else if (element instanceof HTMLTextAreaElement) {
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
                return Array.from(elements).filter((item) => item.checked).map((item) => item.value).join('|');
            }
        } else if (element instanceof HTMLSelectElement) {
            return element.options[element.selectedIndex].value;
        } else if (element instanceof HTMLTextAreaElement) {
            return element.value;
        }
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
            return object && typeof object === 'object' && true && object.nodeType === 1 &&
                typeof object.nodeName === 'string';
        }
    }
    /**
     * DOM 엘리먼트 에러 제거
     * @param element 엘리먼트
     */
    removeDOMElementError(element) {
        element.classList.remove(this.errorClassName);
    }

    /**
     * DOM 엘리먼트 에러 추가
     * @param element 엘리먼트
     */
    addDOMElementError(element) {
        element.classList.add(this.errorClassName);
        element.focus();
    }
    /**
     * DOM 엘리먼트 에러 표시
     * @param isValid 유효성 통과 여부
     * @param element 엘리먼트
     * @param message 에러 메시지
     * @param callback 콜백
     */
    setDOMElementError(isValid, element, message, callback) {
        // 유효성 검증 성공시
        if (isValid) {
            this.removeDOMElementError(element);
            return true;
        }
        //유효성 검증 실패시
        if (this.alert) { // 알림창 사용시
            zAlert.warning(message, () => {
                this.addDOMElementError(element);
                if (typeof callback === 'function') {
                    callback();
                }
            });
        } else { // 알림창 미사용시
            this.addDOMElementError(element);

            if (typeof callback === 'function') { callback(); }
        }
    }
    /**
     * null, undefined 체크
     */
    isDefined(target) {
        return target !== null && target !== undefined;
    }
    /**
     * 배열 체크
     */
    isArray(target) {
        return {}.toString.call(target) === '[object Array]';
    }
    /**
     * 빈값 체크
     */
    isEmpty(target) {
        if (typeof target === 'function') { return false; } // 함수이면 false 반환
        if (typeof target === 'string') { return this.regex.blank.test(target); } // 빈값 체크
        if (!this.isDefined(target)) { return true; } // 정의된 값인지 체크
        if (this.isArray(target)) { return target.length === 0; } // 배열이면 1개라도 값이 존재하는지 체크
        if (target === Object(target)) { // 객체이면 property가 1개라도 존재하는지 체크
            for (const prop in target) {
                return false;
            }
            return true;
        }
        return false;
    }
    /**
     * 숫자인지 체크
     */
    isNumber(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.number.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.number'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (typeof target === 'number' && !isNaN(target));
        }
        return rtn;
    }
    /**
     * 정수인지 체크
     */
    isInteger(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.integer.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.integer'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (this.isNumber(target) && target % 1 === 0);
        }
        return rtn;
    }
    /**
     * 문자인지 체크
     */
    isChar(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.char.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.char'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (typeof target === 'string' && this.regex.char.test(target));
        }
        return rtn;
    }
    /**
     * 특수문자인지 체크
     */
    isSpecialChar(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.specialChar.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.specialChar'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (typeof target === 'string' && this.regex.specialChar.test(target));
        }
        return rtn;
    }
    /**
     * 이메일 체크
     */
    isEmail(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.email.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.checkEmailFormat'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (typeof target === 'string' && this.regex.email.test(target));
        }
        return rtn;
    }
    /**
     * rgb 체크
     */
    isRgb(target, callback) {
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.regex.rgb.test(this.getDOMElementValue(target));
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.checkRgbFormat'), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = (typeof target === 'string' && this.regex.rgb.test(target));
        }
        return rtn;
    }
    /**
     * 최소값 체크
     */
    isMin(target, minValue, callback) {
        if (this.isEmpty(minValue) || !this.isNumber(Number(minValue))) {
            console.error('The minValue is incorrect. Please check the parameters.');
            return true;
        }
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            const targetValue = this.getDOMElementValue(target);
            if (!this.isNumber(Number(targetValue))) {
                console.error('The DOM Element is incorrect. Please check the DOM Element.');
                return true;
            }
            rtn = targetValue >= Number(minValue);
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.min', minValue), callback);
        } else { // 변수이면 true인지 false인지만 반환
            if (!this.isNumber(Number(target))) {
                console.error('The variable is incorrect. Please check the variable.');
                return true;
            }
            rtn = Number(target) >= Number(minValue);
        }
        return rtn;
    }
    /**
     * 최대값 체크
     */
    isMax(target, maxValue, callback) {
        if (this.isEmpty(maxValue) || !this.isNumber(Number(maxValue))) {
            console.error('The maxValue is incorrect. Please check the parameters.');
            return true;
        }
        if (this.isEmpty(target)) { return true; }

        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            const targetValue = this.getDOMElementValue(target);
            if (!this.isNumber(Number(targetValue))) {
                console.error('The DOM Element is incorrect. Please check the DOM Element.');
                return true;
            }
            rtn = targetValue <= Number(maxValue);
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.max', maxValue), callback);
        } else { // 변수이면 true인지 false인지만 반환
            if (!this.isNumber(Number(target))) {
                console.error('The variable is incorrect. Please check the variable.');
                return true;
            }
            rtn = Number(target) <= Number(maxValue);
        }
        return rtn;
    }
    /**
     * 필수값 체크
     * 충족하지 못하면 false 반환 충족하면 true 반환
     */
    isRequired(target, callback) {
        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.getDOMElementValue(target).trim() !== '';
            const requiredDOMElementName = target.getAttribute('data-validation-required-name');
            const msg =  requiredDOMElementName ? i18n.msg('validation.msg.required', requiredDOMElementName) : i18n.msg('common.msg.requiredEnter');
            this.setDOMElementError(rtn, target, msg, callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = !this.isEmpty(target);
        }
        return rtn;
    }
    /**
     * 최소길이 체크
     */
    isMinLength(target, minLength, callback) {
        if (this.isEmpty(minLength) || !this.isNumber(Number(minLength))) {
            console.error('The minLength is incorrect. Please check the parameters.');
            return true;
        }
        if (this.isEmpty(target)) { return true; }
        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.getDOMElementValue(target).length >= Number(minLength);
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.minLength', minLength), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = target.length >= Number(minLength);
        }
        return rtn;
    }
    /**
     * 최대길이 체크
     */
    isMaxLength(target, maxLength, callback) {
        if (this.isEmpty(maxLength) || !this.isNumber(Number(maxLength))) {
            console.error('The maxLength is incorrect. Please check the parameters.');
            return true;
        }
        if (this.isEmpty(target)) { return true; }
        let rtn = true;
        // 유효성 검증
        if (this.isDOMElement(target)) { // DOM 엘리먼트이면 알림창 및 알림메시지 표기
            rtn = this.getDOMElementValue(target).length <= Number(maxLength);
            this.setDOMElementError(rtn, target, i18n.msg('validation.msg.maxLength', maxLength), callback);
        } else { // 변수이면 true인지 false인지만 반환
            rtn = target.length <= Number(maxLength);
        }
        return rtn;
    }
    /**
     * keyup시 type(number, char, email 등), min, max, minLength, maxLength 체크
     * @param target
     * @returns target 유효성 결과값
     */
    keyUpValidationCheck(target) {
        if (!this.isDefined(target)) { return true; } // 정의된 값인지 체크

        let rtn = true;

        if (target.hasAttribute('data-validation-type') &&
            target.getAttribute('data-validation-type') !== '' &&
            KEY_UP_VALID_TYPE.includes(target.getAttribute('data-validation-type'))) {
            rtn = this.emit(target.getAttribute('data-validation-type'), target);
        }
        if (rtn && target.hasAttribute('data-validation-min') &&
            target.getAttribute('data-validation-min') !== '') {
            rtn = this.emit('min', target, target.getAttribute('data-validation-min'));
        }
        if (rtn && target.hasAttribute('data-validation-max') &&
            target.getAttribute('data-validation-max') !== '') {
            rtn = this.emit('max', target, target.getAttribute('data-validation-max'));
        }

        if (rtn && target.hasAttribute('data-validation-min-length') &&
            target.getAttribute('data-validation-min-length') !== '') {
            rtn = this.emit('minLength', target, target.getAttribute('data-validation-min-length'));
        }

        if (rtn && target.hasAttribute('data-validation-max-length') &&
            target.getAttribute('data-validation-max-length') !== '') {
            rtn = this.emit('maxLength', target, target.getAttribute('data-validation-max-length'));
        }
        return rtn;
    }
    /**
     * change시 필수값
     * @param target 이벤트객체
     */
    changeValidationCheck(target) {
        if (!this.isDefined(target)) { return true; } // 정의된 값인지 체크

        let rtn = true;

        if (target.hasAttribute('data-validation-type') &&
            target.getAttribute('data-validation-type') !== '' &&
            CHANGE_VALID_TYPE.includes(target.getAttribute('data-validation-type'))) {
            rtn = this.emit(target.getAttribute('data-validation-type'), target);
        }
        if (target.hasAttribute('data-validation-required') &&
            target.getAttribute('data-validation-required') !== 'false') {
            rtn = this.emit('required', target);
        }
        return rtn;
    }

    /**
     * 데이터 저장전, 에러를 가진 엘리먼트가 존재하는치 체크한다. 에러를 가진 엘리먼트가 존재할 경우 포커싱한다.
     * @returns {boolean}
     */
    hasDOMElementError(target) {
        const errorElem = target.querySelector('.' + this.errorClassName);
        if (errorElem !== null) {
            errorElem.focus();
            return true;
        }
        return false;
    }
}

export const zValidation = new ZValidation();
