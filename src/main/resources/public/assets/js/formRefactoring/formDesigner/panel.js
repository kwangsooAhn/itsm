/**
 * 세부 속성 패널 Class.
 *
 * @author woodajung wdj@brainz.co.kr
 * @version 1.0
 *
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
import { validation } from '../lib/validation.js';
import GroupProperty from './property/type/groupProperty.module.js';

export default class Panel {
    constructor(editor) {
        this.domElement = document.getElementById('propertyPanel'); // 속성 패널
        this.editor = editor;

        // 유효성이 만족할 경우 true, 만족하지 않을 경우 false
        // 유효성 실패시, 다른 이벤트의 동작을 멈추기 위한 용도이다.
        this.validationStatus = true;
    }
    /**
     * 세부 속성 출력
     */
    on() {
        if (!this.editor.selectedObject) { return false; }
        // 세부 속성 이름 출력
        this.setPropertyName(this.editor.selectedObject.type);

        // 세부 속성 표시
        const property = this.editor.selectedObject.getProperty();
        property.map((propertyObject) => {
            const propertyObjectElement = propertyObject.makeProperty(this);
            if (propertyObject instanceof GroupProperty) {
                // display, 라벨, 엘리먼트, 유효성 등 그룹에 포함될 경우
                propertyObject.children.map((childPropertyObject) => {
                    const childPropertyObjectElement = childPropertyObject.makeProperty(this);
                    propertyObjectElement.addUI(childPropertyObjectElement);
                });
            }
            this.domElement.appendChild(propertyObjectElement.domElement);
        });
        return this;
    }
    /**
     * 세부 속성 삭제
     */
    off() {
        this.domElement.innerHTML = '';
        this.setPropertyName('');
        return this;
    }
    /**
     * 세부 속성 이름 표시
     */
    // 세부 속성 이름 출력
    setPropertyName(name) {
        // TODO: Breadcrumb 기능 추가
        document.getElementById('propertyName').textContent = name;
    }
    /**
     * inputBox 컴포넌트일 경우, 기본값 변경시 이벤트 핸들러
     * @param e 이벤트객체
     */
    updateDefaultType(e) {
        e.stopPropagation();
        e.preventDefault();

        let passValidate = true; // 유효성 검증
        let defaultTypeGroup = e.target.parentNode;
        let changeValue = '';
        if (e.type === 'keyup') { // input
            changeValue = 'input|' + e.target.value;
        } else if (e.type === 'change') { // selectbox, input
            // change 일 경우 minLength, maxLength 체크
            passValidate = this.changeValidateCheck(e.target);
            changeValue = ((e.target.type === 'input') ? 'input|' : 'select|') + e.target.value;
        } else { // click - button
            if (e.target.classList.contains('active')) { return false; }

            const buttonGroup =  e.target.parentNode;
            for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                let child = buttonGroup.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            e.target.classList.add('active');

            defaultTypeGroup = defaultTypeGroup.parentNode;
            const input = defaultTypeGroup.querySelector('.input');
            const select = defaultTypeGroup.querySelector('.select');
            if (e.target.getAttribute('data-type') === 'input') { // input 활성화
                input.classList.remove('off');
                select.classList.add('off');
                changeValue = 'input|';
            } else { // select 활성화
                select.classList.remove('off');
                input.classList.add('off');
                select.selectedIndex = 0;
                changeValue = 'select|' + select.options[0].value;
            }
        }
        this.validationStatus = passValidate;
        if (passValidate) {
            const method = e.target.id.substr(0, 1).toUpperCase() +
            e.target.id.substr(1, e.target.id.length);
            // 이력 저장
            this.editor.history.save([{
                type: 'change',
                id: this.editor.selectedObject.id,
                method: 'set' + method,
                from: this.editor.selectedObject['get' + method].call(this.editor.selectedObject),
                to: changeValue
            }]);
            this.editor.selectedObject['set' + method].call(this.editor.selectedObject, changeValue);
        }
    }
    /**
     * switch button / toggle button 변경시 이벤트 핸들러
     * @param e 이벤트객체
     */
    updateButton(e) {
        e.stopPropagation();
        e.preventDefault();

        if (e.target.classList.contains('btn-switch')) { // 정렬
            if (e.target.classList.contains('active')) { return false; }

            const buttonGroup =  e.target.parentNode;
            for (let i = 0, len = buttonGroup.childNodes.length ; i< len; i++) {
                let child = buttonGroup.childNodes[i];
                if (child.classList.contains('active')) {
                    child.classList.remove('active');
                }
            }
            e.target.classList.add('active');
        } else { // bold, italic 등 toggle button
            if (e.target.classList.contains('active')) {
                e.target.classList.remove('active');
                e.target.setAttribute('data-value', false);
            } else {
                e.target.classList.add('active');
                e.target.setAttribute('data-value', true);
            }
        }
        this.updateProperty.call(this, e);
    }
    /**
     * 세부 속성 변경시 이벤트 핸들러
     * @param e 이벤트객체
     */
    updateProperty(e) {
        e.stopPropagation();
        e.preventDefault();
        // enter, tab 입력시
        if (e.keyCode === 13 || e.keyCode === 9) { return false; }
        // 유효성 검증
        this.validationStatus = this.validationCheck(e.type, e.target);
        if (!this.validationStatus) { return false; }

        const changeValue = this.getPropertyValue(e.target);
        const method = e.target.id.substr(0, 1).toUpperCase() +
            e.target.id.substr(1, e.target.id.length);

        // 이력 저장
        this.editor.history.save([{
            type: 'change',
            id: this.editor.selectedObject.id,
            method: 'set' + method,
            from: this.editor.selectedObject['get' + method].call(this.editor.selectedObject),
            to: changeValue
        }]);
        this.editor.selectedObject['set' + method].call(this.editor.selectedObject, changeValue);
        
    }
    /**
     * 속성 엘리먼트의 값 조회
     * @param element DOM 엘리먼트
     */
    getPropertyValue(element) {
        switch (element.type) {
        case 'checkbox':
        case 'radio':
            return element.checked;
        case 'button':
            return element.getAttribute('data-value');
        default:
            return element.value;
        }
    }
    /**
     * keyup 유효성 검증
     * @param evtType 이벤트 타입
     * @param e 이벤트객체
     */
    validationCheck(evtType, element) {
        switch (evtType) {
        case 'keyup': // keyup 일 경우 type, min, max 체크
            return this.keyUpValidationCheck(element);
        case 'change': // // change 일 경우 필수값, minLength, maxLength 체크
            return this.changeValidationCheck(element);
        default:
            return true;
        }
    }
    /**
     * keyup 유효성 검증 이벤트 핸들러
     * @param e 이벤트객체
     */
    keyUpValidationCheck(element) {
        // type(number, char, email 등), min, max 체크
        if (element.getAttribute('data-validation-type') &&
            element.getAttribute('data-validation-type') !== '') {
            return validation.emit(element.getAttribute('data-validation-type'), element);
        }
        if (element.getAttribute('data-validation-min') &&
            element.getAttribute('data-validation-min') !== '') {
            return validation.emit('min', element, element.getAttribute('data-validation-min'));
        }
        if (element.getAttribute('data-validation-max') &&
            element.getAttribute('data-validation-max') !== '') {
            return validation.emit('max', element, element.getAttribute('data-validation-max'));
        }
        return true;
    }
    /**
     * change 유효성 검증 이벤트 핸들러
     * @param e 이벤트객체
     */
    changeValidationCheck(element) {
        // 필수값, minLength, maxLength 체크
        if (element.getAttribute('data-validation-required') &&
            element.getAttribute('data-validation-required') !== 'false') {
            return validation.emit('required', element);
        }
        if (element.getAttribute('data-validation-minLength') &&
            element.getAttribute('data-validation-minLength') !== '') {
            return validation.emit('minLength', element, element.getAttribute('data-validation-minLength'));
        }
        if (element.getAttribute('data-validation-maxLength') &&
            element.getAttribute('data-validation-maxLength') !== '') {
            return validation.emit('maxLength', element, element.getAttribute('data-validation-maxLength'));
        }
        return true;
    }
    /**
     * 세부 속성 첫번째 inputbox 포커싱 - 단축키에서 사용
     */
    selectFirstProperty() {
        const selectElements = this.domElement.querySelectorAll('input[type=text]:not([readonly])');
        if (selectElements.length === 0) { return false; }

        selectElements[0].focus();
    }
}